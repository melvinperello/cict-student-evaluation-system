package com.jhmvin.orm;

import com.jhmvin.Mono;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * DEPENDENCIES: /LIBRARIES/LIBHIB
 *
 * @author Jhon Melvin
 */
public class MonoModels {

    /**
     * Empty Constructor
     */
    private MonoModels() {
        //no args
    }

    /**
     * Initialize model for Controllers
     *
     * @param clazz
     */
    public MonoModels(Class clazz) {
        this.class_type = clazz;
        this.class_name = class_type.getName();
        this.mapIntegrityCheck();
    }

    /**
     * Checks whether the called class by this Models is already installed
     */
    private void mapIntegrityCheck() {
        ArrayList<String> installedMaps = Mono.orm().getModelMappings();
        boolean res = installedMaps.contains(this.class_type.getSimpleName());
        if (!res) {
            HibernateExceptionHelper.unlistedMapping(this.class_type.getSimpleName());
        }
    }

    /* <This is the object type> */
    private Class class_type;

    /**
     * Returns the mapping class
     *
     * @return
     */
    public Class getClass_type() {
        return class_type;
    }

    private String class_name;

    /**
     * returns the last inserted id
     *
     * @param model the new model to be inserted without primary key
     * @return -1 for failure AUTO GEN ID for success
     */
    public Integer insert(Object model) {
        return DatabaseOperations.insert(model);
    }

    /**
     * The model should always have it setId not null or should contain its
     * primary key
     *
     * @param models
     * @return true on success false on failure
     */
    public Boolean update(Object models) {
        return DatabaseOperations.update(models);
    }

    /* <CHANGE OBJECT TYPE> */
 /* Search */
    public List search(ArrayList<Criterion> conditions) {
        return DatabaseOperations.search(conditions, this.class_type);
    }

    /* <CHANGE OBJECT TYPE> */
    public List search(ArrayList<Criterion> conditions, Order... arrangement) {
        return DatabaseOperations.search(conditions, this.class_type, arrangement);
    }


    /* Find  Returns the object */
    public List find(String field, Object value) {
        return new DatabaseOperations().find(field, value, this.class_type, null);
    }

    public List find(String field, Object value, Order arrangement) {
        return DatabaseOperations.find(field, value, this.class_type, arrangement);
    }

    /**
     * Completely deletes the entry without trace
     *
     * @param field database field
     * @param value field value
     * @return TRUE on success FALSE on failure
     */
    public Integer delete(String field, Object value) {
        return DatabaseOperations.delete(this.class_name, field, value);
    }

    /**
     * For data integrity COMMIT has been separated from the from the core
     * functions must start session.
     *
     * @param ses DatabaseOperations.startSession();
     * @param update field name
     * @param update_val new value for field
     * @param key database field [only supports single field]
     * @param value key value
     * @return -1 for failure must call DatabaseOperations.checkForCommit();
     */
    public Integer transactionalUpdate(Session ses, String update, String update_val, String key, String value) {
        return DatabaseOperations.transactionalUpdate(class_name, ses, update, update_val, key, value);
    }

    /**
     * For data integrity COMMIT has been separated from the from the core
     * functions must start session.
     *
     * @param ses
     * @param mapping
     * @return
     */
    public Boolean transactionalSingleUpdate(Session ses, Object mapping) {
        return DatabaseOperations.transactionalSingleUdate(ses, mapping);
    }

    /**
     * For data integrity COMMIT has been separated from the from the core
     * functions must start session using Session s =
     * DatabaseOperations.startSession(); must call
     * DatabaseOperations.checkForCommit(); to save changes
     *
     * @param ses DatabaseOperations.startSession();
     * @param mapping object to be inserted
     * @return
     */
    /* Multiple Insert : Added 04-23-2017*/
    public Integer transactionalInsert(Session ses, Object mapping) {
        return DatabaseOperations.transactionalInsert(ses, mapping);
    }

    /**
     *
     * @param key the primary key of that table
     * @return the results with Object type (FOO)
     */
    public <T> T getPrimary(Serializable key) {
        return (T) DatabaseOperations.getPrimary(class_type, key);
    }

    /**
     *
     * @param field database field
     * @param value search value
     * @return the results with Object type (FOO)
     */
    public Object getBy(String field, Object value) {
        try {
            return DatabaseOperations.find(field, value, this.class_type, null).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
