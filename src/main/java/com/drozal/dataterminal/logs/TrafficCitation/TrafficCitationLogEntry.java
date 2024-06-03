package com.drozal.dataterminal.logs.TrafficCitation;

/**
 * The type Traffic citation log entry.
 */
public class TrafficCitationLogEntry {
	/**
	 * The Citation number.
	 */
	public String citationNumber;
	/**
	 * The Citation date.
	 */
	public String citationDate;
	/**
	 * The Citation time.
	 */
	public String citationTime;
	/**
	 * The Citation charges.
	 */
	public String citationCharges;
	/**
	 * The Citation county.
	 */
	public String citationCounty;
	/**
	 * The Citation area.
	 */
	public String citationArea;
	/**
	 * The Citation street.
	 */
	public String citationStreet;
	/**
	 * The Offender name.
	 */
	public String offenderName;
	/**
	 * The Offender gender.
	 */
	public String offenderGender;
	/**
	 * The Offender age.
	 */
	public String offenderAge;
	/**
	 * The Offender home address.
	 */
	public String offenderHomeAddress;
	/**
	 * The Offender description.
	 */
	public String offenderDescription;
	/**
	 * The Offender vehicle model.
	 */
	public String offenderVehicleModel;
	/**
	 * The Offender vehicle color.
	 */
	public String offenderVehicleColor;
	/**
	 * The Offender vehicle type.
	 */
	public String offenderVehicleType;
	/**
	 * The Offender vehicle plate.
	 */
	public String offenderVehiclePlate;
	/**
	 * The Offender vehicle other.
	 */
	public String offenderVehicleOther;
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
	 * The Citation comments.
	 */
	public String citationComments;
	
	/**
	 * Instantiates a new Traffic citation log entry.
	 */
	public TrafficCitationLogEntry() {
	}
	
	/**
	 * Instantiates a new Traffic citation log entry.
	 *
	 * @param citationNumber       the citation number
	 * @param citationDate         the citation date
	 * @param citationTime         the citation time
	 * @param citationCharges      the citation charges
	 * @param citationCounty       the citation county
	 * @param citationArea         the citation area
	 * @param citationStreet       the citation street
	 * @param offenderName         the offender name
	 * @param offenderGender       the offender gender
	 * @param offenderAge          the offender age
	 * @param offenderHomeAddress  the offender home address
	 * @param offenderDescription  the offender description
	 * @param offenderVehicleModel the offender vehicle model
	 * @param offenderVehicleColor the offender vehicle color
	 * @param offenderVehicleType  the offender vehicle type
	 * @param offenderVehiclePlate the offender vehicle plate
	 * @param offenderVehicleOther the offender vehicle other
	 * @param officerRank          the officer rank
	 * @param officerName          the officer name
	 * @param officerNumber        the officer number
	 * @param officerDivision      the officer division
	 * @param officerAgency        the officer agency
	 * @param citationComments     the citation comments
	 */
	public TrafficCitationLogEntry(String citationNumber, String citationDate, String citationTime, String citationCharges, String citationCounty, String citationArea, String citationStreet, String offenderName, String offenderGender, String offenderAge, String offenderHomeAddress, String offenderDescription, String offenderVehicleModel, String offenderVehicleColor, String offenderVehicleType, String offenderVehiclePlate, String offenderVehicleOther, String officerRank, String officerName, String officerNumber, String officerDivision, String officerAgency, String citationComments) {
		this.citationNumber = citationNumber;
		this.citationDate = citationDate;
		this.citationTime = citationTime;
		this.citationCharges = citationCharges;
		this.citationCounty = citationCounty;
		this.citationArea = citationArea;
		this.citationStreet = citationStreet;
		this.offenderName = offenderName;
		this.offenderGender = offenderGender;
		this.offenderAge = offenderAge;
		this.offenderHomeAddress = offenderHomeAddress;
		this.offenderDescription = offenderDescription;
		this.offenderVehicleModel = offenderVehicleModel;
		this.offenderVehicleColor = offenderVehicleColor;
		this.offenderVehicleType = offenderVehicleType;
		this.offenderVehiclePlate = offenderVehiclePlate;
		this.offenderVehicleOther = offenderVehicleOther;
		this.officerRank = officerRank;
		this.officerName = officerName;
		this.officerNumber = officerNumber;
		this.officerDivision = officerDivision;
		this.officerAgency = officerAgency;
		this.citationComments = citationComments;
	}
	
	/**
	 * Gets offender home address.
	 *
	 * @return the offender home address
	 */
	public String getOffenderHomeAddress() {
		return offenderHomeAddress;
	}
	
	/**
	 * Gets citation number.
	 *
	 * @return the citation number
	 */
	public String getCitationNumber() {
		return citationNumber;
	}
	
	/**
	 * Gets citation date.
	 *
	 * @return the citation date
	 */
	public String getCitationDate() {
		return citationDate;
	}
	
	/**
	 * Gets citation time.
	 *
	 * @return the citation time
	 */
	public String getCitationTime() {
		return citationTime;
	}
	
	/**
	 * Gets citation county.
	 *
	 * @return the citation county
	 */
	public String getCitationCounty() {
		return citationCounty;
	}
	
	/**
	 * Gets citation area.
	 *
	 * @return the citation area
	 */
	public String getCitationArea() {
		return citationArea;
	}
	
	/**
	 * Gets citation street.
	 *
	 * @return the citation street
	 */
	public String getCitationStreet() {
		return citationStreet;
	}
	
	/**
	 * Gets offender name.
	 *
	 * @return the offender name
	 */
	public String getOffenderName() {
		return offenderName;
	}
	
	/**
	 * Gets offender gender.
	 *
	 * @return the offender gender
	 */
	public String getOffenderGender() {
		return offenderGender;
	}
	
	/**
	 * Gets offender age.
	 *
	 * @return the offender age
	 */
	public String getOffenderAge() {
		return offenderAge;
	}
	
	/**
	 * Gets offender description.
	 *
	 * @return the offender description
	 */
	public String getOffenderDescription() {
		return offenderDescription;
	}
	
	/**
	 * Gets offender vehicle model.
	 *
	 * @return the offender vehicle model
	 */
	public String getOffenderVehicleModel() {
		return offenderVehicleModel;
	}
	
	/**
	 * Gets offender vehicle color.
	 *
	 * @return the offender vehicle color
	 */
	public String getOffenderVehicleColor() {
		return offenderVehicleColor;
	}
	
	/**
	 * Gets offender vehicle type.
	 *
	 * @return the offender vehicle type
	 */
	public String getOffenderVehicleType() {
		return offenderVehicleType;
	}
	
	/**
	 * Gets offender vehicle plate.
	 *
	 * @return the offender vehicle plate
	 */
	public String getOffenderVehiclePlate() {
		return offenderVehiclePlate;
	}
	
	/**
	 * Gets offender vehicle other.
	 *
	 * @return the offender vehicle other
	 */
	public String getOffenderVehicleOther() {
		return offenderVehicleOther;
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
	 * Gets citation charges.
	 *
	 * @return the citation charges
	 */
	public String getCitationCharges() {
		return citationCharges;
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
	 * Gets citation comments.
	 *
	 * @return the citation comments
	 */
	public String getCitationComments() {
		return citationComments;
	}
}
