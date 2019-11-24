<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\Faculty;
use App\Evaluation;
use App\AcademicTerm;
use App\Student;
use App\Curriculum;
use Carbon\Carbon;

class Student_Evaluation_History extends Controller
{
  public function view_eval(Request $request){
    $collection = array();
    $id = $request->session()->get('SES_CICT_ID');

    #------------------------------------------------------
    //find student with the given params
    $student = Student::where('active','=','1')
    ->where('CICT_id', '=', $id)
    ->first();

    if($student){
      #------------------------------------------------------
      //get curriculum info of student
      $cur = Curriculum::where('active','=','1')
      ->where('id', '=', $student->CURRICULUM_id)
      ->first();

      #------------------------------------------------------
      //get evaluation records of student
      $eval = Evaluation::where('STUDENT_id', '=', $id)
      ->get();

      if($eval->isEmpty()){
        $single_row['result'] = "No Evaluation History";
        array_push($collection,$single_row);
      }else{
      #------------------------------------------------------
      //loop through each evaluation record and get academic term info
      foreach ($eval as $each){
        $acad = AcademicTerm::where('id','=',$each->ACADTERM_id)
        ->where('active','=','1')
        ->first();

        $evaluator = Faculty::where('id','=',$each->FACULTY_id)
        ->where('active','=','1')
        ->first();

        $faculty = Faculty::where('id','=',$each->cancelled_by)
        ->where('active','=','1')
        ->first();

        $date_time = Carbon::parse($each->cancelled_date);

        $single_row = [];
        $single_row['eval'] = $each;
        $single_row['acad'] = $acad;
        $single_row['cancelled_date'] = $date_time->format('m/d/Y g:ia');
        $single_row['evaluator'] = $evaluator;
        $single_row['faculty'] = $faculty;

        array_push($collection,$single_row);
      }
    }

    }else{
      $single_row['result'] = "Student not found";
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
