package com.jhmvin.linked;

import org.json.JSONException;
import org.json.JSONObject;
import org.linkedapplication.requests.FailureEvent;
import org.linkedapplication.requests.GetRequest;
import org.linkedapplication.requests.Request;
import org.linkedapplication.requests.RequestEvent;
import org.linkedapplication.requests.SingleResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhmvin.actors.Student;
import com.jhmvin.commons.Commons;
import com.jhmvin.commons.PublicConstants;
import com.jhmvin.update.concurrency.IkotIkotTask;
import com.jhmvin.update.concurrency.TaskRoll;
import com.loopj.android.http.RequestParams;

public class StudentMain extends Activity {

	private void log(Object message) {
		Log.i("@StudentMain", message.toString());
	}

	private boolean isPause = false;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (!isPause) {
			waitingTask.pause();
			isPause = true;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isPause) {
			waitingTask.resume();
			isPause = false;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
		dlgAlert.setTitle("Exit Application");
		dlgAlert.setMessage("Are you sure you want to close application.");

		dlgAlert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		dlgAlert.setNegativeButton("No", null);

		dlgAlert.setCancelable(false);
		dlgAlert.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this);
		setContentView(R.layout.activity_student_main);

		View main = findViewById(R.id.pnl_student);
		Commons.startGradientBackground(main);

		init();
		events();
	}

	private TextView lbl_studentNumber, lbl_name, lbl_course, lbl_gender,
			lbl_imei;
	private ImageView img_qr;

	/**
	 * Initialization.
	 */
	private void init() {

		// get views
		lbl_studentNumber = (TextView) findViewById(R.id.lbl_studentNumber);
		lbl_name = (TextView) findViewById(R.id.lbl_name);
		lbl_course = (TextView) findViewById(R.id.lbl_course);
		lbl_gender = (TextView) findViewById(R.id.lbl_gender);
		lbl_imei = (TextView) findViewById(R.id.lbl_imei);
		img_qr = (ImageView) findViewById(R.id.img_qr);

		// set content
		lbl_studentNumber.setText(Student.instance().getStudentNumber());
		lbl_name.setText(Student.instance().getFullname());
		lbl_course.setText(Student.instance().getCourseName());
		lbl_gender.setText(Student.instance().getGender());

		String imei = Commons.deviceImei(this);
		lbl_imei.setText(imei);
		String acad_term = String.valueOf(Student.instance()
				.getCurrentAcademicTerm());

		String qr_string = "LINKED#" + imei + "#"
				+ Student.instance().getStudentNumber() + "#"
				+ Student.instance().getAccountID() + "#" + acad_term;

		Bitmap gen_img = Commons.generateQR(qr_string, this);
		img_qr.setImageBitmap(gen_img);
		Student.instance().savedQRCode = gen_img;

		//
		PARAM_CICT_ID = Student.instance().getCictID().toString();
	}

	private String PARAM_CICT_ID;

	/**
	 * Events.
	 */
	private void events() {
		/**
		 * Run waiting task.
		 */
		acceptWaitngTask();
	}

	/**
	 * When the user is added to the list it will show the queue details.
	 */
	private void onViewEntrance() {
		Intent i = new Intent(this, StudentWaiting.class);
		this.startActivity(i);
		this.finish();
	}

	/**
	 * If request is still running.
	 */
	private boolean requestOnProgess = false;

	public boolean isRequestOnProgress() {
		return this.requestOnProgess;
	}

	/**
	 * The task to do while waiting to be added to queue.
	 */

	private void onWaitingToAceppt() {
		// // Create request object
		// Request checkRequest = new Request();
		// // set API Link, only supports post method.
		// checkRequest.setApiLink("linked/check");
		// // create parameters
		// RequestParams post = new RequestParams();
		// post.add("cict_id", Student.instance().getCictID().toString());
		// // add parameters
		// checkRequest.setParameters(post);

		GetRequest checkRequest = new GetRequest();

		checkRequest.setApiLink("linked/check/" + PARAM_CICT_ID);

		// what to do on start
		checkRequest.setOnStart(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				requestOnProgess = true;
			}

		});
		// what to do after request
		checkRequest.setOnFinish(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				requestOnProgess = false;
			}
		});

		// what to do when response is receive
		checkRequest.setOnSuccess(new SingleResponse() {

			@Override
			public void event(JSONObject json) {
				// TODO Auto-generated method stub
				log("%success: " + json.toString());
				try {
					String res = json.getString("res");
					if (res.equals("1")) {
						// next frame here
						onViewEntrance();
						waitingTask.pause();
						waitingTask.stop();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					log("%json_exception");
				}
			}
		});

		// what to do if it fails.
		checkRequest.setOnFailure(new FailureEvent() {

			@Override
			public void salo(int status, String type) {
				// TODO Auto-generated method stub
				log("%failed: status >> " + status + " | type >> " + type);
			}
		});

		// submit form
		checkRequest.start();

	}

	private TaskRoll waitingTask;

	/**
	 * Repeats the task.
	 */
	private void acceptWaitngTask() {

		waitingTask = new TaskRoll();
		waitingTask.setInterval(PublicConstants.getAcceptInterval());
		waitingTask.setTask(new IkotIkotTask() {

			@Override
			public void repeatMe() {
				// TODO Auto-generated method stub
				if (isRequestOnProgress()) {
					// do nothing
					log("%request_on_progress: >> skipping . . .");
				} else {
					// start task again
					log("ACCOUNT ID: " + Student.instance().getCictID());
					onWaitingToAceppt();
				}
			}
		});

		waitingTask.startTask();
	}

}
