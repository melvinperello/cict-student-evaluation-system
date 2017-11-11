// SQL_db: cictems
// SQL_table: faculty_profile
// Mono Models
// Monosync Framewrok v1.8.x
// Created: Nov 08, 2017 05:40:37 PM
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
@Table(name = "faculty_profile", catalog = "cictems")
public class FacultyProfileMapping implements java.io.Serializable {


private java.lang.Integer id;
private java.lang.String birthdate;
private java.lang.String place_of_birth;
private java.lang.String sex;
private java.lang.String civil_status;
private java.lang.String height;
private java.lang.String weight;
private java.lang.String blood_type;
private java.lang.String GSIS_id;
private java.lang.String PAGIBIG_id;
private java.lang.String PHILHEALTH_id;
private java.lang.String SSS_id;
private java.lang.String TIN_no;
private java.lang.String citizenship;
private java.lang.String dual_citizenship;
private java.lang.String dual_type;
private java.lang.String dual_country;
private java.lang.String res_house;
private java.lang.String res_street;
private java.lang.String res_village;
private java.lang.String res_brgy;
private java.lang.String res_city;
private java.lang.String res_province;
private java.lang.String res_zip;
private java.lang.String addr_house;
private java.lang.String addr_street;
private java.lang.String addr_village;
private java.lang.String addr_brgy;
private java.lang.String addr_city;
private java.lang.String addr_province;
private java.lang.String addr_zip;
private java.lang.String telephone;
private java.lang.String mobile;
private java.lang.String email;
private java.lang.String spouse_surname;
private java.lang.String spouse_firstname;
private java.lang.String spouse_middlename;
private java.lang.String spouse_name_ext;
private java.lang.String spouse_occupation;
private java.lang.String spouse_work_agency;
private java.lang.String spouse_work_address;
private java.lang.String spouse_telephone;
private java.lang.String father_surname;
private java.lang.String father_firstname;
private java.lang.String father_middlename;
private java.lang.String father_name_ext;
private java.lang.String mother_maidenname;
private java.lang.String mother_surname;
private java.lang.String mother_firstname;
private java.lang.String mother_middlename;

@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "id", nullable = false, length = 10)
public java.lang.Integer getId() {
	return this.id;
}

public void setId(java.lang.Integer fieldId) {
	this.id = fieldId;
}

@Column(name = "birthdate", nullable = true, length = 50)
public java.lang.String getBirthdate() {
	return this.birthdate;
}

public void setBirthdate(java.lang.String fieldBirthdate) {
	this.birthdate = fieldBirthdate;
}

@Column(name = "place_of_birth", nullable = true, length = 50)
public java.lang.String getPlace_of_birth() {
	return this.place_of_birth;
}

public void setPlace_of_birth(java.lang.String fieldPlaceOfBirth) {
	this.place_of_birth = fieldPlaceOfBirth;
}

@Column(name = "sex", nullable = true, length = 50)
public java.lang.String getSex() {
	return this.sex;
}

public void setSex(java.lang.String fieldSex) {
	this.sex = fieldSex;
}

@Column(name = "civil_status", nullable = true, length = 50)
public java.lang.String getCivil_status() {
	return this.civil_status;
}

public void setCivil_status(java.lang.String fieldCivilStatus) {
	this.civil_status = fieldCivilStatus;
}

@Column(name = "height", nullable = true, length = 50)
public java.lang.String getHeight() {
	return this.height;
}

public void setHeight(java.lang.String fieldHeight) {
	this.height = fieldHeight;
}

@Column(name = "weight", nullable = true, length = 50)
public java.lang.String getWeight() {
	return this.weight;
}

public void setWeight(java.lang.String fieldWeight) {
	this.weight = fieldWeight;
}

@Column(name = "blood_type", nullable = true, length = 50)
public java.lang.String getBlood_type() {
	return this.blood_type;
}

public void setBlood_type(java.lang.String fieldBloodType) {
	this.blood_type = fieldBloodType;
}

@Column(name = "GSIS_id", nullable = true, length = 50)
public java.lang.String getGSIS_id() {
	return this.GSIS_id;
}

public void setGSIS_id(java.lang.String fieldGsisId) {
	this.GSIS_id = fieldGsisId;
}

@Column(name = "PAGIBIG_id", nullable = true, length = 50)
public java.lang.String getPAGIBIG_id() {
	return this.PAGIBIG_id;
}

public void setPAGIBIG_id(java.lang.String fieldPagibigId) {
	this.PAGIBIG_id = fieldPagibigId;
}

@Column(name = "PHILHEALTH_id", nullable = true, length = 50)
public java.lang.String getPHILHEALTH_id() {
	return this.PHILHEALTH_id;
}

public void setPHILHEALTH_id(java.lang.String fieldPhilhealthId) {
	this.PHILHEALTH_id = fieldPhilhealthId;
}

@Column(name = "SSS_id", nullable = true, length = 50)
public java.lang.String getSSS_id() {
	return this.SSS_id;
}

