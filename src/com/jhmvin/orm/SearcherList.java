/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm;

import java.util.ArrayList;

/**
 *
 * @author Jhon Melvin
 */
public class SearcherList extends ArrayList {

    /**
     * Returns the first result
     *
     * @param <T>
     * @return
     */
    public <T> T first() {
        if (this.isEmpty()) {
            return null;
        } else {
            return (T) this.get(0);
        }
    }

    /**
     * Gets the last result
     *
     * @param <T>
     * @return
     */
    public <T> T last() {
        if (this.isEmpty()) {
            return null;
        } else {
            return (T) this.get(this.size() - 1);
        }
    }

    /**
     *
     * @param <T>
     * @param count
     * @return
     */
    public <T> ArrayList<T> take(int count) {
        if (this.isEmpty()) {
            return null;
        }
        ArrayList<T> formatted = new ArrayList<>();
        if (this.size() < count) {
            count = this.size();
        }
        for (int x = 0; x < count; x++) {
            formatted.add((T) this.get(x));
        }
        return formatted;
    }

    public <T> ArrayList<T> all() {
        if (this.isEmpty()) {
            return null;
        }

        ArrayList<T> formatted = new ArrayList<>();
        this.forEach((Object aThi) -> {
            formatted.add((T) aThi);
        });
        return formatted;
    }

}
