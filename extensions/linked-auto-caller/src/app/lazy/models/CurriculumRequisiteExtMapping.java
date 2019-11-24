// SQL_db: cictems
// SQL_table: curriculum_requisite_ext
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
@Table(name = "curriculum_requisite_ext", catalog = "cictems")
public class CurriculumRequisiteExtMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.Integer SUBJECT_id_get;
private java.lang.Integer SUBJECT_id_req;
private java.lang.Integer CURRICULUM_id;
private java.lang.String type;
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

@Column(name = "SUBJECT_id_get", nullable = true, length = 10)
public java.lang.Integer getSUBJECT_id_get() {
	return this.SUBJECT_id_get;
}

public void setSUBJECT_id_get(java.lang.Integer fieldSubjectIdGet) {
	this.SUBJECT_id_get = fieldSubjectIdGet;
}

@Column(name = "SUBJECT_id_req", nullable = true, length = 10)
public java.lang.Integer getSUBJECT_id_req() {
	return this.SUBJECT_id_req;
}

public void setSUBJECT_id_req(java.lang.Integer fieldSubjectIdReq) {
	this.SUBJECT_id_req = fieldSubjectIdReq;
}

@Column(name = "CURRICULUM_id", nullable = true, length = 10)
public java.lang.Integer getCURRICULUM_id() {
	return this.CURRICULUM_id;
}

public void setCURRICULUM_id(java.lang.Integer fieldCurriculumId) {
	this.CURRICULUM_id = fieldCurriculumId;
}

@Column(name = "type", nullable = false, length = 50)
public java.lang.String getType() {
	return this.type;
}

public void setType(java.lang.String fieldType) {
	this.type = fieldType;
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

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public CurriculumRequisiteExtMapping copy() {
CurriculumRequisiteExtMapping copyMe = new CurriculumRequisiteExtMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.SUBJECT_id_get = this.SUBJECT_id_get;
copyMe.SUBJECT_id_req = this.SUBJECT_id_req;
copyMe.CURRICULUM_id = this.CURRICULUM_id;
copyMe.type = this.type;
copyMe.added_by = this.added_by;
copyMe.added_date = this.added_date;
copyMe.removed_by = this.removed_by;
copyMe.removed_date = this.removed_date;
copyMe.active = this.active;
return copyMe;
}

}
