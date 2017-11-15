// SQL_db: cictems
// SQL_table: account_faculty
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 15, 2017 01:44:42 PM
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
@Table(name = "account_faculty", catalog = "cictems")
public class AccountFacultyMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer FACULTY_id;
private java.lang.String username;
private java.lang.String password;
private java.lang.String transaction_pin;
private java.lang.String recovery_question;
private java.lang.String recovery_answer;
private java.lang.String access_level;
private java.lang.Integer blocked_count;
private java.util.Date disabled_since;
private java.lang.Integer wrong_count;
private java.util.Date blocked_until;
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

@Column(name = "FACULTY_id", nullable = true, length = 10)
public java.lang.Integer getFACULTY_id() {
	return this.FACULTY_id;
}

public void setFACULTY_id(java.lang.Integer fieldFacultyId) {
	this.FACULTY_id = fieldFacultyId;
}

@Column(name = "username", nullable = true, length = 50)
public java.lang.String getUsername() {
	return this.username;
}

public void setUsername(java.lang.String fieldUsername) {
	this.username = fieldUsername;
}

@Column(name = "password", nullable = true, length = 150)
public java.lang.String getPassword() {
	return this.password;
}

public void setPassword(java.lang.String fieldPassword) {
	this.password = fieldPassword;
}

@Column(name = "transaction_pin", nullable = true, length = 150)
public java.lang.String getTransaction_pin() {
	return this.transaction_pin;
}

public void setTransaction_pin(java.lang.String fieldTransactionPin) {
	this.transaction_pin = fieldTransactionPin;
}

@Column(name = "recovery_question", nullable = true, length = 50)
public java.lang.String getRecovery_question() {
	return this.recovery_question;
}

public void setRecovery_question(java.lang.String fieldRecoveryQuestion) {
	this.recovery_question = fieldRecoveryQuestion;
}

@Column(name = "recovery_answer", nullable = true, length = 150)
public java.lang.String getRecovery_answer() {
	return this.recovery_answer;
}

public void setRecovery_answer(java.lang.String fieldRecoveryAnswer) {
	this.recovery_answer = fieldRecoveryAnswer;
}

@Column(name = "access_level", nullable = true, length = 200)
public java.lang.String getAccess_level() {
	return this.access_level;
}

public void setAccess_level(java.lang.String fieldAccessLevel) {
	this.access_level = fieldAccessLevel;
}

@Column(name = "blocked_count", nullable = true, length = 10)
public java.lang.Integer getBlocked_count() {
	return this.blocked_count;
}

public void setBlocked_count(java.lang.Integer fieldBlockedCount) {
	this.blocked_count = fieldBlockedCount;
}

@Column(name = "disabled_since", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getDisabled_since() {
	return this.disabled_since;
}

public void setDisabled_since(java.util.Date fieldDisabledSince) {
	this.disabled_since = fieldDisabledSince;
}

@Column(name = "wrong_count", nullable = true, length = 10)
public java.lang.Integer getWrong_count() {
	return this.wrong_count;
}

public void setWrong_count(java.lang.Integer fieldWrongCount) {
	this.wrong_count = fieldWrongCount;
}

@Column(name = "blocked_until", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getBlocked_until() {
	return this.blocked_until;
}

public void setBlocked_until(java.util.Date fieldBlockedUntil) {
	this.blocked_until = fieldBlockedUntil;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
