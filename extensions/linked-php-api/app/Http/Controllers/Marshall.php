<?php

namespace App\Http\Controllers;

use App\AcademicTerm;
use App\LinkedEntrace;
use App\LinkedSetting;
use Illuminate\Http\Request;

use App\Student;
use App\Curriculum;
use App\AcademicProgram;
use App\LinkedPila;
use App\AccountStudent;
use App\LinkedFloorFour;
use App\LinkedFloorThree;
use App\StudentProfile;
use phpDocumentor\Reflection\DocBlock\Tags\Link;
use Illuminate\Support\Facades\DB;

class Marshall extends Controller
{
    //----------------------------------------------------------------------------
    /*
    Searching the student
    */
    public function search($student_number)
    {
        //---------------------------------------------------------------
        $student_model = Student::where('id', $student_number)
            ->where('active', 1)
            ->orderBy('cict_id', 'DESC')
            ->first();
        //---------------------------------------------------------------
        if ($student_model) {
            ## student full name
            $full_name = $student_model->first_name
                . " "
                . $student_model->middle_name
                . " "
                . $student_model->last_name;
            //---------------------------------------------------------------
            ## get curriculum
            $curriculum_model = Curriculum::find($student_model->CURRICULUM_id);

            # 09.06.2017
            $curriculum_code = "NOT SET";
            if ($curriculum_model) {
                ## get academic program
                $academic_program_model = AcademicProgram::find($curriculum_model->ACADPROG_id);
                $curriculum_code = $academic_program_model->code;
            }
            //---------------------------------------------------------------
            // Search for expired entries
            $active_entries = LinkedPila::where('STUDENT_id', $student_model->cict_id)
                ->where('active', 1)
                ->where('status', 'EXPIRED')
                ->orderBy('id', 'desc')
                ->get();
            //---------------------------------------------------------
            // if the student has a EXPIRED remarks
            try {
                // start local transaction
                DB::beginTransaction();
                if ($active_entries) {
                    foreach ($active_entries as $each_entry) {
                        $each_entry->status = "VOID";
                        $each_entry->active = '0';
                        $each_entry->save();
                    }
                }
                DB::commit();
            } catch (\PDOException $e) {
                DB::rollback();
                $search_result['result'] = "not_ok";
                $search_result['description'] = "The student has expired entries, the system cannot update the entries right now, please try again.";
                echo json_encode($search_result, JSON_FORCE_OBJECT);
                exit();
            }
            //---------------------------------------------------------------
            ## check pila table
            $link_pila = LinkedPila::where('STUDENT_id', $student_model->cict_id)
                ->where('active', 1)
                ->where('status', '!=', 'VOID')
                ->first();
            //---------------------------------------------------------------
            ## check if it exist
            if ($link_pila) {
                if ($link_pila->status == "CALLED") {
                    $search_result['pila_id'] = "called";
                } else {
                    $search_result['pila_id'] = $link_pila->id;
                }
                $search_result['imei'] = $link_pila->imei;
                # -------------------------------------------------------------
                # Added 11.03.2017
                $search_result['floor_assignment'] = $link_pila->floor_assignment;
                $search_result['floor_number'] = $link_pila->floor_number;
                # -------------------------------------------------------------
            } else {
                $search_result['pila_id'] = 'not_qued';
                $search_result['imei'] = "NONE";
            }

            if (!$student_model->gender) {
                $gender = "NOT SET";
            } else {
                $gender = $student_model->gender;
            }

            ## prepare the results
            $search_result['result'] = "ok";
            $search_result['cict_id'] = $student_model->cict_id;
            $search_result['student_number'] = $student_model->id;
            $search_result['full_name'] = $full_name;
            # 09.06.2017
            $search_result['course'] = $curriculum_code;
            #
            $search_result['gender'] = $gender;
            //---------------------------------------------------------------
            ## profile pciture
            $profile = StudentProfile::where('STUDENT_id', $student_model->cict_id)
                ->where('active', '1')
                ->first();

            if ($profile) {
                $search_result['photo'] = $profile->profile_picture;
            } else {
                $search_result['photo'] = "NONE";
            }
            ## end photo code
            echo json_encode($search_result, JSON_FORCE_OBJECT);
            exit();
        } else {
            ## student not found
            $search_result['result'] = "not_found";
            echo json_encode($search_result, JSON_FORCE_OBJECT);
            exit();
        }

    }

