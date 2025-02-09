package com.Guess.ReportsPlus.logs.Arrest;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArrestReport {
	@XmlElement(name = "arrestNumber")
	private String arrestNumber;
	
	@XmlElement(name = "arrestDate")
	private String arrestDate;
	
	@XmlElement(name = "arrestStatus")
	private String arrestStatus;
	
	@XmlElement(name = "arrestTime")
	private String arrestTime;
	
	@XmlElement(name = "arrestCharges")
	private String arrestCharges;
	
	@XmlElement(name = "arrestCounty")
	private String arrestCounty;
	
	@XmlElement(name = "arrestArea")
	private String arrestArea;
	
	@XmlElement(name = "arrestStreet")
	private String arrestStreet;
	
	@XmlElement(name = "arresteeName")
	private String arresteeName;
	
	@XmlElement(name = "arresteeAge")
	private String arresteeAge;
	
	@XmlElement(name = "arresteeGender")
	private String arresteeGender;
	
	@XmlElement(name = "arresteeDescription")
	private String arresteeDescription;
	
	@XmlElement(name = "ambulanceYesNo")
	private String ambulanceYesNo;
	
	@XmlElement(name = "TaserYesNo")
	private String TaserYesNo;
	
	@XmlElement(name = "arresteeMedicalInformation")
	private String arresteeMedicalInformation;
	
	@XmlElement(name = "arresteeHomeAddress")
	private String arresteeHomeAddress;
	
	@XmlElement(name = "arrestDetails")
	private String arrestDetails;
	
	@XmlElement(name = "officerRank")
	private String officerRank;
	
	@XmlElement(name = "officerName")
	private String officerName;
	
	@XmlElement(name = "officerNumber")
	private String officerNumber;
	
	@XmlElement(name = "officerDivision")
	private String officerDivision;
	
	@XmlElement(name = "officerAgency")
	private String officerAgency;
	
	public String getArresteeGender() {
		return arresteeGender;
	}
	
	public void setArresteeGender(String arresteeGender) {
		this.arresteeGender = arresteeGender;
	}
	
	public String getAmbulanceYesNo() {
		return ambulanceYesNo;
	}
	
	public void setAmbulanceYesNo(String ambulanceYesNo) {
		this.ambulanceYesNo = ambulanceYesNo;
	}
	
	public String getArea() {
		return arrestArea;
	}
	
	public void setArrestArea(String arrestArea) {
		this.arrestArea = arrestArea;
	}
	
	public String getArrestCharges() {
		return arrestCharges;
	}
	
	public void setArrestCharges(String arrestCharges) {
		this.arrestCharges = arrestCharges;
	}
	
	public String getCounty() {
		return arrestCounty;
	}
	
	public void setArrestCounty(String arrestCounty) {
		this.arrestCounty = arrestCounty;
	}
	
	public String getArrestDate() {
		return arrestDate;
	}
	
	public void setArrestDate(String arrestDate) {
		this.arrestDate = arrestDate;
	}
	
	public String getStatus() {
		return arrestStatus != null ? arrestStatus : "Closed";
	}
	
	public void setStatus(String arrestStatus) {
		this.arrestStatus = arrestStatus;
	}
	
	public String getArrestDetails() {
		return arrestDetails;
	}
	
	public void setArrestDetails(String arrestDetails) {
		this.arrestDetails = arrestDetails;
	}
	
	public String getArresteeAge() {
		return arresteeAge;
	}
	
	public void setArresteeAge(String arresteeAge) {
		this.arresteeAge = arresteeAge;
	}
	
	public String getArresteeDescription() {
		return arresteeDescription;
	}
	
	public void setArresteeDescription(String arresteeDescription) {
		this.arresteeDescription = arresteeDescription;
	}
	
	public String getArresteeHomeAddress() {
		return arresteeHomeAddress;
	}
	
	public void setArresteeHomeAddress(String arresteeHomeAddress) {
		this.arresteeHomeAddress = arresteeHomeAddress;
	}
	
	public String getArresteeMedicalInformation() {
		return arresteeMedicalInformation;
	}
	
	public void setArresteeMedicalInformation(String arresteeMedicalInformation) {
		this.arresteeMedicalInformation = arresteeMedicalInformation;
	}
	
	public String getArresteeName() {
		return arresteeName;
	}
	
	public void setArresteeName(String arresteeName) {
		this.arresteeName = arresteeName;
	}
	
	public String getArrestNumber() {
		return arrestNumber;
	}
	
	public void setArrestNumber(String arrestNumber) {
		this.arrestNumber = arrestNumber;
	}
	
	public String getArrestStreet() {
		return arrestStreet;
	}
	
	public void setArrestStreet(String arrestStreet) {
		this.arrestStreet = arrestStreet;
	}
	
	public String getArrestTime() {
		return arrestTime;
	}
	
	public void setArrestTime(String arrestTime) {
		this.arrestTime = arrestTime;
	}
	
	public String getOfficerAgency() {
		return officerAgency;
	}
	
	public void setOfficerAgency(String officerAgency) {
		this.officerAgency = officerAgency;
	}
	
	public String getOfficerDivision() {
		return officerDivision;
	}
	
	public void setOfficerDivision(String officerDivision) {
		this.officerDivision = officerDivision;
	}
	
	public String getOfficerName() {
		return officerName;
	}
	
	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}
	
	public String getOfficerNumber() {
		return officerNumber;
	}
	
	public void setOfficerNumber(String officerNumber) {
		this.officerNumber = officerNumber;
	}
	
	public String getOfficerRank() {
		return officerRank;
	}
	
	public void setOfficerRank(String officerRank) {
		this.officerRank = officerRank;
	}
	
	public String getTaserYesNo() {
		return TaserYesNo;
	}
	
	public void setTaserYesNo(String taserYesNo) {
		TaserYesNo = taserYesNo;
	}
}
