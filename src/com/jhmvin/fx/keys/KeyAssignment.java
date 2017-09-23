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
package com.jhmvin.fx.keys;

import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Jhon Melvin
 */
public class KeyAssignment {

    private KeyCode key;

    public KeyAssignment(KeyCode key) {
        this.key = key;
    }

    private void add(Node node, EventType<KeyEvent> ke, KeyFired whatToDo) {
        node.addEventHandler(ke, (KeyEvent released) -> {
            if (released.getCode().equals(key)) {
                whatToDo.event();
            }
        });
    }

    /**
     * Assign shortcut keys.
     *
     * @param node Node to receive the event.
     * @param whatToDo ()->{ your event here; }
     */
    public void release(Node node, KeyFired whatToDo) {
        add(node, KeyEvent.KEY_RELEASED, whatToDo);
    }

}
