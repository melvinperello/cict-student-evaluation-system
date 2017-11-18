// SQL_db: cictems
// SQL_table: account_faculty_attempt
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
@Table(name = "account_faculty_attempt", catalog = "cictems")
public class AccountFacultyAttemptMapping implements java.io.Serializable {


private java.lang.Integer try_id;
private java.lang.Integer account_id;
private java.util.Date time;
private java.lang.String ip_address;
private java.lang.String pc_name;
private java.lang.String pc_username;
private java.lang.String os_version;
private java.lang.String platform;
private java.lang.String result;
private java.lang.Integer active;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "try_id", nullable = false, length = 10)
public java.lang.Integer getTry_id() {
	return this.try_id;
}

public void setTry_id(java.lang.Integer fieldTryId) {
	this.try_id = fieldTryId;
}

@Column(name = "account_id", nullable = true, length = 10)
public java.lang.Integer getAccount_id() {
	return this.account_id;
}

public void setAccount_id(java.lang.Integer fieldAccountId) {
	this.account_id = fieldAccountId;
}

@Column(name = "time", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getTime() {
	return this.time;
}

public void setTime(java.util.Date fieldTime) {
	this.time = fieldTime;
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

@Column(name = "os_version", nullable = true, length = 50)
public java.lang.String getOs_version() {
	return this.os_version;
}

public void setOs_version(java.lang.String fieldOsVersion) {
	this.os_version = fieldOsVersion;
}

@Column(name = "platform", nullable = true, length = 50)
public java.lang.String getPlatform() {
	return this.platform;
}

public void setPlatform(java.lang.String fieldPlatform) {
	this.platform = fieldPlatform;
}

@Column(name = "result", nullable = true, length = 50)
public java.lang.String getResult() {
	return this.result;
}

public void setResult(java.lang.String fieldResult) {
	this.result = fieldResult;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
