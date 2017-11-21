// SQL_db: cictems
// SQL_table: linked_pila_4f
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 21, 2017 10:36:17 PM
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
@Table(name = "linked_pila_4f", catalog = "cictems")
public class LinkedPila4fMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.Integer pila_id;
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

@Column(name = "pila_id", nullable = true, length = 10)
public java.lang.Integer getPila_id() {
	return this.pila_id;
}

public void setPila_id(java.lang.Integer fieldPilaId) {
	this.pila_id = fieldPilaId;
}

@Column(name = "active", nullable = true, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
