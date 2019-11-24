<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\LoadGroupSchedule;
use App\LoadSubject;
use App\LoadGroup;
use App\Subject;
use App\Faculty;
use App\Evaluation;
use App\AcademicTerm;
use App\Student;
use App\Curriculum;

class Student_Schedule extends Controller
{
  public function view_sched(Request $request){
    $collection = array();
    $id = $request->session()->get('SES_CICT_ID');

    #------------------------------------------------------
    // Get students subjects
    $load_subject = LoadSubject::where('STUDENT_id', '=', $id)
    ->where('active', '=', '1')
    ->orderBy('id','DESC')
    ->get();

    if($load_subject->isEmpty()){
      $single_row['result'] = "no load subjects";
      array_push($collection,$single_row);
    }else{
    #------------------------------------------------------
    //loop through each subject
    foreach ($load_subject as $each){
      $subject = Subject::where('id','=',$each->SUBJECT_id)
      ->where('active','=','1')
      ->get();

      #------------------------------------------------------
      //get schedule of subject if there is
      $load_group_schedule = LoadGroupSchedule::where('load_group_id', '=', $each->LOADGRP_id)
      ->where('active', '=', '1')
      ->orderBy('class_start','DESC')
      ->get();

      #------------------------------------------------------
      //get load_group info for the faculty id
      $load_group = LoadGroup::where('id', '=', $each->LOADGRP_id)
      ->where('active', '=', '1')
      ->get();

      #------------------------------------------------------
      //get each faculty info
      foreach ($load_group as $each_faculty) {
        $faculty = Faculty::where('id', '=', $each_faculty->faculty)
        ->where('active', '=', '1')
        ->get();
      }

      $single_row  = [];
      $single_row['subject'] = $subject;
      $single_row['load_group_schedule'] = $load_group_schedule;
      $single_row['load_group'] = $load_group;
      $single_row['faculty'] = $faculty;

      array_push($collection,$single_row);
    }
  }
  #------------------------------------------------------
  // format collection
  $array_result = json_encode($collection,JSON_OBJECT_AS_ARRAY);

  #------------------------------------------------------
  // send result
  echo $array_result;
  }

  public function get_current_term(Request $request){
    $id = $request->session()->get('SES_CICT_ID');

    #------------------------------------------------------
    //get current term
    $acad = AcademicTerm::where('current','=',1)
    ->where('active','=','1')
    ->first();

    if($acad){
      $reply['acad_term'] = $acad;
    }else{
      $reply['acad_term'] = "no current term";
    }
    #------------------------------------------------------
    //get student info
    $student = Student::where('active', '=', '1')
    ->where('cict_id', '=', $id)
    ->orderBy('id','DESC')
    ->first();

    if($student){
      $reply['section'] = $student;
    }else{
      $reply['section'] = "no student record";
    }
    #------------------------------------------------------
    //get curriculum info of student
    $cur  = Curriculum::where('active', '=', '1')
    ->where('id', '=', $student->CURRICULUM_id)
    ->orderBy('id','DESC')
    ->first();

    if($cur){
      $reply['course'] = $cur;
    }else{
      $reply['course'] = "no curriculum";
    }
    #------------------------------------------------------
    // send response
    echo json_encode($reply, JSON_FORCE_OBJECT);
  }
}
