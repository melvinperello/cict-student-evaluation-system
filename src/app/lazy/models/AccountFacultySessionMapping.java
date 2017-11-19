// SQL_db: cictems
// SQL_table: account_faculty_session
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 19, 2017 02:01:21 PM
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
@Table(name = "account_faculty_session", catalog = "cictems")
public class AccountFacultySessionMapping implements java.io.Serializable {


private java.lang.Integer session_id;
private java.lang.Integer FACULTY_account_id;
private java.util.Date session_start;
private java.util.Date keep_alive;
private java.lang.String ip_address;
private java.lang.String pc_name;
private java.lang.String pc_username;
private java.lang.String os;
private java.util.Date session_end;
private java.lang.String platform;
private java.lang.Integer active;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "session_id", nullable = false, length = 10)
public java.lang.Integer getSession_id() {
	return this.session_id;
}

public void setSession_id(java.lang.Integer fieldSessionId) {
	this.session_id = fieldSessionId;
}

@Column(name = "FACULTY_account_id", nullable = false, length = 10)
public java.lang.Integer getFACULTY_account_id() {
	return this.FACULTY_account_id;
}

public void setFACULTY_account_id(java.lang.Integer fieldFacultyAccountId) {
	this.FACULTY_account_id = fieldFacultyAccountId;
}

@Column(name = "session_start", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getSession_start() {
	return this.session_start;
}

public void setSession_start(java.util.Date fieldSessionStart) {
	this.session_start = fieldSessionStart;
}

@Column(name = "keep_alive", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getKeep_alive() {
	return this.keep_alive;
}

public void setKeep_alive(java.util.Date fieldKeepAlive) {
	this.keep_alive = fieldKeepAlive;
}

@Column(name = "ip_address", nullable = true, length = 200)
public java.lang.String getIp_address() {
	return this.ip_address;
}

public void setIp_address(java.lang.String fieldIpAddress) {
	this.ip_address = fieldIpAddress;
}

@Column(name = "pc_name", nullable = true, length = 50)
public java.lang.String getPc_name() {
	return this.pc_name;
}

public void setPc_name(java.lang.String fieldPcName) {
	this.pc_name = fieldPcName;
}

@Column(name = "pc_username", nullable = true, length = 50)
public java.lang.String getPc_username() {
	return this.pc_username;
}

public void setPc_username(java.lang.String fieldPcUsername) {
	this.pc_username = fieldPcUsername;
}

@Column(name = "os", nullable = true, length = 50)
public java.lang.String getOs() {
	return this.os;
}

public void setOs(java.lang.String fieldOs) {
	this.os = fieldOs;
}

@Column(name = "session_end", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getSession_end() {
	return this.session_end;
}

public void setSession_end(java.util.Date fieldSessionEnd) {
	this.session_end = fieldSessionEnd;
}

@Column(name = "platform", nullable = true, length = 50)
public java.lang.String getPlatform() {
	return this.platform;
}

public void setPlatform(java.lang.String fieldPlatform) {
	this.platform = fieldPlatform;
}

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
