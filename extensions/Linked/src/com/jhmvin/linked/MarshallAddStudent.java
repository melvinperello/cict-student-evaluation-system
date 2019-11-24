package com.jhmvin.linked;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jhmvin.actors.Student;
import com.jhmvin.commons.Commons;
import com.jhmvin.http.HttpRequest;
import com.jhmvin.http.JsonResponse;
import com.loopj.android.http.RequestParams;
import com.melvin.android.Notify;

import cz.msebera.android.httpclient.Header;

public class MarshallAddStudent extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this);
		setContentView(R.layout.activity_marshall_add_student);
		this.CLUSTER_ASSIGNMENT = "";
		init();
		eventHandling();
	}

	private EditText txt_add_id, txt_add_last, txt_add_first, txt_add_middle,
			txt_add_course, txt_add_year_section, txt_add_admission;

	private Button txt_add_button;

	private void init() {
		txt_add_id = (EditText) findViewById(R.id.txt_add_id);
		txt_add_last = (EditText) findViewById(R.id.txt_add_last);
		txt_add_first = (EditText) findViewById(R.id.txt_add_first);
		txt_add_middle = (EditText) findViewById(R.id.txt_add_middle);
		txt_add_course = (EditText) findViewById(R.id.txt_add_course);
		txt_add_year_section = (EditText) findViewById(R.id.txt_add_year_section);
		txt_add_admission = (EditText) findViewById(R.id.txt_add_admission);

		txt_add_button = (Button) findViewById(R.id.txt_add_button);

		txt_add_course.setVisibility(View.GONE);
		txt_add_year_section.setVisibility(View.GONE);
		txt_add_admission.setVisibility(View.GONE);
	}

	private String CLUSTER_ASSIGNMENT;

	/**
	 * When the cluster radio buttons were clicked.
	 * 
	 * @param v
	 */
	public void onRadioButtonClicked(View view) {
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

	private void eventHandling() {
		txt_add_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addStudent();
			}
		});
	}

	/**
	 * Trims and formats text.
	 * 
	 * @param edt
	 *            the edit text source.
	 * @return formatted string.
	 */
	public static String prepareEntry(EditText edt) {
		return edt.getText().toString().trim().toUpperCase(Locale.ENGLISH);
	}

	/**
	 * Invokes the create new student request to the database after successfully
	 * filtering entries.
	 */
	private void addStudent() {

		/**
		 * Will be transferred to the system.
		 */

		// String course = prepareEntry(txt_add_course);
		// boolean courseIsValid = false;
		// for (String validCourse : PublicConstants.VALID_COURSES) {
		// if (course.equals(validCourse)) {
		// courseIsValid = true;
		// break;
		// }
		// }
		// if (!courseIsValid) {
		// messageMe("You have entered an unrecognized or invalid Course.");
		// return;
		// }
		//
		// String yearSection = prepareEntry(txt_add_year_section);
		// String[] literals = yearSection.split("/");
		// if (literals.length != 3) {
		// messageMe("You have entered and invalid Year/Section/Group.");
		// return;
		// }
		//
		// if (literals.length == 3) { // START IF 01 LITERALS
		// /**
		// * Check for year level
		// */
		// String year = literals[0].trim();
		// boolean is_valid_year = false;
		// for (String validYear : PublicConstants.ACCEPTED_YEAR_LEVELS) {
		// if (year.equals(validYear)) {
		// is_valid_year = true;
		// break;
		// }
		// }
		//
		// if (!is_valid_year) {
		// messageMe("You have entered and invalid Year Level.");
		// return;
		// }
		//
		// /**
		// * Section
		// */
		// String section = literals[1].trim();
		//
		// if ((!StringUtils.isAlpha(section)) || section.length() != 1) {
		// messageMe("You have entered and invalid Section.");
		// return;
		// }
		//
		// String group = literals[2].trim();
		// boolean is_valid_group = false;
		// for (String validGroup : PublicConstants.ACCEPTED_GROUP) {
		// if (group.equals(validGroup)) {
		// is_valid_group = true;
		// break;
		// }
		// }
		//
		// if (!is_valid_group) {
		// messageMe("You have entered and invalid Section Group.");
		// return;
		// }
		//
		// String admissionYear = prepareEntry(txt_add_admission);
		// if (admissionYear.isEmpty()) {
		// messageMe("Please enter the Admission Year.");
		// return;
		// }

		String studentNumber = prepareEntry(txt_add_id);
		String lastName = prepareEntry(txt_add_last);
		String firstName = prepareEntry(txt_add_first);
		String middleName = prepareEntry(txt_add_middle);

		boolean filterResult = filterEntries(studentNumber, lastName,
				firstName, middleName, this.CLUSTER_ASSIGNMENT, this);

		if (!filterResult) {
			return;
		}

		/**
		 * End of filter start request to database.
		 */
		RequestParams post = new RequestParams();
		post.add("student_number", studentNumber);
		post.add("last_name", lastName);
		post.add("first_name", firstName);
		post.add("middle_name", middleName);
		// post.add("curriculum_id", "1");
		// post.add("year_level", year);
		// post.add("section", section);
		// post.add("group", group);
		// post.add("admission_year", admissionYear);
		post.add("created_by", getCreatedBy());
		post.add("cluster_assignment", CLUSTER_ASSIGNMENT);
		requestToAdd(post);
		/**
		 * Success.
		 */

		// }// == 3 end literals // END IF 01 LITERALS

	}

	/**
	 * Made static so the update info can share with this filter
	 * 
	 * @param studentNumber
	 * @param lastName
	 * @param firstName
	 * @param middleName
	 * @param cluster
	 * @param activity
	 * @return
	 */
	public static boolean filterEntries(String studentNumber, String lastName,
			String firstName, String middleName, String cluster,
			Activity activity) {

		if (!StringUtils.isAlphanumeric(studentNumber)) {
			messageMe("You have entered an invalid Student Number.", activity);
			return false;
		}

		if ((!StringUtils.isAlphaSpace(lastName)) || (lastName.isEmpty())) {
			messageMe("You have entered an invalid Last Name details."
					+ " only letters and spaces are allowed", activity);
			return false;
		}

		if ((!StringUtils.isAlphaSpace(firstName)) || (firstName.isEmpty())) {
			messageMe("You have entered an invalid First Name details."
					+ " only letters and spaces are allowed", activity);
			return false;
		}

		// allows empty middle name
		if (!middleName.isEmpty()) {
			if ((!StringUtils.isAlphaSpace(middleName))
					|| (middleName.isEmpty())) {
				messageMe("You have entered an invalid Middle Name details."
						+ " only letters and spaces are allowed", activity);
				return false;
			}
		}

		// -----------------------------------------------------------
		if (cluster.isEmpty() || cluster.trim().equals("")) {

			messageMe("Please select Student's Cluster Assignment", activity);
			return false;
		}

		return true;
	}

	/**
	 * Returns the student information [marshal] that created the student.
	 * 
	 * @return
	 */
	public static String getCreatedBy() {
		String created_by = "LINKED_ACC_" + Student.instance().getAccountID()
				+ "_USER_" + Student.instance().getStudentNumber() + "_"
				+ Student.instance().getLastName();
		return created_by;
	}

	/**
	 * Request to server.
	 */
	ProgressDialog progressDialog;

	public void requestToAdd(RequestParams param) {
		HttpRequest.post("linked/create_student", param, new JsonResponse() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setCancelable(false);
				progressDialog.setIndeterminate(true);
				progressDialog.setTitle("Adding Student");
				progressDialog.setMessage("Please Wait . . .");
				progressDialog.show();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				progressDialog.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					try {
						String res = response.getString("result");
						messageMeWithTitle("Already Exist",
								"The student with the same Student Number is already existing.");
						if (res.equals("exist")) {

						} else if (res.equals("ok")) {
							successMessage();
						} else {
							// error
							messageMeWithTitle("Something Went Wrong.",
									"Please Try Again, Thank You!");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						log("JSON EXCEPTION");
					}

				}
			}

			@Override
			public void onRequestFailed(int statusCode) {
				// TODO Auto-generated method stub
				super.onRequestFailed(statusCode);
				messageMeWithTitle("Error",
						"Sorry we cannot process your request for the moment.");
			}

		});
	}

	/**
	 * Called when successfully added.
	 */
	private void successMessage() {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
		dlgAlert.setTitle("Added Successfully");
		dlgAlert.setMessage("Student was added successfully to the system, student is now ready for profiling.");
		dlgAlert.setPositiveButton("Back",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});

		dlgAlert.setCancelable(true);
		dlgAlert.show();
	}

	private void log(Object message) {
		Log.i("@MarshallAddStudent", message.toString());
	}

	private MarshallAddStudent getActivity() {
		return this;
	}

	/**
	 * Message sender.
	 * 
	 * @param message
	 */
	private static void messageMe(String message, Activity activity) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
		dlgAlert.setTitle("Incorrect Field");
		dlgAlert.setMessage(message);
		dlgAlert.setPositiveButton("Ok", null);
		dlgAlert.setCancelable(true);
		dlgAlert.show();
	}

	private void messageMeWithTitle(String title, String message) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
		dlgAlert.setTitle(title);
		dlgAlert.setMessage(message);
		dlgAlert.setPositiveButton("Ok", null);
		dlgAlert.setCancelable(true);
		dlgAlert.show();
	}

}
