package com.jhmvin.linked.requests;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.jhmvin.http.JsonResponse;

import cz.msebera.android.httpclient.Header;

/**
 * This class is used to perform checking if the phone is already in used.
 * 
 * @author Jhon Melvin
 * 
 */
public class PhoneCheckHandler extends JsonResponse {

	private void log(Object message) {
		boolean enableLogs = true;
		if (enableLogs) {
			Log.i("@PhoneChecker", message.toString());
		}
	}

	public PhoneCheckHandler(Activity taskActvity) {
		this.taskActivity = taskActvity;
	}

	// ------------------------------------------------

	public Activity taskActivity;
	//
	private String isUsed;
	private String accountID;

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		log("Phone Check Request Started . . .");

		super.onStart();
	}

	/**
	 * For Single Reply
	 */
	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		// TODO Auto-generated method stub
		if (statusCode == 200) {
			try {
				log("Request Susccess, JSON Recieved");

				this.isUsed = response.getString("using");
				if (this.isUsed.equals("YES")) {
					log("Phone Is Used");
					this.accountID = response.getString("acc_id");
				} else {
					log("Phone is Not Used");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				log("JSON Exception, invalid field");
			}
		} else {
			log("Request Failed: " + statusCode);
		}

		super.onSuccess(statusCode, headers, response);
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		super.onFinish();
	}

	// -----------------------------------------------------
	@Override
	public void onRequestFailed(int statusCode) {
		// TODO Auto-generated method stub
		super.onRequestFailed(statusCode);
		log("Request Failed");
	}

	// ----------------------------------------------------------------------

	// Results Needed


	public String getAccountID() {
		return accountID;
	}
	
	public String isUsing(){
		return this.isUsed;
	}
	




}
