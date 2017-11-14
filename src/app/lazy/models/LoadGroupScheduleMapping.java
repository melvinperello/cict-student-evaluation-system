// SQL_db: cictems
// SQL_table: load_group_schedule
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 14, 2017 06:36:01 PM
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
@Table(name = "load_group_schedule", catalog = "cictems")
public class LoadGroupScheduleMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer load_group_id;
private java.lang.String class_day;
private java.lang.String class_start;
private java.lang.String class_end;
private java.lang.String class_room;
private java.util.Date created_date;
private java.lang.Integer created_by;
private java.util.Date updated_date;
private java.lang.Integer updated_by;
private java.lang.Integer active;
private java.lang.Integer archived;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "id", nullable = false, length = 10)
public java.lang.Integer getId() {
	return this.id;
}

public void setId(java.lang.Integer fieldId) {
	this.id = fieldId;
}

@Column(name = "load_group_id", nullable = true, length = 10)
public java.lang.Integer getLoad_group_id() {
	return this.load_group_id;
}

public void setLoad_group_id(java.lang.Integer fieldLoadGroupId) {
	this.load_group_id = fieldLoadGroupId;
}

@Column(name = "class_day", nullable = true, length = 50)
public java.lang.String getClass_day() {
	return this.class_day;
}

public void setClass_day(java.lang.String fieldClassDay) {
	this.class_day = fieldClassDay;
}

@Column(name = "class_start", nullable = true, length = 50)
public java.lang.String getClass_start() {
	return this.class_start;
}

public void setClass_start(java.lang.String fieldClassStart) {
	this.class_start = fieldClassStart;
}

@Column(name = "class_end", nullable = true, length = 50)
public java.lang.String getClass_end() {
	return this.class_end;
}

public void setClass_end(java.lang.String fieldClassEnd) {
	this.class_end = fieldClassEnd;
}

@Column(name = "class_room", nullable = true, length = 50)
public java.lang.String getClass_room() {
	return this.class_room;
}

public void setClass_room(java.lang.String fieldClassRoom) {
	this.class_room = fieldClassRoom;
}

@Column(name = "created_date", nullable = false, length = 19)
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

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Column(name = "archived", nullable = false, length = 10)
public java.lang.Integer getArchived() {
	return this.archived;
}

public void setArchived(java.lang.Integer fieldArchived) {
	this.archived = fieldArchived;
}

}
