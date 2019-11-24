package com.jhmvin.http;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class JsonResponse extends JsonHttpResponseHandler {

	// -----------------------------------------------------------------------
	@Override
	public void onFailure(int statusCode, Header[] headers,
			String responseString, Throwable throwable) {
		// TODO Auto-generated method stub
		this.onRequestFailed(statusCode);
		super.onFailure(statusCode, headers, responseString, throwable);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONArray errorResponse) {
		// TODO Auto-generated method stub
		this.onRequestFailed(statusCode);
		super.onFailure(statusCode, headers, throwable, errorResponse);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONObject errorResponse) {
		// TODO Auto-generated method stub
		this.onRequestFailed(statusCode);
		super.onFailure(statusCode, headers, throwable, errorResponse);
	}

	/*
	 * Main Call
	 */
	public void onRequestFailed(int statusCode) {

	}
}
