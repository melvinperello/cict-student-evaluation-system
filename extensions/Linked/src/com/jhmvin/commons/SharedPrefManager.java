package com.jhmvin.commons;

import android.app.Activity;
import android.content.SharedPreferences;

public class SharedPrefManager {

	private final static String defaultServer = ApplicationCache.view().APP_IP;

	public static String getIP(Activity a) {
		SharedPreferences sp = a.getSharedPreferences("com.cict.values", 0);
		String ip = sp.getString("ip", defaultServer);
		return ip;
	}

	public static boolean setIp(Activity a, String ip) {
		SharedPreferences sp = a.getSharedPreferences("com.cict.values", 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("ip", ip);
		return editor.commit();
	}

}
