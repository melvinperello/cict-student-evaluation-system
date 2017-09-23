package org.cict.authentication.authenticator;

import app.lazy.models.AcademicTermMapping;
import org.apache.commons.lang3.text.WordUtils;

public class SystemProperties {

    private SystemProperties() {
        //
    }

    private static SystemProperties SYSMTEM_INSTANCE;

    public static SystemProperties instance() {
        if (SYSMTEM_INSTANCE == null) {
            SYSMTEM_INSTANCE = new SystemProperties();
        }
        return SYSMTEM_INSTANCE;
    }

    private AcademicTermMapping currentAcademicTerm;

    public AcademicTermMapping getCurrentAcademicTerm() {
        return currentAcademicTerm;
    }

    public void setCurrentAcademicTerm(AcademicTermMapping currentAcademicTerm) {
        this.currentAcademicTerm = currentAcademicTerm;
    }

    public String getCurrentTermString() {
        return "AY " + currentAcademicTerm.getSchool_year() + " " + WordUtils.capitalizeFully(currentAcademicTerm.getSemester());

    }
}
