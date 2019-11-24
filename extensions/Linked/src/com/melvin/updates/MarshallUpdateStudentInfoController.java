package com.melvin.updates;

import org.json.JSONObject;
import org.linkedapplication.requests.FailureEvent;
import org.linkedapplication.requests.GetRequest;
import org.linkedapplication.requests.Request;
import org.linkedapplication.requests.RequestEvent;
import org.linkedapplication.requests.SingleResponse;

import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.jhmvin.linked.MarshallAddStudent;
import com.jhmvin.linked.MarshallUpdatedStudentInfo;
import com.jhmvin.linked.R;
import com.loopj.android.http.RequestParams;
import com.melvin.android.Notify;

public class MarshallUpdateStudentInfoController {

	public static String cictID;
	public static boolean isOpened = false;

	/**
	 * Controller Activity.
	 */
	private MarshallUpdatedStudentInfo activity;

	private String CLUSTER_ASSIGNMENT;
	private ProgressDialog requestProgress;

	/**
	 * Controller Constructor
	 * 
	 * @param activity
	 */
	public MarshallUpdateStudentInfoController(
			MarshallUpdatedStudentInfo activity) {
		this.activity = activity;
		this.CLUSTER_ASSIGNMENT = "";
		// --------------------------
		// marker that the update was opened
		MarshallUpdateStudentInfoController.isOpened = true;

		// Create Progress
		requestProgress = Notify.createProgressDialogOver(getActivity());
		// -------------------------------------
		fetchUpdateData();
		buttonEvents();
	}

	private void buttonEvents() {
		getActivity().getBtn_update().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestUpdate();
			}
		});
	}

	/**
	 * Send Request to server.
	 */
	private void requestUpdate() {
		String studentNumber = MarshallAddStudent.prepareEntry(getActivity()
				.getTxt_add_id());
		String lastName = MarshallAddStudent.prepareEntry(getActivity()
				.getTxt_add_last());
		String firstName = MarshallAddStudent.prepareEntry(getActivity()
				.getTxt_add_first());
		String middleName = MarshallAddStudent.prepareEntry(getActivity()
				.getTxt_add_middle());
		// -----------------------------------------------------------------
		// Apply Filters Here from MarshallAddStudent.java
		boolean filterResult = MarshallAddStudent.filterEntries(studentNumber,
				lastName, firstName, middleName, this.CLUSTER_ASSIGNMENT,
				getActivity());

		if (!filterResult) {
			return;
		}

		// -----------------------------------------------------------------

		Request updateData = new Request();
		updateData.setApiLink("linked/student/update");
		// create parameters
		RequestParams post = new RequestParams();
		post.add("cict_id", cictID);
		post.add("student_number", studentNumber);
		post.add("last_name", lastName);
		post.add("first_name", firstName);
		post.add("middle_name", middleName);
		post.add("cluster_assignment", CLUSTER_ASSIGNMENT);
		// set parameters
		updateData.setParameters(post);

		updateData.setOnStart(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				requestProgress.setMessage("Updating Student Data . . .");
				Notify.log("Progress Message Set");
				requestProgress.show();
				Notify.log("Request Started");
			}
		});

		updateData.setOnSuccess(new SingleResponse() {

			@Override
			public void event(JSONObject json) {
				// TODO Auto-generated method stub
				try {
					String success = json.getString("success");
					String message = json.getString("message");
					if (success.equals("0")) {
						// some prompt
						Notify.showMessage(getActivity(), "Request Failed",
								message);
						return;
					}
					// ---------------------------------
					// Successfully updated
					// Notify.showMessage(getActivity(), "Successfully Updated",
					// message);
					getActivity().finish();
				} catch (Exception e) {
					// when unable to parse the json request
					Notify.toastShort(getActivity(),
							"Server response was unreadable, Please Try Again.");
				}
			}
		});

		updateData.setOnFailure(new FailureEvent() {

			@Override
			public void salo(int status, String type) {
				// TODO Auto-generated method stub
				// when failed
				// when failed
				Notify.showMessage(getActivity(), "Request Failed",
						"The server is unreachable at the moment. [ERR: "
								+ status + " - " + type + "]");
			}
		});

		updateData.setOnFinish(new RequestEvent() {

			@Override
			public void event() {
				// TODO Auto-generated method stub
				requestProgress.dismiss();
				Notify.log("Request Finished");
			}
		});

		updateData.start();

	}

	/**
	 * Get the data for display before update.
	 */
	private void fetchUpdateData() {
		GetRequest fetchData = new GetRequest();
		fetchData.setApiLink("linked/student/update/fetch/" + cictID);
		fetchData.setOnStart(new RequestEvent() {
			@Override
			public void event() {
				requestProgress.setMessage("Fetching Update Data . . .");
				Notify.log("Progress Message Set");
				requestProgress.show();
				Notify.log("Request Started");
			}
		});

		fetchData.setOnSuccess(new SingleResponse() {
			@Override
			public void event(JSONObject json) {
				// what to do when success
				try {
					String success = json.getString("success");
					if (success.equals("0")) {
						// some prompt
						String message = json.getString("message");
						Notify.showMessage(getActivity(), "Request Failed",
								message);
						getActivity().finish();
						return;
					}
					// When Success = 1
					String student_number = json.getString("student_number");
					String last_name = json.getString("last_name");
					String first_name = json.getString("first_name");
					String middle_name = json.getString("middle_name");
					String cluster_assignment = json
							.getString("cluster_assignment");

					getActivity().getTxt_add_id().setText(student_number);
					getActivity().getTxt_add_last().setText(last_name);
					getActivity().getTxt_add_first().setText(first_name);
					getActivity().getTxt_add_middle().setText(middle_name);

					if (cluster_assignment.equals("no_assignment")) {
						// do nothing
					} else if (cluster_assignment.equals("3")) {
						CLUSTER_ASSIGNMENT = "3";
						getActivity().getRadio_cluster_1().setChecked(true);
					} else if (cluster_assignment.equals("4")) {
						CLUSTER_ASSIGNMENT = "4";
						getActivity().getRadio_cluster_2().setChecked(true);
					} else {
						// do nothing
					}

				} catch (Exception e) {
					// when unable to parse the json request
					Notify.toastShort(getActivity(),
							"Server response was unreadable, Please Try Again.");
					getActivity().finish();
				}
			}
		});

		fetchData.setOnFailure(new FailureEvent() {
			@Override
			public void salo(int status, String type) {
				// when failed
				// when failed
				Notify.showMessage(getActivity(), "Request Failed",
						"The server is unreachable at the moment. [ERR: "
								+ status + " - " + type + "]");
				getActivity().finish();
			}
		});

		fetchData.setOnFinish(new RequestEvent() {
			@Override
			public void event() {
				// re enable the button
				requestProgress.dismiss();
				Notify.log("Request Finished");
			}
		});
		//
		fetchData.start();
	}

	/**
	 * Activity Getter.
	 * 
	 * @return
	 */
	public MarshallUpdatedStudentInfo getActivity() {
		return activity;
	}

	/**
	 * When the radio button was clicked.
	 * 
	 * @param view
	 */
	public void onRadioButtonClick(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.radio_cluster_1:
			if (checked)
				this.CLUSTER_ASSIGNMENT = "3";
			break;
		case R.id.radio_cluster_2:
			if (checked)
				this.CLUSTER_ASSIGNMENT = "4";
			break;
		}
	}

}
