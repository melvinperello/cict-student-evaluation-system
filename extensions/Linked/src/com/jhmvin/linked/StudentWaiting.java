package com.jhmvin.linked;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.linkedapplication.VoidReference;
import org.linkedapplication.requests.FailureEvent;
import org.linkedapplication.requests.GetRequest;
import org.linkedapplication.requests.Request;
import org.linkedapplication.requests.RequestEvent;
import org.linkedapplication.requests.SingleResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhmvin.actors.Student;
import com.jhmvin.commons.Commons;
import com.jhmvin.commons.PublicConstants;
import com.jhmvin.update.concurrency.IkotIkotTask;
import com.jhmvin.update.concurrency.TaskRoll;
import com.loopj.android.http.RequestParams;

public class StudentWaiting extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this);
		setContentView(R.layout.activity_student_waiting);

		View main = findViewById(R.id.pnl_waiting);
		Commons.startGradientBackground(main);

		init();
		events();
	}

	/**
	 * Intentionally when the user press backs the app will close.
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		refresh.stop();
	}

	/**
	 * For logging
	 * 
	 * @param message
	 */
	private void logs(Object message) {
		Log.i("@StudentWaiting", message.toString());
	}

	private TextView lbl_mynumber, lbl_room, lbl_accepted, lbl_called,
			lbl_expire, lbl_showinfo, lbl_current, lbl_announcement, lbl_title,
			lbl_evaluator, lbl_terminal;
	private ImageView img_logout;

	private void init() {
		lbl_mynumber = (TextView) findViewById(R.id.lbl_mynumber);
		lbl_room = (TextView) findViewById(R.id.lbl_room);
		lbl_accepted = (TextView) findViewById(R.id.lbl_accepted);
		lbl_called = (TextView) findViewById(R.id.lbl_called);
		lbl_expire = (TextView) findViewById(R.id.lbl_expired);
		lbl_showinfo = (TextView) findViewById(R.id.lbl_showinfo);

		lbl_current = (TextView) findViewById(R.id.lbl_current);
		img_logout = (ImageView) findViewById(R.id.img_logout);
		lbl_announcement = (TextView) findViewById(R.id.lbl_announcement);
		this.lbl_title = (TextView) findViewById(R.id.lbl_announcement_title);
		//
		this.lbl_evaluator = (TextView) findViewById(R.id.lbl_caller);
		this.lbl_terminal = (TextView) findViewById(R.id.lbl_terminal);

		//
		requestOnProgress = false;

		PARAM_CICT_ID = Student.instance().getCictID().toString();
	}

	private String PARAM_CICT_ID;

	/**
	 * Returns the current activity.
	 * 
	 * @return
	 */
	private Activity getCurrentActivity() {
		return this;
	}

	private String pila_id;

	private void events() {
		/**
		 * Run the loop. to refresh details
		 */
		onRefreshDetails();

		/**
		 * Displays the entrance pass.
		 */
		findViewById(R.id.img_info).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// viewEntrance();
			}
		});

		/**
		 * Displays the entrance pass.
		 */
		lbl_showinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// viewEntrance();
			}
		});

		/**
		 * Logouts the user and cancel's his/her number.
		 */
		img_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pila_id != null) {

					AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
							getCurrentActivity());
					dlgAlert.setTitle("Cancel Your Number");
					dlgAlert.setMessage("Are you sure you want to cancel your number?");
					dlgAlert.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									VoidReference vr = new VoidReference(
											getCurrentActivity());
									vr.cancel(pila_id);
									Log.i("CANCEL_PILA_ID", pila_id);
									// getCurrentActivity().finish();
								}
							});
					dlgAlert.setNegativeButton("No", null);
					dlgAlert.setCancelable(false);
					dlgAlert.show();

				}

			}
		});
	}

	/**
	 * Displays the entrance pass. entrance pass is just the same as an ordinary
	 * qr for scanning.
	 */
	private void viewEntrance() {
		Intent epass = new Intent(this, StudentEntrance.class);
		String message = "";
		if (lbl_called.getText().toString().isEmpty()) {
			epass.putExtra("message", "Wait for your number to be called.");
		} else {
			epass.putExtra("message", "Expire: "
					+ lbl_expire.getText().toString());
		}
		this.startActivity(epass);

	}

	/**
	 * Loop Request.
	 */
	private TaskRoll refresh;

	private void onRefreshDetails() {
		refresh = new TaskRoll();
		refresh.setInterval(PublicConstants.getWaitingInterval());
		refresh.setTask(new IkotIkotTask() {

			@Override
			public void repeatMe() {
				// TODO Auto-generated method stub
				if (requestOnProgress) {
					logs("Request on progress skipping >>>>");
				} else {
					getReferenceDetails();
				}
			}
		});

		refresh.startTask();
	}

	/**
	 * Request to get details.
	 */
	private boolean requestOnProgress;

	private JSONObject reference_pila;

	/**
	 * The request that asks the server about the details of the student queue
	 * number. this is the one theat displays the waiting and other information.
	 */
	private void getReferenceDetails() {
		/**
		 * @REVISION 11/13/2017 - This request should include also the room
		 *           assignment of the cluster where the student is belonging
		 *           to.
		 */
		GetRequest pilaRequest = new GetRequest();
		pilaRequest.setApiLink("linked/check_number/" + PARAM_CICT_ID);
		// -------------------------------------------------------------
		pilaRequest.setOnStart(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				requestOnProgress = true;
			}
		});
		// -------------------------------------------------------------
		pilaRequest.setOnSuccess(new SingleResponse() {

			@Override
			public void event(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					String pila_status = json.getString("pila_status");
					if (pila_status.equalsIgnoreCase("no")) {
						Log.i("FINISHED", "ENTERED SUCCESSFULLY");
						refresh.stop();
						yeheyDialog();
						return;
					}
					// -------------------------------------------------
					// Get Server Response
					reference_pila = json.getJSONObject("pila_info");
					String currentlyCalled = json.getString("called");
					String latestAnnouncement = json.getString("announcement");
					String title = json.getString("title");
					// -------------------------------------------------
					showCalled(currentlyCalled);
					processResults(title, latestAnnouncement);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					logs("JSON EXCEPTION @ onSuccess: " + e.toString());
				}
			}
		});
		// -------------------------------------------------------------
		pilaRequest.setOnFinish(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				requestOnProgress = false;
			}
		});
		// -------------------------------------------------------------
		pilaRequest.setOnFailure(new FailureEvent() {

			@Override
			public void salo(int status, String type) {
				// TODO Auto-generated method stub
				logs("STATUS: " + status + " @ TYPE: " + type);
				/**
				 * The App should just ignore failed request since this is a
				 * repeating task.
				 */
			}
		});
		// -------------------------------------------------------------
		// Start The Request.
		pilaRequest.start();
	}

	/**
	 * Displays the current number that is being called.
	 * 
	 * @param number
	 */
	private void showCalled(String number) {
		this.lbl_current.setText("# " + number);
	}

	// flag that tells whet
	private boolean callerNotify = false;

	private void processResults(String title, String latestAnnouncement) {
		try {
			String myNumber = reference_pila.getString("floor_number");
			String floor = reference_pila.getString("floor_assignment");
			// -------------------------------------------------------------
			// Get Room Information Locally From the App
			// String room = "Not Assigned";
			// if (floor.equals(PublicConstants.getFLR_3())) {
			// room = PublicConstants.getFLOOR_THREE();
			// } else if (floor.equals(PublicConstants.getFLR_4())) {
			// room = PublicConstants.getFlOOR_FOUR();
			// }
			// -------------------------------------------------------------

			String accepted = reference_pila.getString("request_accepted");
			String called = reference_pila.getString("request_called");
			String validity = reference_pila.getString("request_validity");
			String room = reference_pila.getString("cluster_name");
			// --------------------------------------------------------------
			// Get Current Announcement
			String announcement = latestAnnouncement;
			String announcement_title = title;
			// --------------------------------------------------------------
			String evaluator = reference_pila.getString("called_by");
			String terminal = reference_pila.getString("called_on_terminal");

			//
			pila_id = reference_pila.getString("id");
			//
			String remarks = reference_pila.getString("remarks");

			if (remarks.equalsIgnoreCase("EXPIRED")) {
				// call expiration window.
				refresh.stop();
				expireDialog();
			}

			// set text
			lbl_mynumber.setText(myNumber);
			lbl_room.setText(room);
			lbl_accepted.setText(convertDate(accepted));
			lbl_called.setText(convertDate(called));
			lbl_expire.setText(convertDate(validity));
			lbl_announcement.setText(announcement);
			lbl_title.setText(announcement_title);
			//
			lbl_evaluator.setText(evaluator);
			lbl_terminal.setText(terminal);

			// display student number at the footer
			lbl_showinfo.setText("E-pass #" + myNumber + " @ " + room);

			if (called.equals("null")) {
				// if the student is not yet called
				called = "";
				validity = "";
			} else {
				// if the student was called.
				this.lbl_current.setTextColor(Color.RED);
				if (!callerNotify) {
					callerNotify = true;
					// --------------------------------------------------------------------------

					// --------------------------------------------------------------------------
					// display view entrance
					// this.viewEntrance();
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logs("JSON EXCEPTION @ processResults: " + e.toString());
		}
	}

	private String convertCallerInformation(String info) {
		if (info != null) {
			return info;
		}
		return "";

	}

	/**
	 * When the number of the student had already expired. when the okay button
	 * is clicked it will send a void reference to this students pila id. to
	 * mark the number as expired. this is to ensure that the student will know
	 * that his/her number have expired.
	 */
	private void expireDialog() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				getCurrentActivity());
		dlgAlert.setTitle("Ooops!");
		dlgAlert.setMessage("Your reference number has expired.");
		dlgAlert.setPositiveButton("Okay",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						VoidReference vr = new VoidReference(
								getCurrentActivity());
						vr.cancel(pila_id);
						Log.i("CANCEL_PILA_ID", pila_id);
						// getCurrentActivity().finish();
					}
				});
		dlgAlert.setCancelable(false);
		dlgAlert.show();
	}

	/**
	 * When the student had gone entrance pass and it is the end of his
	 * application journey. may suggest to be a whole application page rather
	 * than a dialogue
	 */
	private void yeheyDialog() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				getCurrentActivity());
		dlgAlert.setTitle("You're Done!");
		dlgAlert.setMessage("Please wait for your advising slip. Thank You!");
		dlgAlert.setPositiveButton("OK !",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						getCurrentActivity().finish();
						// getCurrentActivity().finish();
					}
				});
		dlgAlert.setCancelable(false);
		dlgAlert.show();
	}

	/**
	 * Converts date into a proper format.
	 * 
	 * @param dateString
	 * @return
	 */
	private String convertDate(String dateString) {
		SimpleDateFormat stringFormat = new SimpleDateFormat(
				"yyyy-MM-dd kk:mm:ss");
		String formated = "";
		try {
			SimpleDateFormat newFormat = new SimpleDateFormat(
					"MMM dd, hh:mm:ss a");
			Date date = stringFormat.parse(dateString);
			formated = newFormat.format(date);
			return formated;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

}
