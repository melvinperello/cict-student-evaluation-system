<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;

use App\Student;
use App\AccountStudent;
use App\StudentProfile;
use App\Curriculum;
use App\AcademicTerm;
use DB; use PDF;
#use Illuminate\Support\Facades\Route;

class Student_Profile extends Controller
{
  public function show_profile(){
    return view('profile.student_profile');
  }

  public function get_profile_details(Request $request){
    $id = $request->session()->get('SES_CICT_ID');

    #------------------------------------------------------
    //get student with the given parameter
    $student = Student::where('active', '=', '1')
    ->where('cict_id', '=', $id)
    ->orderBy('id','DESC')
    ->first();

    if($student){
      #------------------------------------------------------
      //get student profile of student
      $student_profile = StudentProfile::where('active', '=', '1')
      ->where('STUDENT_id', '=', $id)
      ->orderBy('id','DESC')
      ->first();

      $reply['info'] =$student;
      if($student_profile){
      //  echo $student_profile->student_address;
        #------------------------------------------------------
        //get curriculum info of student
        $curriculum = Curriculum::where('active', '=', '1')
        ->where('id', '=', $student->CURRICULUM_id)
        ->orderBy('id','DESC')
        ->first();

        #------------------------------------------------------
        //get current academic term info
        $academic_term = AcademicTerm::where('active', '=', '1')
        ->where('current', '=', 1)
        ->first();

        $reply['current_term'] =$academic_term;
        $reply['profile'] =$student_profile;

        //if curriculum is not null/empty
        if($curriculum){
          $reply['curriculum'] =$curriculum;
        }else {
          $reply['curriculum'] ="no curriculum";
        }
        if($academic_term){
          $reply['current_term'] =$academic_term;
        }else {
          $reply['current_term'] ="no current term";
        }
      }else{
        $reply['student_profile'] ="No student profile";
      //  return redirect()->route('profile-catch');
      }
    }else {
      $reply['student'] = "No student";
    }

      #------------------------------------------------------
      //send response
      echo json_encode($reply,JSON_FORCE_OBJECT);
    }

    public function update_profile(Request $request){
      // get session with default value if no value was assigned
      $id = $request->session()->get('SES_CICT_ID');

      $post = $request->all();
      $gender = $request['gender'];
      $contact_no = $request['contact_no'];
      $zipcode = $request['zipcode'];
      $email = $request['email'];
      // $house_no = $request['house_no'];
      // $street = $request['street'];
      // $brgy = $request['brgy'];
      // $city = $request['city'];
      $student_address = $request['student_address'];
      $province = $request['province'];
      $ice_name = $request['ice_name'];
      $ice_contact = $request['ice_contact'];
      $ice_address = $request['ice_address'];

      #---------------------------------------------------
      // check if student record exists in table student
      $student = Student::where('cict_id', $id)
      ->where('active', '1')
      ->orderBy('cict_id', 'desc')
      ->first();

      //if student record exists
      if($student){
        #---------------------------------------------------
        //search latest student profile
        $student_profile = StudentProfile::where('STUDENT_id', $id)
        ->where('active', '1')
        ->orderBy('id', 'desc')
        ->first();
        //if there is a record, clone and update
        if ($student_profile) {
          $cloned_profile = MonoUtility::cloneProfile($student_profile);
        } else {
          //if there is no record, create
          $cloned_profile = new StudentProfile;
          $cloned_profile->STUDENT_id = $id;
        }
        //update gender changes
        $student->has_profile = '1';
        $student->gender = strtoupper($gender);
        $cloned_profile->mobile = strtoupper($contact_no);
        $cloned_profile->zipcode = strtoupper($zipcode);
        $cloned_profile->email = $email;
        // $cloned_profile->house_no = strtoupper($house_no);
        // $cloned_profile->street = strtoupper($street);
        // $cloned_profile->brgy = strtoupper($brgy);
        // $cloned_profile->city = strtoupper($city);
        // $cloned_profile->province = strtoupper($province);
        $cloned_profile->student_address = strtoupper($student_address);
        $cloned_profile->ice_name = strtoupper($ice_name);
        $cloned_profile->ice_contact = strtoupper($ice_contact);
        $cloned_profile->ice_address = strtoupper($ice_address);

        //multiple insertion
        $data['result'] = "failed";
        try {
          DB::beginTransaction();
          $student->save();
          StudentProfile::where('STUDENT_id', $id)
          ->where('active', '1')
          ->update(['active' => '0']);
          $cloned_profile->save();
          DB::commit();
          $data['result'] = "saved";
          $data['message'] = 'Information successfully updated.';
        } catch (\PDOException $e) {
          DB::rollback();
          $data['message'] = 'Failed to update please try again.';
        }
        //if student record does not exists
      }else {
        $data['message'] = 'Cannot update information the student is not existing.';
      }

      echo json_encode($data,JSON_FORCE_OBJECT);
    }

