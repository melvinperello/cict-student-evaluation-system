// SQL_db: cictems
// SQL_table: curriculum
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 21, 2017 10:36:15 PM
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
@Table(name = "curriculum", catalog = "cictems")
public class CurriculumMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer ACADPROG_id;
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
private java.lang.Integer obsolete_term;
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

@Column(name = "ACADPROG_id", nullable = true, length = 10)
public java.lang.Integer getACADPROG_id() {
	return this.ACADPROG_id;
}

public void setACADPROG_id(java.lang.Integer fieldAcadprogId) {
	this.ACADPROG_id = fieldAcadprogId;
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

@Column(name = "implemented", nullable = true, length = 3)
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

@Column(name = "obsolete_term", nullable = true, length = 10)
public java.lang.Integer getObsolete_term() {
	return this.obsolete_term;
}

public void setObsolete_term(java.lang.Integer fieldObsoleteTerm) {
	this.obsolete_term = fieldObsoleteTerm;
}

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
