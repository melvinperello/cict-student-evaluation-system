// SQL_db: cictems
// SQL_table: student_course_history
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 04, 2017 10:55:07 PM
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
@Table(name = "student_course_history", catalog = "cictems")
public class StudentCourseHistoryMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer student_id;
private java.lang.Integer curriculum_id;
private java.util.Date curriculum_assigment;
private java.lang.Integer prep_id;
private java.util.Date prep_assignment;
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

@Column(name = "student_id", nullable = true, length = 10)
public java.lang.Integer getStudent_id() {
	return this.student_id;
}

public void setStudent_id(java.lang.Integer fieldStudentId) {
	this.student_id = fieldStudentId;
}

@Column(name = "curriculum_id", nullable = true, length = 10)
public java.lang.Integer getCurriculum_id() {
	return this.curriculum_id;
}

public void setCurriculum_id(java.lang.Integer fieldCurriculumId) {
	this.curriculum_id = fieldCurriculumId;
}

@Column(name = "curriculum_assigment", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getCurriculum_assigment() {
	return this.curriculum_assigment;
}

public void setCurriculum_assigment(java.util.Date fieldCurriculumAssigment) {
	this.curriculum_assigment = fieldCurriculumAssigment;
}

@Column(name = "prep_id", nullable = true, length = 10)
public java.lang.Integer getPrep_id() {
	return this.prep_id;
}

public void setPrep_id(java.lang.Integer fieldPrepId) {
	this.prep_id = fieldPrepId;
}

@Column(name = "prep_assignment", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getPrep_assignment() {
	return this.prep_assignment;
}

public void setPrep_assignment(java.util.Date fieldPrepAssignment) {
	this.prep_assignment = fieldPrepAssignment;
}

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
