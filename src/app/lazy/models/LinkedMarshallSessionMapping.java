// SQL_db: cictems
// SQL_table: linked_marshall_session
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 08, 2017 10:22:14 PM
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
@Table(name = "linked_marshall_session", catalog = "cictems")
public class LinkedMarshallSessionMapping implements java.io.Serializable {


private java.lang.Integer ses_id;
private java.lang.Integer cict_id;
private java.lang.Integer account_id;
private java.lang.String name;
private java.lang.String org;
private java.lang.String imei;
private java.util.Date session_start;
private java.util.Date session_end;
private java.lang.String status;
private java.lang.Integer active;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "ses_id", nullable = false, length = 10)
public java.lang.Integer getSes_id() {
	return this.ses_id;
}

public void setSes_id(java.lang.Integer fieldSesId) {
	this.ses_id = fieldSesId;
}

@Column(name = "cict_id", nullable = true, length = 10)
public java.lang.Integer getCict_id() {
	return this.cict_id;
}

public void setCict_id(java.lang.Integer fieldCictId) {
	this.cict_id = fieldCictId;
}

@Column(name = "account_id", nullable = true, length = 10)
public java.lang.Integer getAccount_id() {
	return this.account_id;
}

public void setAccount_id(java.lang.Integer fieldAccountId) {
	this.account_id = fieldAccountId;
}

@Column(name = "name", nullable = true, length = 50)
public java.lang.String getName() {
	return this.name;
}

public void setName(java.lang.String fieldName) {
	this.name = fieldName;
}

@Column(name = "org", nullable = true, length = 50)
public java.lang.String getOrg() {
	return this.org;
}

public void setOrg(java.lang.String fieldOrg) {
	this.org = fieldOrg;
}

@Column(name = "imei", nullable = true, length = 100)
public java.lang.String getImei() {
	return this.imei;
}

public void setImei(java.lang.String fieldImei) {
	this.imei = fieldImei;
}

@Column(name = "session_start", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getSession_start() {
	return this.session_start;
}

public void setSession_start(java.util.Date fieldSessionStart) {
	this.session_start = fieldSessionStart;
}

@Column(name = "session_end", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getSession_end() {
	return this.session_end;
}

public void setSession_end(java.util.Date fieldSessionEnd) {
	this.session_end = fieldSessionEnd;
}

@Column(name = "status", nullable = true, length = 50)
public java.lang.String getStatus() {
	return this.status;
}

public void setStatus(java.lang.String fieldStatus) {
	this.status = fieldStatus;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
