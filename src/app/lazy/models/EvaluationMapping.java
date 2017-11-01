// SQL_db: cictems
// SQL_table: evaluation
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 01, 2017 07:15:25 PM
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
@Table(name = "evaluation", catalog = "cictems")
public class EvaluationMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer STUDENT_id;
private java.lang.Integer ACADTERM_id;
private java.lang.Integer FACULTY_id;
private java.lang.Integer adding_reference_id;
private java.util.Date evaluation_date;
private java.lang.Integer year_level;
private java.lang.String type;
private java.lang.String remarks;
private java.lang.String print_type;
private java.lang.Integer cancelled_by;
private java.util.Date cancelled_date;
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

@Column(name = "STUDENT_id", nullable = true, length = 10)
public java.lang.Integer getSTUDENT_id() {
	return this.STUDENT_id;
}

public void setSTUDENT_id(java.lang.Integer fieldStudentId) {
	this.STUDENT_id = fieldStudentId;
}

@Column(name = "ACADTERM_id", nullable = true, length = 10)
public java.lang.Integer getACADTERM_id() {
	return this.ACADTERM_id;
}

public void setACADTERM_id(java.lang.Integer fieldAcadtermId) {
	this.ACADTERM_id = fieldAcadtermId;
}

@Column(name = "FACULTY_id", nullable = true, length = 10)
public java.lang.Integer getFACULTY_id() {
	return this.FACULTY_id;
}

public void setFACULTY_id(java.lang.Integer fieldFacultyId) {
	this.FACULTY_id = fieldFacultyId;
}

@Column(name = "adding_reference_id", nullable = true, length = 10)
public java.lang.Integer getAdding_reference_id() {
	return this.adding_reference_id;
}

public void setAdding_reference_id(java.lang.Integer fieldAddingReferenceId) {
	this.adding_reference_id = fieldAddingReferenceId;
}

@Column(name = "evaluation_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getEvaluation_date() {
	return this.evaluation_date;
}

public void setEvaluation_date(java.util.Date fieldEvaluationDate) {
	this.evaluation_date = fieldEvaluationDate;
}

@Column(name = "year_level", nullable = true, length = 10)
public java.lang.Integer getYear_level() {
	return this.year_level;
}

public void setYear_level(java.lang.Integer fieldYearLevel) {
	this.year_level = fieldYearLevel;
}

@Column(name = "type", nullable = true, length = 50)
public java.lang.String getType() {
	return this.type;
}

public void setType(java.lang.String fieldType) {
	this.type = fieldType;
}

@Column(name = "remarks", nullable = true, length = 50)
public java.lang.String getRemarks() {
	return this.remarks;
}

public void setRemarks(java.lang.String fieldRemarks) {
	this.remarks = fieldRemarks;
}

@Column(name = "print_type", nullable = true, length = 50)
public java.lang.String getPrint_type() {
	return this.print_type;
}

public void setPrint_type(java.lang.String fieldPrintType) {
	this.print_type = fieldPrintType;
}

@Column(name = "cancelled_by", nullable = true, length = 10)
public java.lang.Integer getCancelled_by() {
	return this.cancelled_by;
}

public void setCancelled_by(java.lang.Integer fieldCancelledBy) {
	this.cancelled_by = fieldCancelledBy;
}

@Column(name = "cancelled_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getCancelled_date() {
	return this.cancelled_date;
}

public void setCancelled_date(java.util.Date fieldCancelledDate) {
	this.cancelled_date = fieldCancelledDate;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