    //----------------------------------------------------------------------------

    /**
     * This will add the student to the lane and will assigned a number
     * @param Request $request
     */
    public function add_student(Request $request)
    {
        $post = $request->all();
        //---------------------------------------------------------
        // get request values
        $new_pila = new LinkedPila();
        $new_pila->ACADTERM_id = $post['acad_term'];
        $new_pila->STUDENT_id = $post['cict_id'];
        $new_pila->conforme = $post['conforme'];
        $new_pila->course = $post['course'];
        $new_pila->imei = $post['imei'];
        //---------------------------------------------------------
        // check student account access and student existence
        $student_info = Student::where('active', '1')
            ->where('cict_id', $new_pila->STUDENT_id)
            ->first();
        //---------------------------------------------------------
        // check if student is not exist
        if (!$student_info) {
            # must not exist
            $reply["success"] = "0";
            $reply["description"] = "Student is not existing.";
            //
            //$reply["result"] = "student_not_exist";
            echo json_encode($reply, JSON_FORCE_OBJECT);
            exit();
        }
        //---------------------------------------------------------
//        // if the student has no profile
//        if ($student_info->has_profile == '0') {
//
//            $reply["result"] = "student_0_profile";
//            echo json_encode($reply, JSON_FORCE_OBJECT);
//            exit();
//        }
        //---------------------------------------------------------
        // get student account
        $student_acc = AccountStudent::where('active', '1')
            ->where('STUDENT_id', $new_pila->STUDENT_id)
            ->orderBy('id', 'DESC')
            ->first();
        // if the student has an account check the access if no account ok lang
        if ($student_acc) {
            if ($student_acc->access_level != 'STUDENT') {
                $reply["success"] = "0";
                $reply["description"] = "Only students are allowed to be added.";
                //
                //$reply["result"] = "access_not_student";
                echo json_encode($reply, JSON_FORCE_OBJECT);
                exit();
            }
        }
        //---------------------------------------------------------
        // server validation
        // if existing in the queue.
        $is_exist = LinkedPila::where('STUDENT_id', $post['cict_id'])
            ->where('active', 1)
            ->where('status', '!=', 'VOID')
            ->orderBy('id', 'desc')
            ->first();
        //---------------------------------------------------------
        // get_academic_program
        // Validation if the student has profile.
        // replaced by floor_assignment search profile instead
        //$academic_program = AcademicProgram::where('code',$new_pila->course)->first();
        $student_profile = StudentProfile::where('STUDENT_id', $new_pila->STUDENT_id)
            ->orderBy('id', 'DESC')
            ->where('active', '1')
            ->first();
        if (!$student_profile) {
            $reply["success"] = "0";
            $reply["description"] = "Student has no profile.";
            //
            //$reply["result"] = "no_profile";
            echo json_encode($reply, JSON_FORCE_OBJECT);
            exit();
        } else {
            $floor = $student_profile->floor_assignment;
            # if profile exist but not floor assignment
            if (!$floor) {
                $reply["success"] = "0";
                $reply["description"] = "No floor assignment found in the student's profile.";
                //
                //$reply["result"] = "not_inserted_floor";
                echo json_encode($reply, JSON_FORCE_OBJECT);
                exit();
            }
        }
        //---------------------------------------------------------
        // there is a floor assignment
        $new_pila->floor_assignment = $floor;
        ## student is already assigned to a lane number
        if ($is_exist) {
            $reply["success"] = "0";
            $reply["description"] = "Student is already in the line.";
            //
            //$reply["result"] = "not_void_existing";
            echo json_encode($reply, JSON_FORCE_OBJECT);
            exit();
        }
        //----------------------------- SETTINGS AND CUT OFF
        ##// get settings table
        $settings = LinkedSetting::where('active', '1')
            ->orderBy('id', 'DESC')
            ->first();
        ## check setting
        if (!$settings) {
            # no setting table found
            $reply["success"] = "0";
            $reply["description"] = "Server is not accepting request, no session found";
            //
            //$reply["result"] = "no_setting";
            echo json_encode($reply, JSON_FORCE_OBJECT);
            exit();
        }
        # --------------------------------------------------------
        # Settings Filter Passed
        if ($floor == 3) {
            $new_pila->cluster_name = $settings->floor_3_name;
            $this->check_floor_settings($settings->floor_3_cut, $settings->floor_3_last, $settings->floor_3_max);
        } else if ($floor == 4) {
            $new_pila->cluster_name = $settings->floor_4_name;
            $this->check_floor_settings($settings->floor_4_cut, $settings->floor_4_last, $settings->floor_4_max);
        } else {
            $reply["success"] = "0";
            $reply["description"] = "Unknown floor assignment";
            echo json_encode($reply, JSON_FORCE_OBJECT);
        }
        //---------------------------------------------------------
        $new_pila->SETTINGS_id = $settings->id;
        if ($student_acc) {
            $new_pila->ACCOUNT_id = $student_acc->id;
        } else {
            $new_pila->ACCOUNT_id = null;
        }
        //---------------------------------------------------------
        try {
            // start local transaction
            DB::connection()->getPdo()->beginTransaction();
            // insert in linked_pila table
            $new_pila->save();
            $ref_id = $this->generateFloorId($floor, $new_pila->id);
            $ref_id->save();
            $new_pila->floor_number = $ref_id->id;
            $new_pila->save();
            DB::connection()->getPdo()->commit();
            $reply["success"] = "1";
            $reply["description"] = "Successfully Added [Assigned ID: FLR" . $floor . " / # " . $ref_id->id . "]";
            echo json_encode($reply, JSON_FORCE_OBJECT);
        } catch (\PDOException $e) {
            DB::connection()->getPdo()->rollBack();
            $reply["success"] = "0";
            $reply["description"] = "Failed to insert, try again.";
            echo json_encode($reply, JSON_FORCE_OBJECT);
        }
    }


