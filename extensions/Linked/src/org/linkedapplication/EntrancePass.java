package org.linkedapplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.linkedapplication.requests.FailureEvent;
import org.linkedapplication.requests.Request;
import org.linkedapplication.requests.RequestEvent;
import org.linkedapplication.requests.SingleResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.loopj.android.http.RequestParams;

/**
 * This class is a request from the server to issue entrance pass to students
 * that are already called.
 * 
 * SCOPE: MARSHALS only.
 * 
 * @author Jhon Melvin
 * 
 */
public class EntrancePass {

	private Activity activity;

	public EntrancePass(Activity a) {
		this.activity = a;
	}

	public void entrance_request(String student_number, String acad_term) {
		Request enter = new Request();
		enter.setApiLink("linked/entrance");
		RequestParams post = new RequestParams();
		post.add("id", student_number);
		post.add("term", acad_term);

		enter.setParameters(post);

		enter.setOnStart(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				LoadingMondal.instance().create(activity);
				LoadingMondal.instance().setMessage("Sending Request . . .");
				LoadingMondal.instance().show();
			}
		});

		enter.setOnSuccess(new SingleResponse() {

			@Override
			public void event(JSONObject json) {
				// TODO Auto-generated method stub
				Log.i("json", json.toString());
				onSuccessRequest(json);
			}
		});

		enter.setOnFailure(new FailureEvent() {

			@Override
			public void salo(int status, String type) {
				// TODO Auto-generated method stub
				showFailed(type + " " + String.valueOf(status));
			}
		});

		enter.setOnFinish(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				LoadingMondal.instance().dismiss();
			}
		});

		enter.start();
	}

	private void onSuccessRequest(JSONObject json) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this.activity);
		dlgAlert.setCancelable(false);
		dlgAlert.setNegativeButton("Okay !", null);
		try {
			String res = json.getString("res");
			if (res.equalsIgnoreCase("ok")) {
				dlgAlert.setTitle("Success");
				dlgAlert.setMessage("Student was transferred to entrance lane.");
			} else {
				dlgAlert.setTitle("Failed");
				dlgAlert.setMessage("Cannot process request. ( " + res + " )");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i("JSON ERROR", e.getLocalizedMessage());
			dlgAlert.setTitle("JSON EXCEPTION");
			dlgAlert.setMessage("Cannot fetch resutls.");
		}
		dlgAlert.show();
	}

	private void showFailed(String title) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this.activity);
		dlgAlert.setTitle(title);
		dlgAlert.setCancelable(false);
		dlgAlert.setMessage("Cannot connect to the server right now.");
		dlgAlert.setPositiveButton("Okay !", null);
		dlgAlert.show();
	}
}
