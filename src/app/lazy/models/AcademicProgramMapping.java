// SQL_db: cictems
// SQL_table: academic_program
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 21, 2017 03:55:49 PM
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
@Table(name = "academic_program", catalog = "cictems")
public class AcademicProgramMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.String code;
private java.lang.String name;
private java.lang.Integer floor_assignment;
private java.util.Date created_date;
private java.lang.Integer created_by;
private java.util.Date removed_date;
private java.lang.Integer removed_by;
private java.lang.Integer implemented;
private java.util.Date implementation_date;
private java.lang.Integer implemented_by;
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

@Column(name = "code", nullable = true, length = 50)
public java.lang.String getCode() {
	return this.code;
}

public void setCode(java.lang.String fieldCode) {
	this.code = fieldCode;
}

@Column(name = "name", nullable = true, length = 50)
public java.lang.String getName() {
	return this.name;
}

public void setName(java.lang.String fieldName) {
	this.name = fieldName;
}

@Column(name = "floor_assignment", nullable = true, length = 10)
public java.lang.Integer getFloor_assignment() {
	return this.floor_assignment;
}

public void setFloor_assignment(java.lang.Integer fieldFloorAssignment) {
	this.floor_assignment = fieldFloorAssignment;
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

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
