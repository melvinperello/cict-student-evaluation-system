// SQL_db: cictems
// SQL_table: load_group
// Mono Models
// Monosync Framewrok v9.08.16
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
@Table(name = "load_group", catalog = "cictems")
public class LoadGroupMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.Integer SUBJECT_id;
private java.lang.Integer LOADSEC_id;
private java.lang.Integer faculty;
private java.lang.String group_type;
private java.util.Date added_date;
private java.lang.Integer added_by;
private java.util.Date removed_date;
private java.lang.Integer removed_by;
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

@Column(name = "SUBJECT_id", nullable = true, length = 10)
public java.lang.Integer getSUBJECT_id() {
	return this.SUBJECT_id;
}

public void setSUBJECT_id(java.lang.Integer fieldSubjectId) {
	this.SUBJECT_id = fieldSubjectId;
}

@Column(name = "LOADSEC_id", nullable = true, length = 10)
public java.lang.Integer getLOADSEC_id() {
	return this.LOADSEC_id;
}

public void setLOADSEC_id(java.lang.Integer fieldLoadsecId) {
	this.LOADSEC_id = fieldLoadsecId;
}

@Column(name = "faculty", nullable = true, length = 10)
public java.lang.Integer getFaculty() {
	return this.faculty;
}

public void setFaculty(java.lang.Integer fieldFaculty) {
	this.faculty = fieldFaculty;
}

@Column(name = "group_type", nullable = true, length = 50)
public java.lang.String getGroup_type() {
	return this.group_type;
}

public void setGroup_type(java.lang.String fieldGroupType) {
	this.group_type = fieldGroupType;
}

@Column(name = "added_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getAdded_date() {
	return this.added_date;
}

public void setAdded_date(java.util.Date fieldAddedDate) {
	this.added_date = fieldAddedDate;
}

@Column(name = "added_by", nullable = true, length = 10)
public java.lang.Integer getAdded_by() {
	return this.added_by;
}

public void setAdded_by(java.lang.Integer fieldAddedBy) {
	this.added_by = fieldAddedBy;
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

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Column(name = "archived", nullable = true, length = 10)
public java.lang.Integer getArchived() {
	return this.archived;
}

public void setArchived(java.lang.Integer fieldArchived) {
	this.archived = fieldArchived;
}

@Override
public LoadGroupMapping copy() {
LoadGroupMapping copyMe = new LoadGroupMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.SUBJECT_id = this.SUBJECT_id;
copyMe.LOADSEC_id = this.LOADSEC_id;
copyMe.faculty = this.faculty;
copyMe.group_type = this.group_type;
copyMe.added_date = this.added_date;
copyMe.added_by = this.added_by;
copyMe.removed_date = this.removed_date;
copyMe.removed_by = this.removed_by;
copyMe.active = this.active;
copyMe.archived = this.archived;
return copyMe;
}

}
