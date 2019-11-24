package com.jhmvin.actors;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.jhmvin.commons.Commons;
import com.jhmvin.http.HttpRequest;
import com.jhmvin.linked.Login;
import com.jhmvin.linked.MarshallMain;
import com.jhmvin.linked.StudentMain;
import com.jhmvin.linked.marshall.LogoutHandler;
import com.jhmvin.linked.requests.LoginHandler;
import com.jhmvin.linked.requests.PhoneCheckHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class Authenticator {

	private void log(Object message) {
		if (true) {
			Log.i("@Authenticator", message.toString());
		}
	}

	public Authenticator(Activity activity) {
		this.authActivity = activity;
		/**
		 * Progress dialog setup
		 */
		this.progressDialog = new ProgressDialog(activity);
		this.progressDialog.setCancelable(false);
		this.progressDialog.setIndeterminate(true);
	}

	private Activity authActivity;
	private ProgressDialog progressDialog;

	// flags
	private boolean isConnected = false;
	private boolean isValidating = false;

	//
	private ConnectivityManager connManager;
	private NetworkInfo myWifi;
	private WifiManager wifiManager;
	private WifiInfo wifiInformation;

	/**
	 * Checks if the phone has wifi connection
	 */
	public void checkWifi() {

		/**
		 * Show Progress
		 */
		this.progressDialog.setTitle("Checking Connectivity");
		this.progressDialog.setMessage("Please wait . . .");
		this.progressDialog.show();

		// get connectivity manager
		this.connManager = (ConnectivityManager) authActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// get wifi Manager
		this.myWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		// check if connected
		if (myWifi.isConnected()) {
			/**
			 * Wifi Is Connected
			 */
			this.wifiManager = (WifiManager) authActivity
					.getSystemService(Context.WIFI_SERVICE);

			// get information
			this.wifiInformation = wifiManager.getConnectionInfo();
			// String ssid = wifiInformation.getSSID();
			/**
			 * Proceed to verification
			 */
			this.isConnected = true;
			this.verifyPhone();
		} else {
			/**
			 * If No Wi-Fi Connection
			 */
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
					this.authActivity);
			dlgAlert.setTitle("Your Wi-Fi is Off");
			dlgAlert.setMessage("Hi CICT Students! Please Turn On Your Wi-Fi, and connect to CICT EVALUATION.");
			dlgAlert.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					authActivity.finish();
				}
			});
			// dismiss progress
			progressDialog.dismiss();
			dlgAlert.show();
		}
	}

	/**
	 * if there is wifi connection verify if the phone is already used
	 */
	private void verifyPhone() {
		if (this.isConnected) {
			final String imei = Commons.getIMEI(this.authActivity
					.getSystemService(Context.TELEPHONY_SERVICE));

			HttpRequest.get("linked/is_used/" + imei, null,
					new PhoneCheckHandler(authActivity) {
						@Override
						public void onStart() {

							/**
							 * Set to true so one process only will be dismissed
							 * on alert message
							 */
							isValidating = true;
							progressDialog.setTitle("Phone Verification");
							progressDialog.setMessage("Please wait . . .");
						};

						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							// TODO Auto-generated method stub
							isValidating = false;
							super.onSuccess(statusCode, headers, response);
							// do logic here
							if (super.isUsing().equalsIgnoreCase("YES")) {
								skipAuth(super.getAccountID(), imei);

							}

						}

						@Override
						public void onRequestFailed(int statusCode) {
							// TODO Auto-generated method stub
							super.onRequestFailed(statusCode);
							retryVerification();
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							super.onFinish();

							progressDialog.dismiss();
						}

					});
		}
	}

	/**
	 * if verification has failed, retry prompt
	 */
	private void retryVerification() {
		String retry_message = "Hi There! We cannot verify your mobile phone right now,"
				+ " connection to the server may be having some problems. Please bear with us for the moment,"
				+ " Thank You!";

		DialogInterface.OnClickListener retry_choices = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.i("choice", String.valueOf(which));
				switch (which) {
				case -1:
					isValidating = false;
					progressDialog.show();
					verifyPhone();
					break;
				case -2:
					authActivity.finish();
					break;
				default:
					break;
				}
			}
		};

		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				this.authActivity);
		dlgAlert.setTitle("Cannot Verify");
		dlgAlert.setMessage(retry_message);
		dlgAlert.setPositiveButton("Retry", retry_choices);
		dlgAlert.setNegativeButton("Exit", retry_choices);
		dlgAlert.setCancelable(false);
		dlgAlert.show();
	}

	/**
	 * if verification is successful and true
	 * 
	 */
	private void skipAuth(String account_id, String imei) {
		authUsername = "cict";
		authPassword = "cict";
		authIMEI = imei;
		authIsSkipped = Integer.valueOf(account_id);
		authenticate();
	}

	public boolean isValidating() {
		return this.isValidating;
	}

	// -------------Login

	public String authUsername;
	public String authPassword;
	public String authIMEI;
	public int authIsSkipped;

	public void authenticate() {

//		HttpRequest.get("linked/login/" + authUsername + "/" + authPassword
//				+ "/" + authIMEI + "/" + authIsSkipped, null, new LoginHandler(
//				this.authActivity) {
		RequestParams post = new RequestParams();
		post.add("username", authUsername);
		post.add("password", authPassword);
		post.add("imei", authIMEI);
		post.add("skipped", String.valueOf(authIsSkipped));
		
		HttpRequest.post("linked/login", post, new LoginHandler(
		this.authActivity) {
			@Override
			public void onStart() {
				/**
				 * Set to true so nothing will happen on resume
				 */
				isValidating = true;
				// TODO Auto-generated method stub
				progressDialog.setTitle("Logging In");
				progressDialog
						.setMessage("Please wait while authenticating . . .");
				progressDialog.show();
				super.onStart();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				isValidating = false;
				progressDialog.dismiss();
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if (super.getRequestResult().equals("success")) {
					log("$auth_request: success");
					whenLoginSuccess(response);
				} else if (super.getRequestResult().equals("wrong_password")) {
					log("$auth_request: wrong_password");
					whenWrongPassword();
				} else if (super.getRequestResult().equals("not_exist")) {
					log("$auth_request: not_exist");
					whenNotExisting();
				} else if (super.getRequestResult().equals("blocked")) {
					log("$auth_request: blocked");
					whenBlocked(response);
				} else if (super.getRequestResult().equals("is_active")) {
					log("$auth_request: is_active");
					whenAlreadyActive();
				}
			}
		});
	}

	/**
	 * Successfully logged in
	 */
	private void whenLoginSuccess(JSONObject response) {
		try {
			Student.instance().setAccountID(response.getString("account_id"));
			Student.instance().setUsername(response.getString("username"));
			Student.instance().setAccessLevel(response.getString("access"));
			Student.instance().setAffiliates(response.getString("affiliates"));
			Student.instance().setCictID(response.getString("cict_id"));
			Student.instance().setStudentNumber(
					response.getString("student_number"));
			Student.instance().setLastName(response.getString("last_name"));
			Student.instance().setFirstName(response.getString("first_name"));
			Student.instance().setMiddleName(response.getString("middle_name"));
			Student.instance().setGender(response.getString("gender"));
			Student.instance().setYear(response.getString("year"));
			Student.instance().setSection(response.getString("section"));
			Student.instance().setGroup(response.getString("group"));
			Student.instance().setHasProfile(response.getString("has_profile"));
			Student.instance().setEnrollmentType(
					response.getString("enrollment_type"));
			Student.instance().setAdmissionYear(
					response.getString("admission_year"));

			// added
			Student.instance().setCourseCode(response.getString("course_code"));
			Student.instance().setCourseName(response.getString("course_name"));

			// set student curriculum
			Student.instance().setCurrentAcademicTerm(
					response.getString("current_term"));

			// meron na dito
			Log.i("PUTA", Student.instance().getCurrentAcademicTerm()
					.toString());

			if (Student.instance().check()) {
				// User is authenticated
				userIsAuthenticated();
			} else {
				whenAuthenticationCheckFails();
			}

		} catch (JSONException e) {
			log("$whenLoginSuccess: JSON EXCEPTION");
		}
	}

	private void userIsAuthenticated() {
		if (Student.instance().getAccessLevel().equals("ORGANIZATIONAL")) {
			Intent marshallMain = new Intent(this.authActivity,
					MarshallMain.class);
			this.authActivity.startActivity(marshallMain);
			this.authActivity.finish();
		} else if (Student.instance().getAccessLevel().equals("STUDENT")) {
			Intent studentMain = new Intent(this.authActivity,
					StudentMain.class);
			this.authActivity.startActivity(studentMain);
			this.authActivity.finish();
		} else {
			// what happened?
		}
	}

	private void whenAuthenticationCheckFails() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				this.authActivity);
		dlgAlert.setTitle("Authentication Error");
		dlgAlert.setMessage("There was an error retrieving authentication values. Sorry For The Inconvenience, Thank You !");
		dlgAlert.setPositiveButton("Ok", null);
		dlgAlert.setCancelable(true);
		dlgAlert.show();
	}

	private void whenWrongPassword() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				this.authActivity);
		dlgAlert.setTitle("Wrong Password");
		dlgAlert.setMessage("Your password is wrong, please remember it carefully first before trying again.");
		dlgAlert.setPositiveButton("Okay", null);
		dlgAlert.setCancelable(true);
		dlgAlert.show();
	}

	private void whenNotExisting() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				this.authActivity);
		dlgAlert.setTitle("Account Not Recognize");
		dlgAlert.setMessage("This account is not existing, please create you account first if you don't have one. Thank You !");
		dlgAlert.setPositiveButton("Try Again !", null);
		dlgAlert.setNegativeButton("Register Me !", null);
		dlgAlert.setCancelable(true);
		dlgAlert.show();

	}

	private void whenBlocked(JSONObject response) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				this.authActivity);
		try {
			String timeUntilAllow = response.getString("blocked_until");
			dlgAlert.setTitle("Hey ! Don't Spam !");
			dlgAlert.setMessage("Too many incorrect attemps, you are not allowed to logged in until "
					+ timeUntilAllow + ". Thank You !");
			dlgAlert.setPositiveButton("Okay, I Understand.", null);
			dlgAlert.setNegativeButton("I Forgot My Password", null);
			dlgAlert.setCancelable(true);
			dlgAlert.show();
		} catch (JSONException e) {

		}
	}

	private void whenAlreadyActive() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				this.authActivity);
		dlgAlert.setTitle("This Account is Online");
		dlgAlert.setMessage("Your account is already active in another phone, for students that did not scanned their code please use the web version.");
		dlgAlert.setPositiveButton("I see!", null);
		dlgAlert.setCancelable(true);
		dlgAlert.show();
	}

	// ----------------------------------------------------------------

}
