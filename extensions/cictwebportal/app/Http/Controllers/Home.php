<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\Announcements;
use App\Faculty;
use App\Student;
use App\AccountStudent;
use App\LoadGroupSchedule;
use App\LoadSubject;
use App\LoadGroup;
use App\Subject;
use Carbon\Carbon;

class Home extends Controller
{
  public function show_home(){
    return view('home.home');
  }

  public function show_login(){
    return view('login.login');
  }

  public function verify_login(Request $request){
    $post = $request->all();
    $username = $request['username'];
    $password = $request['password'];

    #-------------------------------------------------------
    // Find the student's account with the given username and password
    $account_student = AccountStudent::where('active', '=', '1')
    ->where('username', '=', $username)
    ->where('password', '=', $password)
    ->orderBy('id','DESC')
    ->first();

    #-------------------------------------------------------
    // Find the student's account with the given username but wrong password
    $check_username = AccountStudent::where('active', '=', '1')
    ->where('username', '=', $username)
    ->where('password', '!=', $password)
    ->orderBy('id','DESC')
    ->first();

    #-------------------------------------------------------
    // If the student's credentials match
    if($account_student){
      $request_result['result'] = 'existing';
      $request_result['id'] = $account_student->STUDENT_id;

      //------------------------------------------------------------------------
      // STORE SESSION VALUES PLEASE APPEN SES_ PREFIX IN SESSION VALUES
      // ALL UPPER CASE
      // THIS SESSIONS VALUES ARE REQUIRED and must be added upon login.
      session()->put('SES_AUTHENTICATED','YES');
      session()->put('SES_ACCOUNT_ID',$account_student->id);
      session()->put('SES_CICT_ID',$account_student->STUDENT_id);
      session()->put('SES_USERNAME',$account_student->username);
      //------------------------------------------------------------------------
    }

    #-------------------------------------------------------
    // If username exists but entered the wrong password
    else if($check_username){
      $request_result['result'] = 'wrong_pass';
    #-------------------------------------------------------
    // If account does not exists
    }else{
      $request_result['result'] = 'not_existing';
    }

    #-------------------------------------------------------
    // send response
    echo json_encode($request_result, JSON_FORCE_OBJECT);
  }

  public function get_all_announcements(){
    $collection = array();
    #------------------------------------------------------
    // get all announcements
    $all = Announcements::where('active','=',1)
    ->orderBy('id','DESC')
    ->take(5)
    ->get();

    #------------------------------------------------------
    // if an announcement exists
    if($all){
      foreach ($all as $each) {
      $faculty = Faculty::where('id','=',$each->announced_by)
      ->where('active','=',1)
      ->first();

      $date_time = Carbon::parse($each->date);

      $single_row = [];
      $single_row['all'] = $each;
      $single_row['date_time'] = $date_time->format('F d, Y g:ia');
      $single_row['faculty'] = $faculty;

      array_push($collection, $single_row);
      }
    //if there are no announcements
    }else{
    }

    #------------------------------------------------------
    // send response
    echo json_encode($collection, JSON_OBJECT_AS_ARRAY);
  }

  public function show_more_announcements(){
    return view('announcements.more-announcements');
  }

    public function get_more_announcements(){
      $collection = array();
      #------------------------------------------------------
      // get all announcements
      $all = Announcements::where('active','=',1)
      ->orderBy('id','DESC')
      ->get();

      #------------------------------------------------------
      // if an announcement exists
      if($all){
        foreach ($all as $each) {
        $faculty = Faculty::where('id','=',$each->announced_by)
        ->where('active','=',1)
        ->first();

        $date_time = Carbon::parse($each->date);

        $single_row = [];
        $single_row['all'] = $each;
        $single_row['date_time'] = $date_time->format('F d, Y g:ia');
        $single_row['faculty'] = $faculty;

        array_push($collection, $single_row);
        }
      //if there are no announcements
      }else{}

      #------------------------------------------------------
      // send response
      echo json_encode($collection, JSON_OBJECT_AS_ARRAY);
    }

    public function show_teacher_finder(){
      return view('teacher_finder.teacher_finder');
    }

  public function get_faculty_name(Request $request){
    #--------------------------------------------------------
    //search faculty name
    $faculty = Faculty::where('last_name','like','%'. $request['txt_faculty_sched'] .'%')
    ->where('active','=',1)
    ->get();

    //if faculty table does not contain a single faculty info
    if($faculty->isEmpty()){
      $request = "No data";
    }else {
      $request = $faculty;
    }

    #--------------------------------------------------------
    //send response
    echo json_encode($request, JSON_FORCE_OBJECT);
  }

  public function get_faculty_sched($id){
    $collection = array();
    #------------------------------------------------------
    // See if faculty exists
    $faculty = Faculty::where('id', '=', $id)
    ->where('active', '=', '1')
    ->first();

    if($faculty){
    #------------------------------------------------------
    // Get faculty load group
    $load_group = LoadGroup::where('faculty', '=', $id)
    ->where('active', '=', '1')
    ->get();

    if($load_group->isEmpty()){
      $single_row['result'] = "No load_group";
      array_push($collection,$single_row);
    }else{
    #------------------------------------------------------
    //loop through each group
    foreach ($load_group as $each){
      #------------------------------------------------------
      //get schedule of subject if there is
      $load_group_schedule = LoadGroupSchedule::where('load_group_id', '=', $each->id)
      ->where('active', '=', '1')
      ->orderBy('class_start','DESC')
      ->get();

      $subject = Subject::where('id','=',$each->SUBJECT_id)
      ->where('active','=','1')
      ->get();

      #------------------------------------------------------
      $single_row  = [];
      $single_row['load_group'] = $each;
      $single_row['load_group_schedule'] = $load_group_schedule;
      $single_row['subject'] = $subject;

      array_push($collection,$single_row);
      }
     }
   }else {
     $single_row['result'] = "No faculty matched";
     array_push($collection,$single_row);
   }
  #------------------------------------------------------
  // format collection
  $array_result = json_encode($collection,JSON_OBJECT_AS_ARRAY);

  #------------------------------------------------------
  // send result
  echo $array_result;
}

}
