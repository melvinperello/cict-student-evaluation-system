// SQL_db: cictems
// SQL_table: load_section
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 27, 2017 10:27:35 AM
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
@Table(name = "load_section", catalog = "cictems")
public class LoadSectionMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer ACADTERM_id;
private java.lang.Integer ACADPROG_id;
private java.lang.Integer CURRICULUM_id;
private java.lang.String section_name;
private java.lang.String section_description;
private java.lang.Integer year_level;
private java.lang.Integer _group;
private java.lang.String type;
private java.lang.String college;
private java.util.Date created_date;
private java.lang.Integer created_by;
private java.util.Date updated_date;
private java.lang.Integer updated_by;
private java.lang.Integer adviser;
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

@Column(name = "ACADTERM_id", nullable = true, length = 10)
public java.lang.Integer getACADTERM_id() {
	return this.ACADTERM_id;
}

public void setACADTERM_id(java.lang.Integer fieldAcadtermId) {
	this.ACADTERM_id = fieldAcadtermId;
}

@Column(name = "ACADPROG_id", nullable = true, length = 10)
public java.lang.Integer getACADPROG_id() {
	return this.ACADPROG_id;
}

public void setACADPROG_id(java.lang.Integer fieldAcadprogId) {
	this.ACADPROG_id = fieldAcadprogId;
}

@Column(name = "CURRICULUM_id", nullable = true, length = 10)
public java.lang.Integer getCURRICULUM_id() {
	return this.CURRICULUM_id;
}

public void setCURRICULUM_id(java.lang.Integer fieldCurriculumId) {
	this.CURRICULUM_id = fieldCurriculumId;
}

@Column(name = "section_name", nullable = true, length = 50)
public java.lang.String getSection_name() {
	return this.section_name;
}

public void setSection_name(java.lang.String fieldSectionName) {
	this.section_name = fieldSectionName;
}

@Column(name = "section_description", nullable = true, length = 50)
public java.lang.String getSection_description() {
	return this.section_description;
}

public void setSection_description(java.lang.String fieldSectionDescription) {
	this.section_description = fieldSectionDescription;
}

@Column(name = "year_level", nullable = true, length = 10)
public java.lang.Integer getYear_level() {
	return this.year_level;
}

public void setYear_level(java.lang.Integer fieldYearLevel) {
	this.year_level = fieldYearLevel;
}

@Column(name = "_group", nullable = true, length = 10)
public java.lang.Integer get_group() {
	return this._group;
}

public void set_group(java.lang.Integer fieldGroup) {
	this._group = fieldGroup;
}

@Column(name = "type", nullable = false, length = 50)
public java.lang.String getType() {
	return this.type;
}

public void setType(java.lang.String fieldType) {
	this.type = fieldType;
}

@Column(name = "college", nullable = false, length = 50)
public java.lang.String getCollege() {
	return this.college;
}

public void setCollege(java.lang.String fieldCollege) {
	this.college = fieldCollege;
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

@Column(name = "adviser", nullable = true, length = 10)
public java.lang.Integer getAdviser() {
	return this.adviser;
}

public void setAdviser(java.lang.Integer fieldAdviser) {
	this.adviser = fieldAdviser;
}

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
