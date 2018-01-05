// SQL_db: cictems
// SQL_table: retention_policy
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
@Table(name = "retention_policy", catalog = "cictems")
public class RetentionPolicyMapping implements java.io.Serializable, com.jhmvin.orm.MonoMapping {


private java.lang.Integer id;
private java.lang.Integer STUDENT_id;
private java.lang.String section;
private java.lang.Integer CURRICULUM_id;
private java.lang.String year_level;
private java.lang.String semester;
private java.lang.String academic_year;
private java.lang.String academic_semester;
private java.util.Date verification_date;
private java.lang.Integer verified_by;
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

@Column(name = "section", nullable = true, length = 50)
public java.lang.String getSection() {
	return this.section;
}

public void setSection(java.lang.String fieldSection) {
	this.section = fieldSection;
}

@Column(name = "CURRICULUM_id", nullable = true, length = 10)
public java.lang.Integer getCURRICULUM_id() {
	return this.CURRICULUM_id;
}

public void setCURRICULUM_id(java.lang.Integer fieldCurriculumId) {
	this.CURRICULUM_id = fieldCurriculumId;
}

@Column(name = "year_level", nullable = true, length = 50)
public java.lang.String getYear_level() {
	return this.year_level;
}

public void setYear_level(java.lang.String fieldYearLevel) {
	this.year_level = fieldYearLevel;
}

@Column(name = "semester", nullable = true, length = 50)
public java.lang.String getSemester() {
	return this.semester;
}

public void setSemester(java.lang.String fieldSemester) {
	this.semester = fieldSemester;
}

@Column(name = "academic_year", nullable = true, length = 50)
public java.lang.String getAcademic_year() {
	return this.academic_year;
}

public void setAcademic_year(java.lang.String fieldAcademicYear) {
	this.academic_year = fieldAcademicYear;
}

@Column(name = "academic_semester", nullable = true, length = 50)
public java.lang.String getAcademic_semester() {
	return this.academic_semester;
}

public void setAcademic_semester(java.lang.String fieldAcademicSemester) {
	this.academic_semester = fieldAcademicSemester;
}

@Column(name = "verification_date", nullable = true, length = 19)
@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
public java.util.Date getVerification_date() {
	return this.verification_date;
}

public void setVerification_date(java.util.Date fieldVerificationDate) {
	this.verification_date = fieldVerificationDate;
}

@Column(name = "verified_by", nullable = true, length = 10)
public java.lang.Integer getVerified_by() {
	return this.verified_by;
}

public void setVerified_by(java.lang.Integer fieldVerifiedBy) {
	this.verified_by = fieldVerifiedBy;
}

@Column(name = "active", nullable = true, length = 3)
public java.lang.Integer getActive() {
	return this.active;
}

public void setActive(java.lang.Integer fieldActive) {
	this.active = fieldActive;
}

@Override
public RetentionPolicyMapping copy() {
RetentionPolicyMapping copyMe = new RetentionPolicyMapping();
        /**
         * A.I. Field Do Not Copy.
         *
         * copyMe.id = this.id;
         */
copyMe.STUDENT_id = this.STUDENT_id;
copyMe.section = this.section;
copyMe.CURRICULUM_id = this.CURRICULUM_id;
copyMe.year_level = this.year_level;
copyMe.semester = this.semester;
copyMe.academic_year = this.academic_year;
copyMe.academic_semester = this.academic_semester;
copyMe.verification_date = this.verification_date;
copyMe.verified_by = this.verified_by;
copyMe.active = this.active;
return copyMe;
}

}
