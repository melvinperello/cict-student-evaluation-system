// SQL_db: cictems
// SQL_table: linked_entrance
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 21, 2017 10:49:57 PM
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
@Table(name = "linked_entrance", catalog = "cictems")
public class LinkedEntranceMapping implements java.io.Serializable {


private java.lang.Integer reference_id;
private java.lang.String student_number;
private java.lang.String name;
private java.lang.String status;
private java.lang.String faculty_name;
private java.lang.Integer floor_assignment;
private java.lang.Integer active;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "reference_id", nullable = false, length = 10)
public java.lang.Integer getReference_id() {
	return this.reference_id;
}

public void setReference_id(java.lang.Integer fieldReferenceId) {
	this.reference_id = fieldReferenceId;
}

@Column(name = "student_number", nullable = true, length = 50)
public java.lang.String getStudent_number() {
	return this.student_number;
}

public void setStudent_number(java.lang.String fieldStudentNumber) {
	this.student_number = fieldStudentNumber;
}

@Column(name = "name", nullable = true, length = 50)
public java.lang.String getName() {
	return this.name;
}

public void setName(java.lang.String fieldName) {
	this.name = fieldName;
}

@Column(name = "status", nullable = true, length = 50)
public java.lang.String getStatus() {
	return this.status;
}

public void setStatus(java.lang.String fieldStatus) {
	this.status = fieldStatus;
}

@Column(name = "faculty_name", nullable = true, length = 50)
public java.lang.String getFaculty_name() {
	return this.faculty_name;
}

public void setFaculty_name(java.lang.String fieldFacultyName) {
	this.faculty_name = fieldFacultyName;
}

@Column(name = "floor_assignment", nullable = true, length = 10)
public java.lang.Integer getFloor_assignment() {
	return this.floor_assignment;
}

public void setFloor_assignment(java.lang.Integer fieldFloorAssignment) {
	this.floor_assignment = fieldFloorAssignment;
}

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
