package com.melvin.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.Toast;

/**
 * A Notification Utility Class For Android Widgets.
 * 
 * @author Jhon Melvin
 * 
 */
public class Notify {

	/**
	 * Let's have fun debugging our code with colored messages in the logcat.
	 * 
	 * @param color
	 *            choices are [i,e,d,v,w]
	 * @param message
	 */
	public static void log(char color, Object message) {
		switch (color) {
		case 'i':
			Log.i("NOTIFY", String.valueOf(message));
			break;
		case 'e':
			Log.e("NOTIFY", String.valueOf(message));
			break;
		case 'd':
			Log.d("NOTIFY", String.valueOf(message));
			break;
		case 'v':
			Log.v("NOTIFY", String.valueOf(message));
			break;
		case 'w':
			Log.w("NOTIFY", String.valueOf(message));
			break;
		default:
			Log.i("NOTIFY", String.valueOf(message));
			break;
		}
	}

	/**
	 * Default log color. write in the logcat using this command.
	 * 
	 * @param message
	 */
	public static void log(Object message) {
		Log.i("NOTIFY", String.valueOf(message));
	}

	/**
	 * A Short Toast Message.
	 * 
	 * @param activity
	 * @param text
	 */
	public static void toastShort(Activity activity, String text) {
		Toast.makeText(activity.getApplicationContext(), text,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Creates a progress dialog that is always on top and not cancel-able.
	 * 
	 * @param activity
	 * @return
	 */
	public static ProgressDialog createProgressDialogOver(Activity activity) {
		ProgressDialog progressDialog = new ProgressDialog(activity);
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		return progressDialog;
	}

	/**
	 * A Message Alert Utility this message box is cancel-able and do nothing
	 * when clicked. it is only use to notify the user.
	 * 
	 * @param activity
	 * @param title
	 * @param message
	 */
	public static void showMessage(Activity activity, String title,
			String message) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
		dlgAlert.setTitle(title);
		dlgAlert.setCancelable(true);
		dlgAlert.setPositiveButton("Dismiss", null);
		dlgAlert.setMessage(message);
		dlgAlert.show();
	}

	/**
	 * Shows a message then dismiss the parent activity.
	 * 
	 * @param activity
	 * @param title
	 * @param message
	 */
	public static void showMessageThenClose(final Activity activity,
			String title, String message) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
		dlgAlert.setTitle(title);
		dlgAlert.setCancelable(true);
		OnClickListener dialogClick = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		};
		dlgAlert.setPositiveButton("Dismiss", dialogClick);
		dlgAlert.setMessage(message);
		dlgAlert.show();
	}
}
