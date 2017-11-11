// SQL_db: cictems
// SQL_table: account_student
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 08, 2017 05:40:36 PM
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
@Table(name = "account_student", catalog = "cictems")
public class AccountStudentMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer STUDENT_id;
private java.lang.String username;
private java.lang.String password;
private java.lang.String recovery_question;
private java.lang.String recovery_answer;
private java.lang.String access_level;
private java.lang.String affiliates;
private java.lang.Integer state;
private java.lang.Integer tries;
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

@Column(name = "STUDENT_id", nullable = true, length = 10)
public java.lang.Integer getSTUDENT_id() {
	return this.STUDENT_id;
}

public void setSTUDENT_id(java.lang.Integer fieldStudentId) {
	this.STUDENT_id = fieldStudentId;
}

@Column(name = "username", nullable = true, length = 50)
public java.lang.String getUsername() {
	return this.username;
}

public void setUsername(java.lang.String fieldUsername) {
	this.username = fieldUsername;
}

@Column(name = "password", nullable = true, length = 50)
public java.lang.String getPassword() {
	return this.password;
}

public void setPassword(java.lang.String fieldPassword) {
	this.password = fieldPassword;
}

@Column(name = "recovery_question", nullable = true, length = 100)
public java.lang.String getRecovery_question() {
	return this.recovery_question;
}

public void setRecovery_question(java.lang.String fieldRecoveryQuestion) {
	this.recovery_question = fieldRecoveryQuestion;
}

@Column(name = "recovery_answer", nullable = true, length = 100)
public java.lang.String getRecovery_answer() {
	return this.recovery_answer;
}

public void setRecovery_answer(java.lang.String fieldRecoveryAnswer) {
	this.recovery_answer = fieldRecoveryAnswer;
}

@Column(name = "access_level", nullable = true, length = 100)
public java.lang.String getAccess_level() {
	return this.access_level;
}

public void setAccess_level(java.lang.String fieldAccessLevel) {
	this.access_level = fieldAccessLevel;
}

@Column(name = "affiliates", nullable = true, length = 100)
public java.lang.String getAffiliates() {
	return this.affiliates;
}

public void setAffiliates(java.lang.String fieldAffiliates) {
	this.affiliates = fieldAffiliates;
}

@Column(name = "state", nullable = true, length = 10)
public java.lang.Integer getState() {
	return this.state;
}

public void setState(java.lang.Integer fieldState) {
	this.state = fieldState;
}

@Column(name = "tries", nullable = true, length = 10)
public java.lang.Integer getTries() {
	return this.tries;
}

public void setTries(java.lang.Integer fieldTries) {
	this.tries = fieldTries;
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
