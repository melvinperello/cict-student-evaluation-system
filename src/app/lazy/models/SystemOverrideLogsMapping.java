// SQL_db: cictems
// SQL_table: system_override_logs
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 04, 2017 08:09:17 PM
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
@Table(name = "system_override_logs", catalog = "cictems")
public class SystemOverrideLogsMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.String category;
private java.lang.String description;
private java.lang.Integer executed_by;
private java.util.Date executed_date;
private java.lang.Integer academic_term;
private java.lang.String conforme;
private java.lang.String conforme_type;
private java.lang.Integer conforme_id;
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

@Column(name = "category", nullable = true, length = 50)
public java.lang.String getCategory() {
	return this.category;
}

public void setCategory(java.lang.String fieldCategory) {
	this.category = fieldCategory;
}

@Column(name = "description", nullable = true, length = 50)
public java.lang.String getDescription() {
	return this.description;
}

public void setDescription(java.lang.String fieldDescription) {
	this.description = fieldDescription;
}

@Column(name = "executed_by", nullable = true, length = 10)
public java.lang.Integer getExecuted_by() {
	return this.executed_by;
}

public void setExecuted_by(java.lang.Integer fieldExecutedBy) {
	this.executed_by = fieldExecutedBy;
}

@Column(name = "executed_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getExecuted_date() {
	return this.executed_date;
}

public void setExecuted_date(java.util.Date fieldExecutedDate) {
	this.executed_date = fieldExecutedDate;
}

@Column(name = "academic_term", nullable = true, length = 10)
public java.lang.Integer getAcademic_term() {
	return this.academic_term;
}

public void setAcademic_term(java.lang.Integer fieldAcademicTerm) {
	this.academic_term = fieldAcademicTerm;
}

@Column(name = "conforme", nullable = true, length = 50)
public java.lang.String getConforme() {
	return this.conforme;
}

public void setConforme(java.lang.String fieldConforme) {
	this.conforme = fieldConforme;
}

@Column(name = "conforme_type", nullable = true, length = 50)
public java.lang.String getConforme_type() {
	return this.conforme_type;
}

public void setConforme_type(java.lang.String fieldConformeType) {
	this.conforme_type = fieldConformeType;
}

@Column(name = "conforme_id", nullable = true, length = 10)
public java.lang.Integer getConforme_id() {
	return this.conforme_id;
}

public void setConforme_id(java.lang.Integer fieldConformeId) {
	this.conforme_id = fieldConformeId;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
