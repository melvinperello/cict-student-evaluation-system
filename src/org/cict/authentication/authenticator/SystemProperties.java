package org.cict.authentication.authenticator;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import com.jhmvin.Mono;
import javafx.application.Platform;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.MainApplication;
import org.cict.ThreadMill;
import org.hibernate.criterion.Order;

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

    private void terminate() {
        try {
            Platform.runLater(() -> {
                Mono.fx().alert().createWarning()
                        .setTitle("Warning")
                        .setHeader("System Checking")
                        .setMessage("There might be changes done in the server, to ensure data integrity the program needs to exit. Please Try Logging in Again, Thank You !")
                        .showAndWait();
                ThreadMill.threads().shutdown();
                MainApplication.die(0);
            });
        } catch (Exception e) {
            MainApplication.die(0);
        }

    }

    private AcademicTermMapping currentAcademicTerm;

    /**
     * Get Current Academic Term.
     *
     * @fixed: applied refresh every call.
     *
     * @return
     */
    public AcademicTermMapping getCurrentAcademicTerm() {
        try {
            AcademicTermMapping currentTerm = Mono.orm()
                    .newSearch(Database.connect().academic_term())
                    .eq(DB.academic_term().current, 1)
                    .active(Order.desc(DB.academic_term().id))
                    .first();

            this.currentAcademicTerm = currentTerm;

        } catch (Exception e) {
            terminate();
        }

        return currentAcademicTerm;
    }

    public void setCurrentAcademicTerm(AcademicTermMapping currentAcademicTerm) {
        this.currentAcademicTerm = currentAcademicTerm;
    }

    public String getCurrentTermString() {
        if(currentAcademicTerm==null)
            return null;
        return "AY " + currentAcademicTerm.getSchool_year() + " " + WordUtils.capitalizeFully(currentAcademicTerm.getSemester());
    }
}
