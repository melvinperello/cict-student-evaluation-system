package com.jhmvin.http;

import com.jhmvin.commons.ApplicationCache;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpRequest {
	private static String requestHost = ApplicationCache.view().APP_IP;
	// private final static String requestHost = "cictportal.dx.am";
	private final static String restServer = "/laravel/linked-php-api/public/";
	// private final static String restServer = "/linked/public/";
	private final static AsyncHttpClient httpClient = new AsyncHttpClient();

	private static String getRequestUrl(String route) {
		return "http://" + requestHost + restServer + route;
	}

	/**
	 * Make a get request to the server.
	 * 
	 * @param url
	 * @param params
	 * @param requestHandler
	 */
	public static void get(String url, RequestParams params,
			JsonHttpResponseHandler requestHandler) {
		httpClient.get(getRequestUrl(url), params, requestHandler);
	}

	/**
	 * Make a post request to the server.
	 * 
	 * @param url
	 * @param params
	 * @param requestHandler
	 */
	public static void post(String url, RequestParams params,
			JsonHttpResponseHandler requestHandler) {
		httpClient.post(getRequestUrl(url), params, requestHandler);
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param request
	 */
	public static void filePost(String url, RequestParams params,
			FileAsyncHttpResponseHandler request) {
		httpClient.post(getRequestUrl(url), params, request);
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param request
	 */
	public static void fileGet(String url, RequestParams params,
			FileAsyncHttpResponseHandler request) {
		httpClient.get(getRequestUrl(url), null, request);
	}

}
