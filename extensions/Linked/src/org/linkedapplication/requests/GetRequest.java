package org.linkedapplication.requests;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.jhmvin.http.HttpRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Only supports post request for security reasons.
 * 
 * @author Jhon Melvin
 * 
 */
public class GetRequest {

	public GetRequest() {
		//
	}

	private String apiLink;

	private JsonHttpResponseHandler handler;

	public void setApiLink(String apiLink) {
		this.apiLink = apiLink;
	}

	public void start() {
		HttpRequest.get(this.apiLink, null, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				if (onStart != null) {
					onStart.event();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					if (onSuccess != null) {
						onSuccess.event(response);
					}

				} else {
					onFailure.salo(statusCode, "ERROR_SUCCESS_OBJECT");
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					if (onSuccessArray != null) {
						onSuccessArray.event(response);
					}

				} else {
					onFailure.salo(statusCode, "ERROR_SUCCESS_ARRAY");
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				if (onFinish != null) {
					onFinish.event();
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				super.onCancel();
				if (onCancel != null) {
					onCancel.event();
				}

			}

			// ----- fails
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);

				if (onFailure != null) {
					onFailure.salo(statusCode, "ERROR_STRING");
				}
				Log.e("@RequestFailedString", responseString);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				if (onFailure != null) {
					onFailure.salo(statusCode, "ERROR_ARRAY");
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);

				if (onFailure != null) {
					onFailure.salo(statusCode, "ERROR_OBJECT");
				}
			}
		});
	}

	private SingleResponse onSuccess;
	private ArrayResponse onSuccessArray;
	private RequestEvent onStart;
	private RequestEvent onFinish;
	private RequestEvent onCancel;
	private FailureEvent onFailure;

	public void setOnSuccess(SingleResponse onSuccess) {
		this.onSuccess = onSuccess;
	}

	public void setOnSuccessArray(ArrayResponse onSuccessArray) {
		this.onSuccessArray = onSuccessArray;
	}

	public void setOnStart(RequestEvent onStart) {
		this.onStart = onStart;
	}

	public void setOnFinish(RequestEvent onFinish) {
		this.onFinish = onFinish;
	}

	public void setOnCancel(RequestEvent onCancel) {
		this.onCancel = onCancel;
	}

	public void setOnFailure(FailureEvent onFailure) {
		this.onFailure = onFailure;
	}

}