public void setSSS_id(java.lang.String fieldSssId) {
	this.SSS_id = fieldSssId;
}

@Column(name = "TIN_no", nullable = true, length = 50)
public java.lang.String getTIN_no() {
	return this.TIN_no;
}

public void setTIN_no(java.lang.String fieldTinNo) {
	this.TIN_no = fieldTinNo;
}

@Column(name = "citizenship", nullable = true, length = 50)
public java.lang.String getCitizenship() {
	return this.citizenship;
}

public void setCitizenship(java.lang.String fieldCitizenship) {
	this.citizenship = fieldCitizenship;
}

@Column(name = "dual_citizenship", nullable = true, length = 50)
public java.lang.String getDual_citizenship() {
	return this.dual_citizenship;
}

public void setDual_citizenship(java.lang.String fieldDualCitizenship) {
	this.dual_citizenship = fieldDualCitizenship;
}

@Column(name = "dual_type", nullable = true, length = 50)
public java.lang.String getDual_type() {
	return this.dual_type;
}

public void setDual_type(java.lang.String fieldDualType) {
	this.dual_type = fieldDualType;
}

@Column(name = "dual_country", nullable = true, length = 50)
public java.lang.String getDual_country() {
	return this.dual_country;
}

public void setDual_country(java.lang.String fieldDualCountry) {
	this.dual_country = fieldDualCountry;
}

@Column(name = "res_house", nullable = true, length = 50)
public java.lang.String getRes_house() {
	return this.res_house;
}

public void setRes_house(java.lang.String fieldResHouse) {
	this.res_house = fieldResHouse;
}

@Column(name = "res_street", nullable = true, length = 50)
public java.lang.String getRes_street() {
	return this.res_street;
}

public void setRes_street(java.lang.String fieldResStreet) {
	this.res_street = fieldResStreet;
}

@Column(name = "res_village", nullable = true, length = 50)
public java.lang.String getRes_village() {
	return this.res_village;
}

public void setRes_village(java.lang.String fieldResVillage) {
	this.res_village = fieldResVillage;
}

@Column(name = "res_brgy", nullable = true, length = 50)
public java.lang.String getRes_brgy() {
	return this.res_brgy;
}

public void setRes_brgy(java.lang.String fieldResBrgy) {
	this.res_brgy = fieldResBrgy;
}

@Column(name = "res_city", nullable = true, length = 50)
public java.lang.String getRes_city() {
	return this.res_city;
}

public void setRes_city(java.lang.String fieldResCity) {
	this.res_city = fieldResCity;
}

@Column(name = "res_province", nullable = true, length = 50)
public java.lang.String getRes_province() {
	return this.res_province;
}

public void setRes_province(java.lang.String fieldResProvince) {
	this.res_province = fieldResProvince;
}

@Column(name = "res_zip", nullable = true, length = 50)
public java.lang.String getRes_zip() {
	return this.res_zip;
}

public void setRes_zip(java.lang.String fieldResZip) {
	this.res_zip = fieldResZip;
}

@Column(name = "addr_house", nullable = true, length = 50)
public java.lang.String getAddr_house() {
	return this.addr_house;
}

public void setAddr_house(java.lang.String fieldAddrHouse) {
	this.addr_house = fieldAddrHouse;
}

@Column(name = "addr_street", nullable = true, length = 50)
public java.lang.String getAddr_street() {
	return this.addr_street;
}

public void setAddr_street(java.lang.String fieldAddrStreet) {
	this.addr_street = fieldAddrStreet;
}

@Column(name = "addr_village", nullable = true, length = 50)
public java.lang.String getAddr_village() {
	return this.addr_village;
}

public void setAddr_village(java.lang.String fieldAddrVillage) {
	this.addr_village = fieldAddrVillage;
}

@Column(name = "addr_brgy", nullable = true, length = 50)
public java.lang.String getAddr_brgy() {
	return this.addr_brgy;
}

public void setAddr_brgy(java.lang.String fieldAddrBrgy) {
	this.addr_brgy = fieldAddrBrgy;
}

@Column(name = "addr_city", nullable = true, length = 50)
public java.lang.String getAddr_city() {
	return this.addr_city;
}

public void setAddr_city(java.lang.String fieldAddrCity) {
	this.addr_city = fieldAddrCity;
}

@Column(name = "addr_province", nullable = true, length = 50)
public java.lang.String getAddr_province() {
	return this.addr_province;
}

public void setAddr_province(java.lang.String fieldAddrProvince) {
	this.addr_province = fieldAddrProvince;
}

@Column(name = "addr_zip", nullable = true, length = 50)
public java.lang.String getAddr_zip() {
	return this.addr_zip;
}

public void setAddr_zip(java.lang.String fieldAddrZip) {
	this.addr_zip = fieldAddrZip;
}

@Column(name = "telephone", nullable = true, length = 50)
public java.lang.String getTelephone() {
	return this.telephone;
}

public void setTelephone(java.lang.String fieldTelephone) {
	this.telephone = fieldTelephone;
}

