package update.org.cict.controller.adding;

import app.lazy.models.*;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import update.org.cict.TransactionHelperFunctions;

import java.util.ArrayList;

public class CheckEvaluatedSubjects extends Transaction {

    public EvaluationMapping evaluationMap;
    public StudentMapping studentMap;

    private ArrayList<SubjectInformationHolder> subjectCollection;

    public ArrayList<SubjectInformationHolder> getSubjectCollection() {
        return subjectCollection;
    }

    @Override
    protected boolean transaction() {

        ArrayList<LoadSubjectMapping> evaluatedSubjects = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, evaluationMap.getId())
                .eq(DB.load_subject().STUDENT_id, studentMap.getCict_id())
                .active()
                .all();

        subjectCollection = new ArrayList<>();
        for (LoadSubjectMapping lsMap : evaluatedSubjects) {
            SubjectInformationHolder subjectHolder = new SubjectInformationHolder();

            LoadGroupMapping lgMap = Mono.orm()
                    .newSearch(Database.connect().load_group())
                    .eq(DB.load_group().id, lsMap.getLOADGRP_id())
                    .execute()
                    .first();

            subjectHolder.setLoadGroup(lgMap);

            // add subject
            SubjectMapping subject = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq(DB.subject().id, lsMap.getSUBJECT_id())
                    .execute()
                    .first();

            subjectHolder.setSubjectMap(subject);

            LoadSectionMapping section = TransactionHelperFunctions.
                    getSectionFromLoadGroup(lsMap.getLOADGRP_id());

            subjectHolder.setSectionMap(section);

            AcademicProgramMapping acadprog = Mono.orm()
                    .newSearch(Database.connect().academic_program())
                    .eq(DB.academic_program().id, section.getACADPROG_id())
                    .execute()
                    .first();

            subjectHolder.setAcademicProgramMapping(acadprog);
            subjectCollection.add(subjectHolder);
        }

        // data are gathered prepare the table contruction.
        // end transaction.
        return true;
    }

    @Override
    protected void after() {

    }

}
