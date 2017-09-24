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
package com.jhmvin.fx.display;

import com.jhmvin.fx.events.EventsFX;
import com.jhmvin.Mono;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Jhon Melvin
 */
public class SceneFX extends EventsFX {

    private Pane __APPLICATION_ROOT;

    protected SceneFX() {
        //
    }

    /**
     * Bind this scene to this application's root.
     *
     * @param applicationRoot
     */
    protected void bindScene(Pane applicationRoot) {
        this.__APPLICATION_ROOT = applicationRoot;
        //
        this.init();
    }

    private void init() {
        this.__APPLICATION_ROOT.setOnKeyReleased(release -> {
            if (release.getCode().equals(KeyCode.F11)) {
                this.getStage().setFullScreen(!this.getStage().isFullScreen());
            }
        });
    }

    /**
     * Replaces this scene with another scene.
     *
     * @param newScene
     */
    protected void replaceScene(Scene newScene) {
        Mono.fx().getParentStage(__APPLICATION_ROOT).setScene(newScene);
    }

    /**
     * Recommended rather than replacing the scene.
     *
     * @param pane
     */
    protected void replaceRoot(Pane pane) {
        matchRootToStage(pane);
        this.__APPLICATION_ROOT.getScene().setRoot(pane);
    }

    /**
     *
     * @return
     */
    protected Stage getStage() {
        return Mono.fx().getParentStage(__APPLICATION_ROOT);
    }

    /**
     * Get Scene
     *
     * @return
     */
    protected Scene getScene() {
        return this.__APPLICATION_ROOT.getScene();
    }

    /**
     * Cursors.
     */
    protected void cursorWait() {
        this.getScene().setCursor(Cursor.WAIT);
    }

    protected void cursorDefault() {
        this.getScene().setCursor(Cursor.DEFAULT);
    }

    protected void cursorHand() {
        this.getScene().setCursor(Cursor.HAND);
    }

    protected void setSceneColor(String hexColor) {
        getScene().setFill(Color.web(hexColor));
    }

    protected void matchSceneToStage(Scene scene) {
        Pane pane = (Pane) scene.getRoot();
        pane.setPrefWidth(this.getStage().getWidth());
        pane.setPrefHeight(this.getStage().getHeight());
    }

    protected void matchRootToStage(Pane root) {
        Pane pane = root;
        pane.setPrefWidth(this.getStage().getWidth());
        pane.setPrefHeight(this.getStage().getHeight());

    }

    /**
     * Closes this window.
     */
    protected void finish() {
        Mono.fx().getParentStage(__APPLICATION_ROOT).close();
    }

    //--------------------------------------------------------------------------
    /**
     * Iterates the entire Scene Graph of the root pane to find the
     * accessibility text.
     *
     * @param <T> Type Cast to any control or node type, including panes.
     * @param parent the loaded parent.
     * @param accessibleText assigned accessible text to the control.
     * @return null if not found.
     */
    protected <T> T searchAccessibilityText(Pane parent, String accessibleText) {
        /**
         * Iterate parent's immediate children.
         */
        for (Node node : parent.getChildren()) {
            String nodeAccessibleText = node.getAccessibleText();

            if (nodeAccessibleText == null) {
                // do nothing no accessible text assigned
            } else if (nodeAccessibleText.equals(accessibleText)) {
                // if matched
                return (T) node;
            }

            //---------------Child's Play---------------------------------------
            // if not matched check parent hood.
            if (node.getAccessibleRole().equals(AccessibleRole.PARENT)) {
                // if parent
                Pane motherfucker = (Pane) node;
                Iterator<Node> children = motherfucker.getChildren().iterator();
                // contain located parents
                ArrayList<Pane> temp_container = new ArrayList<>();

                //--------------- Search Party ---------------------------------
                while (children.hasNext()) {
                    Node currentIteration = children.next();
                    String currentAccessibleText = currentIteration.getAccessibleText();

                    if (currentAccessibleText == null) {
                        // skip
                    } else if (currentAccessibleText.equals(accessibleText)) {
                        return (T) currentIteration;
                    }

                    // check parent hood.
                    if (currentIteration.getAccessibleRole().equals(AccessibleRole.PARENT)) {
                        temp_container.add((Pane) currentIteration);
                    }

                    if (!children.hasNext()) {
                        if (!temp_container.isEmpty()) {
                            children = temp_container.get(0).getChildren().iterator();
                            temp_container.remove(0);
                        }
                    }

                } // end of while
                //--------------- Search Party -----------------------------
            }
            //---------------Child's Play-----------------------------------
        } // end foreach
        return null;
    }

