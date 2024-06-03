package com.drozal.dataterminal.logs.Patrol;

/**
 * The type Patrol log entry.
 */
public class PatrolLogEntry {
	/**
	 * The Patrol number.
	 */
	public String patrolNumber;
	/**
	 * The Patrol date.
	 */
	public String patrolDate;
	/**
	 * The Patrol length.
	 */
	public String patrolLength;
	/**
	 * The Patrol start time.
	 */
	public String patrolStartTime;
	/**
	 * The Patrol stop time.
	 */
	public String patrolStopTime;
	/**
	 * The Officer rank.
	 */
	public String officerRank;
	/**
	 * The Officer name.
	 */
	public String officerName;
	/**
	 * The Officer number.
	 */
	public String officerNumber;
	/**
	 * The Officer division.
	 */
	public String officerDivision;
	/**
	 * The Officer agency.
	 */
	public String officerAgency;
	/**
	 * The Officer vehicle.
	 */
	public String officerVehicle;
	/**
	 * The Patrol comments.
	 */
	public String patrolComments;
	
	/**
	 * Instantiates a new Patrol log entry.
	 */
	public PatrolLogEntry() {
	}
	
	/**
	 * Instantiates a new Patrol log entry.
	 *
	 * @param patrolNumber    the patrol number
	 * @param patrolDate      the patrol date
	 * @param patrolLength    the patrol length
	 * @param patrolStartTime the patrol start time
	 * @param patrolStopTime  the patrol stop time
	 * @param officerRank     the officer rank
	 * @param officerName     the officer name
	 * @param officerNumber   the officer number
	 * @param officerDivision the officer division
	 * @param officerAgency   the officer agency
	 * @param officerVehicle  the officer vehicle
	 * @param patrolComments  the patrol comments
	 */
	public PatrolLogEntry(String patrolNumber, String patrolDate, String patrolLength, String patrolStartTime, String patrolStopTime, String officerRank, String officerName, String officerNumber, String officerDivision, String officerAgency, String officerVehicle, String patrolComments) {
		this.patrolNumber = patrolNumber;
		this.patrolDate = patrolDate;
		this.patrolLength = patrolLength;
		this.patrolStartTime = patrolStartTime;
		this.patrolStopTime = patrolStopTime;
		this.officerRank = officerRank;
		this.officerName = officerName;
		this.officerNumber = officerNumber;
		this.officerDivision = officerDivision;
		this.officerAgency = officerAgency;
		this.officerVehicle = officerVehicle;
		this.patrolComments = patrolComments;
	}
	
	/**
	 * Gets patrol number.
	 *
	 * @return the patrol number
	 */
	public String getPatrolNumber() {
		return patrolNumber;
	}
	
	/**
	 * Gets patrol date.
	 *
	 * @return the patrol date
	 */
	public String getPatrolDate() {
		return patrolDate;
	}
	
	/**
	 * Gets patrol length.
	 *
	 * @return the patrol length
	 */
	public String getPatrolLength() {
		return patrolLength;
	}
	
	/**
	 * Gets patrol start time.
	 *
	 * @return the patrol start time
	 */
	public String getPatrolStartTime() {
		return patrolStartTime;
	}
	
	/**
	 * Gets patrol stop time.
	 *
	 * @return the patrol stop time
	 */
	public String getPatrolStopTime() {
		return patrolStopTime;
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
	 * Gets officer name.
	 *
	 * @return the officer name
	 */
	public String getOfficerName() {
		return officerName;
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
	 * Gets officer division.
	 *
	 * @return the officer division
	 */
	public String getOfficerDivision() {
		return officerDivision;
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
	 * Gets officer vehicle.
	 *
	 * @return the officer vehicle
	 */
	public String getOfficerVehicle() {
		return officerVehicle;
	}
	
	/**
	 * Gets patrol comments.
	 *
	 * @return the patrol comments
	 */
	public String getPatrolComments() {
		return patrolComments;
	}
}
