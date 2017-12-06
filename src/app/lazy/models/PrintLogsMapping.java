// SQL_db: cictems
// SQL_table: print_logs
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
@Table(name = "print_logs", catalog = "cictems")
public class PrintLogsMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.Integer STUDENT_id;
private java.lang.Integer EVALUATION_id;
private java.lang.String title;
private java.lang.String module;
private java.lang.String type;
private java.lang.String terminal;
private java.lang.Integer printed_by;
private java.util.Date printed_date;
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

@Column(name = "EVALUATION_id", nullable = true, length = 10)
public java.lang.Integer getEVALUATION_id() {
	return this.EVALUATION_id;
}

public void setEVALUATION_id(java.lang.Integer fieldEvaluationId) {
	this.EVALUATION_id = fieldEvaluationId;
}

@Column(name = "title", nullable = false, length = 100)
public java.lang.String getTitle() {
	return this.title;
}

public void setTitle(java.lang.String fieldTitle) {
	this.title = fieldTitle;
}

@Column(name = "module", nullable = false, length = 100)
public java.lang.String getModule() {
	return this.module;
}

public void setModule(java.lang.String fieldModule) {
	this.module = fieldModule;
}

@Column(name = "type", nullable = false, length = 100)
public java.lang.String getType() {
	return this.type;
}

public void setType(java.lang.String fieldType) {
	this.type = fieldType;
}

@Column(name = "terminal", nullable = false, length = 200)
public java.lang.String getTerminal() {
	return this.terminal;
}

public void setTerminal(java.lang.String fieldTerminal) {
	this.terminal = fieldTerminal;
}

@Column(name = "printed_by", nullable = false, length = 10)
public java.lang.Integer getPrinted_by() {
	return this.printed_by;
}

public void setPrinted_by(java.lang.Integer fieldPrintedBy) {
	this.printed_by = fieldPrintedBy;
}

@Column(name = "printed_date", nullable = false, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getPrinted_date() {
	return this.printed_date;
}

public void setPrinted_date(java.util.Date fieldPrintedDate) {
	this.printed_date = fieldPrintedDate;
}

@Column(name = "active", nullable = false, length = 10)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public PrintLogsMapping copy() {
PrintLogsMapping copyMe = new PrintLogsMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.STUDENT_id = this.STUDENT_id;
copyMe.EVALUATION_id = this.EVALUATION_id;
copyMe.title = this.title;
copyMe.module = this.module;
copyMe.type = this.type;
copyMe.terminal = this.terminal;
copyMe.printed_by = this.printed_by;
copyMe.printed_date = this.printed_date;
copyMe.active = this.active;
return copyMe;
}

}