    /**
     * Generates a number per floor reference.
     * @param $floor
     * @param $ref_id
     * @return mixed
     */
    private function generateFloorId($floor, $ref_id)
    {
        $floor_assigned = null;
        if ($floor == 3) {
            $floor_assigned = new LinkedFloorThree();
        } else if ($floor == 4) {
            $floor_assigned = new LinkedFloorFour();
        }
        $floor_assigned->pila_id = $ref_id;
        return $floor_assigned;
    }

    /**
     * Checks the status and settings for a particular floor number
     * @param $floor_cut
     * @param $floor_last
     * @param $floor_max
     */
    private function check_floor_settings($floor_cut, $floor_last, $floor_max)
    {
        if ($floor_cut == 1) {
            // number has been cut
            $reply["success"] = "0";
            $reply["description"] = "The server disables the numbering service, no students can be added.";
            echo json_encode($reply, JSON_FORCE_OBJECT);
            exit();
        }

        if ($floor_last >= $floor_max) {
            // max number reached
            $reply["success"] = "0";
            $reply["description"] = "Max number of clients have been reached, no students can be added.";
            echo json_encode($reply, JSON_FORCE_OBJECT);
            exit();
        }
    }


    /**
     * Create new student.
     * @param Request $request
     */
    public function create_new_student(Request $request)
    {
        //----------------------------------------------------------
        $post = $request->all();
        //----------------------------------------------------------
        $new_student = new Student();
        //----------------------------------------------------------
        # required values for creating student
        $new_student->id = $post['student_number'];
        $new_student->last_name = $post['last_name'];
        $new_student->first_name = $post['first_name'];
        $new_student->middle_name = $post['middle_name'];
        $new_student->created_by = $post['created_by'];
        //----------------------------------------------------------
        $new_student->enrollment_type = "NOT_SET";
        //----------------------------------------------------------
        # already existing
        if (Student::where('id', $post['student_number'])->first()) {
            $reply['result'] = "exist";
            echo json_encode($reply);
            exit();
        }
        //----------------------------------------------------------
        # REVISION: since the student needs first to fill up a profile
        # before having a number the system will need to give the student a number
        # so while waiting the student can fill up his/her profile rather than
        # filling up the profile first before having a number
        # an entry to the student profile table is necessary
        //----------------------------------------------------------
        $temp_student_profile = new StudentProfile;
        # Cluster 1 is Floor 3 while Cluster 2 is Floor 4
        $temp_student_profile->floor_assignment = $post['cluster_assignment'];
        //----------------------------------------------------------
        try {
            DB::beginTransaction();
            $new_student->save();
            $student_cict_id = $new_student->cict_id;
            // Update previous profile if applicable
            //-------------------------------------------------
            # ELOQUENT MASS UPDATE USAGE
            $fields_to_update['active'] = '0';
            StudentProfile::where('active', '1')
                ->where('STUDENT_id', $student_cict_id)
                ->update($fields_to_update);
            //-------------------------------------------------

            // insert profile for cluster assignment
            $temp_student_profile->STUDENT_id = $student_cict_id;
            $temp_student_profile->save();
            DB::commit();
            return response()->json(['result' => 'ok']);
        } catch (\PDOException $e) {
            DB::rollback();
            return response()->json(['result' => 'error']);
        }
    }

