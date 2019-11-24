// SQL_db: cictems
// SQL_table: otp_generator
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
@Table(name = "otp_generator", catalog = "cictems")
public class OtpGeneratorMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.String code;
private java.util.Date date_created;
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

@Column(name = "code", nullable = false, length = 150)
public java.lang.String getCode() {
	return this.code;
}

public void setCode(java.lang.String fieldCode) {
	this.code = fieldCode;
}

@Column(name = "date_created", nullable = false, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getDate_created() {
	return this.date_created;
}

public void setDate_created(java.util.Date fieldDateCreated) {
	this.date_created = fieldDateCreated;
}

@Column(name = "active", nullable = false, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public OtpGeneratorMapping copy() {
OtpGeneratorMapping copyMe = new OtpGeneratorMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.code = this.code;
copyMe.date_created = this.date_created;
copyMe.active = this.active;
return copyMe;
}

}
