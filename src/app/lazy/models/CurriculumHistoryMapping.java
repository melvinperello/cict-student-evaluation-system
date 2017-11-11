// SQL_db: cictems
// SQL_table: curriculum_history
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 08, 2017 05:40:36 PM
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
@Table(name = "curriculum_history", catalog = "cictems")
public class CurriculumHistoryMapping implements java.io.Serializable {


private java.lang.Integer history_id;
private java.lang.Integer CURRICULUM_id;
private java.lang.String major;
private java.lang.String name;
private java.lang.String description;
private java.lang.String ladderization;
private java.lang.String ladderization_type;
private java.lang.Integer study_years;
private java.util.Date created_date;
private java.lang.Integer created_by;
private java.util.Date updated_date;
private java.lang.Integer updated_by;
private java.util.Date removed_date;
private java.lang.Integer removed_by;
private java.lang.Integer implemented;
private java.util.Date implementation_date;
private java.lang.Integer implemented_by;
private java.lang.Integer active;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "history_id", nullable = false, length = 10)
public java.lang.Integer getHistory_id() {
	return this.history_id;
}

public void setHistory_id(java.lang.Integer fieldHistoryId) {
	this.history_id = fieldHistoryId;
}

@Column(name = "CURRICULUM_id", nullable = true, length = 10)
public java.lang.Integer getCURRICULUM_id() {
	return this.CURRICULUM_id;
}

public void setCURRICULUM_id(java.lang.Integer fieldCurriculumId) {
	this.CURRICULUM_id = fieldCurriculumId;
}

@Column(name = "major", nullable = true, length = 50)
public java.lang.String getMajor() {
	return this.major;
}

public void setMajor(java.lang.String fieldMajor) {
	this.major = fieldMajor;
}

@Column(name = "name", nullable = true, length = 50)
public java.lang.String getName() {
	return this.name;
}

public void setName(java.lang.String fieldName) {
	this.name = fieldName;
}

@Column(name = "description", nullable = true, length = 50)
public java.lang.String getDescription() {
	return this.description;
}

public void setDescription(java.lang.String fieldDescription) {
	this.description = fieldDescription;
}

@Column(name = "ladderization", nullable = true, length = 50)
public java.lang.String getLadderization() {
	return this.ladderization;
}

public void setLadderization(java.lang.String fieldLadderization) {
	this.ladderization = fieldLadderization;
}

@Column(name = "ladderization_type", nullable = true, length = 50)
public java.lang.String getLadderization_type() {
	return this.ladderization_type;
}

public void setLadderization_type(java.lang.String fieldLadderizationType) {
	this.ladderization_type = fieldLadderizationType;
}

@Column(name = "study_years", nullable = true, length = 10)
public java.lang.Integer getStudy_years() {
	return this.study_years;
}

public void setStudy_years(java.lang.Integer fieldStudyYears) {
	this.study_years = fieldStudyYears;
}

@Column(name = "created_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getCreated_date() {
	return this.created_date;
}

public void setCreated_date(java.util.Date fieldCreatedDate) {
	this.created_date = fieldCreatedDate;
}

@Column(name = "created_by", nullable = true, length = 10)
public java.lang.Integer getCreated_by() {
	return this.created_by;
}

public void setCreated_by(java.lang.Integer fieldCreatedBy) {
	this.created_by = fieldCreatedBy;
}

@Column(name = "updated_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getUpdated_date() {
	return this.updated_date;
}

public void setUpdated_date(java.util.Date fieldUpdatedDate) {
	this.updated_date = fieldUpdatedDate;
}

@Column(name = "updated_by", nullable = true, length = 10)
public java.lang.Integer getUpdated_by() {
	return this.updated_by;
}

public void setUpdated_by(java.lang.Integer fieldUpdatedBy) {
	this.updated_by = fieldUpdatedBy;
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

@Column(name = "implemented", nullable = true, length = 10)
public java.lang.Integer getImplemented() {
	return this.implemented;
}

public void setImplemented(java.lang.Integer fieldImplemented) {
	this.implemented = fieldImplemented;
}

@Column(name = "implementation_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getImplementation_date() {
	return this.implementation_date;
}

public void setImplementation_date(java.util.Date fieldImplementationDate) {
	this.implementation_date = fieldImplementationDate;
}

@Column(name = "implemented_by", nullable = true, length = 10)
public java.lang.Integer getImplemented_by() {
	return this.implemented_by;
}

public void setImplemented_by(java.lang.Integer fieldImplementedBy) {
	this.implemented_by = fieldImplementedBy;
}

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
