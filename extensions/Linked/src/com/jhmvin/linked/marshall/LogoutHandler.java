package com.jhmvin.linked.marshall;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.jhmvin.http.JsonResponse;

import cz.msebera.android.httpclient.Header;

public class LogoutHandler extends JsonResponse {

	private void log(Object message) {
		if (true) {
			Log.i("@LogoutHandler", message.toString());
		}
	}

	private Boolean is_ok;

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		// TODO Auto-generated method stub
		super.onSuccess(statusCode, headers, response);
		try {
			String status = response.getString("status");
			if (status.equals("org_log_out")) {
				is_ok = true;
			} else if (status.equals("org_log_out_failed")) {
				is_ok = false;
			} else {
				is_ok = false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			is_ok = false;
			log("JSON EXCEPTION: " + e.getMessage());
		}
	}
	
	@Override
	public void onRequestFailed(int statusCode) {
		// TODO Auto-generated method stub
		super.onRequestFailed(statusCode);
		log("Request Failed");
	}

	public Boolean isOk() {
		return this.is_ok;
	}
}
