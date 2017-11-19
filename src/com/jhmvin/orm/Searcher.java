/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Improved search function
 *
 * @author Jhon Melvin
 */
public class Searcher extends ArrayList<Criterion> {

    private final MonoModels STATIC_MODEL;

    /**
     * Equal only
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher eq(String propertyName, Object value) {
        super.add(Restrictions.eq(propertyName, value));
        return this;
    }

    /**
     * Not Equal.
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher ne(String propertyName, Object value) {
        super.add(Restrictions.ne(propertyName, value));
        return this;
    }

    /**
     * Not equal or not null.
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher neOrNn(String propertyName, Object value) {
        super.add(Restrictions.neOrIsNotNull(propertyName, value));
        return this;
    }

    /**
     * less than only
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher lt(String propertyName, Object value) {
        super.add(Restrictions.lt(propertyName, value));
        return this;
    }

    /**
     * Less than or equal
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher lte(String propertyName, Object value) {
        super.add(Restrictions.le(propertyName, value));
        return this;
    }

    /**
     * Greater than only
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher gt(String propertyName, Object value) {
        super.add(Restrictions.gt(propertyName, value));
        return this;
    }

    /**
     * Greater than equal
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher gte(String propertyName, Object value) {
        super.add(Restrictions.ge(propertyName, value));
        return this;
    }

    /**
     * Between range
     *
     * @param propertyName field in the database
     * @param from range start
     * @param to range end
     * @return
     */
    public Searcher between(String propertyName, Object from, Object to) {
        super.add(Restrictions.between(propertyName, from, to));
        return this;
    }

    /**
     * if the field is null
     *
     * @param propertyName
     * @return
     */
    public Searcher isNull(String propertyName) {
        super.add(Restrictions.isNull(propertyName));
        return this;
    }

    /**
     * if the field is not null
     *
     * @param propertyName
     * @return
     */
    public Searcher notNull(String propertyName) {
        super.add(Restrictions.isNotNull(propertyName));
        return this;
    }

    // String operands
    /**
     * Finds the occurrence of the string in the field <strong>(CASE
     * SENSITIVE)</strong>
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher like(String propertyName, Object value) {
        super.add(Restrictions.like(propertyName, value));
        return this;
    }

    /**
     * Finds the occurrence of the string in the field <strong>(CASE
     * SENSITIVE)</strong>
     *
     * @param propertyName
     * @param value
     * @param mode <ul>
     * <li> %s - search from the beginning of the string </li>
     * <li> s% - search from the end of the string </li>
     * <li> s - exactly the same as the string </li>
     * <li> "" - anywhere in the string </li>
     * </ul> @return
     * @return
     */
    public Searcher like(String propertyName, String value, String mode) {
        super.add(Restrictions.like(propertyName, value, this.getMatch(mode)));
        return this;
    }

    // String operands
    /**
     * Finds the occurrence of the string in the field <strong>( NOT CASE
     * SENSITIVE )</strong>
     *
     * @param propertyName
     * @param value
     * @return
     */
    public Searcher likely(String propertyName, Object value) {
        super.add(Restrictions.ilike(propertyName, value));
        return this;
    }

    /**
     * Finds the occurrence of the string in the field <strong>( NOT CASE
     * SENSITIVE )</strong>
     *
     * @param propertyName
     * @param value
     * @param mode <ul>
     * <li> %s - search from the beginning of the string </li>
     * <li> s% - search from the end of the string </li>
     * <li> s - exactly the same as the string </li>
     * <li> "" - anywhere in the string </li>
     * </ul> @return
     * @return
     */
    public Searcher likely(String propertyName, String value, String mode) {
        super.add(Restrictions.ilike(propertyName, value, this.getMatch(mode)));
        return this;
    }

    //
    private MatchMode getMatch(String mode) {
        String start = "%s";
        String end = "s%";
        String exact = "s";
        if (mode.equalsIgnoreCase(start)) {
            return MatchMode.START;
        } else if (mode.equalsIgnoreCase(end)) {
            return MatchMode.END;
        } else if (mode.equalsIgnoreCase(exact)) {
            return MatchMode.EXACT;
        } else {
            return MatchMode.ANYWHERE;
        }
    }

    //-------------------------------------------------------------------------
    public Searcher(MonoModels mm) {
        this.STATIC_MODEL = mm;
    }

    public MonoModels getSTATIC_MODEL() {
        return STATIC_MODEL;
    }

    public Searcher pull() {
        return this;
    }

    /**
     *
     * @param c
     * @return
     */
    public Searcher put(Criterion c) {
        super.add(c);
        return this;
    }

    /**
     * Returns all the result
     *
     * @param arrangement <ul>
     * <li> Order.asc("<strong>propertyName</strong>") - Arrange in ascending
     * order </li>
     * <li> Order.dsec("<strong>propertyName</strong>") - Arrange in descending
     * order </li>
     * </ul>
     * @return
     */
    public SearcherList execute(Order... arrangement) {
//        SearcherList res = this.executeInterface(arrangement);
//        if (res == null || res.isEmpty()) {
//            System.out.println("no res");
//            return new SearcherList();
//        } else {
//            System.out.println("res");
//            return res;
//        }
        return executeInterface(arrangement);
    }

    /**
     * Main interface for execution
     * <strong> Gate Way for searching </strong>
     *
     * @param arrangment
     * @return
     */
    private SearcherList executeInterface1(Order... arrangment) {
        //return this.STATIC_MODEL.search(this, arrangment);
        SearcherList format_result = new SearcherList();
        List res = this.STATIC_MODEL.search(this, arrangment);
        if (res.isEmpty()) {
            return null;
        } else {
            res.forEach((re) -> {
                format_result.add(re);
            });
        }
        return format_result;
    }

    private SearcherList executeInterface(Order... arrangment) {
        return new SearcherList(STATIC_MODEL, this);
    }

    /**
     * Returns all active entries in the database
     *
     * @throws HibernateException if active field is not existing
     * @param arrangement <ul>
     * <li> Order.asc("<strong>propertyName</strong>") - Arrange in ascending
     * order </li>
     * <li> Order.dsec("<strong>propertyName</strong>") - Arrange in descending
     * order </li>
     * </ul>
     * @return
     */
    public SearcherList active(Order... arrangement) {
        //Searcher active = new Searcher(this.STATIC_MODEL);
        //active.addAll(this);
        this.add(SQL.where("active").equalTo(1));
        return this.execute(arrangement);
    }

    /**
     * Returns all inactive entries in the database
     *
     * @throws HibernateException if active field is not existing
     * @param arrangement <ul>
     * <li> Order.asc("<strong>propertyName</strong>") - Arrange in ascending
     * order </li>
     * <li> Order.dsec("<strong>propertyName</strong>") - Arrange in descending
     * order </li>
     * </ul>
     * @return
     */
    public SearcherList inactive(Order... arrangement) {
        //Searcher active = new Searcher(this.STATIC_MODEL);
        //active.addAll(this);
        this.add(SQL.where("active").equalTo(0));
        return this.execute(arrangement);
    }

}
