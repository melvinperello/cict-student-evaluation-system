// SQL_db: cictems
// SQL_table: academic_term
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 18, 2017 02:34:39 PM
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
private java.lang.Integer evaluation_service;
private java.lang.Integer es_updater;
private java.util.Date es_update_date;
private java.lang.Integer adding_service;
private java.lang.Integer as_updater;
private java.util.Date as_update_date;
private java.lang.Integer encoding_service;
private java.lang.Integer ens_updater;
private java.util.Date ens_update_date;
private java.lang.String type;
private java.lang.String approval_state;
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

@Column(name = "evaluation_service", nullable = true, length = 10)
public java.lang.Integer getEvaluation_service() {
	return this.evaluation_service;
}

public void setEvaluation_service(java.lang.Integer fieldEvaluationService) {
	this.evaluation_service = fieldEvaluationService;
}

@Column(name = "es_updater", nullable = true, length = 10)
public java.lang.Integer getEs_updater() {
	return this.es_updater;
}

public void setEs_updater(java.lang.Integer fieldEsUpdater) {
	this.es_updater = fieldEsUpdater;
}

@Column(name = "es_update_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getEs_update_date() {
	return this.es_update_date;
}

public void setEs_update_date(java.util.Date fieldEsUpdateDate) {
	this.es_update_date = fieldEsUpdateDate;
}

@Column(name = "adding_service", nullable = true, length = 10)
public java.lang.Integer getAdding_service() {
	return this.adding_service;
}

public void setAdding_service(java.lang.Integer fieldAddingService) {
	this.adding_service = fieldAddingService;
}

@Column(name = "as_updater", nullable = true, length = 10)
public java.lang.Integer getAs_updater() {
	return this.as_updater;
}

public void setAs_updater(java.lang.Integer fieldAsUpdater) {
	this.as_updater = fieldAsUpdater;
}

@Column(name = "as_update_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getAs_update_date() {
	return this.as_update_date;
}

public void setAs_update_date(java.util.Date fieldAsUpdateDate) {
	this.as_update_date = fieldAsUpdateDate;
}

@Column(name = "encoding_service", nullable = true, length = 10)
public java.lang.Integer getEncoding_service() {
	return this.encoding_service;
}

public void setEncoding_service(java.lang.Integer fieldEncodingService) {
	this.encoding_service = fieldEncodingService;
}

@Column(name = "ens_updater", nullable = true, length = 10)
public java.lang.Integer getEns_updater() {
	return this.ens_updater;
}

public void setEns_updater(java.lang.Integer fieldEnsUpdater) {
	this.ens_updater = fieldEnsUpdater;
}

@Column(name = "ens_update_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getEns_update_date() {
	return this.ens_update_date;
}

public void setEns_update_date(java.util.Date fieldEnsUpdateDate) {
	this.ens_update_date = fieldEnsUpdateDate;
}

@Column(name = "type", nullable = true, length = 50)
public java.lang.String getType() {
	return this.type;
}

public void setType(java.lang.String fieldType) {
	this.type = fieldType;
}

@Column(name = "approval_state", nullable = true, length = 50)
public java.lang.String getApproval_state() {
	return this.approval_state;
}

public void setApproval_state(java.lang.String fieldApprovalState) {
	this.approval_state = fieldApprovalState;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
