// SQL_db: cictems
// SQL_table: student_data_history
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
@Table(name = "student_data_history", catalog = "cictems")
public class StudentDataHistoryMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.Integer cict_id;
private java.lang.String student_number;
private java.lang.String last_name;
private java.lang.String first_name;
private java.lang.String middle_name;
private java.lang.String gender;
private java.lang.String campus;
private java.lang.String year_level;
private java.lang.String section;
private java.lang.String _group;
private java.lang.String updated_by;
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

@Column(name = "cict_id", nullable = true, length = 10)
public java.lang.Integer getCict_id() {
	return this.cict_id;
}

public void setCict_id(java.lang.Integer fieldCictId) {
	this.cict_id = fieldCictId;
}

@Column(name = "student_number", nullable = true, length = 50)
public java.lang.String getStudent_number() {
	return this.student_number;
}

public void setStudent_number(java.lang.String fieldStudentNumber) {
	this.student_number = fieldStudentNumber;
}

@Column(name = "last_name", nullable = true, length = 100)
public java.lang.String getLast_name() {
	return this.last_name;
}

public void setLast_name(java.lang.String fieldLastName) {
	this.last_name = fieldLastName;
}

@Column(name = "first_name", nullable = true, length = 100)
public java.lang.String getFirst_name() {
	return this.first_name;
}

public void setFirst_name(java.lang.String fieldFirstName) {
	this.first_name = fieldFirstName;
}

@Column(name = "middle_name", nullable = true, length = 100)
public java.lang.String getMiddle_name() {
	return this.middle_name;
}

public void setMiddle_name(java.lang.String fieldMiddleName) {
	this.middle_name = fieldMiddleName;
}

@Column(name = "gender", nullable = true, length = 100)
public java.lang.String getGender() {
	return this.gender;
}

public void setGender(java.lang.String fieldGender) {
	this.gender = fieldGender;
}

@Column(name = "campus", nullable = true, length = 100)
public java.lang.String getCampus() {
	return this.campus;
}

public void setCampus(java.lang.String fieldCampus) {
	this.campus = fieldCampus;
}

@Column(name = "year_level", nullable = true, length = 10)
public java.lang.String getYear_level() {
	return this.year_level;
}

public void setYear_level(java.lang.String fieldYearLevel) {
	this.year_level = fieldYearLevel;
}

@Column(name = "section", nullable = true, length = 10)
public java.lang.String getSection() {
	return this.section;
}

public void setSection(java.lang.String fieldSection) {
	this.section = fieldSection;
}

@Column(name = "_group", nullable = true, length = 10)
public java.lang.String get_group() {
	return this._group;
}

public void set_group(java.lang.String fieldGroup) {
	this._group = fieldGroup;
}

@Column(name = "updated_by", nullable = true, length = 100)
public java.lang.String getUpdated_by() {
	return this.updated_by;
}

public void setUpdated_by(java.lang.String fieldUpdatedBy) {
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
public StudentDataHistoryMapping copy() {
StudentDataHistoryMapping copyMe = new StudentDataHistoryMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.cict_id = this.cict_id;
copyMe.student_number = this.student_number;
copyMe.last_name = this.last_name;
copyMe.first_name = this.first_name;
copyMe.middle_name = this.middle_name;
copyMe.gender = this.gender;
copyMe.campus = this.campus;
copyMe.year_level = this.year_level;
copyMe.section = this.section;
copyMe._group = this._group;
copyMe.updated_by = this.updated_by;
copyMe.updated_date = this.updated_date;
copyMe.active = this.active;
return copyMe;
}

}
