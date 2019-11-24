<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Student;
use App\AccountStudent;
use App\StudentProfile;
use Illuminate\Support\Facades\DB;

class Registration extends Controller
{
  public function show_registration(){
    return view('registration.register');
  }

  public function verify_student(Request $request){
    $post = $request->all();
    $stud_number = $request['stud_id'];

    #------------------------------------------------------
    // Find the student with the given parameters
    $student = Student::where('id', '=', $stud_number)
    ->where('active', '=', '1')
    ->orderBy('id','DESC')
    ->first();

    #------------------------------------------------------
    // If the student exists
    if($student){
      $request_result['result'] = 'true';
      $request_result['id'] = $student->cict_id;
    }else{
      $request_result['result'] = 'false';
    }

    #------------------------------------------------------
    // send response
    echo json_encode($request_result,JSON_FORCE_OBJECT);
  }

  public function check_account(Request $request){
     $post = $request->all();
     $cict_id = $request['cict_id'];

     #------------------------------------------------------
     // Find the student with the given parameters
     $account_student = AccountStudent::where('STUDENT_id', '=', $cict_id)
     ->where('active', '=', '1')
     ->orderBy('id','DESC')
     ->first();

     #------------------------------------------------------
     // If the student has already an account
     if($account_student){
       $request_result['result'] = 'true';
     }else{
       $request_result['result'] = 'false';
       $request_result['id'] = $cict_id;
     }

     #------------------------------------------------------
     // send response
    echo json_encode($request_result,JSON_FORCE_OBJECT);
  }

  public function confirm_information(Request $request){
     $post = $request->all();
     $cict_id = $request['cict_id'];
     $last_name =  $request['last_name'];
     $first_name = $request['first_name'];
     $middle_name = $request['middle_name'];

     #------------------------------------------------------
     // Find the student with the given parameters
     $student = Student::where('cict_id', '=', $cict_id)
     ->where('active', '=', 1)
     ->orderBy('id','DESC')
     ->first();

     #------------------------------------------------------
     // If the student exists
     if($student){
       // get the value of the column middle_name
       $request_result['orig'] = $student->middle_name;
       // if middle_name is empty;
       if((empty($student->middle_name)) and ($request['middle_name'] == "")){
         $middle_name = "";
         $request_result['empty_md'] = $middle_name;
       }else{}
       // if middle_name is null
       if((is_null($student->middle_name)) and ($request['middle_name'] == "")){
         $middle_name = $request['middle_name'];
         $request_result['null_md'] = $middle_name;
       }else{}

      #------------------------------------------------------
      // Find the student with the given parameters
       $student_credentials = Student::where('cict_id', '=', $student->cict_id)
       ->where('last_name', '=', $last_name)
       ->where('first_name', '=', $first_name)
       ->where('middle_name', '=', $middle_name)
       ->where('active', '=', 1)
       ->orderBy('id','DESC')
       ->first();

       #------------------------------------------------------
       // if credentials match
       $request_result['result_md'] = $middle_name;
       if($student_credentials){
          $request_result['result'] = 'true';
       }else {
          $request_result['result'] = 'false_name';
       }

     #------------------------------------------------------
     // if student does not exists
     }else{
       $request_result['result'] = 'false';
     }

    #------------------------------------------------------
    //send response
    echo json_encode($request_result,JSON_FORCE_OBJECT);
  }

  public function check_username(Request $request){
     $post = $request->all();
     $username = $request['username'];

      #------------------------------------------------------
     // find the if the username is taken
     $account_student = AccountStudent::where('username', '=', $username)
     ->where('active', '=', '1')
     ->orderBy('id','DESC')
     ->first();

     //if is already taken
     if($account_student){
       $request_result['result'] = 'taken';
     }else{
       $request_result['result'] = 'available';
     }

    #------------------------------------------------------
    // send response
    echo json_encode($request_result,JSON_FORCE_OBJECT);
  }

  public function create_account(Request $request){
    $student_id = $request['cict_id'];
    $username = $request['username'];
    $password = $request['password'];
    $question = $request['recovery_question'];
    $answer = $request['recovery_answer'];
    $floor_assignment = $request['floor_assignment'];

    $student_account = AccountStudent::where('STUDENT_id', $student_id)
    ->where('active', '1')
    ->first();

    #---------------------------------------------------
    // check if student already has an account
    if(!$student_account){
      //if there is no record, create
    //$hashed_password = $this->Hash($password);

        $new_student = new AccountStudent();
        $new_student->STUDENT_id = $student_id;
        $new_student->username = $username;
        $new_student->password = $password;
        $new_student->recovery_question = $question;
        $new_student->recovery_answer = $answer;
    }else {}

    #---------------------------------------------------
    // check if student record exists in table student
    $student = Student::where('cict_id', $student_id)
    ->where('active', '1')
    ->orderBy('cict_id', 'desc')
    ->first();

    //if student record exists
    if($student){
      #---------------------------------------------------
      //search latest student profile
        $student_profile = StudentProfile::where('STUDENT_id', $student_id)
            ->where('active', '1')
            ->orderBy('id', 'desc')
            ->first();
        //if there is a record, clone
        if ($student_profile) {
            $cloned_profile = MonoUtility::cloneProfile($student_profile);
            $cloned_profile->floor_assignment = $floor_assignment;
            $student->has_profile = '1';
        } else {
        //if there is no record, create
            $cloned_profile = new StudentProfile;
            $cloned_profile->STUDENT_id = $student_id;
            $cloned_profile->floor_assignment = $floor_assignment;
        }
      //multiple insertion
      $data['result'] = "false";
       try {
           DB::beginTransaction();
           $new_student->save();
           $student->save();
           StudentProfile::where('STUDENT_id', $student_id)
               ->where('active', '1')
               ->update(['active' => '0']);
           $cloned_profile->save();
           DB::commit();

        //------------------------------------------------------------------------
        session()->put('SES_AUTHENTICATED','YES');
        session()->put('SES_ACCOUNT_ID',$new_student->id);
        session()->put('SES_CICT_ID',$new_student->STUDENT_id);
        session()->put('SES_USERNAME',$new_student->username);
        //------------------------------------------------------------------------

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

  #------------------------------------------------------
  //send response
   echo json_encode($data,JSON_FORCE_OBJECT);
  }

  // public function Hash($password){
  //   $hashed_password = Hash::make($password);
  //
  //   return $hashed_password;
  // }
}