    public function view_pdf(Request $request){
      $id = $request->session()->get('SES_CICT_ID');
      #------------------------------------------------------
      //find student with the given parameter
      $student = Student::where('cict_id', '=', $id)
      ->where('active', '=', '1')
      ->orderBy('id','DESC')
      ->first();

      #------------------------------------------------------
      //find student profile with the given parameter
      $student_profile = StudentProfile::where('STUDENT_id', '=', $id)
      ->where('active', '=', '1')
      ->orderBy('id','DESC')
      ->first();

      #------------------------------------------------------
      //find academic term of student
      $academic_term = AcademicTerm::where('current', '=', 1)
      ->where('active', '=', '1')
      ->orderBy('id','DESC')
      ->first();

      //$get_photo = $request->route('get-photo')->getActionName();
      //Route::getByName('get-photo');
      $get_photo = "http://localhost/laravel/cictwebportal/public/media/photo/";
      $display_pic = $get_photo.$student_profile->profile_picture;
      if($academic_term){
        $get_sem = $academic_term->semester;
        $get_sy = $academic_term->school_year;
      }else{
        $get_sem = "---";
        $get_sy = "---";
      }


      #------------------------------------------------------
      $get_sem = $this->convert_to_words($get_sem);
      $view =\View::make('pdf.student_profile_pdf', ['student' => $student, 'student_profile' => $student_profile, 'display_pic' => $display_pic, 'sem' =>  $get_sem, 'sy' => $get_sy ]);
      $html_content = $view->render();
      // $PDF = new TCPDF('L', 'mm', array(210,97), true, 'UTF-8', false);
      PDF::SetTitle($student->last_name.', '.$student->first_name.' '.$student->middle_name);
      //  PDF::SetMargins(25,17,25, true);
      $resolution= array(170,210);
      PDF::AddPage('L', $resolution);
      PDF::SetFont('gothic');
      // PDF::AddPage();
      PDF::writeHTML($html_content, true, false, true, false, '');
      PDF::Output($student->last_name.', '.$student->first_name.' '.$student->middle_name.'.pdf');
    }

    public function convert_to_words($sem){
      if($sem == "FIRST SEMESTER"){
        $new_sem = "FIRST";
      }else if($sem == "SECOND SEMESTER"){
        $new_sem = "SECOND";
      }else if($sem == "FIRST SUMMER"){
        $new_sem = "1st SUMMER";
      }else if($sem == "SECOND SUMMER"){
        $new_sem = "2nd SUMMER";
      }else{
        $new_sem = $sem;
      }

      return $new_sem;
    }

    public function show_student_profile(){
      return view('profile.student_profile_cluster');
    }

    public function create_profile(){
      // get session with default value if no value was assigned
      $id = $request->session()->get('SES_CICT_ID');

      $post = $request->all();
      $cluster = $request['cluster'];

      #---------------------------------------------------
      // check if student record exists in table student
      $student = Student::where('cict_id', $id)
      ->where('active', '1')
      ->orderBy('cict_id', 'desc')
      ->first();

      //if student record exists
      if($student){
        #---------------------------------------------------
        //search latest student profile
        $student_profile = StudentProfile::where('STUDENT_id', $id)
        ->where('active', '1')
        ->orderBy('id', 'desc')
        ->first();
        //if there is a record
        if ($student_profile) {
          $cloned_profile = MonoUtility::cloneProfile($student_profile);
        } else {
          //if there is no record, create
          $cloned_profile = new StudentProfile;
          $cloned_profile->STUDENT_id = $id;
          $cloned_profile->floor_assignment = $cluster;
        }

        //multiple insertion
        $data['result'] = "failed";
        try {
          DB::beginTransaction();
          $cloned_profile->save();
          DB::commit();
          $data['result'] = "saved";
          $data['message'] = 'Information successfully created.';
        } catch (\PDOException $e) {
          DB::rollback();
          $data['message'] = 'Failed to create please try again.';
        }
        //if student record does not exists
      }else {
        $data['message'] = 'Cannot create information the student is not existing.';
      }

      echo json_encode($data,JSON_FORCE_OBJECT);
    }

    public function logout(Request $request){
      $request->session()->flush();
    }

  }
