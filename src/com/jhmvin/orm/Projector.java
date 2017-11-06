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
package com.jhmvin.orm;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

/**
 *
 * @author Jhon Melvin
 */
public class Projector {

    private Class entity;
    private Session local_session;
    private Criteria projected_results;
    private Searcher search;

    public Projector(Searcher search) {
        this.search = search;
        MonoModels model = this.search.getSTATIC_MODEL();
        this.entity = model.getClass_type();
        this.local_session = MonoHibernate.getInstance().session();
    }

    private List projectionType(Projection projectionType) {
        // create projection
        projected_results = this.local_session.createCriteria(entity);
        //
        this.search.forEach(criterion -> {
            projected_results.add(criterion);
        });
        // add projection
        projected_results.setProjection(projectionType);

        try {
            return projected_results.list();
        } catch (ClassCastException cce) {
            System.err.println(" - - - - - - - - - - - - - - - - - - - - - - - -");
            System.err.println("Please check the Data Type of the fields in the searcher\n"
                    + " for example the .eq('fieldName',value) the corresponding.\n"
                    + "data type of value must be the same as the data type of field name\n"
                    + "If fieldName is Integer , value must be Integer also"
                    + "This is a searcher value comparison error.");
            System.err.println(" - - - - - - - - - - - - - - - - - - - - - - - -");
            throw new ClassCastException("Search Value Data Type Error");
        }

    }

    private String singleResult(Projection projection) {
        List res = projectionType(projection);
        if (res.isEmpty()) {
            return "";
        } else {
            return res.get(0).toString();
        }
    }

    /**
     * Average of the specified column.
     *
     * @param property
     * @return
     */
    public String average(String property) {
        return singleResult(Projections.avg(property));
    }

    /**
     * The total number of results.
     *
     * @param property
     * @return
     */
    public String count(String property) {
        return singleResult(Projections.count(property));
    }

    /**
     * Total number of rows of the specified unique columns.
     *
     * @param property
     * @return
     */
    public String uniqueCount(String property) {
        return singleResult(Projections.countDistinct(property));
    }

    /**
     * Returns unique columns
     *
     * @param property
     * @return
     */
    public ArrayList<String> unique(String property) {
        List res = projectionType(Projections.distinct(Projections.property(property)));
        ArrayList<String> uniqueRows = new ArrayList<>();
        res.forEach((re) -> {
            uniqueRows.add(re.toString());
        });
        return uniqueRows;
    }

    /**
     * The highest value in this property.
     *
     * @param property
     * @return
     */
    public String max(String property) {
        return singleResult(Projections.max(property));
    }

    /**
     * The lowest value in this property.
     *
     * @param property
     * @return
     */
    public String min(String property) {
        return singleResult(Projections.min(property));
    }

    /**
     * Returns the sum of the column.
     *
     * @param property
     * @return
     */
    public String sum(String property) {
        return singleResult(Projections.sum(property));
    }

    public String rowCount() {
        return singleResult(Projections.rowCount());
    }

}
