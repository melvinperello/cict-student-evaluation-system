// SQL_db: cictems
// SQL_table: linked_telemetry
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Oct 16, 2017 07:52:08 PM
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
@Table(name = "linked_telemetry", catalog = "cictems")
public class LinkedTelemetryMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.String class_name;
private java.lang.String method_name;
private java.lang.String line;
private java.lang.String classification;
private java.lang.String exception_type;
private java.lang.String description;
private java.lang.String student_number;
private java.lang.String student_name;
private java.lang.String version;
private java.lang.String build;
private java.lang.String model;
private java.lang.String board;
private java.lang.String brand;
private java.lang.String hardware;
private java.lang.String manufacturer;
private java.lang.String serial;
private java.util.Date date_submitted;
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

@Column(name = "class_name", nullable = false, length = 50)
public java.lang.String getClass_name() {
	return this.class_name;
}

public void setClass_name(java.lang.String fieldClassName) {
	this.class_name = fieldClassName;
}

@Column(name = "method_name", nullable = false, length = 50)
public java.lang.String getMethod_name() {
	return this.method_name;
}

public void setMethod_name(java.lang.String fieldMethodName) {
	this.method_name = fieldMethodName;
}

@Column(name = "line", nullable = false, length = 50)
public java.lang.String getLine() {
	return this.line;
}

public void setLine(java.lang.String fieldLine) {
	this.line = fieldLine;
}

@Column(name = "classification", nullable = false, length = 50)
public java.lang.String getClassification() {
	return this.classification;
}

public void setClassification(java.lang.String fieldClassification) {
	this.classification = fieldClassification;
}

@Column(name = "exception_type", nullable = false, length = 50)
public java.lang.String getException_type() {
	return this.exception_type;
}

public void setException_type(java.lang.String fieldExceptionType) {
	this.exception_type = fieldExceptionType;
}

@Column(name = "description", nullable = false, length = 200)
public java.lang.String getDescription() {
	return this.description;
}

public void setDescription(java.lang.String fieldDescription) {
	this.description = fieldDescription;
}

@Column(name = "student_number", nullable = false, length = 30)
public java.lang.String getStudent_number() {
	return this.student_number;
}

public void setStudent_number(java.lang.String fieldStudentNumber) {
	this.student_number = fieldStudentNumber;
}

@Column(name = "student_name", nullable = false, length = 100)
public java.lang.String getStudent_name() {
	return this.student_name;
}

public void setStudent_name(java.lang.String fieldStudentName) {
	this.student_name = fieldStudentName;
}

@Column(name = "version", nullable = false, length = 100)
public java.lang.String getVersion() {
	return this.version;
}

public void setVersion(java.lang.String fieldVersion) {
	this.version = fieldVersion;
}

@Column(name = "build", nullable = false, length = 100)
public java.lang.String getBuild() {
	return this.build;
}

public void setBuild(java.lang.String fieldBuild) {
	this.build = fieldBuild;
}

@Column(name = "model", nullable = false, length = 100)
public java.lang.String getModel() {
	return this.model;
}

public void setModel(java.lang.String fieldModel) {
	this.model = fieldModel;
}

@Column(name = "board", nullable = false, length = 100)
public java.lang.String getBoard() {
	return this.board;
}

public void setBoard(java.lang.String fieldBoard) {
	this.board = fieldBoard;
}

@Column(name = "brand", nullable = false, length = 100)
public java.lang.String getBrand() {
	return this.brand;
}

public void setBrand(java.lang.String fieldBrand) {
	this.brand = fieldBrand;
}

@Column(name = "hardware", nullable = false, length = 100)
public java.lang.String getHardware() {
	return this.hardware;
}

public void setHardware(java.lang.String fieldHardware) {
	this.hardware = fieldHardware;
}

@Column(name = "manufacturer", nullable = false, length = 100)
public java.lang.String getManufacturer() {
	return this.manufacturer;
}

public void setManufacturer(java.lang.String fieldManufacturer) {
	this.manufacturer = fieldManufacturer;
}

@Column(name = "serial", nullable = false, length = 100)
public java.lang.String getSerial() {
	return this.serial;
}

public void setSerial(java.lang.String fieldSerial) {
	this.serial = fieldSerial;
}

@Column(name = "date_submitted", nullable = false, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getDate_submitted() {
	return this.date_submitted;
}

public void setDate_submitted(java.util.Date fieldDateSubmitted) {
	this.date_submitted = fieldDateSubmitted;
}

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

}
