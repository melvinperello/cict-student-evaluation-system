package com.jhmvin.commons;

import java.util.HashMap;

public class PublicConstants {

	// /**
	// * Available courses.
	// */
	// public final static String[] VALID_COURSES = { "ACT", "IT", "CT" };

	// /**
	// * Stores local ID's of curriculum.
	// *
	// * @param course
	// * @return
	// */
	// public static String COURSE_ID(String course) {
	// HashMap<String, String> ids = new HashMap<String, String>();
	// ids.put("ACT", "3");
	// ids.put("CT", "2");
	// ids.put("IT", "1");
	//
	// return ids.get(course);
	// }

	// /**
	// * Accepted Year Levels.
	// */
	// public final static String[] ACCEPTED_YEAR_LEVELS = { "1", "2", "3", "4"
	// };

	// /**
	// * Accepted Section Group.
	// */
	// public final static String[] ACCEPTED_GROUP = { "1", "2" };

	// /**
	// * Room Assignmtns.
	// */
	// public static String getFLOOR_THREE(){
	// return FLOOR_THREE;
	// }
	//
	// public static String getFlOOR_FOUR(){
	// return FLOOR_FOUR;
	// }
	// public static String FLOOR_THREE = "3FLR/IT-8";
	// public static String FLOOR_FOUR = "4FLR/CT-6";
	//
	// public static String FLR_3 = "3";
	// public static String FLR_4 = "4";
	//
	// public static String getFLR_3(){
	// return FLR_3;
	// }
	//
	// public static String getFLR_4(){
	// return FLR_4;
	// }

	// REFRESH RATES
	public static long getAcceptInterval() {
		return 1000;
	}

	public static long getWaitingInterval() {
		return 2000;
	}

	/**
	 * Constant Value for the First Cluster.
	 * 
	 * @return
	 */
	public static String getCluster_1() {
		return "3";
	}

	/**
	 * Constant Value for the Second Cluster.
	 * 
	 * @return
	 */
	public static String getCluster_2() {
		return "4";
	}
}
