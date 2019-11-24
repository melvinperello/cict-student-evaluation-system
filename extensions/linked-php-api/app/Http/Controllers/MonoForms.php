<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Student;
use App\StudentProfile;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;

class MonoForms extends Controller
{
    //
    public function mono_profile(Request $request)
    {
        // Get Values
        $values = $request->all();
        $id = $values['id'];
        $lname = $values['lname'];
        $fname = $values['fname'];
        $mname = $values['mname'];
        $gender = $values['gender'];
        $floor = $values['floor'];
        $mobile = $values['mobile'];
        // address values
        $zip = $values['zip'];
//        $house = $values['house'];
//        $street = $values['street'];
//        $brgy = $values['brgy'];
//        $city = $values['city'];
//        $province = $values['province'];
        $full_address = $values['full_address'];
        $email = $values['email'];
        // emergency contact
        $ice_name = $values['ice_name'];
        $ice_address = $values['ice_address'];
        $ice_contact = $values['ice_contact'];
        //
        $created_by = $values['created_by'];
        //--------------------------------------------
        // create a new student
        // this is only assigning of data no insertion is involved
        $new_student = new Student;
        $new_student->id = $id;
        $new_student->last_name = $lname;
        $new_student->first_name = $fname;
        $new_student->middle_name = $mname;
        $new_student->gender = $gender;
        $new_student->has_profile = '1';
        $new_student->enrollment_type = 'NOT_SET';
        $new_student->created_by = $created_by;
        //--------------------------------------------
        // Create Profile
        // this is only assigning of data no insertion is involved
        $profile = new StudentProfile;
        $profile->floor_assignment = $floor;
        $profile->profile_picture = "NONE";
        $profile->mobile = $mobile;
//        $profile->house_no = $house;
//        $profile->street = $street;
//        $profile->brgy = $brgy;
//        $profile->city = $city;
//        $profile->province = $province;
        $profile->student_address = $full_address;
        $profile->zipcode = $zip;
        $profile->email = $email;
        $profile->ice_name = $ice_name;
        $profile->ice_contact = $ice_contact;
        $profile->ice_address = $ice_address;

        // before inserting check if already existing
        $check_student = Student::where('id', $id)
            ->orderBy('cict_id', 'desc')
            ->first();

        //--------------------------------------------
        if ($check_student) {
            // if record in the student table is existing update it
            $check_student->last_name = $lname;
            $check_student->first_name = $fname;
            $check_student->middle_name = $mname;
            $check_student->gender = $gender;
            $check_student->updated_by = $created_by;
            //-----------------------------------------------
            // get current date and time
            $updated_time = Carbon::now();
            $updated_time->timezone = 'Asia/Taipei';
            //-----------------------------------------------
            $check_student->updated_date = $updated_time;
            // if existing just update the student profile
            $this->existing_update_profile($check_student, $profile);
        } else {
            // if student is not existing insert student and profile
            $this->insert_data($new_student, $profile);
        }
    }

    /**
     * When the student is existing just update the current profile or create if no profile.
     */
    private function existing_update_profile($check_student, $new_profile)
    {
        $cict_id = $check_student->cict_id;
        $check_profile = StudentProfile::where('STUDENT_id', $cict_id)
            ->where('active', '1')
            ->orderBy('id', 'desc')
            ->get();

        if ($check_profile) {
            try {
                // start local transaction
                DB::connection()->getPdo()->beginTransaction();
                // profile exist deactivate them and insert new
                foreach ($check_profile as $profile) {
                    $profile->active = '0';
                    $profile->save();
                }
                // save new info
                $check_student->save();
                // save new profile
                $new_profile->STUDENT_id = $cict_id;
                $new_profile->save();
                // save local session
                DB::connection()->getPdo()->commit();
                $res['success'] = "1";
                $res['description'] = "Student is Already Existing, Profile was Updated";
                print json_encode($res, JSON_FORCE_OBJECT);
                exit();
            } catch (\PDOException $e) {
                DB::connection()->getPdo()->rollBack();
                $res['success'] = "0";
                $res['description'] = "Student is Already Existing, Failed to update Profile";
                print json_encode($res, JSON_FORCE_OBJECT);
                exit();
            }
        } else {
            // no active profile just insert the profile
        }
    }

    /*
     * Insert new Student and Student Profile
     */
    private function insert_data($student, $profile)
    {
        try {
            // start local transaction
            DB::connection()->getPdo()->beginTransaction();
            // lahat sa ilalim hindi nasesave sa db
            $student->save();
            $cict_id = $student->cict_id;
            $profile->STUDENT_id = $cict_id; // use the newly inserted id
            $profile->save();
            // para msave
            DB::connection()->getPdo()->commit();
            $res['success'] = "1";
            $res['description'] = "Successfully Added";
            print json_encode($res, JSON_FORCE_OBJECT);
            exit();
        } catch (\PDOException $e) {
            DB::connection()->getPdo()->rollBack();
            $res['success'] = "0";
            $res['description'] = "Cannot Process Transaction";
            print json_encode($res, JSON_FORCE_OBJECT);
            exit();
        }
    }
}
