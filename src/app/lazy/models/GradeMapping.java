// SQL_db: cictems
// SQL_table: grade
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 21, 2017 03:55:51 PM
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
@Table(name = "grade", catalog = "cictems")
public class GradeMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer STUDENT_id;
private java.lang.Integer SUBJECT_id;
private java.lang.Integer ACADTERM_id;
private java.lang.String rating;
private java.lang.String remarks;
private java.lang.Double credit;
private java.lang.String credit_method;
private java.lang.Integer created_by;
private java.util.Date created_date;
private java.lang.Integer posted;
private java.lang.Integer posted_by;
private java.util.Date posting_date;
private java.util.Date inc_expire;
private java.lang.Integer updated_by;
private java.util.Date updated_date;
private java.lang.String reason_for_update;
private java.lang.Integer referrence_curriculum;
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

@Column(name = "SUBJECT_id", nullable = true, length = 10)
public java.lang.Integer getSUBJECT_id() {
	return this.SUBJECT_id;
}

public void setSUBJECT_id(java.lang.Integer fieldSubjectId) {
	this.SUBJECT_id = fieldSubjectId;
}

@Column(name = "ACADTERM_id", nullable = true, length = 10)
public java.lang.Integer getACADTERM_id() {
	return this.ACADTERM_id;
}

public void setACADTERM_id(java.lang.Integer fieldAcadtermId) {
	this.ACADTERM_id = fieldAcadtermId;
}

@Column(name = "rating", nullable = true, length = 50)
public java.lang.String getRating() {
	return this.rating;
}

public void setRating(java.lang.String fieldRating) {
	this.rating = fieldRating;
}

@Column(name = "remarks", nullable = true, length = 100)
public java.lang.String getRemarks() {
	return this.remarks;
}

public void setRemarks(java.lang.String fieldRemarks) {
	this.remarks = fieldRemarks;
}

@Column(name = "credit", nullable = true, length = 22)
public java.lang.Double getCredit() {
	return this.credit;
}

public void setCredit(java.lang.Double fieldCredit) {
	this.credit = fieldCredit;
}

@Column(name = "credit_method", nullable = true, length = 100)
public java.lang.String getCredit_method() {
	return this.credit_method;
}

public void setCredit_method(java.lang.String fieldCreditMethod) {
	this.credit_method = fieldCreditMethod;
}

@Column(name = "created_by", nullable = true, length = 10)
public java.lang.Integer getCreated_by() {
	return this.created_by;
}

public void setCreated_by(java.lang.Integer fieldCreatedBy) {
	this.created_by = fieldCreatedBy;
}

@Column(name = "created_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getCreated_date() {
	return this.created_date;
}

public void setCreated_date(java.util.Date fieldCreatedDate) {
	this.created_date = fieldCreatedDate;
}

@Column(name = "posted", nullable = true, length = 10)
public java.lang.Integer getPosted() {
	return this.posted;
}

public void setPosted(java.lang.Integer fieldPosted) {
	this.posted = fieldPosted;
}

@Column(name = "posted_by", nullable = true, length = 10)
public java.lang.Integer getPosted_by() {
	return this.posted_by;
}

public void setPosted_by(java.lang.Integer fieldPostedBy) {
	this.posted_by = fieldPostedBy;
}

@Column(name = "posting_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getPosting_date() {
	return this.posting_date;
}

public void setPosting_date(java.util.Date fieldPostingDate) {
	this.posting_date = fieldPostingDate;
}

@Column(name = "inc_expire", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getInc_expire() {
	return this.inc_expire;
}

public void setInc_expire(java.util.Date fieldIncExpire) {
	this.inc_expire = fieldIncExpire;
}

@Column(name = "updated_by", nullable = true, length = 10)
public java.lang.Integer getUpdated_by() {
	return this.updated_by;
}

public void setUpdated_by(java.lang.Integer fieldUpdatedBy) {
	this.updated_by = fieldUpdatedBy;
}

@Column(name = "updated_date", nullable = true, length = 26)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getUpdated_date() {
	return this.updated_date;
}

public void setUpdated_date(java.util.Date fieldUpdatedDate) {
	this.updated_date = fieldUpdatedDate;
}

@Column(name = "reason_for_update", nullable = true, length = 50)
public java.lang.String getReason_for_update() {
	return this.reason_for_update;
}

public void setReason_for_update(java.lang.String fieldReasonForUpdate) {
	this.reason_for_update = fieldReasonForUpdate;
}

@Column(name = "referrence_curriculum", nullable = true, length = 10)
public java.lang.Integer getReferrence_curriculum() {
	return this.referrence_curriculum;
}

public void setReferrence_curriculum(java.lang.Integer fieldReferrenceCurriculum) {
	this.referrence_curriculum = fieldReferrenceCurriculum;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
