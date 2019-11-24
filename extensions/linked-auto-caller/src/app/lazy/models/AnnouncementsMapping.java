// SQL_db: cictems
// SQL_table: announcements
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
@Table(name = "announcements", catalog = "cictems")
public class AnnouncementsMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.String title;
private java.lang.String message;
private java.util.Date date;
private java.lang.Integer announced_by;
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

@Column(name = "title", nullable = true, length = 50)
public java.lang.String getTitle() {
	return this.title;
}

public void setTitle(java.lang.String fieldTitle) {
	this.title = fieldTitle;
}

@Column(name = "message", nullable = true, length = 300)
public java.lang.String getMessage() {
	return this.message;
}

public void setMessage(java.lang.String fieldMessage) {
	this.message = fieldMessage;
}

@Column(name = "date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getDate() {
	return this.date;
}

public void setDate(java.util.Date fieldDate) {
	this.date = fieldDate;
}

@Column(name = "announced_by", nullable = true, length = 10)
public java.lang.Integer getAnnounced_by() {
	return this.announced_by;
}

public void setAnnounced_by(java.lang.Integer fieldAnnouncedBy) {
	this.announced_by = fieldAnnouncedBy;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public AnnouncementsMapping copy() {
AnnouncementsMapping copyMe = new AnnouncementsMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.title = this.title;
copyMe.message = this.message;
copyMe.date = this.date;
copyMe.announced_by = this.announced_by;
copyMe.active = this.active;
return copyMe;
}

}
