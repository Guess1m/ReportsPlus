package com.drozal.dataterminal.logs.Patrol;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatrolReport {
	// TODO make sure cases match
	@XmlElement(name = "patrolNumber")
	private String patrolNumber;
	
	@XmlElement(name = "patrolDate")
	private String patrolDate;
	
	@XmlElement(name = "patrolLength")
	private String patrolLength;
	
	@XmlElement(name = "patrolStartTime")
	private String patrolStartTime;
	
	@XmlElement(name = "patrolStopTime")
	private String patrolStopTime;
	
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
	
	@XmlElement(name = "officerVehicle")
	private String officerVehicle;
	
	@XmlElement(name = "patrolComments")
	private String patrolComments;
	
	public String getPatrolDate() {
		return patrolDate;
	}
	
	public void setPatrolDate(String patrolDate) {
		this.patrolDate = patrolDate;
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
	
	public String getOfficerVehicle() {
		return officerVehicle;
	}
	
	public void setOfficerVehicle(String officerVehicle) {
		this.officerVehicle = officerVehicle;
	}
	
	public String getPatrolComments() {
		return patrolComments;
	}
	
	public void setPatrolComments(String patrolComments) {
		this.patrolComments = patrolComments;
	}
	
	public String getPatrolLength() {
		return patrolLength;
	}
	
	public void setPatrolLength(String patrolLength) {
		this.patrolLength = patrolLength;
	}
	
	public String getPatrolNumber() {
		return patrolNumber;
	}
	
	public void setPatrolNumber(String patrolNumber) {
		this.patrolNumber = patrolNumber;
	}
	
	public String getPatrolStartTime() {
		return patrolStartTime;
	}
	
	public void setPatrolStartTime(String patrolStartTime) {
		this.patrolStartTime = patrolStartTime;
	}
	
	public String getPatrolStopTime() {
		return patrolStopTime;
	}
	
	public void setPatrolStopTime(String patrolStopTime) {
		this.patrolStopTime = patrolStopTime;
	}
}
