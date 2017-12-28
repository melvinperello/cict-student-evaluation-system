// SQL_db: cictems
// SQL_table: backup_logs
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
@Table(name = "backup_logs", catalog = "cictems")
public class BackupLogsMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.util.Date time_executed;
private java.lang.String backup_result;
private java.lang.Integer executed_with;
private java.lang.String executed_on;
private java.lang.String backup_mode;
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

@Column(name = "time_executed", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getTime_executed() {
	return this.time_executed;
}

public void setTime_executed(java.util.Date fieldTimeExecuted) {
	this.time_executed = fieldTimeExecuted;
}

@Column(name = "backup_result", nullable = true, length = 50)
public java.lang.String getBackup_result() {
	return this.backup_result;
}

public void setBackup_result(java.lang.String fieldBackupResult) {
	this.backup_result = fieldBackupResult;
}

@Column(name = "executed_with", nullable = true, length = 10)
public java.lang.Integer getExecuted_with() {
	return this.executed_with;
}

public void setExecuted_with(java.lang.Integer fieldExecutedWith) {
	this.executed_with = fieldExecutedWith;
}

@Column(name = "executed_on", nullable = true, length = 50)
public java.lang.String getExecuted_on() {
	return this.executed_on;
}

public void setExecuted_on(java.lang.String fieldExecutedOn) {
	this.executed_on = fieldExecutedOn;
}

@Column(name = "backup_mode", nullable = true, length = 50)
public java.lang.String getBackup_mode() {
	return this.backup_mode;
}

public void setBackup_mode(java.lang.String fieldBackupMode) {
	this.backup_mode = fieldBackupMode;
}

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public BackupLogsMapping copy() {
BackupLogsMapping copyMe = new BackupLogsMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.time_executed = this.time_executed;
copyMe.backup_result = this.backup_result;
copyMe.executed_with = this.executed_with;
copyMe.executed_on = this.executed_on;
copyMe.backup_mode = this.backup_mode;
copyMe.active = this.active;
return copyMe;
}

}
