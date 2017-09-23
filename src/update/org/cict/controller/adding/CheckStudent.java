package update.org.cict.controller.adding;

import app.lazy.models.*;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import update.org.cict.TransactionHelperFunctions;

import java.util.ArrayList;

public class CheckStudent extends Transaction {

    public String studentNumber;
    public Integer acadTermID;


    /**
     * Transaction Variables.
     */
    private final String TYPE_REGULAR = "REGULAR";
    private final String TYPE_ADDING_CHANGING = "ADDING_CHANGING";
    private final String REMARKS_ACCEPTED = "ACCEPTED";
    /**
     * Result handler of this transaction.
     */
    private String txResult = "";
    private StudentMapping studentMap;
    private EvaluationMapping evaluationMap;
    private String studentSection;

    public String getTxResult() {
        return txResult;
    }

    public StudentMapping getStudentMap() {
        return studentMap;
    }

    public String getStudentSection() {
        return studentSection;
    }

    public EvaluationMapping getEvaluationMap() {
        return evaluationMap;
    }

    @Override
    protected boolean transaction() {
        // Search for the student
        studentMap = Mono.orm()
                .newSearch(Database.connect().student())
                .eq(DB.student().id, studentNumber)
                .active()
                .first();


        if (studentMap == null) {
            txResult = "STUDENT_NOT_EXIST";
            return true;
        }

        // student course name
        AcademicProgramMapping aMap = TransactionHelperFunctions.getAcademicProgram(studentMap.getCURRICULUM_id());
        studentSection = aMap.getName() + " | " +
                studentMap.getYear_level() + studentMap.getSection() +
                "-G" + studentMap.get_group();

        evaluationMap = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().STUDENT_id, studentMap.getCict_id())
                .eq(DB.evaluation().ACADTERM_id, this.acadTermID)
                //.eq(DB.evaluation().type, this.TYPE_REGULAR)
                .eq(DB.evaluation().remarks, this.REMARKS_ACCEPTED)
                .active()
                .first();

        if (evaluationMap != null) {
            if(evaluationMap.getType().equalsIgnoreCase(this.TYPE_ADDING_CHANGING)) {
                txResult = "RE_EVALUATE_ADD_CHANGE";
            }
            return true;
        } else {
            txResult = "NO_EVALUATION";
        }
        
        return true;
    }

    @Override
    protected void after() {

    }
}
