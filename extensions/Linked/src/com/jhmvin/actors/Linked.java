package com.jhmvin.actors;

import android.app.Activity;
import android.util.Log;

public class Linked {
	private static Linked PROCESS_INSTANCE;

	private Linked() {
		//
	}

	public static Linked instance() {
		if (PROCESS_INSTANCE == null) {
			PROCESS_INSTANCE = new Linked();
		}
		return PROCESS_INSTANCE;
	}

	private Authenticator auth;

	public void authenticator(Activity activity) {
		if (auth == null) {
			this.auth = new Authenticator(activity);
			this.auth.checkWifi();
		} else {
			if (this.auth.isValidating()) {
				// validation in progress
				Log.i("VALIDATING", "ON PROGRESS");
			} else {
				// validation is done
				this.auth = new Authenticator(activity);
				this.auth.checkWifi();
			}
		}
	}
	
	public Authenticator createAuthenticator(Activity activity){
		return new Authenticator(activity);
	}
	
	public Marshall createMarshall(Activity activity){
		return new Marshall(activity);
	}

}
