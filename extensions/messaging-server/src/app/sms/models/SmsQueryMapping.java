// SQL_db: sms_server
// SQL_table: sms_query
// Mono Models
// Monosync Framewrok v9.08.16
// Generated using LazyMono
// This code is computer generated, do not modify
// Author: Jhon Melvin Nieto Perello
// Contact: jhmvinperello@gmail.com
//
// The Framework uses Hibernate as its ORM
// For more information about Hibernate visit hibernate.org

package app.sms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "sms_query", catalog = "sms_server")
public class SmsQueryMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.String reciepients_number;
private java.lang.String message_body;
private java.lang.String sender_name;
private java.util.Date date;
private java.lang.Integer status;
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

@Column(name = "reciepients_number", nullable = true, length = 50)
public java.lang.String getReciepients_number() {
	return this.reciepients_number;
}

public void setReciepients_number(java.lang.String fieldReciepientsNumber) {
	this.reciepients_number = fieldReciepientsNumber;
}

@Column(name = "message_body", nullable = true, length = 160)
public java.lang.String getMessage_body() {
	return this.message_body;
}

public void setMessage_body(java.lang.String fieldMessageBody) {
	this.message_body = fieldMessageBody;
}

@Column(name = "sender_name", nullable = true, length = 50)
public java.lang.String getSender_name() {
	return this.sender_name;
}

public void setSender_name(java.lang.String fieldSenderName) {
	this.sender_name = fieldSenderName;
}

@Column(name = "date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getDate() {
	return this.date;
}

public void setDate(java.util.Date fieldDate) {
	this.date = fieldDate;
}

@Column(name = "status", nullable = true, length = 10)
public java.lang.Integer getStatus() {
	return this.status;
}

public void setStatus(java.lang.Integer fieldStatus) {
	this.status = fieldStatus;
}

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public SmsQueryMapping copy() {
SmsQueryMapping copyMe = new SmsQueryMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.reciepients_number = this.reciepients_number;
copyMe.message_body = this.message_body;
copyMe.sender_name = this.sender_name;
copyMe.date = this.date;
copyMe.status = this.status;
copyMe.active = this.active;
return copyMe;
}

}
