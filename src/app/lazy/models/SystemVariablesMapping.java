// SQL_db: cictems
// SQL_table: system_variables
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 27, 2017 10:27:36 AM
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
@Table(name = "system_variables", catalog = "cictems")
public class SystemVariablesMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.String name;
private java.lang.String value;
private java.lang.Integer created_by;
private java.util.Date created_date;
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

@Column(name = "name", nullable = false, length = 50)
public java.lang.String getName() {
	return this.name;
}

public void setName(java.lang.String fieldName) {
	this.name = fieldName;
}

@Column(name = "value", nullable = false, length = 300)
public java.lang.String getValue() {
	return this.value;
}

public void setValue(java.lang.String fieldValue) {
	this.value = fieldValue;
}

@Column(name = "created_by", nullable = true, length = 10)
public java.lang.Integer getCreated_by() {
	return this.created_by;
}

public void setCreated_by(java.lang.Integer fieldCreatedBy) {
	this.created_by = fieldCreatedBy;
}

@Column(name = "created_date", nullable = false, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getCreated_date() {
	return this.created_date;
}

public void setCreated_date(java.util.Date fieldCreatedDate) {
	this.created_date = fieldCreatedDate;
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

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
