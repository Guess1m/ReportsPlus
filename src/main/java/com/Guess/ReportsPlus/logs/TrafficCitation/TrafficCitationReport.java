package com.Guess.ReportsPlus.logs.TrafficCitation;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrafficCitationReport {
	@XmlElement(name = "citationNumber")
	private String citationNumber;
	
	@XmlElement(name = "citationDate")
	private String citationDate;
	
	@XmlElement(name = "citationStatus")
	private String citationStatus;
	
	@XmlElement(name = "citationTime")
	private String citationTime;
	
	@XmlElement(name = "citationCharges")
	private String citationCharges;
	
	@XmlElement(name = "citationCounty")
	private String citationCounty;
	
	@XmlElement(name = "citationArea")
	private String citationArea;
	
	@XmlElement(name = "citationStreet")
	private String citationStreet;
	
	@XmlElement(name = "offenderName")
	private String offenderName;
	
	@XmlElement(name = "offenderGender")
	private String offenderGender;
	
	@XmlElement(name = "offenderAge")
	private String offenderAge;
	
	@XmlElement(name = "offenderHomeAddress")
	private String offenderHomeAddress;
	
	@XmlElement(name = "offenderDescription")
	private String offenderDescription;
	
	@XmlElement(name = "offenderVehicleModel")
	private String offenderVehicleModel;
	
	@XmlElement(name = "offenderVehicleColor")
	private String offenderVehicleColor;
	
	@XmlElement(name = "offenderVehicleType")
	private String offenderVehicleType;
	
	@XmlElement(name = "offenderVehiclePlate")
	private String offenderVehiclePlate;
	
	@XmlElement(name = "offenderVehicleOther")
	private String offenderVehicleOther;
	
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
	
	@XmlElement(name = "citationComments")
	private String citationComments;
	
	public String getCitationArea() {
		return citationArea;
	}
	
	public void setCitationArea(String citationArea) {
		this.citationArea = citationArea;
	}
	
	public String getCitationCharges() {
		return citationCharges;
	}
	
	public void setCitationCharges(String citationCharges) {
		this.citationCharges = citationCharges;
	}
	
	public String getCitationComments() {
		return citationComments;
	}
	
	public void setCitationComments(String citationComments) {
		this.citationComments = citationComments;
	}
	
	public String getCitationCounty() {
		return citationCounty;
	}
	
	public void setCitationCounty(String citationCounty) {
		this.citationCounty = citationCounty;
	}
	
	public String getCitationDate() {
		return citationDate;
	}
	
	public void setCitationDate(String citationDate) {
		this.citationDate = citationDate;
	}
	
	public String getCitationNumber() {
		return citationNumber;
	}
	
	public void setCitationNumber(String citationNumber) {
		this.citationNumber = citationNumber;
	}
	
	public String getCitationStreet() {
		return citationStreet;
	}
	
	public void setCitationStreet(String citationStreet) {
		this.citationStreet = citationStreet;
	}
	
	public String getStatus() {
		return citationStatus != null ? citationStatus : "Closed";
	}
	
	public void setStatus(String citationStatus) {
		this.citationStatus = citationStatus;
	}
	
	public String getCitationTime() {
		return citationTime;
	}
	
	public void setCitationTime(String citationTime) {
		this.citationTime = citationTime;
	}
	
	public String getOffenderAge() {
		return offenderAge;
	}
	
	public void setOffenderAge(String offenderAge) {
		this.offenderAge = offenderAge;
	}
	
	public String getOffenderDescription() {
		return offenderDescription;
	}
	
	public void setOffenderDescription(String offenderDescription) {
		this.offenderDescription = offenderDescription;
	}
	
	public String getOffenderGender() {
		return offenderGender;
	}
	
	public void setOffenderGender(String offenderGender) {
		this.offenderGender = offenderGender;
	}
	
	public String getOffenderHomeAddress() {
		return offenderHomeAddress;
	}
	
	public void setOffenderHomeAddress(String offenderHomeAddress) {
		this.offenderHomeAddress = offenderHomeAddress;
	}
	
	public String getOffenderName() {
		return offenderName;
	}
	
	public void setOffenderName(String offenderName) {
		this.offenderName = offenderName;
	}
	
	public String getOffenderVehicleColor() {
		return offenderVehicleColor;
	}
	
	public void setOffenderVehicleColor(String offenderVehicleColor) {
		this.offenderVehicleColor = offenderVehicleColor;
	}
	
	public String getOffenderVehicleModel() {
		return offenderVehicleModel;
	}
	
	public void setOffenderVehicleModel(String offenderVehicleModel) {
		this.offenderVehicleModel = offenderVehicleModel;
	}
	
	public String getOffenderVehicleOther() {
		return offenderVehicleOther;
	}
	
	public void setOffenderVehicleOther(String offenderVehicleOther) {
		this.offenderVehicleOther = offenderVehicleOther;
	}
	
	public String getOffenderVehiclePlate() {
		return offenderVehiclePlate;
	}
	
	public void setOffenderVehiclePlate(String offenderVehiclePlate) {
		this.offenderVehiclePlate = offenderVehiclePlate;
	}
	
	public String getOffenderVehicleType() {
		return offenderVehicleType;
	}
	
	public void setOffenderVehicleType(String offenderVehicleType) {
		this.offenderVehicleType = offenderVehicleType;
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
}
