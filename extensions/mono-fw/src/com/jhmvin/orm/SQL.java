package com.jhmvin.orm;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class SQL {

    private final String propertyName;

    public SQL(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     *
     * @param propertyName the column in the database
     * @return
     */
    public static SQL where(String propertyName) {
        return new SQL(propertyName);
    }

    // Logical
    public static Criterion or(Criterion... predicates) {
        return Restrictions.or(predicates);
    }

    public static Criterion and(Criterion... predicates) {
        return Restrictions.and(predicates);
    }

    public Criterion equalTo(Object value) {
        return Restrictions.eq(this.propertyName, value);
    }

    public Criterion greaterThan(Object value) {
        return Restrictions.gt(this.propertyName, value);
    }

    public Criterion greaterThanEqual(Object value) {
        return Restrictions.ge(this.propertyName, value);
    }

    public Criterion lessThan(Object value) {
        return Restrictions.lt(this.propertyName, value);
    }

    public Criterion lessThanEqual(Object value) {
        return Restrictions.le(this.propertyName, value);
    }

    public Criterion between(Object from, Object to) {
        return Restrictions.between(this.propertyName, from, to);
    }

    public Criterion isNull() {
        return Restrictions.isNull(propertyName);
    }

    public Criterion isNotNull() {
        return Restrictions.isNotNull(propertyName);
    }

}
