package com.melvin.monoforms;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.linkedapplication.requests.FailureEvent;
import org.linkedapplication.requests.Request;
import org.linkedapplication.requests.RequestEvent;
import org.linkedapplication.requests.SingleResponse;

import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.jhmvin.linked.MarshallAddStudent;
import com.jhmvin.linked.ReadMonoProfile;
import com.loopj.android.http.RequestParams;
import com.melvin.android.Notify;

/**
 * Manipulates the view of Reading profile from mono forms profiling.
 * 
 * @author Jhon Melvin
 * 
 */
public class ReadMonoProfileController {

	private ReadMonoProfile activity;

	/**
	 * @return the activity
	 */
	public ReadMonoProfile getActivity() {
		return activity;
	}

	private String[] profileValues;
	private ProgressDialog requestProgress;

	public ReadMonoProfileController(ReadMonoProfile activity) {
		this.activity = activity;
		// -----------------------------------------------------------------
		// get profile values
		this.profileValues = getActivity().getIntent().getStringArrayExtra(
				"profile_values");
		if (this.profileValues == null) {
			// values was not passed
			getActivity().finish();
			Notify.toastShort(activity, "Cannot Display Information");
		}
		// -----------------------------------------------------------------
		this.readValues();
		// -----------------------------------------------------------------
		this.addEventsToButtons();
		// -----------------------------------------------------------------
		// create progress dialog
		requestProgress = Notify.createProgressDialogOver(getActivity());
	}

	/**
	 * Profile Values separated;
	 */
	private String id, lname, fname, mname, gender, floor, mobile, zip
	// , house,
	// street, brgy, city, province
			, email, ice_name, ice_address,
			ice_contact;
	
	private String studentAddress;

	private void readValues() {
		String[] values = this.profileValues;

		id = values[2];
		lname = values[3];
		fname = values[4];
		mname = values[5];
		gender = values[6];
		floor = values[7];
		mobile = values[8];
		// address values
		zip = values[9];
		studentAddress = values[10];
		// house = values[10];
		// street = values[11];
		// brgy = values[12];
		// city = values[13];
		// province = values[14];
		//
		email = values[11];
		// emergency contact
		ice_name = values[12];
		ice_address = values[13];
		ice_contact = values[14];
		//
		

		getActivity().getLblID().setText(nullOrEmpty(id));
		String fullname = "";
		fullname += nullOrEmpty(lname);
		fullname += ", " + nullOrEmpty(fname);
		fullname += " " + nullOrEmpty(mname);
		fullname = fullname.trim();
		getActivity().getLblName().setText(fullname);
		getActivity().getLblGender().setText(nullOrEmpty(gender));
		getActivity().getLblFloor().setText(nullOrEmpty(floor));
		getActivity().getLblMobile().setText(nullOrEmpty(mobile));
		// address value
		String fullAddress = "";
//		fullAddress += addressConcat(house);
//		fullAddress += addressConcat(street);
//		fullAddress += addressConcat(brgy);
//		fullAddress += addressConcat(city);
//		fullAddress += addressConcat(province);
		//----------------------------------------------
		fullAddress += addressConcat(studentAddress);
		//----------------------------------------------
		getActivity().getLblAddress().setText(fullAddress.trim());
		//
		getActivity().getLblEmail().setText(nullOrEmpty(email));
		getActivity().getLblZip().setText(nullOrEmpty(zip));
		getActivity().getLblGuardian().setText(nullOrEmpty(ice_name));
		getActivity().getLblGuardianMobile().setText(nullOrEmpty(ice_contact));
		getActivity().getLblGuardianAddress().setText(nullOrEmpty(ice_address));

	}

	/**
	 * For Address Concatination
	 * 
	 * @param str
	 * @return
	 */
	private String addressConcat(String str) {
		String address = nullOrEmpty(str).trim();

		if (!address.isEmpty()) {
			address += " ";
		}
		return address.toUpperCase(Locale.ENGLISH);
	}

	/**
	 * String protection for null reference and format.
	 * 
	 * @param str
	 * @return
	 */
	private String nullOrEmpty(String str) {
		try {
			if (str == null) {
				return "";
			}
			return str.trim().toUpperCase(Locale.ENGLISH);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Add events to the buttons of this view.
	 */
	private void addEventsToButtons() {
		this.getActivity().getBtnAdd()
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Notify.log("Button Was Clicked");
						sendRequestForProfile();

					}
				});

		// cancels the intent
		this.getActivity().getBtnCancel()
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getActivity().finish();
					}
				});
	}

	/**
	 * Send the profiling request to the server.
	 */
	private void sendRequestForProfile() {
		Notify.log("Creating request");
		Request profilingRequest = new Request();
		profilingRequest.setApiLink("linked/monoforms/profile");
		RequestParams post = new RequestParams();
		post.add("id", this.id);
		post.add("lname", this.lname);
		post.add("fname", this.fname);
		post.add("mname", this.mname);
		post.add("gender", this.gender);
		post.add("floor", this.floor);
		post.add("mobile", this.mobile);
		//
		post.add("zip", this.zip);
//		post.add("house", this.house);
//		post.add("street", this.street);
//		post.add("brgy", this.brgy);
//		post.add("city", this.city);
//		post.add("province", this.province);
		post.add("full_address", this.studentAddress);
		post.add("email", this.email);
		//
		post.add("ice_name", this.ice_name);
		post.add("ice_address", this.ice_address);
		post.add("ice_contact", this.ice_contact);
		post.add("created_by", MarshallAddStudent.getCreatedBy());

		profilingRequest.setParameters(post);

		profilingRequest.setOnStart(new RequestEvent() {
			@Override
			public void event() {
				// what to do on starting of the request
				// disable first the add button
				requestProgress.setMessage("Sending Request . . .");
				Notify.log("Progress Message Set");
				requestProgress.show();
				Notify.log("Request Started");
			}
		});

		profilingRequest.setOnSuccess(new SingleResponse() {
			@Override
			public void event(JSONObject json) {
				// what to do when success
				try {
					String success = json.getString("success"); // 1 or 0
					String description = json.getString("description");

					if (success.equals("1")) {
						Notify.showMessageThenClose(getActivity(), "Success",
								description);
					} else {
						Notify.showMessage(getActivity(), "Failed", description);
					}
				} catch (Exception e) {
					// when unable to parse the json request
					Notify.toastShort(getActivity(),
							"Server response was unreadable, Please Try Again.");
				}
			}
		});

		profilingRequest.setOnFailure(new FailureEvent() {
			@Override
			public void salo(int status, String type) {
				// when failed
				Notify.showMessage(getActivity(), "Profiling Failed",
						"The server is unreachable at the moment. [ERR: "
								+ status + " - " + type + "]");
			}
		});

		profilingRequest.setOnFinish(new RequestEvent() {
			@Override
			public void event() {
				// re enable the button
				requestProgress.dismiss();
				Notify.log("Request Finished");
			}
		});

		// send request to server
		profilingRequest.start();
	}

}
