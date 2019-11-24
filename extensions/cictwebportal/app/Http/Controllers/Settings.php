<?php
namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\AccountStudent;
use App\StudentProfile;

class Settings extends Controller
{

  public function change_floor_assignment(Request $request){
    $post = $request->all();
    $new_flr = $request['floor_assignment'];
    $id = $request->session()->get('SES_CICT_ID');
    #------------------------------------------------------
    // Find the student with the given parameters
    $student_profile = StudentProfile::where('active', '=', '1')
    ->where('STUDENT_id', '=', $id)
    ->orderBy('id','DESC')
    ->take(1)
    ->update(['floor_assignment' => $new_flr]);

    #------------------------------------------------------
    // if floor_assignment successfully updated
    if($student_profile){
      $request_result['result'] = 'saved';
    }else{
      $request_result['result'] = 'failed';
    }

    #------------------------------------------------------
    //send response
    echo json_encode($request_result,JSON_FORCE_OBJECT);
  }

  public function reset_password(Request $request){
    $post = $request->all();
    $old_password = $request['old_password'];
    $new_password = $request['new_password'];
    $id = $request->session()->get('SES_CICT_ID');

    #------------------------------------------------------
    // check if old_password is correct
    $check_pass = AccountStudent::where('STUDENT_id', '=', $id)
    ->where('password', '=', $old_password)
    ->where('active', '=', '1')
    ->orderBy('id','DESC')
    ->first();

    #------------------------------------------------------
    // if old_password equals inputted password
    if($check_pass){
      $account_student = AccountStudent::where('STUDENT_id', '=', $id)
      ->where('active', '=', '1')
      ->orderBy('id','DESC')
      ->take(1)
      ->update(['password' => $new_password,]);

      #------------------------------------------------------
      // if password successfully updated
      if($account_student){
        $request_result['result'] = 'saved';
      }else{
        $request_result['result'] = 'failed';
      }

      #------------------------------------------------------
      // old password does not match
    }else{
      $request_result['result'] = 'wrong_pass';
    }

    #------------------------------------------------------
    //send response
    echo json_encode($request_result,JSON_FORCE_OBJECT);
  }
}
