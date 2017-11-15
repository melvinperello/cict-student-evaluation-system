// SQL_db: cictems
// SQL_table: load_subject
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 15, 2017 01:44:45 PM
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
@Table(name = "load_subject", catalog = "cictems")
public class LoadSubjectMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer SUBJECT_id;
private java.lang.Integer LOADGRP_id;
private java.lang.Integer STUDENT_id;
private java.lang.Integer EVALUATION_id;
private java.util.Date added_date;
private java.lang.Integer added_by;
private java.lang.String remarks;
private java.util.Date removed_date;
private java.lang.Integer removed_by;
private java.lang.Integer changing_reference;
private java.lang.Integer active;
private java.lang.Integer arhived;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "id", nullable = false, length = 10)
public java.lang.Integer getId() {
	return this.id;
}

public void setId(java.lang.Integer fieldId) {
	this.id = fieldId;
}

@Column(name = "SUBJECT_id", nullable = true, length = 10)
public java.lang.Integer getSUBJECT_id() {
	return this.SUBJECT_id;
}

public void setSUBJECT_id(java.lang.Integer fieldSubjectId) {
	this.SUBJECT_id = fieldSubjectId;
}

@Column(name = "LOADGRP_id", nullable = true, length = 10)
public java.lang.Integer getLOADGRP_id() {
	return this.LOADGRP_id;
}

public void setLOADGRP_id(java.lang.Integer fieldLoadgrpId) {
	this.LOADGRP_id = fieldLoadgrpId;
}

@Column(name = "STUDENT_id", nullable = true, length = 10)
public java.lang.Integer getSTUDENT_id() {
	return this.STUDENT_id;
}

public void setSTUDENT_id(java.lang.Integer fieldStudentId) {
	this.STUDENT_id = fieldStudentId;
}

@Column(name = "EVALUATION_id", nullable = true, length = 10)
public java.lang.Integer getEVALUATION_id() {
	return this.EVALUATION_id;
}

public void setEVALUATION_id(java.lang.Integer fieldEvaluationId) {
	this.EVALUATION_id = fieldEvaluationId;
}

@Column(name = "added_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getAdded_date() {
	return this.added_date;
}

public void setAdded_date(java.util.Date fieldAddedDate) {
	this.added_date = fieldAddedDate;
}

@Column(name = "added_by", nullable = true, length = 10)
public java.lang.Integer getAdded_by() {
	return this.added_by;
}

public void setAdded_by(java.lang.Integer fieldAddedBy) {
	this.added_by = fieldAddedBy;
}

@Column(name = "remarks", nullable = true, length = 50)
public java.lang.String getRemarks() {
	return this.remarks;
}

public void setRemarks(java.lang.String fieldRemarks) {
	this.remarks = fieldRemarks;
}

@Column(name = "removed_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getRemoved_date() {
	return this.removed_date;
}

public void setRemoved_date(java.util.Date fieldRemovedDate) {
	this.removed_date = fieldRemovedDate;
}

@Column(name = "removed_by", nullable = true, length = 10)
public java.lang.Integer getRemoved_by() {
	return this.removed_by;
}

public void setRemoved_by(java.lang.Integer fieldRemovedBy) {
	this.removed_by = fieldRemovedBy;
}

@Column(name = "changing_reference", nullable = true, length = 10)
public java.lang.Integer getChanging_reference() {
	return this.changing_reference;
}

public void setChanging_reference(java.lang.Integer fieldChangingReference) {
	this.changing_reference = fieldChangingReference;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Column(name = "arhived", nullable = true, length = 10)
public java.lang.Integer getArhived() {
	return this.arhived;
}

public void setArhived(java.lang.Integer fieldArhived) {
	this.arhived = fieldArhived;
}

}