    //----------------------------------------------------------------------------
    public function check($id /*Request $request*/)
    {
        //$post = $request->all();
        $cict_id = $id; //['cict_id'];
        $pila = $this->checkStudent($cict_id);
        if ($pila) {
            $reply['res'] = 1;
            $reply['status'] = $pila->status;
        } else {
            $reply['res'] = 0;
        }
        echo json_encode($reply, JSON_FORCE_OBJECT);
    }

    private function checkStudent($cict_id)
    {
        // get current academic term
        $acad_term = AcademicTerm::where('active', '1')
            ->where('current', '1')
            ->first();


        if ($acad_term) {
            $latest_acadterm = $acad_term->id;
        } else {
            return 0;
        }

        // latest
        $pila = LinkedPila::where('STUDENT_id', $cict_id)
            ->where('status', '!=', 'VOID')
            ->where('ACADTERM_id', $latest_acadterm)
            ->orderBy('id', 'DESC')
            ->first();

        if ($pila) {
            return $pila;
        } else {
            return 0;
        }
    }


    //-----------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------
    /**
     * add students to the entry lane
     * @param Request $request
     */
    public function entrance_pass(Request $request)
    {
        $post = $request->all();
        $student_number = $post['id']; // student number 2014113844
        $acad_term = $post['term']; // id of acad term

        $student = Student::where('id', $student_number)
            ->where('active', '1')
            ->first();

        # if the student is not existing
        if (!$student) {
            $res['res'] = "not_exist";
            print json_encode($res, JSON_FORCE_OBJECT);
            exit();
        }

        # if there is a student
        $id = $student->cict_id;

        $linked_pila = LinkedPila::where('STUDENT_id', $id)
            ->where('active', '1')
            ->where('status', 'CALLED')
            ->where('remarks', 'NONE')
            ->where('ACADTERM_id', $acad_term)
            ->first();

        if (!$linked_pila) {
            $res['res'] = "invalid_reference"; // the student has no valid line id
            print json_encode($res, JSON_FORCE_OBJECT);
            exit();
        }

        # if found

        $linked_pila->status = "VOID";
        $linked_pila->remarks = "ENTERED";

        ## update
        $res_1 = $linked_pila->save();

        if (!$res_1) {
            $res['res'] = "error_line_update";
            print json_encode($res, JSON_FORCE_OBJECT);
            exit();
        }


        # create lane entrance entry.
        $entrace = new LinkedEntrace();
        $entrace->name = $linked_pila->conforme; // must be short only
        $entrace->student_number = $student_number; // student number
        $entrace->floor_assignment = $linked_pila->floor_assignment; // floor
        $res_2 = $entrace->save();

        if ($res_2) {
            $res['res'] = "ok";
            print json_encode($res, JSON_FORCE_OBJECT);
            exit();
        } else {
            $res['res'] = "error_entry_pass";
            print json_encode($res, JSON_FORCE_OBJECT);
            exit();
        }
    }


