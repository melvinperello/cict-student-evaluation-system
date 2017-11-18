// SQL_db: cictems
// SQL_table: curriculum_history_summary
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
@Table(name = "curriculum_history_summary", catalog = "cictems")
public class CurriculumHistorySummaryMapping implements java.io.Serializable {


private java.lang.Integer history_id;
private java.lang.Integer curriculum_id;
private java.lang.String description;
private java.lang.Integer created_by;
private java.util.Date created_date;
private java.lang.Integer active;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "history_id", nullable = false, length = 10)
public java.lang.Integer getHistory_id() {
	return this.history_id;
}

public void setHistory_id(java.lang.Integer fieldHistoryId) {
	this.history_id = fieldHistoryId;
}

@Column(name = "curriculum_id", nullable = true, length = 10)
public java.lang.Integer getCurriculum_id() {
	return this.curriculum_id;
}

public void setCurriculum_id(java.lang.Integer fieldCurriculumId) {
	this.curriculum_id = fieldCurriculumId;
}

@Column(name = "description", nullable = true, length = 300)
public java.lang.String getDescription() {
	return this.description;
}

public void setDescription(java.lang.String fieldDescription) {
	this.description = fieldDescription;
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

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
