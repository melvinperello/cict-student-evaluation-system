package org.linkedapplication;

import android.app.Activity;
import android.app.ProgressDialog;

public class LoadingMondal {

	private static LoadingMondal instance;

	public static LoadingMondal instance() {
		if (instance == null) {
			instance = new LoadingMondal();
		}
		return instance;
	}

	private LoadingMondal() {
		//
	}

	private ProgressDialog progressDialog;

	public void create(Activity activity) {
		this.progressDialog = new ProgressDialog(activity);
		this.progressDialog.setCancelable(false);
		this.progressDialog.setIndeterminate(true);
	}

	public void setTitle(String title) {
		this.progressDialog.setTitle(title);
	}

	public void setMessage(String message) {
		this.progressDialog.setMessage(message);
	}

	public void show() {
		this.progressDialog.show();
	}

	public void dismiss() {
		this.progressDialog.hide();
	}

}
