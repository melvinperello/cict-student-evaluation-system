package com.jhmvin.actors;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import org.linkedapplication.requests.FailureEvent;
import org.linkedapplication.requests.GetRequest;
import org.linkedapplication.requests.Request;
import org.linkedapplication.requests.RequestEvent;
import org.linkedapplication.requests.SingleResponse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.jhmvin.http.HttpRequest;
import com.jhmvin.linked.Login;
import com.jhmvin.linked.MarshallMain;
import com.jhmvin.linked.MarshallUpdatedStudentInfo;
import com.jhmvin.linked.R;
import com.jhmvin.linked.marshall.LogoutHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.melvin.android.Notify;
import com.melvin.updates.MarshallUpdateStudentInfoController;

import cz.msebera.android.httpclient.Header;

public class Marshall {

	private void log(Object message) {
		if (true) {
			Log.i("@Marshall", message.toString());
		}
	}

	private MarshallMain marshall_activity;
	private ProgressDialog progressDialog;

	public String student_number;

	public Marshall(Activity act) {
		this.marshall_activity = (MarshallMain) act;
		this.progressDialog = new ProgressDialog(act);
		this.progressDialog.setCancelable(false);
		this.progressDialog.setIndeterminate(true);
	}

	// --------------------------------------------------------------------
	/**
	 * kore wa section , imi to search desu !
	 */

