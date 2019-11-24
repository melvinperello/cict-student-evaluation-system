package com.jhmvin.linked;

import org.linkedapplication.EntrancePass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhmvin.actors.Marshall;
import com.jhmvin.actors.Student;
import com.jhmvin.commons.Commons;
import com.jhmvin.linked.marshall.FloatingActionButton;
import com.melvin.updates.MarshallUpdateStudentInfoController;

public class MarshallMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this);
		setContentView(R.layout.activity_marshall_main);

		View main = findViewById(R.id.pnl_marshall_main);
		Commons.startGradientBackground(main);

		// --------------------------------------------
		// Initialize Components
		initialize();
		// --------------------------------------------
		// Default View
		this.pnl_landing.setVisibility(View.VISIBLE);
		this.fabButton.setVisibility(View.VISIBLE);
		this.pnl_show.setVisibility(View.GONE);
		// --------------------------------------------
		eventHandling();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/**
		 * After updating refresh values.
		 */
		if (MarshallUpdateStudentInfoController.isOpened) {
			if (!txt_studentNumber.getText().toString().isEmpty()) {
				this.onSearch();
			}
			MarshallUpdateStudentInfoController.isOpened = false;
		}

	}

	private Marshall marshall;

	/**
	 * 
	 * @return
	 */
	public MarshallMain getActivity() {
		return (MarshallMain) this;
	}

	private ImageView img_search, img_scan, img_logout;
	private EditText txt_studentNumber;
	/**
	 * Dear variables, Breaking the rules, sorry for making you visible for
	 * everyone. it's for the best LOLS
	 * 
	 */
	public View pnl_landing, pnl_show;

	/**
	 * Mo ichido, Gomen ne!, hontoni sumimasen deshita >_<
	 */
	public TextView lbl_studentNumber, lbl_name, lbl_course, lbl_gender,
			lbl_imei, lbl_listing;

	/**
	 * mazui, kore wa arienai !, kore wa hontoni yusurenai dayo !
	 */
	public Button btn_showCancel, btn_showPositive, btn_update;
	public FloatingActionButton fabButton;

	private TextView lbl_showuser;

	public ImageView img_avatar;

	private void initialize() {
		lbl_showuser = (TextView) findViewById(R.id.lbl_showinfo);
		// visible components
		img_scan = (ImageView) findViewById(R.id.img_scan);
		img_search = (ImageView) findViewById(R.id.img_search);
		img_logout = (ImageView) findViewById(R.id.img_logout);
		txt_studentNumber = (EditText) findViewById(R.id.txt_studentNumber);
		// panels
		pnl_landing = findViewById(R.id.pnl_landing);
		pnl_show = findViewById(R.id.pnl_show);
		// show
		lbl_studentNumber = (TextView) findViewById(R.id.lbl_studentNumber);
		lbl_name = (TextView) findViewById(R.id.lbl_name);
		lbl_course = (TextView) findViewById(R.id.lbl_course);
		lbl_gender = (TextView) findViewById(R.id.lbl_gender);
		lbl_imei = (TextView) findViewById(R.id.lbl_imei);
		lbl_listing = (TextView) findViewById(R.id.lbl_listing);

		// show buttons
		btn_showCancel = (Button) findViewById(R.id.btn_cancel);
		btn_showPositive = (Button) findViewById(R.id.btn_add);
		btn_update = (Button) findViewById(R.id.btn_update);

		fabButton = new FloatingActionButton.Builder(this)
				.withDrawable(getResources().getDrawable(R.drawable.plus64))
				.withButtonColor(Color.rgb(219, 60, 50))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 15, 65).create();

		// ------------------------------
		img_avatar = (ImageView) findViewById(R.id.img_avatar);

		// display user
		String aff = Student.instance().getAffiliates() + " | ";
		if (aff.equalsIgnoreCase("NONE")) {
			aff = "";
		}
		String user = aff + Student.instance().getFirstName();
		lbl_showuser.setText(user);
	}

	private void eventHandling() {
		img_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onLogout();
			}
		});
		img_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onScan();
			}
		});
		img_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSearch();
			}
		});

		btn_showCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onShowCancel();
			}
		});
		btn_showPositive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button b = (Button) v;
				if (b.getText().toString().equalsIgnoreCase("ADD TO QUE")) {
					onAddStudent();
				} else if (b.getText().toString()
						.equalsIgnoreCase("ENTRANCE PASS")) {

					// Entrance Request
					EntrancePass epass = new EntrancePass(getActivity());
					String number = lbl_studentNumber.getText().toString();
					String acad_term = Student.instance()
							.getCurrentAcademicTerm().toString();
					epass.entrance_request(number, acad_term);
					// back to main
					onShowCancel();
					//

				} else {
					onShowCancel();
				}

			}
		});

		fabButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onFab();
			}
		});
	}

	// -----------------------------------------------------------
	private void onFab() {
		Intent add = new Intent(this, MarshallAddStudent.class);
		this.startActivity(add);
	}

	/**
	 * if there is a successful search
	 */
	private void onAddStudent() {
		if (this.marshall != null) {
			marshall.addStudent();
		} else {
			onShowCancel();
		}
	}

	// -----------------------------------------------------------

	private void onShowCancel() {
		this.marshall = null;
		this.txt_studentNumber.setText("");
		this.txt_studentNumber.requestFocus();
		this.pnl_landing.setVisibility(View.VISIBLE);
		this.fabButton.setVisibility(View.VISIBLE);
		this.pnl_show.setVisibility(View.GONE);
	}

	private void onLogout() {
		/**
		 * Log out user
		 */
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
		dlgAlert.setTitle("Please Confirm");
		dlgAlert.setMessage("Are you sure you want to logout your account ?");
		dlgAlert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						marshall = new Marshall(getActivity());
						marshall.logout();
					}
				});
		dlgAlert.setNegativeButton("No", null);
		dlgAlert.setCancelable(true);
		dlgAlert.show();

	}

	// -------------------------------------------------------------------
	/**
	 * Searching students
	 */

	private void onSearch() {

		String studentNumber = txt_studentNumber.getText().toString().trim();
		if (studentNumber.isEmpty()) {
			mainToaster("Kindly enter a student number to search.");
			return;
		}
		this.searchInterface(studentNumber, "NONE");
	}

	private void onSearch(String studentNumber, String imei) {
		searchInterface(studentNumber, imei);
	}

	/**
	 * SEARCH HERE
	 * 
	 * @param studentNumber
	 * @param imei
	 */
	private void searchInterface(String studentNumber, String imei) {
		/**
		 * create new instance of marshall to refresh values
		 */
		marshall = new Marshall(this);
		marshall.student_number = studentNumber;
		marshall.search();
	}

	// ------------------------------------------------------------------

	/**
	 * Scannig Qr Code
	 */

	private final int SCAN_REQUEST = 918;
	// recieved qr
	private String RES_QR_STRING;

	private void onScan() {

		try {
			Intent scanner = new Intent(getApplicationContext(),
					CodeScanner.class);
			startActivityForResult(scanner, SCAN_REQUEST);
		} catch (Exception e) {
			mainToaster(e.getMessage());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SCAN_REQUEST) {
			if (resultCode == RESULT_OK) {
				RES_QR_STRING = data.getExtras().get("qr_string").toString();
				showResults(RES_QR_STRING);
			}
		}
	}

	/**
	 * For Evaluation, the results should be. FOR_EVALUATION DEVICE_IMEI
	 * 2014113844 ACCOUNT_ID ACAD_TERM
	 */
	public boolean searchScan = false;

	/**
	 * Scanner Callback. everytime the QR scanner was success this function will
	 * be called.
	 * 
	 * @param res
	 */
	private void showResults(String res) {
		/**
		 * Check code results it must have 4 divisions by #
		 */
		String[] res_parts = res.split("#");
		if (res_parts.length == 5) {
			// code is valid
			try {
				// what type of request
				String type = res_parts[0];
				// scanned phone's imei
				String imei = res_parts[1];
				// the student number of the student
				String studentNumber = res_parts[2];

				// unused details in qr code.
				String accountID = res_parts[3];
				String acadTerm = res_parts[4];
				//
				if (type.equals("LINKED")) {
					// mainToaster("Linked");
					this.txt_studentNumber.setText(studentNumber);
					this.lbl_imei.setText(imei);
					this.onSearch(studentNumber, imei);
					//
					searchScan = true;
				} else {
					mainToaster("Unrecognized QR Code - 1");
				}
			} catch (NullPointerException e) {
				// just to make sure it will be catched
				mainToaster("Unrecognized QR Code");
			}
		} else {
			// --------------------------------------------------------------
			try {
				String type = res_parts[0];
				String head = res_parts[1];
				/**
				 * CHECKING FOR MONOFORMS
				 */
				if (type.equalsIgnoreCase("MF") && head != null) {
					// this is a monoform QR
					this.executeMonoForm(res_parts);
				} else {
					mainToaster("Unrecognized QR Code - 2");
				}
			} catch (Exception e) {
				// code is invalid
				mainToaster("Unreadable QR Code - 3");
			}
			// --------------------------------------------------------------

		}
	}

	/**
	 * Mono Forms Main method.
	 * 
	 * @param values
	 */
	private void executeMonoForm(String[] values) {
		int MF_HEAD = 0;
		int MF_TYPE = 1;
		/**
		 * Values from 2 to int Max are the passed parameters
		 */
		/**
		 * Form TYPES
		 */
		String TYPE_NUMBER = "N";
		String TYPE_PROFILE = "P";

		String HEADER = values[MF_HEAD];
		String TYPE = values[MF_TYPE];

		if (TYPE.equalsIgnoreCase(TYPE_NUMBER)) {
			// this is a numbering request.
			// 3 fields must be present including header and type
			if (values.length == 3) {
				String studentNumber = values[2];
				this.onSearch(studentNumber, "NONE");
			} else {
				mainToaster("Invalid Numbering Form");
			}
		} else if (TYPE.equalsIgnoreCase(TYPE_PROFILE)) {
			// 15 fields must be present including header and type
			if (values.length == 15) {
				readProfileForm(values);
			} else {
				mainToaster("Invalid Profiling Form");
			}
		}

	}

	/**
	 * Reading a profiling form and launching a display intent.
	 * 
	 * @param values
	 */
	private void readProfileForm(String[] values) {
		// always start from 2
		// total of 443 Character Maxed including delimeter
		// personal values
		Intent i = new Intent(this, ReadMonoProfile.class);
		i.putExtra("profile_values", values);
		this.startActivity(i);
	}

	// -------------------------------------------------------------------
	/**
	 * An easy way to deliver message, in this activity.
	 * 
	 * @param message
	 *            your message
	 */
	private void mainToaster(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

}
