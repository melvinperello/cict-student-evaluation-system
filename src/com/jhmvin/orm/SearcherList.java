/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class SearcherList extends ArrayList {

    public SearcherList() {
        //
    }

    private MonoModels staticModels;
    private Searcher criteria;
    private Order[] arrangement;

    public SearcherList(MonoModels staticModels, Searcher criteria, Order... arrangment) {
        this.staticModels = staticModels;
        this.criteria = criteria;
        this.arrangement = arrangment;
    }

    /**
     * Returns the first result
     *
     * @param <T>
     * @return
     */
    public <T> T first() {
//        if (this.isEmpty()) {
//            return null;
//        } else {
//            return (T) this.get(0);
//        }
        try {
            return this.<T>take(1).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     *
     * @param <T>
     * @param count
     * @return
     */
    public <T> ArrayList<T> take(int count) {
//        if (this.isEmpty()) {
//            return null;
//        }
//        ArrayList<T> formatted = new ArrayList<>();
//        if (this.size() < count) {
//            count = this.size();
//        }
//        for (int x = 0; x < count; x++) {
//            formatted.add((T) this.get(x));
//        }
//        return formatted;

        ArrayList<T> formatted = new ArrayList<>();
        List result = this.staticModels.search(this.criteria, count, this.arrangement);
        for (int x = 0; x < result.size(); x++) {
            formatted.add((T) result.get(x));
        }
        return formatted;
    }

    public <T> ArrayList<T> all() {
//        if (this.isEmpty()) {
//            return null;
//        }
//
//        ArrayList<T> formatted = new ArrayList<>();
//        this.forEach((Object aThi) -> {
//            formatted.add((T) aThi);
//        });
//        return formatted;

        /**
         * 0 means no limit
         */
        return this.<T>take(0);
    }

}
