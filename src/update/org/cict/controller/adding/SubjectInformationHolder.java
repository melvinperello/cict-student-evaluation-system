package update.org.cict.controller.adding;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;

public class SubjectInformationHolder {

    private LoadSectionMapping sectionMap;
    private SubjectMapping subjectMap;
    private AcademicProgramMapping academicProgramMapping;
    //--------------------------------------------------------------------------
    /**
     * Added Value
     *
     * @date 08.24.2017
     */
    private LoadGroupMapping loadGroup;
    private LoadGroupMapping loadGroup_changed;

    public LoadGroupMapping getLoadGroup_changed() {
        return loadGroup_changed;
    }

    public void setLoadGroup_changed(LoadGroupMapping loadGrp_changed) {
        this.loadGroup_changed = loadGrp_changed;
    }

    public LoadGroupMapping getLoadGroup() {
        return loadGroup;
    }
    
    public void setLoadGroup(LoadGroupMapping loadGroup) {
        this.loadGroup = loadGroup;
    }

    //--------------------------------------------------------------------------
    public SubjectInformationHolder() {

    }

    public LoadSectionMapping getSectionMap() {
        return sectionMap;
    }

    public void setSectionMap(LoadSectionMapping sectionMap) {
        this.sectionMap = sectionMap;
    }

    public SubjectMapping getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(SubjectMapping subjectMap) {
        this.subjectMap = subjectMap;
    }

    public AcademicProgramMapping getAcademicProgramMapping() {
        return academicProgramMapping;
    }

    public void setAcademicProgramMapping(AcademicProgramMapping academicProgramMapping) {
        this.academicProgramMapping = academicProgramMapping;
    }

    /**
     * @return
     */
    public String getFullSectionName() {
        String sectionName = "";
        
        try{
            sectionName = this.academicProgramMapping.getCode() + " "
                + this.sectionMap.getYear_level()
                + this.sectionMap.getSection_name()
                + "-G"
                + this.sectionMap.get_group();
        } catch(NullPointerException a) {
            sectionName = this.sectionMap.getSection_name();
        }
        return sectionName;
    }
    
    public String getFullSectionNameOfChanged() {
        String sectionName = "";
        LoadSectionMapping section = Mono.orm().newSearch(Database.connect().load_section())
                .eq(DB.load_section().id, loadGroup_changed.getLOADSEC_id())
                .active()
                .first();
        try{
            sectionName = this.academicProgramMapping.getCode() + " "
                + section.getYear_level()
                + section.getSection_name()
                + "-G"
                + section.get_group();
        } catch(NullPointerException a) {
            sectionName = section.getSection_name();
        }
        return sectionName;
    }
}
