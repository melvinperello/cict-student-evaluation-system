// SQL_db: cictems
// SQL_table: subject
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
@Table(name = "subject", catalog = "cictems")
public class SubjectMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.String code;
private java.lang.String descriptive_title;
private java.lang.Double lec_units;
private java.lang.Double lab_units;
private java.lang.String type;
private java.lang.String subtype;
private java.lang.Integer added_by;
private java.util.Date added_date;
private java.lang.Integer updated_by;
private java.util.Date updated_date;
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

@Column(name = "code", nullable = true, length = 100)
public java.lang.String getCode() {
	return this.code;
}

public void setCode(java.lang.String fieldCode) {
	this.code = fieldCode;
}

@Column(name = "descriptive_title", nullable = true, length = 100)
public java.lang.String getDescriptive_title() {
	return this.descriptive_title;
}

public void setDescriptive_title(java.lang.String fieldDescriptiveTitle) {
	this.descriptive_title = fieldDescriptiveTitle;
}

@Column(name = "lec_units", nullable = true, length = 22)
public java.lang.Double getLec_units() {
	return this.lec_units;
}

public void setLec_units(java.lang.Double fieldLecUnits) {
	this.lec_units = fieldLecUnits;
}

@Column(name = "lab_units", nullable = true, length = 22)
public java.lang.Double getLab_units() {
	return this.lab_units;
}

public void setLab_units(java.lang.Double fieldLabUnits) {
	this.lab_units = fieldLabUnits;
}

@Column(name = "type", nullable = true, length = 50)
public java.lang.String getType() {
	return this.type;
}

public void setType(java.lang.String fieldType) {
	this.type = fieldType;
}

@Column(name = "subtype", nullable = true, length = 50)
public java.lang.String getSubtype() {
	return this.subtype;
}

public void setSubtype(java.lang.String fieldSubtype) {
	this.subtype = fieldSubtype;
}

@Column(name = "added_by", nullable = true, length = 10)
public java.lang.Integer getAdded_by() {
	return this.added_by;
}

public void setAdded_by(java.lang.Integer fieldAddedBy) {
	this.added_by = fieldAddedBy;
}

@Column(name = "added_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getAdded_date() {
	return this.added_date;
}

public void setAdded_date(java.util.Date fieldAddedDate) {
	this.added_date = fieldAddedDate;
}

@Column(name = "updated_by", nullable = true, length = 10)
public java.lang.Integer getUpdated_by() {
	return this.updated_by;
}

public void setUpdated_by(java.lang.Integer fieldUpdatedBy) {
	this.updated_by = fieldUpdatedBy;
}

@Column(name = "updated_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getUpdated_date() {
	return this.updated_date;
}

public void setUpdated_date(java.util.Date fieldUpdatedDate) {
	this.updated_date = fieldUpdatedDate;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
