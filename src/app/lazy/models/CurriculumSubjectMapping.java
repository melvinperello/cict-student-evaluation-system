// SQL_db: cictems
// SQL_table: curriculum_subject
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 19, 2017 02:01:22 PM
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
@Table(name = "curriculum_subject", catalog = "cictems")
public class CurriculumSubjectMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer CURRICULUM_id;
private java.lang.Integer SUBJECT_id;
private java.lang.Integer year;
private java.lang.Integer semester;
private java.lang.Integer sequence;
private java.lang.Integer added_by;
private java.util.Date added_date;
private java.lang.Integer removed_by;
private java.util.Date removed_date;
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

@Column(name = "CURRICULUM_id", nullable = true, length = 10)
public java.lang.Integer getCURRICULUM_id() {
	return this.CURRICULUM_id;
}

public void setCURRICULUM_id(java.lang.Integer fieldCurriculumId) {
	this.CURRICULUM_id = fieldCurriculumId;
}

@Column(name = "SUBJECT_id", nullable = true, length = 10)
public java.lang.Integer getSUBJECT_id() {
	return this.SUBJECT_id;
}

public void setSUBJECT_id(java.lang.Integer fieldSubjectId) {
	this.SUBJECT_id = fieldSubjectId;
}

@Column(name = "year", nullable = false, length = 10)
public java.lang.Integer getYear() {
	return this.year;
}

public void setYear(java.lang.Integer fieldYear) {
	this.year = fieldYear;
}

@Column(name = "semester", nullable = false, length = 10)
public java.lang.Integer getSemester() {
	return this.semester;
}

public void setSemester(java.lang.Integer fieldSemester) {
	this.semester = fieldSemester;
}

@Column(name = "sequence", nullable = false, length = 10)
public java.lang.Integer getSequence() {
	return this.sequence;
}

public void setSequence(java.lang.Integer fieldSequence) {
	this.sequence = fieldSequence;
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

@Column(name = "removed_by", nullable = true, length = 10)
public java.lang.Integer getRemoved_by() {
	return this.removed_by;
}

public void setRemoved_by(java.lang.Integer fieldRemovedBy) {
	this.removed_by = fieldRemovedBy;
}

@Column(name = "removed_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getRemoved_date() {
	return this.removed_date;
}

public void setRemoved_date(java.util.Date fieldRemovedDate) {
	this.removed_date = fieldRemovedDate;
}

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
