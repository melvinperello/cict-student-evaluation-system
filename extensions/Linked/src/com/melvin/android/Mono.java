package com.melvin.android;

import android.app.Activity;
import android.content.Intent;

public class Mono {
	public static void startActivity(Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.startActivity(intent);
	}
}
