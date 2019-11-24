<?php
namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\Subject;
use App\Grade;
use App\Evaluation;
use App\AcademicTerm;
use PDF;
use App\Student;
use Carbon\Carbon;
use App\Curriculum;

class Student_Grade extends Controller
{
  public function get_grade(Request $request){
    $collection = array();
    $id = $request->session()->get('SES_CICT_ID');

    #------------------------------------------------------
    // Get the evaluation records of student
    $eval = Evaluation::where('STUDENT_id', '=', $id)
    ->where('active','=','1')
    ->where('remarks','=','ACCEPTED')
    ->get();

    if($eval){
      // Loop through every evaluation record
      foreach ($eval as $each){
        $collection2 = array();

        #------------------------------------------------------
        // Get academic term record
        $acad = AcademicTerm::where('id','=',$each->ACADTERM_id)
        ->where('active','=','1')
        ->first();

        #------------------------------------------------------
        // Get grades on each academic term
        $grade = Grade::where('STUDENT_id','=',$id)
        ->where('ACADTERM_id','=',$each->ACADTERM_id)
        ->where('active','=','1')
        ->get();

        if($grade->isEmpty()){
          // if grades is empty
          $single['result'] = "No grade record of student";
          array_push($collection2,$single);
        }else{
          // Loop through each grades and get data and corresponding subject info
          foreach ($grade as $each_grade){
            $subject = Subject::where('id','=',$each_grade->SUBJECT_id)
            ->where('active','=','1')
            ->first();

            $grades_row['grade'] = $each_grade;
            $grades_row['subject'] = $subject;

            array_push($collection2,$grades_row);
          }
        }

        $single_row = [];
        $single_row['eval'] = $each;
        $single_row['acad'] = $acad;

        #------------------------------------------------------
        //push grades on each row
        array_push($single_row,$collection2);
        array_push($collection,$single_row);
      }
    }else{
      $single_row['result'] = "No evaluation record of student";
      array_push($collection,$single_row);
    }

    #------------------------------------------------------
    // format collection
    $array_result = json_encode($collection,JSON_OBJECT_AS_ARRAY);

    #------------------------------------------------------
    // send result
    echo $array_result;
  }

  public function view_pdf($year_sem, Request $request){
        $id = $request->session()->get('SES_CICT_ID');

        $year_sem = explode("_", $year_sem);
        $year = $year_sem[0];
        $sem = $year_sem[1];

        #------------------------------------------------------
        //find student with the given parameter
        $student = Student::where('cict_id', '=', $id)
        ->where('active', '=', '1')
        ->orderBy('id','DESC')
        ->first();

        $collection = array();
        if($student){
        $cur = Curriculum::where('id','=',$student->CURRICULUM_id)
        ->where('active','=','1')
        ->first();

        if($cur){
          $cur=$cur->description;
        }else {
          $cur = "----";
        }

        $acad = AcademicTerm::where('school_year','=',$year)
        ->where('semester','=',$sem)
        ->where('active','=','1')
        ->first();

        if($acad){
          $grade = Grade::where('STUDENT_id','=',$id)
          ->where('ACADTERM_id','=',$acad->id)
          ->where('active','=','1')
          ->get();

          if($grade->isEmpty()){
            // if grades is empty
            $single['result'] = "No grade record of student";
            array_push($collection,$single);
          }else{
            // Loop through each grades and get data and corresponding subject info
            foreach ($grade as $each_grade){
              $subject = Subject::where('id','=',$each_grade->SUBJECT_id)
              ->where('active','=','1')
              ->first();

              $grades_row['grade'] = $each_grade;
              $grades_row['subject'] = $subject;

              array_push($collection,$grades_row);
            }

            $date = Carbon::now('Asia/Manila');
            $date = $date->format('F d, Y g:ia');

            #------------------------------------------------------
            $view =\View::make('pdf.student_grade_pdf',['student' => $student, 'cur' => $cur, 'collection' => $collection, 'year_sem' => $year." ".$sem, 'time' => $date ]);
            $html_content = $view->render();
            //  PDF::new TCPDF('L', 'mm', array(210,97), true, 'UTF-8', false);
            PDF::SetTitle($student->last_name.', '.$student->first_name.' '.$student->middle_name.' '.$year.' '.$sem);
            //  PDF::SetMargins(25,17,25, true);
            //  $resolution= array(165, 172);
            //  PDF::AddPage('P', $resolution);
            PDF::SetFont('gothic');
            PDF::AddPage();
            PDF::writeHTML($html_content, true, false, true, false, '');
            PDF::Output($student->last_name.', '.$student->first_name.' '.$student->middle_name.' '.$year.' '.$sem.'.pdf');
          }
        }
        }
      else{
        return redirect()->route('error-status', 'Failed');
      }
    }//function

}
