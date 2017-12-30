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
package org.bsu.cict.queue;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LinkedPilaMapping;
import app.lazy.models.StudentMapping;
import com.jhmvin.Mono;
import java.util.Date;
import javax.inject.Inject;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.bsu.cict.threads.DataTransaction;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class CallNextTransaction extends DataTransaction {

    @Inject
    private Integer floorAssignment;
    @Inject
    private Integer linkedSessionID;
    @Inject
    private String terminalName;
    @Inject
    private String callerName;

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    /**
     * The current linked Session.
     *
     * @param linkedSessionID
     */
    public void setLinkedSessionID(Integer linkedSessionID) {
        this.linkedSessionID = linkedSessionID;
    }

    /**
     * set what floor to call.
     *
     * @param floorAssignment
     */
    public void setFloorAssignment(Integer floorAssignment) {
        this.floorAssignment = floorAssignment;
    }

    private LinkedPilaMapping nextCalled;
    private StudentMapping student;
    private boolean withNext;

    /**
     * check whether there is a student to be called next.
     *
     * @return
     */
    public boolean isWithNext() {
        return withNext;
    }

    /**
     * Linked pila information of the student.
     *
     * @return
     */
    public LinkedPilaMapping getNextCalled() {
        return nextCalled;
    }

//    /**
//     * Student information.
//     *
//     * @return
//     */
//    public StudentMapping getStudent() {
//        return student;
//    }
    @Override
    public void transaction() {
        this.withNext = false;
        //----------------------------------------------------------------------
        LinkedPilaMapping nextCalled = Mono.orm().newSearch(Database.connect().linked_pila())
                .eq(DB.linked_pila().floor_assignment, this.floorAssignment)
                .eq(DB.linked_pila().status, "INLINE")
                .eq(DB.linked_pila().remarks, "NONE")
                .eq(DB.linked_pila().SETTINGS_id, this.linkedSessionID)
                .active(Order.asc(DB.linked_pila().id))
                .first();
        //----------------------------------------------------------------------
        if (nextCalled == null) {
            return;
        }
        //----------------------------------------------------------------------
        Date currentDate = Mono.orm().getServerTime().getDateWithFormat();
        nextCalled.setStatus("CALLED");
        nextCalled.setCalled_by(this.callerName);
        nextCalled.setCalled_on_terminal(this.terminalName);
        nextCalled.setRequest_called(currentDate);
        nextCalled.setRequest_validity(currentDate);
        boolean updated = Database.connect().linked_pila().update(nextCalled);
        //----------------------------------------------------------------------
        if (!updated) {
            return; //error
        }
        //----------------------------------------------------------------------
        // refresh values
        nextCalled = Database.connect().linked_pila().getPrimary(nextCalled.getId());

        if (nextCalled == null) {
            return;
        }

//        StudentMapping student = Database.connect()
//                .student().getPrimary(nextCalled.getSTUDENT_id());
        this.nextCalled = nextCalled;
        //this.student = student;
        this.withNext = true;

    }

}
