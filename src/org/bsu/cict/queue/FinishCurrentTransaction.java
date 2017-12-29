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

import app.lazy.models.Database;
import app.lazy.models.LinkedPilaMapping;
import com.jhmvin.Mono;
import javax.inject.Inject;
import org.bsu.cict.threads.DataTransaction;

/**
 * When there is a currently called student. the purpose of this class is to
 * mark that student pila information as void. this will mean that the
 * transaction has already ended.
 *
 * @author Jhon Melvin
 */
public class FinishCurrentTransaction extends DataTransaction {

    @Inject
    private Integer linkedPilaID;

    /**
     * set the id.
     *
     * @param linkedPilaID
     */
    public void setLinkedPilaID(Integer linkedPilaID) {
        this.linkedPilaID = linkedPilaID;
    }

    @Override
    public void transaction() {
        LinkedPilaMapping pila = Database.connect()
                .linked_pila().getPrimary(linkedPilaID);

        if (pila == null) {
            // do nothing
            return;
        }
        // UPDATE VALUES
        pila.setRemarks("DONE");
        pila.setStatus("VOID");
        pila.setActive(0);

        boolean res = Database.connect().linked_pila().update(pila);
        // do nothing on result.
    }

}
