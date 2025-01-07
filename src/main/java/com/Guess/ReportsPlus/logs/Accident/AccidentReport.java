package com.Guess.ReportsPlus.logs.Accident;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccidentReport {
	@XmlElement(name = "AccidentNumber")
	private String accidentNumber;
	
	@XmlElement(name = "AccidentDate")
	private String accidentDate;
	
	@XmlElement(name = "accidentStatus")
	private String accidentStatus;
	
	@XmlElement(name = "AccidentTime")
	private String accidentTime;
	
	@XmlElement(name = "Street")
	private String street;
	
	@XmlElement(name = "Area")
	private String area;
	
	@XmlElement(name = "County")
	private String county;
	
	@XmlElement(name = "WeatherConditions")
	private String weatherConditions;
	
	@XmlElement(name = "RoadConditions")
	private String roadConditions;
	
	@XmlElement(name = "OtherVehiclesInvolved")
	private String otherVehiclesInvolved;
	
	@XmlElement(name = "PlateNumber")
	private String PlateNumber;
	
	@XmlElement(name = "Color")
	private String Color;
	
	@XmlElement(name = "Type")
	private String Type;
	
	@XmlElement(name = "Model")
	private String model;
	
	@XmlElement(name = "OwnerName")
	private String ownerName;
	
	@XmlElement(name = "OwnerAge")
	private String ownerAge;
	
	@XmlElement(name = "OwnerGender")
	private String ownerGender;
	
	@XmlElement(name = "OwnerAddress")
	private String ownerAddress;
	
	@XmlElement(name = "OwnerDescription")
	private String ownerDescription;
	
	@XmlElement(name = "Witnesses")
	private String witnesses;
	
	@XmlElement(name = "InjuriesReported")
	private String injuriesReported;
	
	@XmlElement(name = "DamageDetails")
	private String damageDetails;
	
	@XmlElement(name = "OfficerRank")
	private String officerRank;
	
	@XmlElement(name = "OfficerName")
	private String officerName;
	
	@XmlElement(name = "OfficerNumber")
	private String officerNumber;
	
	@XmlElement(name = "OfficerAgency")
	private String officerAgency;
	
	@XmlElement(name = "OfficerDivision")
	private String officerDivision;
	
	@XmlElement(name = "Comments")
	private String comments;
	
	public String getAccidentDate() {
		return accidentDate;
	}
	
	public void setAccidentDate(String accidentDate) {
		this.accidentDate = accidentDate;
	}
	
	public String getAccidentNumber() {
		return accidentNumber;
	}
	
	public void setAccidentNumber(String accidentNumber) {
		this.accidentNumber = accidentNumber;
	}
	
	public String getAccidentTime() {
		return accidentTime;
	}
	
	public void setAccidentTime(String accidentTime) {
		this.accidentTime = accidentTime;
	}
	
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getColor() {
		return Color;
	}
	
	public void setColor(String color) {
		Color = color;
	}
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getCounty() {
		return county;
	}
	
	public void setCounty(String county) {
		this.county = county;
	}
	
	public String getDamageDetails() {
		return damageDetails;
	}
	
	public void setDamageDetails(String damageDetails) {
		this.damageDetails = damageDetails;
	}
	
	public String getInjuriesReported() {
		return injuriesReported;
	}
	
	public void setInjuriesReported(String injuriesReported) {
		this.injuriesReported = injuriesReported;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
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
	
	public String getOtherVehiclesInvolved() {
		return otherVehiclesInvolved;
	}
	
	public void setOtherVehiclesInvolved(String otherVehiclesInvolved) {
		this.otherVehiclesInvolved = otherVehiclesInvolved;
	}
	
	public String getOwnerAddress() {
		return ownerAddress;
	}
	
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	
	public String getOwnerAge() {
		return ownerAge;
	}
	
	public void setOwnerAge(String ownerAge) {
		this.ownerAge = ownerAge;
	}
	
	public String getOwnerDescription() {
		return ownerDescription;
	}
	
	public void setOwnerDescription(String ownerDescription) {
		this.ownerDescription = ownerDescription;
	}
	
	public String getOwnerGender() {
		return ownerGender;
	}
	
	public void setOwnerGender(String ownerGender) {
		this.ownerGender = ownerGender;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getPlateNumber() {
		return PlateNumber;
	}
	
	public void setPlateNumber(String plateNumber) {
		PlateNumber = plateNumber;
	}
	
	public String getRoadConditions() {
		return roadConditions;
	}
	
	public void setRoadConditions(String roadConditions) {
		this.roadConditions = roadConditions;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getType() {
		return Type;
	}
	
	public void setType(String type) {
		Type = type;
	}
	
	public String getWeatherConditions() {
		return weatherConditions;
	}
	
	public void setWeatherConditions(String weatherConditions) {
		this.weatherConditions = weatherConditions;
	}
	
	public String getWitnesses() {
		return witnesses;
	}
	
	public void setWitnesses(String witnesses) {
		this.witnesses = witnesses;
	}
	
	public String getStatus() {
		return accidentStatus != null ? accidentStatus : "Closed";
	}
	
	public void setStatus(String accidentStatus) {
		this.accidentStatus = accidentStatus;
	}
}