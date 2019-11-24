package org.linkedapplication.telemetry;

import com.jhmvin.actors.Student;
import com.loopj.android.http.RequestParams;

public class TelemetryManager {

	public void send() {
		DeviceKeeper dk = DeviceKeeper.values();
		Student st = Student.instance();
		RequestParams post = new RequestParams();
		// error info
		String class_name = "com.jhmvin.Marshall.class";
		String method_name = "getStudent()";
		String line = "100 - 200";
		String classification = "E";
		String exception = "NullPointerException";
		String description = "error";
		// owner info
		post.add("student_number", st.getStudentNumber());
		post.add("name", st.getFullname());
		// phone info
		post.add("version", dk.getAndroidVersion());
		post.add("build", dk.getBuildID());
		post.add("model", dk.getPhoneModel());
		post.add("board", dk.getPhoneBoard());
		post.add("brand", dk.getPhoneBrand());
		post.add("hardware", dk.getHardware());
		post.add("manufacturer", dk.getManufacturer());
		post.add("serial", dk.getSerial());
		
	}
}