@Column(name = "mobile", nullable = true, length = 50)
public java.lang.String getMobile() {
	return this.mobile;
}

public void setMobile(java.lang.String fieldMobile) {
	this.mobile = fieldMobile;
}

@Column(name = "email", nullable = true, length = 50)
public java.lang.String getEmail() {
	return this.email;
}

public void setEmail(java.lang.String fieldEmail) {
	this.email = fieldEmail;
}

@Column(name = "spouse_surname", nullable = true, length = 50)
public java.lang.String getSpouse_surname() {
	return this.spouse_surname;
}

public void setSpouse_surname(java.lang.String fieldSpouseSurname) {
	this.spouse_surname = fieldSpouseSurname;
}

@Column(name = "spouse_firstname", nullable = true, length = 50)
public java.lang.String getSpouse_firstname() {
	return this.spouse_firstname;
}

public void setSpouse_firstname(java.lang.String fieldSpouseFirstname) {
	this.spouse_firstname = fieldSpouseFirstname;
}

@Column(name = "spouse_middlename", nullable = true, length = 50)
public java.lang.String getSpouse_middlename() {
	return this.spouse_middlename;
}

public void setSpouse_middlename(java.lang.String fieldSpouseMiddlename) {
	this.spouse_middlename = fieldSpouseMiddlename;
}

@Column(name = "spouse_name_ext", nullable = true, length = 50)
public java.lang.String getSpouse_name_ext() {
	return this.spouse_name_ext;
}

public void setSpouse_name_ext(java.lang.String fieldSpouseNameExt) {
	this.spouse_name_ext = fieldSpouseNameExt;
}

@Column(name = "spouse_occupation", nullable = true, length = 50)
public java.lang.String getSpouse_occupation() {
	return this.spouse_occupation;
}

public void setSpouse_occupation(java.lang.String fieldSpouseOccupation) {
	this.spouse_occupation = fieldSpouseOccupation;
}

@Column(name = "spouse_work_agency", nullable = true, length = 50)
public java.lang.String getSpouse_work_agency() {
	return this.spouse_work_agency;
}

public void setSpouse_work_agency(java.lang.String fieldSpouseWorkAgency) {
	this.spouse_work_agency = fieldSpouseWorkAgency;
}

@Column(name = "spouse_work_address", nullable = true, length = 50)
public java.lang.String getSpouse_work_address() {
	return this.spouse_work_address;
}

public void setSpouse_work_address(java.lang.String fieldSpouseWorkAddress) {
	this.spouse_work_address = fieldSpouseWorkAddress;
}

@Column(name = "spouse_telephone", nullable = true, length = 50)
public java.lang.String getSpouse_telephone() {
	return this.spouse_telephone;
}

public void setSpouse_telephone(java.lang.String fieldSpouseTelephone) {
	this.spouse_telephone = fieldSpouseTelephone;
}

@Column(name = "father_surname", nullable = true, length = 50)
public java.lang.String getFather_surname() {
	return this.father_surname;
}

public void setFather_surname(java.lang.String fieldFatherSurname) {
	this.father_surname = fieldFatherSurname;
}

@Column(name = "father_firstname", nullable = true, length = 50)
public java.lang.String getFather_firstname() {
	return this.father_firstname;
}

public void setFather_firstname(java.lang.String fieldFatherFirstname) {
	this.father_firstname = fieldFatherFirstname;
}

@Column(name = "father_middlename", nullable = true, length = 50)
public java.lang.String getFather_middlename() {
	return this.father_middlename;
}

public void setFather_middlename(java.lang.String fieldFatherMiddlename) {
	this.father_middlename = fieldFatherMiddlename;
}

@Column(name = "father_name_ext", nullable = true, length = 50)
public java.lang.String getFather_name_ext() {
	return this.father_name_ext;
}

public void setFather_name_ext(java.lang.String fieldFatherNameExt) {
	this.father_name_ext = fieldFatherNameExt;
}

@Column(name = "mother_maidenname", nullable = true, length = 50)
public java.lang.String getMother_maidenname() {
	return this.mother_maidenname;
}

public void setMother_maidenname(java.lang.String fieldMotherMaidenname) {
	this.mother_maidenname = fieldMotherMaidenname;
}

@Column(name = "mother_surname", nullable = true, length = 50)
public java.lang.String getMother_surname() {
	return this.mother_surname;
}

public void setMother_surname(java.lang.String fieldMotherSurname) {
	this.mother_surname = fieldMotherSurname;
}

@Column(name = "mother_firstname", nullable = true, length = 50)
public java.lang.String getMother_firstname() {
	return this.mother_firstname;
}

public void setMother_firstname(java.lang.String fieldMotherFirstname) {
	this.mother_firstname = fieldMotherFirstname;
}

@Column(name = "mother_middlename", nullable = true, length = 50)
public java.lang.String getMother_middlename() {
	return this.mother_middlename;
}

public void setMother_middlename(java.lang.String fieldMotherMiddlename) {
	this.mother_middlename = fieldMotherMiddlename;
}

}