    //--------------------------------------------------------------------------
    /**
     * Use this to find an immediate child of a parent. this will not find any
     * child that is wrapped inside a sub parent of the root parent.
     *
     * @param <T> Type Cast to any control or node type, including panes.
     * @param parent the loaded parent.
     * @param accessibleText assigned accessible text to the control.
     * @return null if not found.
     */
    protected <T> T findByAccessibilityText(Pane parent, String accessibleText) {
        for (Node node : parent.getChildren()) {

            String storedAccessText = node.getAccessibleText();
            if (storedAccessText == null) {
                continue;
            }
            if (storedAccessText.equalsIgnoreCase(accessibleText)) {
                return (T) node;
            }
        }
        return null;
    }

    protected <T> T findByAccessibilityHelp(Pane parent, String accessibleHelp) {
        for (Node node : parent.getChildren()) {
            String storedAccessHelp = node.getAccessibleHelp();
            if (storedAccessHelp == null) {
                continue;
            }
            if (storedAccessHelp.equalsIgnoreCase(accessibleHelp)) {
                return (T) node;
            }
        }
        return null;
    }

    protected <T> ArrayList findByAccessibilityRole(Pane parent, AccessibleRole accessibleRole) {
        ArrayList<T> controls = new ArrayList<>();

        for (Node node : parent.getChildren()) {
            AccessibleRole storedRole = node.getAccessibleRole();
            if (storedRole.equals(AccessibleRole.NODE)) {
                continue;
            }
            if (storedRole.equals(accessibleRole)) {
                controls.add((T) node);
            }
        }
        return controls;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
//    protected void addKeyReleasedEvent(KeyCode keyCode, com.jhmvin.fx.keys.KeyFired event) {
//        Mono.fx().key(keyCode).release(__APPLICATION_ROOT, event);
//    }
//    protected void addClickEvent(Node node, EventHandler<MouseEvent> clicked) {
//        node.addEventHandler(MouseEvent.MOUSE_CLICKED, clicked);
//    }
    //-------------------------------------------------------------------------
    @Override
    protected Object clone() {
        System.err.println("SceneFX Cloning was disabled.");
        return null;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Deprecated methods.
//    @Deprecated
//    private <T> T searchAccessibilityTextOld(Pane parent, String accessibleText) {
//        //System.out.println(parent.getChildren().size());
//        for (Node node : parent.getChildren()) {
//            ArrayList<Pane> temp_container = new ArrayList<>();
//            /**
//             * Get Current node's access text.
//             */
//            String nodeAccessText = node.getAccessibleText();
//            // if current node
//            boolean currentNodeIsParent = node.getAccessibleRole().equals(AccessibleRole.PARENT);
//
//            // check if immediate child node is a parent
//            if (currentNodeIsParent) {
//
//                /**
//                 * check if this parent if match.
//                 */
//                if (nodeAccessText == null) {
//                    // do nothing
//                } else if (nodeAccessText.equals(accessibleText)) {
//                    // if match return this node.
//                    return (T) node;
//                }
//                // if not proceed as usual
//                //System.out.println("IMMEDIATE CHILDREN [PARENT]");
//                Pane motherfucker = (Pane) node;
//                Iterator<Node> children = motherfucker.getChildren().iterator();
//                /**
//                 * Check children
//                 */
//                while (children.hasNext()) {
//                    Node currentIteration = children.next();
//                    String childAccessText = currentIteration.getAccessibleText();
//                    boolean isChildParent = currentIteration.getAccessibleRole().equals(AccessibleRole.PARENT);
//
//                    // if child is another parent.
//                    if (isChildParent) {
//                        //System.out.println("SECONDARY CHILDREN [PARENT]");
//                        /**
//                         * Check this parent.
//                         */
//                        if (childAccessText == null) {
//                            //skip
//                        } else if (childAccessText.equals(accessibleText)) {
//                            return (T) currentIteration;
//                        }
//                        // if not procceed as usual.
//                        temp_container.add((Pane) currentIteration);
//                    } else if (childAccessText == null) {
//                        // do nothing
//                        // if child is ignorable control
//                        //System.out.println("SECONDARY CHILDREN [NULL]");
//                    } else if (childAccessText.equals(accessibleText)) {
//                        //System.out.println("SECONDARY CHILDREN [YES]");
//                        return (T) currentIteration;
//                    }
//
//                    // if not found in this iteration proceed to next.
//                    if (!children.hasNext()) {
//                        // there is no next iteration.
//                        // if temp container has values?
//                        if (!temp_container.isEmpty()) {
//                            children = temp_container.get(0).getChildren().iterator();
//                            temp_container.remove(0);
//                            // values reload until the temp container is empty.
//                        }
//                    }
//
//                    // if no next values and temp_container is empty
//                } // end of while
//            } else {
//                // immediate child of this container.
//                // check if immediate child.
//                //String currentAccessibleText = node.getAccessibleText();
//
//                if (nodeAccessText == null) {
//                    //System.out.println("IMMEDIATE CHILDREN [NULL]");
//                    continue;
//                }
//                if (nodeAccessText.equals(accessibleText)) {
//                    //System.out.println("IMMEDIATE CHILDREN [YES]");
//                    return (T) node;
//                }
//            }
//
//        }
//        return null;
//    }
}
