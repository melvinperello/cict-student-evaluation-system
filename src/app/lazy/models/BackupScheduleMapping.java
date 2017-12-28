// SQL_db: cictems
// SQL_table: backup_schedule
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
@Table(name = "backup_schedule", catalog = "cictems")
public class BackupScheduleMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.String time;
private java.lang.Integer created_by;
private java.util.Date created_date;
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

@Column(name = "time", nullable = true, length = 50)
public java.lang.String getTime() {
	return this.time;
}

public void setTime(java.lang.String fieldTime) {
	this.time = fieldTime;
}

@Column(name = "created_by", nullable = true, length = 10)
public java.lang.Integer getCreated_by() {
	return this.created_by;
}

public void setCreated_by(java.lang.Integer fieldCreatedBy) {
	this.created_by = fieldCreatedBy;
}

@Column(name = "created_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getCreated_date() {
	return this.created_date;
}

public void setCreated_date(java.util.Date fieldCreatedDate) {
	this.created_date = fieldCreatedDate;
}

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public BackupScheduleMapping copy() {
BackupScheduleMapping copyMe = new BackupScheduleMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.time = this.time;
copyMe.created_by = this.created_by;
copyMe.created_date = this.created_date;
copyMe.active = this.active;
return copyMe;
}

}
