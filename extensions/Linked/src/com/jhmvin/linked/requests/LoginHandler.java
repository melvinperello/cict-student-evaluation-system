package com.jhmvin.linked.requests;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.jhmvin.http.JsonResponse;

import cz.msebera.android.httpclient.Header;

public class LoginHandler extends JsonResponse {

	private void log(Object message) {
		boolean enableLogs = true;
		if (enableLogs) {
			Log.i("@LoginHandler", message.toString());
		}
	}

	private Activity taskActivity;
	private String requestResult;
	private JSONObject requestResponse;

	public LoginHandler(Activity activity) {
		this.taskActivity = activity;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		log("Login Request Started");
		super.onStart();
	}

	/**
	 * Recieved JSON OBJECT
	 */
	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		// TODO Auto-generated method stub
		if (statusCode == 200) {
			try {
				log("Request Success");
				requestResult = response.getString("state");
				requestResponse = response;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

		}
		super.onSuccess(statusCode, headers, response);
	}

	@Override
	public void onRequestFailed(int statusCode) {
		// TODO Auto-generated method stub
		log("Request Failed");
		super.onRequestFailed(statusCode);
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		log("Request Finished");
		super.onFinish();
	}



	public String getRequestResult() {
		return requestResult;
	}

	public JSONObject getRequestResponse() {
		return requestResponse;
	}

}
