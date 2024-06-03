package com.drozal.dataterminal.logs.Impound;

/**
 * The type Impound log entry.
 */
public class ImpoundLogEntry {
	/**
	 * The Impound number.
	 */
	public String impoundNumber;
	/**
	 * The Impound date.
	 */
	public String impoundDate;
	/**
	 * The Impound time.
	 */
	public String impoundTime;
	/**
	 * The Owner name.
	 */
	public String ownerName;
	/**
	 * The Owner age.
	 */
	public String ownerAge;
	/**
	 * The Owner gender.
	 */
	public String ownerGender;
	/**
	 * The Owner address.
	 */
	public String ownerAddress;
	/**
	 * The Impound plate number.
	 */
	public String impoundPlateNumber;
	/**
	 * The Impound model.
	 */
	public String impoundModel;
	/**
	 * The Impound type.
	 */
	public String impoundType;
	/**
	 * The Impound color.
	 */
	public String impoundColor;
	/**
	 * The Impound comments.
	 */
	public String impoundComments;
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
	 * Instantiates a new Impound log entry.
	 */
	public ImpoundLogEntry() {
	}
	
	/**
	 * Instantiates a new Impound log entry.
	 *
	 * @param impoundNumber      the impound number
	 * @param impoundDate        the impound date
	 * @param impoundTime        the impound time
	 * @param ownerName          the owner name
	 * @param ownerAge           the owner age
	 * @param ownerGender        the owner gender
	 * @param ownerAddress       the owner address
	 * @param impoundPlateNumber the impound plate number
	 * @param impoundModel       the impound model
	 * @param impoundType        the impound type
	 * @param impoundColor       the impound color
	 * @param impoundComments    the impound comments
	 * @param officerRank        the officer rank
	 * @param officerName        the officer name
	 * @param officerNumber      the officer number
	 * @param officerDivision    the officer division
	 * @param officerAgency      the officer agency
	 */
	public ImpoundLogEntry(String impoundNumber, String impoundDate, String impoundTime, String ownerName, String ownerAge, String ownerGender, String ownerAddress, String impoundPlateNumber, String impoundModel, String impoundType, String impoundColor, String impoundComments, String officerRank, String officerName, String officerNumber, String officerDivision, String officerAgency) {
		this.impoundNumber = impoundNumber;
		this.impoundDate = impoundDate;
		this.impoundTime = impoundTime;
		this.ownerName = ownerName;
		this.ownerAge = ownerAge;
		this.ownerGender = ownerGender;
		this.ownerAddress = ownerAddress;
		this.impoundPlateNumber = impoundPlateNumber;
		this.impoundModel = impoundModel;
		this.impoundType = impoundType;
		this.impoundColor = impoundColor;
		this.impoundComments = impoundComments;
		this.officerRank = officerRank;
		this.officerName = officerName;
		this.officerNumber = officerNumber;
		this.officerDivision = officerDivision;
		this.officerAgency = officerAgency;
	}
	
	/**
	 * Gets impound number.
	 *
	 * @return the impound number
	 */
	public String getImpoundNumber() {
		return impoundNumber;
	}
	
	/**
	 * Gets impound date.
	 *
	 * @return the impound date
	 */
	public String getImpoundDate() {
		return impoundDate;
	}
	
	/**
	 * Gets impound time.
	 *
	 * @return the impound time
	 */
	public String getImpoundTime() {
		return impoundTime;
	}
	
	/**
	 * Gets owner name.
	 *
	 * @return the owner name
	 */
	public String getOwnerName() {
		return ownerName;
	}
	
	/**
	 * Gets owner age.
	 *
	 * @return the owner age
	 */
	public String getOwnerAge() {
		return ownerAge;
	}
	
	/**
	 * Gets owner gender.
	 *
	 * @return the owner gender
	 */
	public String getOwnerGender() {
		return ownerGender;
	}
	
	/**
	 * Gets owner address.
	 *
	 * @return the owner address
	 */
	public String getOwnerAddress() {
		return ownerAddress;
	}
	
	/**
	 * Gets impound plate number.
	 *
	 * @return the impound plate number
	 */
	public String getImpoundPlateNumber() {
		return impoundPlateNumber;
	}
	
	/**
	 * Gets impound model.
	 *
	 * @return the impound model
	 */
	public String getImpoundModel() {
		return impoundModel;
	}
	
	/**
	 * Gets impound type.
	 *
	 * @return the impound type
	 */
	public String getImpoundType() {
		return impoundType;
	}
	
	/**
	 * Gets impound color.
	 *
	 * @return the impound color
	 */
	public String getImpoundColor() {
		return impoundColor;
	}
	
	/**
	 * Gets impound comments.
	 *
	 * @return the impound comments
	 */
	public String getImpoundComments() {
		return impoundComments;
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
}
