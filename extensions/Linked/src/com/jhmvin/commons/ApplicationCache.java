package com.jhmvin.commons;

public class ApplicationCache {

	private ApplicationCache() {
		//
	}

	private static ApplicationCache CACHE_INSTANCE;

	public static ApplicationCache view() {
		if (CACHE_INSTANCE == null) {
			CACHE_INSTANCE = new ApplicationCache();
		}
		return CACHE_INSTANCE;
	}

	/**
	 * This will be the default IP upon installation
	 */
	public String APP_IP = "10.10.10.10";
}
