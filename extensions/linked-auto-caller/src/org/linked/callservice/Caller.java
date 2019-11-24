/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linked.callservice;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LinkedEntranceMapping;
import app.lazy.models.LinkedPilaMapping;
import com.jhmvin.Mono;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class Caller {

    private final static String exipreStatement = "UPDATE linked_pila as lp SET lp.`status` = 'EXPIRED', lp.remarks = 'EXPIRED' WHERE (lp.`status` = 'CALLED' AND lp.floor_assignment = 3 AND lp.active = 1 AND lp.request_validity <= NOW())";

    /**
     * Capacity of the evaluation rooms.
     */
    public final static int ROOM_CAPACITY = 10;
    /**
     * Expiration Minutes.
     */
    public final static int EXP_MIN = 3;

    /**
     * Checks for expired clients and mark it as expired.
     *
     * @param floor
     */
    public static void expireWatcher(int floor) {
//        // SQL Query for the expire watcher
//        QueryBuilder expireQuery = QueryBuilder.basicQuery()
//                // Update Values
//                .statement("UPDATE linked_pila as lp")
//                .statement("SET")
//                .statement("lp.`status` = ?,")
//                .addParamerter("EXPIRED")
//                .statement("lp.remarks = ?")
//                .addParamerter("EXPIRED")
//                //--------------------------------------------------------------
//                // Condition
//                .statement("WHERE ( lp.`status` = ?")
//                .addParamerter("CALLED")
//                .statement("AND lp.floor_assignment = ?")
//                .addParamerter(floor)
//                .statement("AND lp.active = ?")
//                .addParamerter(1)
//                .statement("AND lp.request_validity <= NOW() )")
//                .build();
//        try {
//            int affected = Neko.app().executeUpdate(expireQuery);
//            System.out.println("EXPIRED UPDATE: " + affected);
//        } catch (Exception e) {
//            System.out.println("ERROR");
//        }

        ArrayList<LinkedPilaMapping> called_group = Mono.orm()
                .newSearch(Database.connect().linked_pila())
                .eq("status", "CALLED")
                .eq("floor_assignment", floor)
                .active()
                .all();

        /**
         * Skip if empty
         */
        if (called_group == null) {
            return;
        }
        for (LinkedPilaMapping lp_map : called_group) {
            if (lp_map.getRequest_validity() != null) {
                Date valid_until = lp_map.getRequest_validity();
                // check if expired
                // mark as void if expired
                if (Mono.orm().getServerTime().isPastServerTime(valid_until)) {
                    lp_map.setStatus("EXPIRED");
                    lp_map.setRemarks("EXPIRED");
                    Database.connect().linked_pila().update(lp_map);
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Returns the capacity of each evaluation room.
     *
     * @return
     */
    private static int getRoomCapactity() {
        return ROOM_CAPACITY;
    }

    /**
     * Time in minutes before expiration.
     *
     * @return
     */
    private static int getExpireTime() {
        return EXP_MIN;
    }

    /**
     * Automatically calls the students using an algorithm base on the capacity.
     *
     * @param floor
     */
    public static void autoCall(int floor) {
        /**
         * How does the auto call works.
         */
        // first it will get how many students are inside the room using
        // the linked_evaluation table that are markes as NONE
        int floorEntered = entranceCount(floor);
        // gets the room capacity
        // room capacity referes to how many students can be allowed inside the room.
        // if the room capacity is greater than the number inside the room
        // it means that there are space inside the room and
        // the caller needs to call students
        if (getRoomCapactity() > floorEntered) {
            // how many studets are marked as called.
            int floorCalled = calledCount(floor);
            // to get how many students can be called room capacity - how many are inside
            int canCall = getRoomCapactity() - floorEntered;
            // if the number of students that can be called is greater than
            // the current number of students that is already called
            // for example we have 2 vacants in the evaluation room
            // and we have no students that is called
            // it means that 2 > 0
            // so the system will call 2 students
            if (canCall > floorCalled) {
                // how many students will be called
                int toCall = canCall - floorCalled;
                // check if there are students that are available to be called.
                ArrayList<LinkedPilaMapping> callList = callStudent(toCall, floor);
                if (callList == null) {
                    return;
                }
                // if there are students that will be called
                // update their information 
                for (LinkedPilaMapping next_to_call : callList) {
                    // set status to called
                    next_to_call.setStatus("CALLED");
                    // get server time
                    Calendar serverTime = Mono.orm().getServerTime().getCalendar();
                    next_to_call.setRequest_called(serverTime.getTime());
                    // add  minustes expiry
                    serverTime.add(Calendar.MINUTE, getExpireTime());
                    next_to_call.setRequest_validity(serverTime.getTime());
                    // if the update will fail
                    // the auto call will call try to reupdate that value on the next
                    // thread loop.
                    Database.connect().linked_pila().update(next_to_call);
                }
            }
        }
    }

    /**
     * Takes the student that will be called.
     *
     * @param count
     * @param floor
     * @return
     */
    private static ArrayList<LinkedPilaMapping> callStudent(int count, int floor) {
        ArrayList<LinkedPilaMapping> next_to_call = Mono.orm()
                .newSearch(Database.connect().linked_pila())
                .eq(DB.linked_marshall_session().status, "INLINE")
                .eq(DB.linked_entrance().floor_assignment, floor)
                .active(Order.asc(DB.linked_pila().id))
                .take(count);

        return next_to_call;
    }

    //--------------------------------------------------------------------------
    /**
     * Checks how many students are marked as called.
     *
     * @param floor
     * @return
     */
    private static int calledCount(int floor) {
        ArrayList<LinkedPilaMapping> calledMap = Mono.orm()
                .newSearch(Database.connect().linked_pila())
                .eq("status", "CALLED")
                .eq("floor_assignment", floor)
                .active(Order.desc("id"))
                .all();

        if (calledMap == null) {
            return 0;
        }

        return calledMap.size();
    }

    /**
     * Checks how many students are inside the room.
     *
     * @param floor
     * @return
     */
    private static int entranceCount(int floor) {
//        QueryBuilder entranceCount = QueryBuilder.basicQuery()
//                .statement("SELECT COUNT(le.reference_id) as student_inside")
//                .statement("FROM linked_entrance AS le")
//                .statement("WHERE le.floor_assignment = ?")
//                .addParamerter(floor)
//                .statement("AND le.`status` = ?")
//                .addParamerter("NONE")
//                .statement("AND le.active = ?")
//                .addParamerter(1)
//                .build();
//
//        try {
//            ResultList resultList = Neko.app().executeQuery(entranceCount);
//            if (resultList.size() == 0) {
//                System.out.println("No Results");
//                return 0;
//            } else {
//                System.out.println("SUCCESS");
//                Integer count = resultList.getRowAt(0).getTyped("student_inside");
//                return count;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }

        ArrayList<LinkedEntranceMapping> entranceMap = Mono.orm()
                .newSearch(Database.connect().linked_entrance())
                .eq(DB.linked_entrance().floor_assignment, floor)
                .eq(DB.linked_entrance().status, "NONE")
                .active(Order.desc(DB.linked_entrance().reference_id))
                .all();

        if (entranceMap == null) {
            return 0;
        } else {
            return entranceMap.size();
        }
    }
}
