package com.jhmvin.orm;

import com.jhmvin.Mono;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

/**
 * DEPENDENCIES: /LIBRARIES/LIBHIB <br>
 * <strong>Updates</strong><br>
 * added feature to asses Integrity Constraint Violation
 *
 * @author Jhon Melvin
 */
public class DatabaseOperations {

    /**
     * Constructor
     */
    public DatabaseOperations() {

    }

    /* Insert new object returns auto_gen id if success return -1 for failure */
    public static Integer insert(Object mapping) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = Mono.orm().session();
            transaction = session.beginTransaction();
            Object gen_id = session.save(mapping);
            transaction.commit();

            return Integer.parseInt(gen_id.toString());
        } catch (Exception he) {
            if (transaction != null) {
                transaction.rollback();
            }
            errorHelper(he, "INSERT");
            return he instanceof ConstraintViolationException ? -1 : -2;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /* End of Insert */
    /**
     *
     * @param mapping
     * @return true for success delete false for failure null for constraint
     * violation
     */
    public static Boolean update(Object mapping) {
        Session session = null;
        Transaction transaction = null;
        Boolean transactionSuccess = false;
        try {

            session = Mono.orm().session();
            transaction = session.beginTransaction();
            session.update(mapping);
            transaction.commit();

            transactionSuccess = true;
        } catch (Exception he) {
            if (transaction != null) {
                transaction.rollback();
            }

            errorHelper(he, "UPDATE");
            transactionSuccess = he instanceof ConstraintViolationException ? null : false;
        } finally {
            if (session != null) {
                session.close();
            }

        }
        return transactionSuccess;
    }

    /* End of Update */

 /* Search */
    public static List search(ArrayList<Criterion> conditions, Class mapping, Order... arrangement) {
        Session session = null;
        try {
            session = Mono.orm().session();
            Criteria criteria = session.createCriteria(mapping);
            //adding restrictions
            conditions.forEach((restrictions) -> {
                criteria.add(restrictions);
            });
            //adding order
            if (arrangement.length > 0) {
                for (Order order : arrangement) {
                    criteria.addOrder(order);
                }
            }

            return criteria.list();
        } catch (Exception he) {
            errorHelper(he, "SEARCH");
            return null;
        } finally {
            if (session != null) {
                session.close();

            }

        }
    }

    /*End of Search*/

 /* find */
    public static List find(String field, Object value, Class mapping, Order arrangement) {
        Session session = null;
        try {
            session = Mono.orm().session();
            Criteria criteria = session.createCriteria(mapping);
            criteria.add(Restrictions.eq(field, value));

            //adding order
            if (arrangement != null) {
                criteria.addOrder(arrangement);
            }

            return criteria.list();
        } catch (Exception he) {
            errorHelper(he, "FIND");
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /* End of Find */
    /**
     * @since 2.0
     * @param mapping
     * @param field
     * @param value
     * @return 0 if no rows were deleted, number of rows deleted, -1 for foreign
     * key violation, -2 for connection error
     */
    public static Integer delete(String mapping, String field, Object value) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = Mono.orm().session();
            transaction = session.beginTransaction();
            Query createQuery = session.createQuery("delete from " + mapping + " m where m." + field + " =:value");
            createQuery.setParameter("value", value);
            int rowsAffected = createQuery.executeUpdate();
            transaction.commit();

            return rowsAffected;
        } catch (Exception he) {
            if (transaction != null) {
                transaction.rollback();
            }
            errorHelper(he, "DELETE");
            return he instanceof ConstraintViolationException ? -1 : -2;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /* GetPK */
    public static Object getPrimary(Class type, Serializable key) {
        Session session = null;
        try {
            session = Mono.orm().getInstance().session();

            return session.get(type, key);
        } catch (Exception he) {
            errorHelper(he, "GET PRIMARY");
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    /* Cumulative Updated Codes */
 /*----------------------------------------------------------------------*/
    public static Boolean transactionalSingleUdate(Session ses, Object mapping) {
        Boolean transactionSuccess;
        try {
            ses.update(mapping);
            transactionSuccess = true;
        } catch (Exception he) {
            errorHelper(he, "TRANSACTIONAL SINGLE UPDATE");
            transactionSuccess = he instanceof ConstraintViolationException ? null : false;
        }
        return transactionSuccess;
    }

    /* Multiple Update with safety commit feature */
    public static Integer transactionalUpdate(String tablemap, Session ses, String update, String update_val, String key, String value) {

        Session session = null;
        try {
            session = ses;
            String hql = "UPDATE " + tablemap + " SET " + update + " = :field_value" + " WHERE " + key + " = :value";
            Query createQuery = session.createQuery(hql);
            createQuery.setParameter("field_value", update_val);
            createQuery.setParameter("value", value);
            Integer row_affect = createQuery.executeUpdate();
            //transaction.commit();
            return row_affect;
        } catch (Exception he) {
            errorHelper(he, "TRANSACTIONAL UPDATE");
            return -1;
        } finally {
            if (session != null) {
                //session.close();
            }
        }

    }

    /**
     * Added 07/31/2017.
     *
     * @param ses
     * @param mapping
     * @return -2 integer error -1 database error
     */
    public static Integer transactionalInsert(Session ses, Object mapping) {
        try {

            Object gen_id = ses.save(mapping);
            return Integer.parseInt(gen_id.toString());
        } catch (NumberFormatException nfe) {
            // integer conversion error
            return -3;
        } catch (Exception he) {
            errorHelper(he, "TRANSACTIONAL INSERT");
            return he instanceof ConstraintViolationException ? -1 : -2;
        }
    }

    /* End of Transaction Handlers */
    private static void errorHelper(Exception he, String operation) {
        HibernateExceptionHelper.errorChecker(operation, he);
    }

}
