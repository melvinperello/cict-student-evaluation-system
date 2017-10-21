// SQL_db: cictems
// SQL_table: linked_settings
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
@Table(name = "linked_settings", catalog = "cictems")
public class LinkedSettingsMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer floor_3_max;
private java.lang.Integer floor_4_max;
private java.lang.String floor_3_name;
private java.lang.String floor_4_name;
private java.lang.Integer floor_3_last;
private java.lang.Integer floor_4_last;
private java.lang.Integer floor_3_cut;
private java.lang.Integer floor_4_cut;
private java.lang.Integer created_by;
private java.util.Date created_date;
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

@Column(name = "floor_3_max", nullable = true, length = 10)
public java.lang.Integer getFloor_3_max() {
	return this.floor_3_max;
}

public void setFloor_3_max(java.lang.Integer fieldFloor3Max) {
	this.floor_3_max = fieldFloor3Max;
}

@Column(name = "floor_4_max", nullable = true, length = 10)
public java.lang.Integer getFloor_4_max() {
	return this.floor_4_max;
}

public void setFloor_4_max(java.lang.Integer fieldFloor4Max) {
	this.floor_4_max = fieldFloor4Max;
}

@Column(name = "floor_3_name", nullable = true, length = 50)
public java.lang.String getFloor_3_name() {
	return this.floor_3_name;
}

public void setFloor_3_name(java.lang.String fieldFloor3Name) {
	this.floor_3_name = fieldFloor3Name;
}

@Column(name = "floor_4_name", nullable = true, length = 50)
public java.lang.String getFloor_4_name() {
	return this.floor_4_name;
}

public void setFloor_4_name(java.lang.String fieldFloor4Name) {
	this.floor_4_name = fieldFloor4Name;
}

@Column(name = "floor_3_last", nullable = true, length = 10)
public java.lang.Integer getFloor_3_last() {
	return this.floor_3_last;
}

public void setFloor_3_last(java.lang.Integer fieldFloor3Last) {
	this.floor_3_last = fieldFloor3Last;
}

@Column(name = "floor_4_last", nullable = true, length = 10)
public java.lang.Integer getFloor_4_last() {
	return this.floor_4_last;
}

public void setFloor_4_last(java.lang.Integer fieldFloor4Last) {
	this.floor_4_last = fieldFloor4Last;
}

@Column(name = "floor_3_cut", nullable = true, length = 10)
public java.lang.Integer getFloor_3_cut() {
	return this.floor_3_cut;
}

public void setFloor_3_cut(java.lang.Integer fieldFloor3Cut) {
	this.floor_3_cut = fieldFloor3Cut;
}

@Column(name = "floor_4_cut", nullable = true, length = 10)
public java.lang.Integer getFloor_4_cut() {
	return this.floor_4_cut;
}

public void setFloor_4_cut(java.lang.Integer fieldFloor4Cut) {
	this.floor_4_cut = fieldFloor4Cut;
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

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
