package com.jhmvin.actors;

import java.util.Objects;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * 
 * @author Jhon Melvin
 * 
 */
public class Student {
	private Integer accountID;
	private String username;
	private String accessLevel;
	private String affiliates;
	private Integer cictID;
	private String studentNumber;
	private String lastName;
	private String firstName;
	private String middleName;
	private String gender;
	private Integer year;
	private String section;
	private Integer group;
	private Integer hasProfile;
	private String enrollmentType;
	private String admissionYear;

	// others
	private Integer sessionID;

	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		try {
			this.accountID = Integer.valueOf(accountID);
		} catch (NumberFormatException nfe) {
			Log.i("@student", "$setAccountID : Null");
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getAffiliates() {
		return affiliates;
	}

	public void setAffiliates(String affiliates) {
		if (affiliates.equalsIgnoreCase("NONE")) {
			this.affiliates = "";
		} else {
			this.affiliates = affiliates;
		}

	}

	public Integer getCictID() {
		return cictID;
	}

	public void setCictID(String cictID) {
		try {
			this.cictID = Integer.valueOf(cictID);
		} catch (NumberFormatException nfe) {
			Log.i("@student", "$setCictID : Null");
		}
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		if (middleName.equalsIgnoreCase("null")) {
			this.middleName = "";
			return;
		}
		this.middleName = middleName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		if (gender.equalsIgnoreCase("null")) {
			this.gender = "NOT SET";
			return;
		}
		this.gender = gender;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		if (section.equalsIgnoreCase("null")) {
			this.section = "";
			return;
		}

		this.section = section;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(String year) {
		try {
			this.year = Integer.valueOf(year);
		} catch (NumberFormatException nfe) {
			this.year = 0;
			Log.i("@student", "$setYear : Null");
		}
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(String group) {
		try {
			this.group = Integer.valueOf(group);
		} catch (NumberFormatException nfe) {
			this.group = 0;
			Log.i("@student", "$setGroup : Null");
		}

	}

	public Integer hasProfile() {
		return hasProfile;
	}

	public void setHasProfile(String hasProfile) {
		try {
			this.hasProfile = Integer.valueOf(hasProfile);
		} catch (NumberFormatException nfe) {
			Log.i("@student", "$setHasProfile : Null");
		}

	}

	public String getEnrollmentType() {
		return enrollmentType;
	}

	public void setEnrollmentType(String enrollmentType) {
		this.enrollmentType = enrollmentType;
	}

	public String getAdmissionYear() {
		return admissionYear;
	}

	public void setAdmissionYear(String admissionYear) {

		this.admissionYear = admissionYear;
	}

	// -------------------------------------------------------
	private static Student STUDENT_INSTANCE;

	private Student() {
		//
	}

	public static Student instance() {
		if (STUDENT_INSTANCE == null) {
			STUDENT_INSTANCE = new Student();
		}
		return STUDENT_INSTANCE;
	}

	private boolean isNotNull(Object... objs) {
		boolean valid = true;
		for (int x = 0; x < objs.length; x++) {
			try {
				Log.i("@student " + String.valueOf(x),
						"$recieved : " + objs[x].toString());
			} catch (NullPointerException ne) {
				Log.i("@student", "$null_pointer");
			}
			//
			if (objs[x] == null) {
				valid = false;
				break;
			}
		}
		return valid;
	}

	/**
	 * Added attributes.
	 * 
	 * @Date: 08/09/2017
	 * @author JHMVIN
	 */
	private String courseName;
	private String courseCode;
	private Integer currentAcademicTerm;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	// -------------------------------------------
	public Integer getCurrentAcademicTerm() {
		return currentAcademicTerm;
	}

	public void setCurrentAcademicTerm(String currentAcademicTerm) {
		try {
			this.currentAcademicTerm = Integer.parseInt(currentAcademicTerm);
			Log.i("RECIEVED TERM", currentAcademicTerm);
		} catch (NumberFormatException nfe) {

		}

	}

	public boolean check() {
		return isNotNull(accountID, username, accessLevel, affiliates, cictID,
				studentNumber, lastName, firstName, middleName, gender, year,
				section, group, hasProfile, enrollmentType, admissionYear,
				courseName, courseCode, currentAcademicTerm);
	}

	public Integer getSessionID() {
		return sessionID;
	}

	public void setSessionID(Integer sessionID) {
		this.sessionID = sessionID;
	}

	// useful methods
	public String getFullname() {
		try {
			return this.firstName + " " + this.middleName + " " + this.lastName;
		} catch (Exception e) {
			return "";
		}

	}

	public String getFullSection() {
		try {
			return this.courseCode + " " + this.getYear().toString()
					+ this.getSection() + "-G" + this.getGroup().toString();
		} catch (Exception e) {
			return "";
		}
	}

	//
	public Bitmap savedQRCode;

}
