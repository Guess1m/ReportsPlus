package com.drozal.dataterminal.logs.Arrest;

/**
 * The type Arrest log entry.
 */
public class ArrestLogEntry {

    /**
     * The Arrest number.
     */
    public String arrestNumber;
    /**
     * The Arrest date.
     */
    public String arrestDate;
    /**
     * The Arrest time.
     */
    public String arrestTime;
    /**
     * The Arrest charges.
     */
    public String arrestCharges;
    /**
     * The Arrest county.
     */
    public String arrestCounty;
    /**
     * The Arrest area.
     */
    public String arrestArea;
    /**
     * The Arrest street.
     */
    public String arrestStreet;
    /**
     * The Arrestee name.
     */
    public String arresteeName;
    /**
     * The Arrestee age.
     */
    public String arresteeAge;
    /**
     * The Arrestee gender.
     */
    public String arresteeGender;
    /**
     * The Arrestee description.
     */
    public String arresteeDescription;
    /**
     * The Ambulance yes no.
     */
    public String ambulanceYesNo;
    /**
     * The Taser yes no.
     */
    public String TaserYesNo;
    /**
     * The Arrestee medical information.
     */
    public String arresteeMedicalInformation;
    /**
     * The Arrestee home address.
     */
    public String arresteeHomeAddress;
    /**
     * The Arrest details.
     */
    public String arrestDetails;
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
     * Instantiates a new Arrest log entry.
     */
    public ArrestLogEntry() {
    }

    /**
     * Instantiates a new Arrest log entry.
     *
     * @param arrestNumber               the arrest number
     * @param arrestDate                 the arrest date
     * @param arrestTime                 the arrest time
     * @param arrestCharges              the arrest charges
     * @param arrestCounty               the arrest county
     * @param arrestArea                 the arrest area
     * @param arrestStreet               the arrest street
     * @param arresteeName               the arrestee name
     * @param arresteeAge                the arrestee age
     * @param arresteeGender             the arrestee gender
     * @param arresteeDescription        the arrestee description
     * @param ambulanceYesNo             the ambulance yes no
     * @param taserYesNo                 the taser yes no
     * @param arresteeMedicalInformation the arrestee medical information
     * @param arresteeHomeAddress        the arrestee home address
     * @param arrestDetails              the arrest details
     * @param officerRank                the officer rank
     * @param officerName                the officer name
     * @param officerNumber              the officer number
     * @param officerDivision            the officer division
     * @param officerAgency              the officer agency
     */
    public ArrestLogEntry(String arrestNumber, String arrestDate, String arrestTime, String arrestCharges, String arrestCounty, String arrestArea, String arrestStreet, String arresteeName, String arresteeAge, String arresteeGender, String arresteeDescription, String ambulanceYesNo, String taserYesNo, String arresteeMedicalInformation, String arresteeHomeAddress, String arrestDetails, String officerRank, String officerName, String officerNumber, String officerDivision, String officerAgency) {
        this.arrestNumber = arrestNumber;
        this.arrestDate = arrestDate;
        this.arrestTime = arrestTime;
        this.arrestCharges = arrestCharges;
        this.arrestCounty = arrestCounty;
        this.arrestArea = arrestArea;
        this.arrestStreet = arrestStreet;
        this.arresteeName = arresteeName;
        this.arresteeAge = arresteeAge;
        this.arresteeGender = arresteeGender;
        this.arresteeDescription = arresteeDescription;
        this.ambulanceYesNo = ambulanceYesNo;
        this.TaserYesNo = taserYesNo;
        this.arresteeMedicalInformation = arresteeMedicalInformation;
        this.arresteeHomeAddress = arresteeHomeAddress;
        this.arrestDetails = arrestDetails;
        this.officerRank = officerRank;
        this.officerName = officerName;
        this.officerNumber = officerNumber;
        this.officerDivision = officerDivision;
        this.officerAgency = officerAgency;
    }

    /**
     * Gets arrest charges.
     *
     * @return the arrest charges
     */
    public String getArrestCharges() {
        return arrestCharges;
    }

    /**
     * Gets ambulance yes no.
     *
     * @return the ambulance yes no
     */
    public String getAmbulanceYesNo() {
        return ambulanceYesNo;
    }

    /**
     * Gets taser yes no.
     *
     * @return the taser yes no
     */
    public String getTaserYesNo() {
        return TaserYesNo;
    }

    /**
     * Gets arrest number.
     *
     * @return the arrest number
     */
    public String getArrestNumber() {
        return arrestNumber;
    }

    /**
     * Gets arrestee home address.
     *
     * @return the arrestee home address
     */
    public String getArresteeHomeAddress() {
        return arresteeHomeAddress;
    }

    /**
     * Gets arrest date.
     *
     * @return the arrest date
     */
    public String getArrestDate() {
        return arrestDate;
    }

    /**
     * Gets arrest time.
     *
     * @return the arrest time
     */
    public String getArrestTime() {
        return arrestTime;
    }

    /**
     * Gets arrest county.
     *
     * @return the arrest county
     */
    public String getArrestCounty() {
        return arrestCounty;
    }

    /**
     * Gets arrest area.
     *
     * @return the arrest area
     */
    public String getArrestArea() {
        return arrestArea;
    }

    /**
     * Gets arrest street.
     *
     * @return the arrest street
     */
    public String getArrestStreet() {
        return arrestStreet;
    }

    /**
     * Gets arrestee name.
     *
     * @return the arrestee name
     */
    public String getArresteeName() {
        return arresteeName;
    }

    /**
     * Gets arrestee age.
     *
     * @return the arrestee age
     */
    public String getArresteeAge() {
        return arresteeAge;
    }

    /**
     * Gets arrestee gender.
     *
     * @return the arrestee gender
     */
    public String getArresteeGender() {
        return arresteeGender;
    }

    /**
     * Gets arrestee description.
     *
     * @return the arrestee description
     */
    public String getArresteeDescription() {
        return arresteeDescription;
    }

    /**
     * Gets arrestee medical information.
     *
     * @return the arrestee medical information
     */
    public String getArresteeMedicalInformation() {
        return arresteeMedicalInformation;
    }

    /**
     * Gets arrest details.
     *
     * @return the arrest details
     */
    public String getArrestDetails() {
        return arrestDetails;
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
