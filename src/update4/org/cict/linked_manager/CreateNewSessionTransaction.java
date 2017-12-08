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
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package update4.org.cict.linked_manager;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LinkedPila3fMapping;
import app.lazy.models.LinkedPila4fMapping;
import app.lazy.models.LinkedPilaMapping;
import app.lazy.models.LinkedSettingsMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.async.TransactionException;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * This transaction is used to create new session with the following conditions.
 * when there are still students that are in line, when the changing of
 * transaction was made (CUT OFF). the students should be added in the next
 * session relative to their current number.
 *
 * When there are 1000 students
 *
 * And cut off was made from 800
 *
 * student number 801 should be the number 1 in the next session.
 *
 * @author Jhon Melvin
 */
public class CreateNewSessionTransaction extends Transaction {

    //--------------------------------------------------------------------------
    private final static String TABLE_3F = "linked_pila_3f";
    private final static String TABLE_4F = "linked_pila_4f";
    //--------------------------------------------------------------------------
    // Required
    private Integer floor3Max;
    private Integer floor4Max;
    private String floor3Name;
    private String floor4Name;
    private Integer createdBy;
    //--------------------------------------------------------------------------

    public void setFloor3Max(Integer floor3Max) {
        this.floor3Max = floor3Max;
    }

    public void setFloor4Max(Integer floor4Max) {
        this.floor4Max = floor4Max;
    }

    public void setFloor3Name(String floor3Name) {
        this.floor3Name = floor3Name;
    }

