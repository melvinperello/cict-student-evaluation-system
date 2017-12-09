// SQL_db: cictems
// SQL_table: faculty_data_history
// Mono Models
// Monosync Framewrok v9.08.16
// Generated using LazyMono
// This code is computer generated, do not modify
// Author: Jhon Melvin Nieto Perello
// Contact: jhmvinperello@gmail.com
//
// The Framework uses Hibernate as its ORM
// For more information about Hibernate visit hibernate.org

package app.lazy.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "faculty_data_history", catalog = "cictems")
public class FacultyDataHistoryMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.Integer faculty_id;
private java.lang.String bulsu_id;
private java.lang.String last_name;
private java.lang.String first_name;
private java.lang.String middle_name;
private java.lang.String gender;
private java.lang.String contact_number;
private java.lang.String rank;
private java.lang.String department;
private java.lang.Integer updated_by;
private java.util.Date updated_date;
private java.lang.Integer active;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "id", nullable = false, length = 10)
public java.lang.Integer getId() {
	return this.id;
}

public void setId(java.lang.Integer fieldId) {
	this.id = fieldId;
}

@Column(name = "faculty_id", nullable = true, length = 10)
public java.lang.Integer getFaculty_id() {
	return this.faculty_id;
}

public void setFaculty_id(java.lang.Integer fieldFacultyId) {
	this.faculty_id = fieldFacultyId;
}

@Column(name = "bulsu_id", nullable = true, length = 50)
public java.lang.String getBulsu_id() {
	return this.bulsu_id;
}

public void setBulsu_id(java.lang.String fieldBulsuId) {
	this.bulsu_id = fieldBulsuId;
}

@Column(name = "last_name", nullable = true, length = 50)
public java.lang.String getLast_name() {
	return this.last_name;
}

public void setLast_name(java.lang.String fieldLastName) {
	this.last_name = fieldLastName;
}

@Column(name = "first_name", nullable = true, length = 50)
public java.lang.String getFirst_name() {
	return this.first_name;
}

public void setFirst_name(java.lang.String fieldFirstName) {
	this.first_name = fieldFirstName;
}

@Column(name = "middle_name", nullable = true, length = 50)
public java.lang.String getMiddle_name() {
	return this.middle_name;
}

public void setMiddle_name(java.lang.String fieldMiddleName) {
	this.middle_name = fieldMiddleName;
}

@Column(name = "gender", nullable = true, length = 50)
public java.lang.String getGender() {
	return this.gender;
}

public void setGender(java.lang.String fieldGender) {
	this.gender = fieldGender;
}

@Column(name = "contact_number", nullable = true, length = 50)
public java.lang.String getContact_number() {
	return this.contact_number;
}

public void setContact_number(java.lang.String fieldContactNumber) {
	this.contact_number = fieldContactNumber;
}

@Column(name = "rank", nullable = true, length = 50)
public java.lang.String getRank() {
	return this.rank;
}

public void setRank(java.lang.String fieldRank) {
	this.rank = fieldRank;
}

@Column(name = "department", nullable = true, length = 50)
public java.lang.String getDepartment() {
	return this.department;
}

public void setDepartment(java.lang.String fieldDepartment) {
	this.department = fieldDepartment;
}

@Column(name = "updated_by", nullable = true, length = 10)
public java.lang.Integer getUpdated_by() {
	return this.updated_by;
}

public void setUpdated_by(java.lang.Integer fieldUpdatedBy) {
	this.updated_by = fieldUpdatedBy;
}

@Column(name = "updated_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getUpdated_date() {
	return this.updated_date;
}

public void setUpdated_date(java.util.Date fieldUpdatedDate) {
	this.updated_date = fieldUpdatedDate;
}

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public FacultyDataHistoryMapping copy() {
FacultyDataHistoryMapping copyMe = new FacultyDataHistoryMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.faculty_id = this.faculty_id;
copyMe.bulsu_id = this.bulsu_id;
copyMe.last_name = this.last_name;
copyMe.first_name = this.first_name;
copyMe.middle_name = this.middle_name;
copyMe.gender = this.gender;
copyMe.contact_number = this.contact_number;
copyMe.rank = this.rank;
copyMe.department = this.department;
copyMe.updated_by = this.updated_by;
copyMe.updated_date = this.updated_date;
copyMe.active = this.active;
return copyMe;
}

}
