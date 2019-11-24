// SQL_db: sms_server
// SQL_table: exception_log
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
@Table(name = "exception_log", catalog = "sms_server")
public class ExceptionLogMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.util.Date date;
private java.lang.Integer count;
private java.lang.String message;
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

@Column(name = "date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getDate() {
	return this.date;
}

public void setDate(java.util.Date fieldDate) {
	this.date = fieldDate;
}

@Column(name = "count", nullable = true, length = 10)
public java.lang.Integer getCount() {
	return this.count;
}

public void setCount(java.lang.Integer fieldCount) {
	this.count = fieldCount;
}

@Column(name = "message", nullable = true, length = 500)
public java.lang.String getMessage() {
	return this.message;
}

public void setMessage(java.lang.String fieldMessage) {
	this.message = fieldMessage;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public ExceptionLogMapping copy() {
ExceptionLogMapping copyMe = new ExceptionLogMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.date = this.date;
copyMe.count = this.count;
copyMe.message = this.message;
copyMe.active = this.active;
return copyMe;
}

}
