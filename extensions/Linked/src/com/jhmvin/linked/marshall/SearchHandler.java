package com.jhmvin.linked.marshall;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.jhmvin.http.JsonResponse;
import com.melvin.android.Notify;

import cz.msebera.android.httpclient.Header;

/**
 * This class was updated and not used in this project anymore.
 * 
 * @author Jhon Melvin
 * @date 11/03/2017
 */
@Deprecated
public class SearchHandler extends JsonResponse {

	private void log(Object message) {
		if (true) {
			Log.i("@SearchHandler", message.toString());
		}
	}

	private Activity taskActivity;

	// res
	public String result;
	public String pila_id;
	public String cict_id;
	public String full_name;
	public String course;
	public String gender;

	public SearchHandler(Activity a) {
		this.taskActivity = a;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		log("Request Started . . .");
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		// TODO Auto-generated method stub
		super.onSuccess(statusCode, headers, response);
		if (statusCode == 200) {
			log("Request Success !");
			try {
				result = response.getString("result");
				if (result.equals("ok")) {
					pila_id = response.getString("pila_id");
					cict_id = response.getString("cict_id");
					full_name = response.getString("full_name");
					course = response.getString("course");
					gender = response.getString("gender");
				} else if (result.equals("not_found")) {
					log("Student not existing");
				} else if ("not_ok".equals(result)) {
					// ---------------------------------------------------
					String description = response.getString("description");
					Notify.showMessage(taskActivity, "Failed", description);
					// ---------------------------------------------------
				} else {
					log("Unrecognized response . . .");
				}
			} catch (JSONException je) {
				log("JSON Exception");
			}

		} else {
			log("Request Code not complete");
		}
	}
}
