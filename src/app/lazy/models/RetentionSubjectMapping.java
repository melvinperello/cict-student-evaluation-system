// SQL_db: cictems
// SQL_table: retention_subject
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
@Table(name = "retention_subject", catalog = "cictems")
public class RetentionSubjectMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.Integer retention_id;
private java.lang.String subject_code;
private java.lang.String subject_title;
private java.lang.String units;
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

@Column(name = "retention_id", nullable = true, length = 10)
public java.lang.Integer getRetention_id() {
	return this.retention_id;
}

public void setRetention_id(java.lang.Integer fieldRetentionId) {
	this.retention_id = fieldRetentionId;
}

@Column(name = "subject_code", nullable = true, length = 50)
public java.lang.String getSubject_code() {
	return this.subject_code;
}

public void setSubject_code(java.lang.String fieldSubjectCode) {
	this.subject_code = fieldSubjectCode;
}

@Column(name = "subject_title", nullable = true, length = 50)
public java.lang.String getSubject_title() {
	return this.subject_title;
}

public void setSubject_title(java.lang.String fieldSubjectTitle) {
	this.subject_title = fieldSubjectTitle;
}

@Column(name = "units", nullable = true, length = 50)
public java.lang.String getUnits() {
	return this.units;
}

public void setUnits(java.lang.String fieldUnits) {
	this.units = fieldUnits;
}

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public RetentionSubjectMapping copy() {
RetentionSubjectMapping copyMe = new RetentionSubjectMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.retention_id = this.retention_id;
copyMe.subject_code = this.subject_code;
copyMe.subject_title = this.subject_title;
copyMe.units = this.units;
copyMe.active = this.active;
return copyMe;
}

}