	public void search() {
		has_found = false;
		GetRequest searchStudent = new GetRequest();
		searchStudent.setApiLink("linked/search/" + student_number);

		searchStudent.setOnStart(new RequestEvent() {
			@Override
			public void event() {
				Notify.log("Search Request Started");
				// TODO Auto-generated method stub
				progressDialog.setTitle("Searching . . .");
				progressDialog.setMessage("Please wait while searching . . .");
				progressDialog.show();
				Notify.log("Search Request Started 2");
			}
		});
		searchStudent.setOnSuccess(new SingleResponse() {

			@Override
			public void event(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					String result = json.getString("result");
					if (result.equals("ok")) {
						whenStudentFound(json);
					} else if (result.equals("not_found")) {
						whenNotExist();
					} else if (result.equals("not_ok")) {
						String description = json.getString("description");
						Notify.showMessage(marshall_activity, "Failed",
								description);
					}
				} catch (Exception e) {
					Notify.toastShort(marshall_activity,
							"Request successfully sent, Cannot display results please reload.");
				}
			}
		});
		searchStudent.setOnFailure(new FailureEvent() {

			@Override
			public void salo(int status, String type) {
				// TODO Auto-generated method stub
				Notify.showMessage(marshall_activity, "Search Failed",
						"The server is unreachable at the moment. [ERR: "
								+ status + " - " + type + "]");
			}
		});
		searchStudent.setOnFinish(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
			}
		});
		searchStudent.start();
	}

	/**
	 * When student was not found result = not_found
	 */
	private void whenNotExist() {
		messageMe("Not Exist !",
				"The student that you are searching for does not exist.");
		this.marshall_activity.pnl_landing.setVisibility(View.VISIBLE);
		this.marshall_activity.pnl_show.setVisibility(View.GONE);
	}

	/**
	 * Search values
	 */
	private String pila_id;
	private String cict_id;
	private String full_name;
	private String course;
	private String gender;
	private String imei;
	private boolean has_found = false;
	private String photo_name;

	/**
	 * When student was found. result = OK
	 * 
	 * @param response
	 */
	private void whenStudentFound(JSONObject response) {
		try {
			has_found = true;
			// -------------------------------------------------
			// Recieve Search Values.
			pila_id = response.getString("pila_id");
			cict_id = response.getString("cict_id");
			student_number = response.getString("student_number");
			full_name = response.getString("full_name");
			course = response.getString("course");
			gender = response.getString("gender");
			imei = response.getString("imei");
			photo_name = response.getString("photo");
			// -------------------------------------------------
			if (pila_id.equals("not_qued")) {
				// ---------------------------------------------
				// Student Not Yet Added To List
				pila_id = "Not Inline";
				// Show add to Que button
				this.marshall_activity.btn_showPositive
						.setVisibility(View.VISIBLE);
				this.marshall_activity.btn_showPositive.setText("ADD TO QUE");
				// Show update info
				this.marshall_activity.btn_update.setVisibility(View.VISIBLE);
				// -------------------------------------------
				// Add Update Event
				this.marshall_activity.btn_update
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent updateIntent = new Intent(
										marshall_activity,
										MarshallUpdatedStudentInfo.class);
								marshall_activity.startActivity(updateIntent);
								MarshallUpdateStudentInfoController.cictID = cict_id;
							}
						});
				// -------------------------------------------

			} else {
				// ---------------------------------------------
				// If student is already added to the list
				if (pila_id.equals("called")) {
					// -----------------------------------------
					// When the student is already called
					// Show the positive button then set the text to Entrance

					// -----------------------------------------------------
					// @UPDATE 12/29/2017
					// since the calling mechanism is manual this is
					// not needed to be shown
					 this.marshall_activity.btn_showPositive
					 .setVisibility(View.GONE);
					 this.marshall_activity.btn_showPositive
					 .setText("ENTRANCE PASS");
					// -----------------------------------------------------
					this.marshall_activity.btn_update.setVisibility(View.GONE);
					// -----------------------------------------
					// Show line number as called
					pila_id = "CALLED";
				} else {
					// -----------------------------------------
					// When the student is not yet called
					String floor_assignment = "";
					String floor_number = "";
					// try to get number information
					try {
						floor_assignment = response
								.getString("floor_assignment");
						floor_number = response.getString("floor_number");
						pila_id = "IN LINE @ FLR" + floor_assignment + "/ # "
								+ floor_number;
					} catch (Exception e) {
						pila_id = "IN LINE";
					}
					// ----------------------------------------
					// Hide add or entrance button
					this.marshall_activity.btn_showPositive
							.setVisibility(View.GONE);
					// Hide update button
					this.marshall_activity.btn_update.setVisibility(View.GONE);
					// --------------------------------------------------
				}
			}
			// -----------------------------------------------------------
			// Set values
			this.marshall_activity.lbl_studentNumber.setText(student_number);
			this.marshall_activity.lbl_name.setText(full_name);
			this.marshall_activity.lbl_course.setText(course);
			this.marshall_activity.lbl_listing.setText(pila_id);
			this.marshall_activity.lbl_gender.setText(gender);
			// check Imei depending on search type
			if (this.marshall_activity.searchScan == false) {
				this.marshall_activity.lbl_imei.setText(imei);
			} else {
				this.marshall_activity.searchScan = false;
			}
			// Show Search Results and Hide the Home Page.
			this.marshall_activity.pnl_landing.setVisibility(View.GONE);
			this.marshall_activity.fabButton.setVisibility(View.GONE);
			this.marshall_activity.pnl_show.setVisibility(View.VISIBLE);
			// -----------------------------------------------------------
			// Load Profile Picture
			if (photo_name.equalsIgnoreCase("NONE")) {
				// do nothing
				loadDefaultPhoto();
			} else {
				// load picture
				loadAvatar(photo_name);
			}
			// ---------------------------------------------
		} catch (JSONException je) {
			Notify.toastShort(marshall_activity,
					"Request successfully sent, Cannot display results please reload.");
		}
	}

	// --------------------------------------------------------------------

	public void logout() {
		Integer acc_id = Student.instance().getAccountID();
		String access = Student.instance().getAccessLevel();
		// HttpRequest.get("linked/logout/" + acc_id + "/" + access, null,
		// new LogoutHandler() {
		RequestParams post = new RequestParams();
		post.add("acc_id", acc_id.toString());
		post.add("access", access);

		HttpRequest.post("linked/logout", post, new LogoutHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				progressDialog.setTitle("Logging Out");
				progressDialog.setMessage("Please wait . . .");
				progressDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if (super.isOk()) {
					Intent login = new Intent(marshall_activity, Login.class);
					marshall_activity.startActivity(login);
					marshall_activity.finish();
				} else {
					log("account already logged out");
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				progressDialog.dismiss();
			}

			@Override
			public void onRequestFailed(int statusCode) {
				// TODO Auto-generated method stub
				super.onRequestFailed(statusCode);
				messageMe(
						"Logout Failed",
						"Sorry we cannot process your request right now. Please try to logout again later.");
			}
		});
	}

	// --------------------------------------------------------------------
	/**
	 * Add student to the Queue.
	 */
	public void addStudent() {
		if (!has_found) {
			return;
		}
		// ---------------------------------------------------------------------
		// Parameters
		RequestParams post = new RequestParams();
		post.add("acad_term", Student.instance().getCurrentAcademicTerm()
				.toString());
		post.add("cict_id", this.cict_id);
		post.add("conforme", this.student_number /* this.full_name */);
		post.add("course", this.course);
		post.add("imei", this.marshall_activity.lbl_imei.getText().toString());
		// ---------------------------------------------------------------------
		Request addToQue = new Request();
		addToQue.setApiLink("linked/add_student");
		addToQue.setParameters(post);

		addToQue.setOnStart(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				Notify.log("Add to que request started . . .");
				progressDialog.setTitle("Adding To List");
				progressDialog.setMessage("Please wait while a while . . .");
				progressDialog.show();
			}
		});

		addToQue.setOnFailure(new FailureEvent() {

			@Override
			public void salo(int status, String type) {
				// TODO Auto-generated method stub
				Notify.showMessage(marshall_activity, "Queueing Failed",
						"The server is unreachable at the moment. [ERR: "
								+ status + " - " + type + "]");
			}
		});

		addToQue.setOnSuccess(new SingleResponse() {

			@Override
			public void event(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					String success = json.getString("success");
					String description = json.getString("description");
					if (success.equals("1")) {
						messageMe("Success", description);
					} else {
						messageMe("Failed", description);
					}
					marshall_activity.btn_showCancel.callOnClick();
				} catch (Exception e) {
					messageMe("Unreadable Result",
							"The request was sent but the server reply with unreadable result.");
				}
			}
		});
		addToQue.setOnFinish(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				Notify.log("Request ended . . .");
			}
		});
		addToQue.start();
		// ---------------------------------------------------------------------
		// HttpRequest.post("linked/add_student", post, new AddStudentHandler()
		// {
		// @Override
		// public void onStart() {
		// // TODO Auto-generated method stub
		// super.onStart();
		// progressDialog.setTitle("Adding To List");
		// progressDialog.setMessage("Please wait while a while . . .");
		// progressDialog.show();
		//
		// }
		//
		// @Override
		// public void onSuccess(int statusCode, Header[] headers,
		// JSONObject response) {
		// // TODO Auto-generated method stub
		// super.onSuccess(statusCode, headers, response);
		// if (super.assigned_id != null) {
		//
		// // already exist
		// if (super.assigned_id.equalsIgnoreCase("already_added")) {
		// messageMe("Already Added",
		// "This student has already added to the list.");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		//
		// if (super.assigned_id
		// .equalsIgnoreCase("not_inserted_floor")) {
		// messageMe("Failed",
		// "Student has not selected a Floor Assignment");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		//
		// // --- profile
		// if (super.assigned_id.equalsIgnoreCase("no_profile")) {
		// messageMe(
		// "No Profile",
		// "Profiling is required before proceeding, please advised the student to fill up profiling form.");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		//
		// // --------------------------------------------------------
		// // settings here
		// if (super.assigned_id.equalsIgnoreCase("cut_off")) {
		// messageMe(
		// "Paused",
		// "Numbering temporarily paused, this may be due to impending cut-off or emergency faculty meeting.");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		//
		// if (super.assigned_id.equalsIgnoreCase("max_reached")) {
		// messageMe("Cut-Off",
		// "Max number of students have been reached. Numbering is now disabled.");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		//
		// // --------------------------------------------------------
		//
		// if (super.assigned_id.equalsIgnoreCase("not_inserted")) {
		// messageMe("Failed", "Cannot add right now.");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		//
		// // validation
		// if (super.assigned_id.equalsIgnoreCase("student_not_exist")) {
		// messageMe("Failed", "Student Not Existing.");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		//
		// // if (super.assigned_id.equalsIgnoreCase("student_0_profile")) {
		// // messageMe("Profile",
		// // "Student is required to fill up profiling form.");
		// // marshall_activity.btn_showCancel.callOnClick();
		// // return;
		// // }
		// if (super.assigned_id.equalsIgnoreCase("no_account")) {
		// messageMe("Account",
		// "Student has no account, account registration is required.");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		// if (super.assigned_id
		// .equalsIgnoreCase("access_not_student")) {
		// messageMe("Access Denied",
		// "Queueing is for students only, Marshalls are not allowed to participate.");
		// marshall_activity.btn_showCancel.callOnClick();
		// return;
		// }
		//
		// // end validation
		//
		// // final filter
		// try {
		// Integer.parseInt(super.assigned_id);
		// } catch (NumberFormatException nfe) {
		// messageMe("Error", "Unknown Server Response Recieved");
		// return;
		// }
		//
		// // if ok
		// messageMe("Successfully Added", "ASSIGNED NUMBER IS: "
		// + super.assigned_id);
		//
		// marshall_activity.btn_showCancel.callOnClick();
		// }
		// }
		//
		// @Override
		// public void onRequestFailed(int statusCode) {
		// // TODO Auto-generated method stub
		// super.onRequestFailed(statusCode);
		// messageMe(
		// "Cannot Process Request",
		// "Sorry but we cannot process your request at the moment. Please try again thank you!");
		// }
		//
		// @Override
		// public void onFinish() {
		// // TODO Auto-generated method stub
		// super.onFinish();
		// progressDialog.dismiss();
		// }
		// });
	}

	// --------------------------------------------------------------------

	private void messageMe(String title, String message) {
		Notify.showMessage(this.marshall_activity, title, message);
		// AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
		// this.marshall_activity);
		// dlgAlert.setTitle(title);
		// dlgAlert.setMessage(message);
		// dlgAlert.setPositiveButton("Okay", null);
		// dlgAlert.setCancelable(true);
		// dlgAlert.show();
	}

	/**
	 * Load student profile picture
	 * 
	 * @param photo_name
	 *            actual name of the photo
	 */
	private void loadAvatar(String photo_name) {
		// RequestParams params = new RequestParams
		HttpRequest.fileGet("linked/photo/" + photo_name, null,
				new FileAsyncHttpResponseHandler(this.marshall_activity) {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						progressDialog.setTitle("Loading");
						progressDialog.setMessage("Checking Profile . . .");
						progressDialog.show();
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, File arg2) {
						// TODO Auto-generated method stub
						Log.i("FILE CAPTURED", arg2.toString());

						Bitmap avatar = BitmapFactory.decodeFile(arg2
								.getAbsolutePath());
						marshall_activity.img_avatar.setImageBitmap(avatar);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1,
							Throwable arg2, File arg3) {
						// TODO Auto-generated method stub
						Log.i("FAILEd", "Image Not Found");
						loadDefaultPhoto();
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						progressDialog.dismiss();
					}
				});

	} // end function

	private void loadDefaultPhoto() {
		marshall_activity.img_avatar.setImageResource(R.drawable.defaultpic);
	}
}
