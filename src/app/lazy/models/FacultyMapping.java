// SQL_db: cictems
// SQL_table: faculty
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 03, 2017 05:38:23 PM
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
@Table(name = "faculty", catalog = "cictems")
public class FacultyMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.String bulsu_id;
private java.lang.String last_name;
private java.lang.String first_name;
private java.lang.String middle_name;
private java.lang.String name_extension;
private java.lang.String gender;
private java.lang.String rank;
private java.lang.String designation;
private java.lang.String department;
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

@Column(name = "bulsu_id", nullable = true, length = 50)
public java.lang.String getBulsu_id() {
	return this.bulsu_id;
}

public void setBulsu_id(java.lang.String fieldBulsuId) {
	this.bulsu_id = fieldBulsuId;
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

@Column(name = "name_extension", nullable = true, length = 100)
public java.lang.String getName_extension() {
	return this.name_extension;
}

public void setName_extension(java.lang.String fieldNameExtension) {
	this.name_extension = fieldNameExtension;
}

@Column(name = "gender", nullable = true, length = 100)
public java.lang.String getGender() {
	return this.gender;
}

public void setGender(java.lang.String fieldGender) {
	this.gender = fieldGender;
}

@Column(name = "rank", nullable = true, length = 50)
public java.lang.String getRank() {
	return this.rank;
}

public void setRank(java.lang.String fieldRank) {
	this.rank = fieldRank;
}

@Column(name = "designation", nullable = true, length = 50)
public java.lang.String getDesignation() {
	return this.designation;
}

public void setDesignation(java.lang.String fieldDesignation) {
	this.designation = fieldDesignation;
}

@Column(name = "department", nullable = true, length = 50)
public java.lang.String getDepartment() {
	return this.department;
}

public void setDepartment(java.lang.String fieldDepartment) {
	this.department = fieldDepartment;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