    /**
     * Update student information for correction.
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function update_student_info(Request $request)
    {
        $post = $request->all();
        $cict_id = $post['cict_id'];
        $student_number = $post['student_number'];
        $last_name = $post['last_name'];
        $first_name = $post['first_name'];
        $middle_name = $post['middle_name'];
        $cluster_assignment = $post['cluster_assignment'];
        #---------------------------------------------------
        # Get student Table
        $student = Student::where('cict_id', $cict_id)
            ->where('active', '1')
            ->orderBy('cict_id', 'desc')
            ->first();
        #---------------------------------------------------
        # If Student is not existing return error;
        if (!$student) {
            $reply['success'] = '0';
            $reply['message'] = 'Cannot update information the student is not existing.';
            return response()->json($reply, 200, [], JSON_FORCE_OBJECT);
        } else {
            //------------------------------------------------------------
            if ($student->verified == '1') {
                $reply['success'] = '0';
                $reply['message'] = 'Cannot Apply Updates for Verified Students. Please approach the local registrar.';
                return response()->json($reply, 200, [], JSON_FORCE_OBJECT);
            }
            //------------------------------------------------------------
            # IF Student is existing test the student number
            if ($student_number == $student->id) {
                // nothing to do just updating info not student number
            } else {
                // Student number is also changed
                // check whether this new student number is already used
                $check_student = Student::where('id', $student_number)
                    ->where('active', '1')
                    ->orderBy('cict_id', 'desc')
                    ->first();
                if ($check_student) {
                    // the student number is already taken
                    $reply['success'] = '0';
                    $reply['message'] = 'Student Number is already used by another student.';
                    return response()->json($reply, 200, [], JSON_FORCE_OBJECT);
                }
            }
        }
        #---------------------------------------------------
        # If the student is existing apply update in Student Table
        $student->id = $student_number;
        $student->last_name = $last_name;
        $student->first_name = $first_name;
        $student->middle_name = $middle_name;
        #---------------------------------------------------
        # Search Latest Student Profile
        $student_profile = StudentProfile::where('STUDENT_id', $cict_id)
            ->where('active', '1')
            ->orderBy('id', 'desc')
            ->first();
        if ($student_profile) {
            # if there is already a profile just clone the values and apply changes
            # Copy student profile
            $cloned_profile = MonoUtility::cloneProfile($student_profile);
            # Apply Changes
            $cloned_profile->floor_assignment = $cluster_assignment;
        } else {
            # If there is no student profile Create one and assigne values
            # In this case i only need to assign floor assignment
            # so i only added floor_assignment this not necessarily means
            # that you should only add floor assignment also
            # in profiling process all values must be assigned
            $cloned_profile = new StudentProfile;
            $cloned_profile->STUDENT_id = $cict_id;
            $cloned_profile->floor_assignment = $cluster_assignment;
        }
        #---------------------------------------------------
        # When values are already applied try to save the changes in the database
        try {
            DB::beginTransaction();
            # Save Changes in the student table
            $student->save();
            # Deactivate All Existing Profiles
            $profile_update_fields['active'] = '0';
            StudentProfile::where('STUDENT_id', $cict_id)
                ->where('active', '1')
                ->update($profile_update_fields);
            # After Deactivating Old Profiles (ALL)
            # Insert the cloned profile
            $cloned_profile->save();
            # Commit these changes
            DB::commit();
            # Create A Reply message
            $reply['success'] = '1';
            $reply['message'] = 'Information updated successfully.';
            return response()->json($reply, 200, [], JSON_FORCE_OBJECT);
        } catch (\PDOException $e) {
            DB::rollback();
            # Create an error message
            $reply['success'] = '0';
            $reply['message'] = 'Failed to updated please try again.';
            return response()->json($reply, 200, [], JSON_FORCE_OBJECT);
        }
    }

    public function fetch_update_information($cict_id)
    {
        #---------------------------------------------------
        # Get student Table
        $student = Student::where('cict_id', $cict_id)
            ->where('active', '1')
            ->orderBy('cict_id', 'desc')
            ->first();
        #---------------------------------------------------
        # If Student is not existing return error;
        if (!$student) {
            $reply['success'] = '0';
            $reply['message'] = 'Cannot recieve requested daya please try again.';
            return response()->json($reply, 200, [], JSON_FORCE_OBJECT);
        }
        #---------------------------------------------------
        # Search Latest Student Profile
        $student_profile = StudentProfile::where('STUDENT_id', $cict_id)
            ->where('active', '1')
            ->orderBy('id', 'desc')
            ->first();
        if ($student_profile) {
            $cluster_assignment = $student_profile->floor_assignment;
        } else {
            $cluster_assignment = "no_assignment";
        }
        $reply['success'] = '1';
        $reply['student_number'] = $student->id;
        $reply['last_name'] = $student->last_name;
        $reply['first_name'] = $student->first_name;
        $middle_name = $student->middle_name;
        if (!$middle_name) {
            # when middle name is null
            $middle_name = "";
        }
        $reply['middle_name'] = $middle_name;
        $reply['cluster_assignment'] = $cluster_assignment;

        return response()->json($reply, 200, [], JSON_FORCE_OBJECT);
    }

}
