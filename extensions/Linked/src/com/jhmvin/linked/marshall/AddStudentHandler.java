package com.jhmvin.linked.marshall;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.jhmvin.http.JsonResponse;

import cz.msebera.android.httpclient.Header;

/**
 * This class was updated and was not used anymore in this project.
 * 
 * @author Jhon Melvin
 * @date 11/03/2017
 */
@Deprecated
public class AddStudentHandler extends JsonResponse {

	public void log(Object message) {
		if (true) {
			Log.i("@AddStudentHandler", message.toString());
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		log("Request Started");
	}

	public String assigned_id;

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		// TODO Auto-generated method stub
		super.onSuccess(statusCode, headers, response);
		if (statusCode == 200) {
			try {
				String res = response.getString("result");
				if (res.equals("ok")) {
					assigned_id = response.getString("assigned");
				} else if (res.equals("not_void_existing")) {
					assigned_id = "already_added";
				} else {
					assigned_id = res;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log("Request Success");
	}

	@Override
	public void onRequestFailed(int statusCode) {
		// TODO Auto-generated method stub
		super.onRequestFailed(statusCode);
		log("Request Failed !");
		log(statusCode);
	}
}
