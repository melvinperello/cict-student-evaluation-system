package com.jhmvin.linked;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jhmvin.commons.Commons;
import com.melvin.monoforms.ReadMonoProfileController;

public class ReadMonoProfile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this);
		setContentView(R.layout.activity_read_mono_profile);

		View main = findViewById(R.id.pnl_read_profile);
		Commons.startGradientBackground(main);

		// run init
		init();
	}

	/**
	 * Buttons.
	 */
	private Button btnAdd, btnCancel;
	/**
	 * TextViews.
	 */
	private TextView lblID, lblName, lblMobile, lblEmail, lblGender, lblFloor,
			lblAddress, lblZip, lblGuardian, lblGuardianMobile,
			lblGuardianAddress;

	/**
	 * Initialize Controls.
	 */
	private void init() {
		this.btnAdd = (Button) findViewById(R.id.btn_add);
		this.btnCancel = (Button) findViewById(R.id.btn_cancel);
		this.lblID = (TextView) findViewById(R.id.lbl_id);
		this.lblName = (TextView) findViewById(R.id.lbl_name);
		this.lblMobile = (TextView) findViewById(R.id.lbl_mobile);
		this.lblEmail = (TextView) findViewById(R.id.lbl_email);
		this.lblGender = (TextView) findViewById(R.id.lbl_gender);
		this.lblFloor = (TextView) findViewById(R.id.lbl_floor);
		this.lblAddress = (TextView) findViewById(R.id.lbl_address);
		this.lblZip = (TextView) findViewById(R.id.lbl_zip);
		this.lblGuardian = (TextView) findViewById(R.id.lbl_guardian);
		this.lblGuardianMobile = (TextView) findViewById(R.id.lbl_guardian_contact);
		this.lblGuardianAddress = (TextView) findViewById(R.id.lbl_guardian_address);


		// Pass the activity to the controller.
		ReadMonoProfileController controller = new ReadMonoProfileController(
				this);
	}

	/**
	 * @return the btnAdd
	 */
	public Button getBtnAdd() {
		return btnAdd;
	}

	/**
	 * @return the btnCancel
	 */
	public Button getBtnCancel() {
		return btnCancel;
	}

	/**
	 * @return the lblID
	 */
	public TextView getLblID() {
		return lblID;
	}

	/**
	 * @return the lblName
	 */
	public TextView getLblName() {
		return lblName;
	}

	/**
	 * @return the lblMobile
	 */
	public TextView getLblMobile() {
		return lblMobile;
	}

	/**
	 * @return the lblEmail
	 */
	public TextView getLblEmail() {
		return lblEmail;
	}

	/**
	 * @return the lblGender
	 */
	public TextView getLblGender() {
		return lblGender;
	}

	/**
	 * @return the lblFloor
	 */
	public TextView getLblFloor() {
		return lblFloor;
	}

	/**
	 * @return the lblAddress
	 */
	public TextView getLblAddress() {
		return lblAddress;
	}

	/**
	 * @return the lblZip
	 */
	public TextView getLblZip() {
		return lblZip;
	}

	/**
	 * @return the lblGuardian
	 */
	public TextView getLblGuardian() {
		return lblGuardian;
	}

	/**
	 * @return the lblGuardianMobile
	 */
	public TextView getLblGuardianMobile() {
		return lblGuardianMobile;
	}

	/**
	 * @return the lblGuardianAddress
	 */
	public TextView getLblGuardianAddress() {
		return lblGuardianAddress;
	}

}
