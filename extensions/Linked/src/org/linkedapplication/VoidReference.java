package org.linkedapplication;

import org.json.JSONObject;
import org.linkedapplication.requests.FailureEvent;
import org.linkedapplication.requests.Request;
import org.linkedapplication.requests.RequestEvent;
import org.linkedapplication.requests.SingleResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.loopj.android.http.RequestParams;

/**
 * This class is a request to the server to cancel an existing line number. this
 * class was also used to void expired numbers.
 * 
 * SCOPE: this is for students used only
 * 
 * @author Jhon Melvin
 * 
 */
public class VoidReference {

	private Request cancelRequest;
	private Activity activity;
	private ProgressDialog progressDialog;
	private String pila_id;

	public VoidReference(Activity activity) {
		this.activity = activity;
		cancelRequest = new Request();
		cancelRequest.setApiLink("linked/cancel_reference");
		this.progressDialog = new ProgressDialog(this.activity);
		this.progressDialog.setCancelable(false);
		this.progressDialog.setIndeterminate(true);
		this.progressDialog.setMessage("Please Wait");
	}

	public void cancel(String pila_id) {
		RequestParams post = new RequestParams();
		post.add("pila_id", pila_id);
		cancelRequest.setParameters(post);
		this.pila_id = pila_id;

		cancelRequest.setOnStart(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				progressDialog.show();
			}
		});

		cancelRequest.setOnSuccess(new SingleResponse() {

			@Override
			public void event(JSONObject json) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		});

		cancelRequest.setOnFailure(new FailureEvent() {

			@Override
			public void salo(int status, String type) {
				// TODO Auto-generated method stub
				showFailed(type + " " + String.valueOf(status));
			}
		});

		cancelRequest.setOnFinish(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
			}
		});

		// start request
		cancelRequest.start();
	}

	private void showFailed(String title) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this.activity);
		dlgAlert.setTitle("Request Failed");
		dlgAlert.setCancelable(false);
		dlgAlert.setPositiveButton("Okay!",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						activity.finish();
					}
				});
		dlgAlert.setMessage("Cannot connect to the server right now.");
		dlgAlert.show();

	}
}
