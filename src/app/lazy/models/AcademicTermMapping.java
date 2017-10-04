// SQL_db: cictems
// SQL_table: academic_term
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 04, 2017 08:09:14 PM
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
@Table(name = "academic_term", catalog = "cictems")
public class AcademicTermMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.String school_year;
private java.lang.String semester;
private java.lang.Integer semester_regular;
private java.lang.Integer current;
private java.util.Date evaluation_start;
private java.util.Date evaluation_end;
private java.util.Date adding_start;
private java.util.Date adding_end;
private java.lang.String type;
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

@Column(name = "school_year", nullable = true, length = 50)
public java.lang.String getSchool_year() {
	return this.school_year;
}

public void setSchool_year(java.lang.String fieldSchoolYear) {
	this.school_year = fieldSchoolYear;
}

@Column(name = "semester", nullable = true, length = 50)
public java.lang.String getSemester() {
	return this.semester;
}

public void setSemester(java.lang.String fieldSemester) {
	this.semester = fieldSemester;
}

@Column(name = "semester_regular", nullable = true, length = 10)
public java.lang.Integer getSemester_regular() {
	return this.semester_regular;
}

public void setSemester_regular(java.lang.Integer fieldSemesterRegular) {
	this.semester_regular = fieldSemesterRegular;
}

@Column(name = "current", nullable = true, length = 10)
public java.lang.Integer getCurrent() {
	return this.current;
}

public void setCurrent(java.lang.Integer fieldCurrent) {
	this.current = fieldCurrent;
}

@Column(name = "evaluation_start", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getEvaluation_start() {
	return this.evaluation_start;
}

public void setEvaluation_start(java.util.Date fieldEvaluationStart) {
	this.evaluation_start = fieldEvaluationStart;
}

@Column(name = "evaluation_end", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getEvaluation_end() {
	return this.evaluation_end;
}

public void setEvaluation_end(java.util.Date fieldEvaluationEnd) {
	this.evaluation_end = fieldEvaluationEnd;
}

@Column(name = "adding_start", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getAdding_start() {
	return this.adding_start;
}

public void setAdding_start(java.util.Date fieldAddingStart) {
	this.adding_start = fieldAddingStart;
}

@Column(name = "adding_end", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getAdding_end() {
	return this.adding_end;
}

public void setAdding_end(java.util.Date fieldAddingEnd) {
	this.adding_end = fieldAddingEnd;
}

@Column(name = "type", nullable = true, length = 50)
public java.lang.String getType() {
	return this.type;
}

public void setType(java.lang.String fieldType) {
	this.type = fieldType;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