    public void setFloor4Name(String floor4Name) {
        this.floor4Name = floor4Name;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    protected boolean transaction() {
        //----------------------------------------------------------------------
        // Create Local Session
        Session localSession = Mono.orm().openSession();
        org.hibernate.Transaction dataTx = localSession.beginTransaction();
        //----------------------------------------------------------------------
        // Get Current linked_settings as an array
        // Just incase a glitch occured update everything
        ArrayList<LinkedSettingsMapping> current_active_settings = Mono.orm()
                .newSearch(Database.connect().linked_settings())
                .active(Order.desc(DB.linked_settings().id))
                .all();
        //----------------------------------------------------------------------
        if (current_active_settings != null) {
            // if there are active settings deactivate them
            // but first get the latest setting
            // this part of code should not throw index out of bound because there is a result
            LinkedSettingsMapping latest_active_setting = current_active_settings.get(0);
            for (LinkedSettingsMapping current_active_setting : current_active_settings) {
                // deactivate all active settings
                current_active_setting.setActive(0);
                try {
                    Database.connect().linked_settings().transactionalSingleUpdate(localSession, current_active_setting);
                } catch (Exception e) {
                    dataTx.rollback();
                    return false;
                }
                //--------------------------------------------------------------
            }
            //------------------------------------------------------------------
            // Get All students that are not yet catered in the linked_pila table
            // Criteria for uncatered students are the following
            ArrayList<LinkedPilaMapping> inline_students = Mono
                    .orm().newSearch(Database.connect().linked_pila())
                    //----------------------------------------------------------
                    // Only from the current settings ID
                    .eq(DB.linked_pila().SETTINGS_id, latest_active_setting.getId())
                    //----------------------------------------------------------
                    .eq(DB.linked_pila().status, "INLINE")
                    .eq(DB.linked_pila().remarks, "NONE")
                    .isNull(DB.linked_pila().request_called)
                    .isNull(DB.linked_pila().request_validity)
                    //----------------------------------------------------------
                    // This is very important order by ascending first come first serve
                    .active(Order.asc(DB.linked_pila().id))
                    //----------------------------------------------------------
                    .all();
            //------------------------------------------------------------------
            // reset numbering
            try {
                // since truncating tables are ireversible
                // rollbacks don't work with them
                // mark this settings as DEAD once truncated
                latest_active_setting.setMark("DEAD");
                boolean updated = Database.connect().linked_settings().update(latest_active_setting);
                if (updated) {
                    this.truncateNumberingTables(localSession);
                } else {
                    throw new TransactionException("FAILED TO UPDATE MARK");
                }
            } catch (Exception e) {
                dataTx.rollback();
                return false;
            }
            //------------------------------------------------------------------
            // create the new session
            LinkedSettingsMapping new_session = createNewSession();
            //------------------------------------------------------------------
            try {
                Integer new_session_id = Database.connect().linked_settings().transactionalInsert(localSession, new_session);
            } catch (Exception e) {
                dataTx.rollback();
                return false;
            }
            //------------------------------------------------------------------
            // transfer the current inline students from the new session
            // day 1 200  students left (#800 - #1000)
            // transfer them to the next session day 2
            if(inline_students != null || !inline_students.isEmpty()) {
                for (LinkedPilaMapping transfering : inline_students) {
                    if (transfering.getFloor_assignment().equals(3)) {
                        try {
                            // floor 3 cluster 1
                            // Create new Entry for the student in the pila_table
                            LinkedPilaMapping new_pila = transferData(transfering, new_session.getId(), new_session.getFloor_3_name());
                            int new_pila_id = Database.connect().linked_pila().transactionalInsert(localSession, new_pila);
                            // Create new numbering entry
                            LinkedPila3fMapping new_pila_number = new LinkedPila3fMapping();
                            new_pila_number.setPila_id(new_pila_id);
                            int new_floor_number = Database.connect().linked_pila_3f().transactionalInsert(localSession, new_pila_number);
                            // assign new floor number
                            new_pila.setFloor_number(new_floor_number);
                            Database.connect().linked_pila().transactionalSingleUpdate(localSession, new_pila);
                        } catch (Exception e) {
                            dataTx.rollback();
                            return false;
                        }
                    } else if (transfering.getFloor_assignment().equals(4)) {
                        try {
                            // floor 4 cluster 2
                            LinkedPilaMapping new_pila = transferData(transfering, new_session.getId(), new_session.getFloor_4_name());
                            int new_pila_id = Database.connect().linked_pila().transactionalInsert(localSession, new_pila);
                            // Create new numbering entry
                            LinkedPila4fMapping new_pila_number = new LinkedPila4fMapping();
                            new_pila_number.setPila_id(new_pila_id);
                            int new_floor_number = Database.connect().linked_pila_4f().transactionalInsert(localSession, new_pila_number);
                            // assign new floor number
                            new_pila.setFloor_number(new_floor_number);
                            Database.connect().linked_pila().transactionalSingleUpdate(localSession, new_pila);
                        } catch (Exception e) {
                            dataTx.rollback();
                            return false;
                        }
                    } else {
                        // do nothing
                    }
                    //--------------------------------------------------------------
                    // Deactivate the old one.
                    transfering.setActive(0);
                    transfering.setStatus("VOID");
                    transfering.setRemarks("TRANSFERRED");
                    Database.connect().linked_pila().transactionalSingleUpdate(localSession, transfering);
                    //--------------------------------------------------------------
                }
            }
        } else {
            // IF FIRST TIME
            // no active settings no active pila no active kahit ano
            //------------------------------------------------------------------
            // just to be sure that this is really the first time
            // paranoid LVL 9999999999999
            ArrayList<LinkedPilaMapping> check_kung_wala_talagang_pila = Mono.orm()
                    .newSearch(Database.connect().linked_pila())
                    .active()
                    .all();
            if (check_kung_wala_talagang_pila != null) {
                // may laman pkidelete
                check_kung_wala_talagang_pila.forEach(bugger -> {
                    bugger.setActive(0);
                    bugger.setStatus("VOID");
                    bugger.setRemarks("TRANSFERRED");
                    Database.connect().linked_pila().transactionalSingleUpdate(localSession, bugger);
                });
            }

            //------------------------------------------------------------------
            this.truncateNumberingTables(localSession);
            //------------------------------------------------------------------

            LinkedSettingsMapping new_session = createNewSession();
            try {
                Integer new_session_id = Database.connect().linked_settings().transactionalInsert(localSession, new_session);
            } catch (Exception e) {
                dataTx.rollback();
                return false;
            }
        }

        dataTx.commit();
        return true;
    }

    /**
     * Create new Session for insertion.
     *
     * @return
     */
    private LinkedSettingsMapping createNewSession() {
        LinkedSettingsMapping new_session = new LinkedSettingsMapping();
        new_session.setFloor_3_max(this.floor3Max);
        new_session.setFloor_4_max(this.floor4Max);
        new_session.setFloor_3_name(this.floor3Name);
        new_session.setFloor_4_name(this.floor4Name);
        new_session.setCreated_by(this.createdBy);
        return new_session;
    }

    /**
     * Reset numbering values.
     *
     * @param localSession
     */
    private void truncateNumberingTables(Session localSession) {
        // Truncate the table to reset numbering ids
        localSession.createNativeQuery("truncate table " + TABLE_3F).executeUpdate();
        localSession.createNativeQuery("truncate table " + TABLE_4F).executeUpdate();
    }

    /**
     * Copy everything except floor number since new numbers will be assigned
     * relatively.
     *
     * @param transferring
     */
    private LinkedPilaMapping transferData(LinkedPilaMapping transferring, Integer setting_id, String new_cluster_name) {
        LinkedPilaMapping new_pila = new LinkedPilaMapping();
        new_pila.setACADTERM_id(transferring.getACADTERM_id());
        new_pila.setSTUDENT_id(transferring.getSTUDENT_id());
        new_pila.setACCOUNT_id(transferring.getACCOUNT_id());
        //----------------------------------------------------------------------
        // Session Already not going to copy
        new_pila.setSETTINGS_id(setting_id);
        //----------------------------------------------------------------------
        new_pila.setNOTIFIED(transferring.getNOTIFIED());
        new_pila.setConforme(transferring.getConforme());
        new_pila.setCourse(transferring.getCourse());
        new_pila.setImei(transferring.getImei());
        new_pila.setRequest_accepted(transferring.getRequest_accepted());
        new_pila.setRequest_called(transferring.getRequest_called());
        new_pila.setRequest_validity(transferring.getRequest_validity());
        new_pila.setFloor_assignment(transferring.getFloor_assignment());
        //----------------------------------------------------------------------
        // new number do not copy
        // new_pila.setFloor_number(----);
        // new setting so cluster name for the floor may be different
        new_pila.setCluster_name(new_cluster_name);
        //----------------------------------------------------------------------
        new_pila.setStatus(transferring.getStatus());
        new_pila.setRemarks(transferring.getRemarks());
        //----------------------------------------------------------------------
        // return this new copy for insertion
        return new_pila;
    }

}
