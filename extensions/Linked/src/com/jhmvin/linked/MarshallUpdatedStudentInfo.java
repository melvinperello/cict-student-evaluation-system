package com.jhmvin.linked;

import com.jhmvin.commons.Commons;
import com.melvin.updates.MarshallUpdateStudentInfoController;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MarshallUpdatedStudentInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this);
		setContentView(R.layout.activity_marshall_updated_student_info);

		View main = findViewById(R.id.pnl_update_student);
		Commons.startGradientBackground(main);

		// ---------------------------------------------
		this.init();
	}

	private EditText txt_add_id, txt_add_last, txt_add_first, txt_add_middle;

	private Button btn_update;

	private RadioButton radio_cluster_1;
	private RadioButton radio_cluster_2;

	private MarshallUpdateStudentInfoController controller;

	private void init() {
		txt_add_id = (EditText) findViewById(R.id.txt_add_id);
		txt_add_last = (EditText) findViewById(R.id.txt_add_last);
		txt_add_first = (EditText) findViewById(R.id.txt_add_first);
		txt_add_middle = (EditText) findViewById(R.id.txt_add_middle);
		//
		radio_cluster_1 = (RadioButton) findViewById(R.id.radio_cluster_1);
		radio_cluster_2 = (RadioButton) findViewById(R.id.radio_cluster_2);
		//
		btn_update = (Button) findViewById(R.id.txt_add_button);
		// ---------------------------------------------
		// Pass Logic to controller
		this.controller = new MarshallUpdateStudentInfoController(getActivity());
	}

	/**
	 * Get the activity of this layout.
	 * 
	 * @return
	 */
	public MarshallUpdatedStudentInfo getActivity() {
		return this;
	}

	/**
	 * @return the txt_add_id
	 */
	public EditText getTxt_add_id() {
		return txt_add_id;
	}

	/**
	 * @return the txt_add_last
	 */
	public EditText getTxt_add_last() {
		return txt_add_last;
	}

	/**
	 * @return the txt_add_first
	 */
	public EditText getTxt_add_first() {
		return txt_add_first;
	}

	/**
	 * @return the txt_add_middle
	 */
	public EditText getTxt_add_middle() {
		return txt_add_middle;
	}

	/**
	 * @return the btn_update
	 */
	public Button getBtn_update() {
		return btn_update;
	}

	/**
	 * @return the radio_cluster_1
	 */
	public RadioButton getRadio_cluster_1() {
		return radio_cluster_1;
	}

	/**
	 * @return the radio_cluster_2
	 */
	public RadioButton getRadio_cluster_2() {
		return radio_cluster_2;
	}

	/**
	 * When member of the radio button group was clicked.
	 * 
	 * @param view
	 */
	public void onRadioButtonClicked(View view) {
		this.controller.onRadioButtonClick(view);
	}

}
