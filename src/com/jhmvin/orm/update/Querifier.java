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
package com.jhmvin.orm.update;

/**
 *
 * @author Jhon Melvin
 */
public class Querifier {

    private Querifier() {
        /*
        //Database.connect();
        // prepare
        Session local_session = Mono.orm().session();
        // create entity
        EntityManager entity_manager = local_session.getEntityManagerFactory().createEntityManager();
        // create criteria
        CriteriaBuilder criteria = entity_manager.getCriteriaBuilder();

        // create query
        CriteriaQuery<StudentMapping> query = criteria.createQuery(StudentMapping.class);
        Root<StudentMapping> c = query.from(StudentMapping.class);
        // add parameter
        ParameterExpression<Integer> group = criteria.parameter(Integer.class);

        // query.select(c).where(criteria.equal(c.get("_group"), group));
        // release query
        TypedQuery<StudentMapping> startQuery = entity_manager.createQuery(query);
        //startQuery.setParameter(group, 2);

        // get results
        List<StudentMapping> results = startQuery.getResultList();

        results.forEach(student -> {
            System.out.println(student.getFirst_name());
        });
         */
    }

}
