// SQL_db: cictems
// SQL_table: curriculum_pre
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 08, 2017 11:47:56 PM
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
@Table(name = "curriculum_pre", catalog = "cictems")
public class CurriculumPreMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer curriculum_id_get;
private java.lang.Integer curriculum_id_req;
private java.lang.String cur_type;
private java.util.Date created_date;
private java.lang.Integer created_by;
private java.util.Date removed_date;
private java.lang.Integer removed_by;
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

@Column(name = "curriculum_id_get", nullable = true, length = 10)
public java.lang.Integer getCurriculum_id_get() {
	return this.curriculum_id_get;
}

public void setCurriculum_id_get(java.lang.Integer fieldCurriculumIdGet) {
	this.curriculum_id_get = fieldCurriculumIdGet;
}

@Column(name = "curriculum_id_req", nullable = true, length = 10)
public java.lang.Integer getCurriculum_id_req() {
	return this.curriculum_id_req;
}

public void setCurriculum_id_req(java.lang.Integer fieldCurriculumIdReq) {
	this.curriculum_id_req = fieldCurriculumIdReq;
}

@Column(name = "cur_type", nullable = true, length = 50)
public java.lang.String getCur_type() {
	return this.cur_type;
}

public void setCur_type(java.lang.String fieldCurType) {
	this.cur_type = fieldCurType;
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

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
