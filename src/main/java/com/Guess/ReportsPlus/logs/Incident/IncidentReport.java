package com.Guess.ReportsPlus.logs.Incident;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class IncidentReport {
	@XmlElement(name = "incidentNumber")
	private String incidentNumber;
	@XmlElement(name = "incidentDate")
	private String incidentDate;
	@XmlElement(name = "incidentStatus")
	private String incidentStatus;
	@XmlElement(name = "incidentTime")
	private String incidentTime;
	@XmlElement(name = "incidentStatement")
	private String incidentStatement;
	@XmlElement(name = "incidentWitnesses")
	private String incidentWitnesses;
	@XmlElement(name = "incidentVictims")
	private String incidentVictims;
	@XmlElement(name = "officerName")
	private String officerName;
	@XmlElement(name = "officerRank")
	private String officerRank;
	@XmlElement(name = "officerNumber")
	private String officerNumber;
	@XmlElement(name = "officerAgency")
	private String officerAgency;
	@XmlElement(name = "officerDivision")
	private String officerDivision;
	@XmlElement(name = "incidentStreet")
	private String incidentStreet;
	@XmlElement(name = "incidentArea")
	private String incidentArea;
	@XmlElement(name = "incidentCounty")
	private String incidentCounty;
	@XmlElement(name = "incidentActionsTaken")
	private String incidentActionsTaken;
	@XmlElement(name = "incidentComments")
	private String incidentComments;

	public String getIncidentComments() {
		return incidentComments;
	}

	public void setIncidentComments(String incidentComments) {
		this.incidentComments = incidentComments;
	}

	public String getIncidentActionsTaken() {
		return incidentActionsTaken;
	}

	public void setIncidentActionsTaken(String incidentActionsTaken) {
		this.incidentActionsTaken = incidentActionsTaken;
	}

	public String getArea() {
		return incidentArea;
	}

	public void setIncidentArea(String incidentArea) {
		this.incidentArea = incidentArea;
	}

	public String getCounty() {
		return incidentCounty;
	}

	public void setIncidentCounty(String incidentCounty) {
		this.incidentCounty = incidentCounty;
	}

	public String getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(String incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public String getIncidentStatement() {
		return incidentStatement;
	}

	public void setIncidentStatement(String incidentStatement) {
		this.incidentStatement = incidentStatement;
	}

	public String getIncidentStreet() {
		return incidentStreet;
	}

	public void setIncidentStreet(String incidentStreet) {
		this.incidentStreet = incidentStreet;
	}

	public String getIncidentTime() {
		return incidentTime;
	}

	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}

	public String getIncidentVictims() {
		return incidentVictims;
	}

	public void setIncidentVictims(String incidentVictims) {
		this.incidentVictims = incidentVictims;
	}

	public String getIncidentWitnesses() {
		return incidentWitnesses;
	}

	public void setIncidentWitnesses(String incidentWitnesses) {
		this.incidentWitnesses = incidentWitnesses;
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

	public String getStatus() {
		return incidentStatus != null ? incidentStatus : "Closed";
	}

	public void setStatus(String incidentStatus) {
		this.incidentStatus = incidentStatus;
	}
}