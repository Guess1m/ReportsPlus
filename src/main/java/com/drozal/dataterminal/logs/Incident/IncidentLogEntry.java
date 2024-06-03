package com.drozal.dataterminal.logs.Incident;

/**
 * The type Incident log entry.
 */
public class IncidentLogEntry {
	/**
	 * The Incident number.
	 */
	public String incidentNumber;
	/**
	 * The Incident date.
	 */
	public String incidentDate;
	/**
	 * The Incident time.
	 */
	public String incidentTime;
	/**
	 * The Incident statement.
	 */
	public String incidentStatement;
	/**
	 * The Incident witnesses.
	 */
	public String incidentWitnesses;
	/**
	 * The Incident victims.
	 */
	public String incidentVictims;
	/**
	 * The Officer name.
	 */
	public String officerName;
	/**
	 * The Officer rank.
	 */
	public String officerRank;
	/**
	 * The Officer number.
	 */
	public String officerNumber;
	/**
	 * The Officer agency.
	 */
	public String officerAgency;
	/**
	 * The Officer division.
	 */
	public String officerDivision;
	/**
	 * The Incident street.
	 */
	public String incidentStreet;
	/**
	 * The Incident area.
	 */
	public String incidentArea;
	/**
	 * The Incident county.
	 */
	public String incidentCounty;
	/**
	 * The Incident actions taken.
	 */
	public String incidentActionsTaken;
	/**
	 * The Incident comments.
	 */
	public String incidentComments;
	
	/**
	 * Instantiates a new Incident log entry.
	 */
	public IncidentLogEntry() {
	}
	
	/**
	 * Instantiates a new Incident log entry.
	 *
	 * @param incidentNumber       the incident number
	 * @param incidentDate         the incident date
	 * @param incidentTime         the incident time
	 * @param incidentStatement    the incident statement
	 * @param incidentWitnesses    the incident witnesses
	 * @param incidentVictims      the incident victims
	 * @param officerName          the officer name
	 * @param officerRank          the officer rank
	 * @param officerNumber        the officer number
	 * @param officerAgency        the officer agency
	 * @param officerDivision      the officer division
	 * @param incidentStreet       the incident street
	 * @param incidentArea         the incident area
	 * @param incidentCounty       the incident county
	 * @param incidentActionsTaken the incident actions taken
	 * @param incidentComments     the incident comments
	 */
	public IncidentLogEntry(String incidentNumber, String incidentDate, String incidentTime, String incidentStatement, String incidentWitnesses, String incidentVictims, String officerName, String officerRank, String officerNumber, String officerAgency, String officerDivision, String incidentStreet, String incidentArea, String incidentCounty, String incidentActionsTaken, String incidentComments) {
		this.incidentNumber = incidentNumber;
		this.incidentDate = incidentDate;
		this.incidentTime = incidentTime;
		this.incidentStatement = incidentStatement;
		this.incidentWitnesses = incidentWitnesses;
		this.incidentVictims = incidentVictims;
		this.officerName = officerName;
		this.officerRank = officerRank;
		this.officerNumber = officerNumber;
		this.officerAgency = officerAgency;
		this.officerDivision = officerDivision;
		this.incidentStreet = incidentStreet;
		this.incidentArea = incidentArea;
		this.incidentCounty = incidentCounty;
		this.incidentActionsTaken = incidentActionsTaken;
		this.incidentComments = incidentComments;
	}
	
	/**
	 * Gets incident number.
	 *
	 * @return the incident number
	 */
	public String getIncidentNumber() {
		return incidentNumber;
	}
	
	/**
	 * Gets incident date.
	 *
	 * @return the incident date
	 */
	public String getIncidentDate() {
		return incidentDate;
	}
	
	/**
	 * Gets incident time.
	 *
	 * @return the incident time
	 */
	public String getIncidentTime() {
		return incidentTime;
	}
	
	/**
	 * Gets incident statement.
	 *
	 * @return the incident statement
	 */
	public String getIncidentStatement() {
		return incidentStatement;
	}
	
	/**
	 * Gets incident witnesses.
	 *
	 * @return the incident witnesses
	 */
	public String getIncidentWitnesses() {
		return incidentWitnesses;
	}
	
	/**
	 * Gets incident victims.
	 *
	 * @return the incident victims
	 */
	public String getIncidentVictims() {
		return incidentVictims;
	}
	
	/**
	 * Gets officer name.
	 *
	 * @return the officer name
	 */
	public String getOfficerName() {
		return officerName;
	}
	
	/**
	 * Gets officer rank.
	 *
	 * @return the officer rank
	 */
	public String getOfficerRank() {
		return officerRank;
	}
	
	/**
	 * Gets officer number.
	 *
	 * @return the officer number
	 */
	public String getOfficerNumber() {
		return officerNumber;
	}
	
	/**
	 * Gets officer agency.
	 *
	 * @return the officer agency
	 */
	public String getOfficerAgency() {
		return officerAgency;
	}
	
	/**
	 * Gets officer division.
	 *
	 * @return the officer division
	 */
	public String getOfficerDivision() {
		return officerDivision;
	}
	
	/**
	 * Gets incident street.
	 *
	 * @return the incident street
	 */
	public String getIncidentStreet() {
		return incidentStreet;
	}
	
	/**
	 * Gets incident area.
	 *
	 * @return the incident area
	 */
	public String getIncidentArea() {
		return incidentArea;
	}
	
	/**
	 * Gets incident county.
	 *
	 * @return the incident county
	 */
	public String getIncidentCounty() {
		return incidentCounty;
	}
	
	/**
	 * Gets incident actions taken.
	 *
	 * @return the incident actions taken
	 */
	public String getIncidentActionsTaken() {
		return incidentActionsTaken;
	}
	
	/**
	 * Gets incident comments.
	 *
	 * @return the incident comments
	 */
	public String getIncidentComments() {
		return incidentComments;
	}
}
