/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.mono.fx.events;

import com.jhmvin.fx.events.EventsFX;
import com.jhmvin.fx.events.FXEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Jhon Melvin
 */
public class MonoClick {

    /**
     * Add a right click event.
     *
     * @param node
     * @param fx
     */
    public static void addRightClickEvent(Node node, MonoEvent fx) {
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                fx.play();
            }
        });
    }

    /**
     * Add a Control + Primary Click Event
     *
     * @param node
     * @param fx
     */
    public static void addCtrlClickEvent(Node node, MonoEvent fx) {
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.isControlDown()) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    fx.play();
                }
            }
        });
    }

    /**
     * Add a primary Click Event.
     *
     * @param node
     * @param fx
     */
    public static void addClickEvent(Node node, MonoEvent fx) {
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                fx.play();
            }
        });
    }

    /**
     * Adds a double click event.
     *
     * @param node
     * @param fx
     */
    public static void addDoubleClickEvent(Node node, MonoEvent fx) {
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    fx.play();
                }
            }
        });
    }
}
