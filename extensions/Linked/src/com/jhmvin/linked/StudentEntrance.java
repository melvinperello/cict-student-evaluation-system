package com.jhmvin.linked;

import com.jhmvin.actors.Student;
import com.jhmvin.commons.Commons;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentEntrance extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Commons.setActivityFullScreen(this);
		setContentView(R.layout.activity_student_entrance);

		init();
	}

	private TextView lbl_studentNumber, lbl_name, lbl_course, lbl_gender,
			lbl_imei, lbl_showinfo;
	private ImageView img_qr;

	private void init() {
		// get views
		lbl_studentNumber = (TextView) findViewById(R.id.lbl_studentNumber);
		lbl_name = (TextView) findViewById(R.id.lbl_name);
		lbl_course = (TextView) findViewById(R.id.lbl_course);
		lbl_gender = (TextView) findViewById(R.id.lbl_gender);
		lbl_imei = (TextView) findViewById(R.id.lbl_imei);
		lbl_showinfo = (TextView) findViewById(R.id.lbl_showinfo);
		img_qr = (ImageView) findViewById(R.id.img_qr);

		// set content
		lbl_studentNumber.setText(Student.instance().getStudentNumber());
		lbl_name.setText(Student.instance().getFullname());
		lbl_course.setText(Student.instance().getCourseName());
		lbl_gender.setText(Student.instance().getGender());
		lbl_imei.setText(Commons.deviceImei(this));
		img_qr.setImageBitmap(Student.instance().savedQRCode);

		lbl_showinfo.setText(this.getIntent().getExtras().getString("message"));
	}

}
