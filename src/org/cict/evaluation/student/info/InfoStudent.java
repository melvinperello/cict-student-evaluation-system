/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 *
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 *
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.evaluation.student.info;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import artifacts.MonoString;
import com.jhmvin.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.control.ComboBox;
import javax.persistence.PersistenceException;
import org.cict.evaluation.student.StudentValues;
import org.controlsfx.control.Notifications;
import org.hibernate.exception.ConstraintViolationException;


/**
 *
 * @author Joemar
 */
public class InfoStudent {
    
    public String removeExtraSpace(String value){
        return MonoString.removeExtraSpace(value);
    }
      
    public Integer selectedYearLevel;
    public void setSelectedYearLevel(String yrlvl){
        selectedYearLevel = null;
        if(!yrlvl.equalsIgnoreCase(StudentValues.ALL))
            this.selectedYearLevel = sv.getYearLevel(yrlvl);
    }
        
    private final StudentValues sv = new StudentValues();
    public void setCmbYearLevel(ComboBox cmb_yrlvl){
        cmb_yrlvl.getItems().addAll(sv.getYearLevels());
        cmb_yrlvl.getSelectionModel().selectFirst();
        setSelectedYearLevel(cmb_yrlvl.getSelectionModel().getSelectedItem().toString());
    }
     
    public void setCmbGender(ComboBox cmb_gender){
        cmb_gender.getItems().addAll(sv.getGenders());
        cmb_gender.getSelectionModel().selectFirst();
    }
    
    public void setCmbEnrollmentType(ComboBox cmb_etype){
        cmb_etype.getItems().addAll(sv.getEnrollmentTypes());
        cmb_etype.getSelectionModel().selectFirst();
    }
    
    public void setCmbCurriculum(ComboBox cmb_curriculum){
        ArrayList<String> curriculums = new ArrayList();
        List cur = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .active()
                .all();
        for (int i = 0; i < cur.size(); i++) {
            CurriculumMapping curriculum = (CurriculumMapping) cur.get(i);
            curriculums.add(curriculum.getName());
        }
        cmb_curriculum.getItems().addAll(curriculums);
        cmb_curriculum.getSelectionModel().selectFirst();
    }
      
    public int validateStudent(Integer cict_id, String id){
        if(findInactive(id)){
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Content Restricted")
                    .setMessage("")
                    .showAndWait();
            return -1; //inactive
        } else if(checkExist(cict_id, id) != 0){
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Student Exist")
                    .setMessage("The student with a student number of " + id + 
                            " already exist.")
                    .showAndWait();
            System.out.println("@InfoStudent: Student exist");
            return -1; //exist
        } else {
            //update
            return 0;
        }
    }
        
    public int checkExist(Integer cict_id, String id){
        StudentMapping student = Mono.orm().newSearch(Database.connect().student())
                .eq("id", id)
                .active()
                .first();
        if(Objects.equals(student.getCict_id(), cict_id))
            return 0;
        else 
            return 1;
            
    }
     
    public boolean findInactive(String id){
        return !Mono.orm().newSearch(Database.connect().student())
                .eq("id", id)
                .inactive()
                .isEmpty();
    }
    
    private String selectedStudentNo;
    public void setSelectedStudent(String id){
        this.selectedStudentNo = id;
    }
    
    public StudentMapping getValues(){
        student = Mono.orm().newSearch(Database.connect().student())
                .eq("id", this.selectedStudentNo)
                .execute()
                .first(); //studentModel.getBy("id", selectedStudentNo);
        if(student.getActive()==1){
            return student;
        }
        return null;
    }
    
    public boolean isActive(String id){
        try{
            StudentMapping student = Mono.orm()
                    .newSearch(Database.connect().student())
                    .eq("id", this.selectedStudentNo)
                    .execute()
                    .first(); //(StudentMapping) studentModel.getBy("id", id);
            if(student.getActive()==0){
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Content Restricted")
                        .setMessage("")
                        .showAndWait();
                return false;
            }
        return true;
        }catch(NullPointerException a){
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Student Not Found")
                    .setMessage("No student found with the student number given.")
                    .showAndWait();
//            MonoDialog.showAlert("Student Not Found", "No student found with the student number given.");
            return false;
        }
    }
    
    public String getCurriculumName(Integer id){
        CurriculumMapping c = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .eq("id", id)
                .execute()
                .first(); //(CurriculumMapping) curriculumModel.getBy("id", id);
        return c.getName();
    }
    
    public Integer getCurriculumId(String name){
        CurriculumMapping c = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .eq("name", name)
                .execute()
                .first(); //(CurriculumMapping) curriculumModel.getBy("name", name);
        return c.getId();
    }
    
    public StudentMapping student = new StudentMapping();
    public boolean updateStudent(ArrayList values){
        try{
            student.setId(values.get(0).toString());
            student.setLast_name(values.get(1).toString());
            student.setFirst_name(values.get(2).toString());
            student.setMiddle_name(values.get(3).toString());
            student.setGender(values.get(4).toString());
            student.setCURRICULUM_id(Integer.valueOf(values.get(5).toString()));
            student.setYear_level(Integer.valueOf(values.get(6).toString()));
            student.setSection(values.get(7).toString());
            student.set_group(Integer.valueOf(values.get(8).toString()));
            student.setEnrollment_type(values.get(9).toString());
//            student.setCreated_by(_UPDATED_BY);
//            student.setCreated_date(_UPDATED_DATE);
            Boolean res = Database.connect()
                    .student()
                    .update(student);//studentModel.update(student);
            if(res){
                System.out.println("@InfoStudent: UPDATE SUCCESS");
                return true;
            } else if(Objects.equals(res, Boolean.FALSE)){
                Notifications.create()
                        .title("Nothing Happened")
                        .text("There is a referenced value violated "
                                + "in the values. Update is not yet possible.")
                        .showError();
            } else {
                Notifications.create()
                        .title("Connection Error")
                        .text("Please check your connectivity to the server.")
                        .showError();
            }
        }catch(IndexOutOfBoundsException s){}
        return false;
    }
     
//    public static boolean remove(String id) {
//        try{
//            StudentMapping subj = (StudentMapping) studentModel.getBy("id", id);
//            subj.setActive(0);
//            return studentModel.update(subj);
//        }catch(NullPointerException e){
//            return false;
//        }
//    }
}
