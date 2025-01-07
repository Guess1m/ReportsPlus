package com.Guess.ReportsPlus.logs.Impound;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImpoundReport {
	
	@XmlElement(name = "impoundNumber")
	private String impoundNumber;
	
	@XmlElement(name = "impoundDate")
	private String impoundDate;
	
	@XmlElement(name = "impoundStatus")
	private String impoundStatus;
	
	@XmlElement(name = "impoundTime")
	private String impoundTime;
	
	@XmlElement(name = "ownerName")
	private String ownerName;
	
	@XmlElement(name = "ownerAge")
	private String ownerAge;
	
	@XmlElement(name = "ownerGender")
	private String ownerGender;
	
	@XmlElement(name = "ownerAddress")
	private String ownerAddress;
	
	@XmlElement(name = "impoundPlateNumber")
	private String impoundPlateNumber;
	
	@XmlElement(name = "impoundModel")
	private String impoundModel;
	
	@XmlElement(name = "impoundType")
	private String impoundType;
	
	@XmlElement(name = "impoundColor")
	private String impoundColor;
	
	@XmlElement(name = "impoundComments")
	private String impoundComments;
	
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
	
	public String getImpoundColor() {
		return impoundColor;
	}
	
	public void setImpoundColor(String impoundColor) {
		this.impoundColor = impoundColor;
	}
	
	public String getImpoundComments() {
		return impoundComments;
	}
	
	public void setImpoundComments(String impoundComments) {
		this.impoundComments = impoundComments;
	}
	
	public String getImpoundDate() {
		return impoundDate;
	}
	
	public void setImpoundDate(String impoundDate) {
		this.impoundDate = impoundDate;
	}
	
	public String getImpoundModel() {
		return impoundModel;
	}
	
	public void setImpoundModel(String impoundModel) {
		this.impoundModel = impoundModel;
	}
	
	public String getImpoundNumber() {
		return impoundNumber;
	}
	
	public void setImpoundNumber(String impoundNumber) {
		this.impoundNumber = impoundNumber;
	}
	
	public String getImpoundPlateNumber() {
		return impoundPlateNumber;
	}
	
	public void setImpoundPlateNumber(String impoundPlateNumber) {
		this.impoundPlateNumber = impoundPlateNumber;
	}
	
	public String getImpoundTime() {
		return impoundTime;
	}
	
	public void setImpoundTime(String impoundTime) {
		this.impoundTime = impoundTime;
	}
	
	public String getImpoundType() {
		return impoundType;
	}
	
	public void setImpoundType(String impoundType) {
		this.impoundType = impoundType;
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
	
	public String getStatus() {
		return impoundStatus != null ? impoundStatus : "Closed";
	}
	
	public void setStatus(String impoundStatus) {
		this.impoundStatus = impoundStatus;
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
}
