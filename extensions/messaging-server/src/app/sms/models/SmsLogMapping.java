// SQL_db: sms_server
// SQL_table: sms_log
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
@Table(name = "sms_log", catalog = "sms_server")
public class SmsLogMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.String gateway_id;
private java.lang.String message_uuid;
private java.util.Date date;
private java.lang.String recipient;
private java.util.Date dispatch_date;
private java.lang.String message_status;
private java.lang.String failure_cause;
private java.lang.String message_body;
private java.lang.String pdu_data;
private java.util.Date entry_date;
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

@Column(name = "gateway_id", nullable = true, length = 150)
public java.lang.String getGateway_id() {
	return this.gateway_id;
}

public void setGateway_id(java.lang.String fieldGatewayId) {
	this.gateway_id = fieldGatewayId;
}

@Column(name = "message_uuid", nullable = true, length = 200)
public java.lang.String getMessage_uuid() {
	return this.message_uuid;
}

public void setMessage_uuid(java.lang.String fieldMessageUuid) {
	this.message_uuid = fieldMessageUuid;
}

@Column(name = "date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getDate() {
	return this.date;
}

public void setDate(java.util.Date fieldDate) {
	this.date = fieldDate;
}

@Column(name = "recipient", nullable = true, length = 50)
public java.lang.String getRecipient() {
	return this.recipient;
}

public void setRecipient(java.lang.String fieldRecipient) {
	this.recipient = fieldRecipient;
}

@Column(name = "dispatch_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getDispatch_date() {
	return this.dispatch_date;
}

public void setDispatch_date(java.util.Date fieldDispatchDate) {
	this.dispatch_date = fieldDispatchDate;
}

@Column(name = "message_status", nullable = true, length = 50)
public java.lang.String getMessage_status() {
	return this.message_status;
}

public void setMessage_status(java.lang.String fieldMessageStatus) {
	this.message_status = fieldMessageStatus;
}

@Column(name = "failure_cause", nullable = true, length = 100)
public java.lang.String getFailure_cause() {
	return this.failure_cause;
}

public void setFailure_cause(java.lang.String fieldFailureCause) {
	this.failure_cause = fieldFailureCause;
}

@Column(name = "message_body", nullable = true, length = 160)
public java.lang.String getMessage_body() {
	return this.message_body;
}

public void setMessage_body(java.lang.String fieldMessageBody) {
	this.message_body = fieldMessageBody;
}

@Column(name = "pdu_data", nullable = true, length = 200)
public java.lang.String getPdu_data() {
	return this.pdu_data;
}

public void setPdu_data(java.lang.String fieldPduData) {
	this.pdu_data = fieldPduData;
}

@Column(name = "entry_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getEntry_date() {
	return this.entry_date;
}

public void setEntry_date(java.util.Date fieldEntryDate) {
	this.entry_date = fieldEntryDate;
}

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public SmsLogMapping copy() {
SmsLogMapping copyMe = new SmsLogMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.gateway_id = this.gateway_id;
copyMe.message_uuid = this.message_uuid;
copyMe.date = this.date;
copyMe.recipient = this.recipient;
copyMe.dispatch_date = this.dispatch_date;
copyMe.message_status = this.message_status;
copyMe.failure_cause = this.failure_cause;
copyMe.message_body = this.message_body;
copyMe.pdu_data = this.pdu_data;
copyMe.entry_date = this.entry_date;
copyMe.active = this.active;
return copyMe;
}

}
