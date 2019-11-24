<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Student;
use App\Subject;
use App\Grade;
use App\CurriculumSubject;
use App\Curriculum;
use App\Evaluation;
use App\AcademicTerm;

class Student_Summary extends Controller
{
  public function get_summary(Request $request){
    $id = $request->session()->get('SES_CICT_ID');

    #------------------------------------------------------
    // Find the student with the given parameters
      $student = Student::where('active', '=', '1')
      ->where('cict_id', '=', $id)
      ->orderBy('id','DESC')
      ->first();

   if($student){
    #------------------------------------------------------
    // Find the student's curriculum info
      $curriculum = Curriculum::where('active', '=', '1')
      ->where('id', '=', $student->CURRICULUM_id)
      ->orderBy('id','DESC')
      ->first();

    #------------------------------------------------------
    // Get curriculum info of preparatory if she/he has preparatory id
      $curriculum_prep = Curriculum::where('active', '=', '1')
      ->where('id', '=', $student->PREP_id)
      ->orderBy('id','DESC')
      ->first();

    #------------------------------------------------------
    //if student does not have a prep id. set out first curriculum from column CURRICULUM_id
    if($student->PREP_id == null){
      if($curriculum){
        $collection = $this->get_curriculum($curriculum->study_years, $curriculum->id, $id);
        $array_result = json_encode($collection,JSON_OBJECT_AS_ARRAY);
      }else{
        $data['result'] = 'curriculum_not_set';
        $array_result = json_encode($data,JSON_OBJECT_AS_ARRAY);
      }
    }//closing if prep_id is null
    #------------------------------------------------------
    //if prep id has a value. set it out first
    else if($student->PREP_id !== null){
      $collection = array();
      $collection2 = array();
      if($curriculum_prep){
        $collection = $this->get_curriculum($curriculum_prep->study_years, $curriculum_prep->id, $id);
      }else{
        $data['result'] = 'curriculum_not_set';
        echo json_encode($data,JSON_FORCE_OBJECT);
      }
      #------------------------------------------------------
      //then add as the conseq, the latest curriculum id
      if($curriculum){
        $collection2 = $this->get_curriculum($curriculum->study_years, $curriculum->id, $id);
        $collection = array_merge($collection,$collection2);
      }else{
        $data['result'] = 'curriculum_not_set';
        echo json_encode($data,JSON_FORCE_OBJECT);
      }
      $array_result = json_encode($collection,JSON_OBJECT_AS_ARRAY);
    }else{
      dd("error. wala ko makita");
    }
#--------------------------------------------------------------------------------------
  }else{
      //if no student record
      $data['result'] = 'no student record';
      $array_result = json_encode($data,JSON_OBJECT_AS_ARRAY);
  }
#--------------------------------------------------------------------------------------
  // send result
 echo $array_result;
}//function closing


  public function get_curriculum($study_years, $cur_id, $id){
    $collection = array();
    for($i=1; $i<=$study_years; $i++){
        $collection2 = array();
        for($e=1; $e<=2; $e++){
          $collection3 = array();
          $cur = CurriculumSubject::where('active', '=', '1')
            ->where('CURRICULUM_id', '=', $cur_id)
            ->where('year', '=', $i)
            ->where('semester', '=', $e)
            ->get();

          if($cur->isEmpty()){
              $single_row['result'] = 'curriculum subjects empty';
              array_push($collection3,$single_row);
          }else{
            foreach ($cur as $each) {
              $subject = Subject::where('active', '=', '1')
                ->where('id', '=', $each->SUBJECT_id)
                ->first();

              $grade = Grade::where('active', '=', '1')
                ->where('STUDENT_id', '=', $id)
                ->where('SUBJECT_id', '=', $each->SUBJECT_id)
                ->first();

                // create container
                $single_row = [];
                $single_row['cur'] = $each;
                $single_row['grade'] = $grade;
                $single_row['subject'] = $subject;
                array_push($collection3,$single_row);
            }
          }

            array_push($collection2,$collection3);
        }
        // add to collection
        array_push($collection,$collection2);
      }
    // format collection
    return $collection;
  }

}//class
