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
package com.jhmvin.transitions;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 *
 * @author Jhon Melvin
 */
public class Animate {

    /**
     * Fade out Transition.
     *
     * @param node
     * @param time
     * @param callback
     */
    public static void fadeOut(Node node, double millis, Runnable callback) {
        FadeTransition ft = new FadeTransition(Duration.millis(millis), node);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(value -> {
            callback.run();
        });
        ft.play();
    }

    /**
     * Fade In Transition.
     *
     * @param node
     * @param time
     * @param callback
     */
    public static void fadeIn(Node node, double millis, Runnable callback) {
        FadeTransition ft = new FadeTransition(Duration.millis(millis), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setOnFinished(value -> {
            callback.run();
        });
        ft.play();
    }

    public static void fade(Node current, double millis, Runnable callback, Node next) {
        current.setDisable(true);
        next.setDisable(false);
        fadeOut(current, millis, () -> {
            // run first callback
            next.setOpacity(0.0);
            callback.run();
            // fade in transition
            fadeIn(next, millis, () -> {
                // no callback display the next window
            });
        });
    }
}
