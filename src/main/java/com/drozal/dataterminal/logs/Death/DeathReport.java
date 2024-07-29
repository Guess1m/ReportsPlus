package com.drozal.dataterminal.logs.Death;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DeathReport {
	
	@XmlElement(name = "Notes")
	private String notesTextArea;
	
	@XmlElement(name = "Division")
	private String division;
	
	@XmlElement(name = "Agency")
	private String agency;
	
	@XmlElement(name = "Number")
	private String number;
	
	@XmlElement(name = "Rank")
	private String rank;
	
	@XmlElement(name = "Name")
	private String name;
	
	@XmlElement(name = "Street")
	private String street;
	
	@XmlElement(name = "County")
	private String county;
	
	@XmlElement(name = "Area")
	private String area;
	
	@XmlElement(name = "Date")
	private String date;
	
	@XmlElement(name = "Time")
	private String time;
	
	@XmlElement(name = "DeathReportNumber")
	private String deathReportNumber;
	
	@XmlElement(name = "Decedent")
	private String decedent;
	
	@XmlElement(name = "Age")
	private String age;
	
	@XmlElement(name = "Gender")
	private String gender;
	
	@XmlElement(name = "Description")
	private String description;
	
	@XmlElement(name = "Address")
	private String address;
	
	@XmlElement(name = "Witnesses")
	private String witnesses;
	
	@XmlElement(name = "CauseOfDeath")
	private String causeOfDeath;
	
	@XmlElement(name = "ModeOfDeath")
	private String modeOfDeath;
	
	@XmlElement(name = "TimeOfDeath")
	private String timeOfDeath;
	
	@XmlElement(name = "DateOfDeath")
	private String dateOfDeath;
	
	public String getDateOfDeath() {
		return dateOfDeath;
	}
	
	public void setDateOfDeath(String dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}
	
	public String getTimeOfDeath() {
		return timeOfDeath;
	}
	
	public void setTimeOfDeath(String timeOfDeath) {
		this.timeOfDeath = timeOfDeath;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAge() {
		return age;
	}
	
	public void setAge(String age) {
		this.age = age;
	}
	
	public String getAgency() {
		return agency;
	}
	
	public void setAgency(String agency) {
		this.agency = agency;
	}
	
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getCauseOfDeath() {
		return causeOfDeath;
	}
	
	public void setCauseOfDeath(String causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}
	
	public String getCounty() {
		return county;
	}
	
	public void setCounty(String county) {
		this.county = county;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDeathReportNumber() {
		return deathReportNumber;
	}
	
	public void setDeathReportNumber(String deathReportNumber) {
		this.deathReportNumber = deathReportNumber;
	}
	
	public String getDecedent() {
		return decedent;
	}
	
	public void setDecedent(String decedent) {
		this.decedent = decedent;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDivision() {
		return division;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getModeOfDeath() {
		return modeOfDeath;
	}
	
	public void setModeOfDeath(String modeOfDeath) {
		this.modeOfDeath = modeOfDeath;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNotesTextArea() {
		return notesTextArea;
	}
	
	public void setNotesTextArea(String notesTextArea) {
		this.notesTextArea = notesTextArea;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getWitnesses() {
		return witnesses;
	}
	
	public void setWitnesses(String witnesses) {
		this.witnesses = witnesses;
	}
}