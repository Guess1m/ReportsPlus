package com.drozal.dataterminal.logs.Arrest;

// Data model for a log entry
public class ArrestLogEntry {

    public String arrestNumber;
    public String arrestDate;
    public String arrestTime;
    public String arrestCharges; //
    public String arrestCounty;
    public String arrestArea;
    public String arrestStreet;
    public String arresteeName;
    public String arresteeAge;
    public String arresteeGender;
    public String arresteeDescription;
    public String ambulanceYesNo;
    public String TaserYesNo;
    public String arresteeMedicalInformation;//
    public String arresteeHomeAddress;//
    public String arrestDetails;//
    public String officerRank;
    public String officerName;
    public String officerNumber;
    public String officerDivision;
    public String officerAgency;

    public ArrestLogEntry() {
    }

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

    public String getArrestCharges() {
        return arrestCharges;
    }

    public String getAmbulanceYesNo() {
        return ambulanceYesNo;
    }

    public String getTaserYesNo() {
        return TaserYesNo;
    }

    public String getArrestNumber() {
        return arrestNumber;
    }

    public String getArresteeHomeAddress() {
        return arresteeHomeAddress;
    }

    public String getArrestDate() {
        return arrestDate;
    }

    public String getArrestTime() {
        return arrestTime;
    }

    public String getArrestCounty() {
        return arrestCounty;
    }

    public String getArrestArea() {
        return arrestArea;
    }

    public String getArrestStreet() {
        return arrestStreet;
    }

    public String getArresteeName() {
        return arresteeName;
    }

    public String getArresteeAge() {
        return arresteeAge;
    }

    public String getArresteeGender() {
        return arresteeGender;
    }


    public String getArresteeDescription() {
        return arresteeDescription;
    }

    public String getArresteeMedicalInformation() {
        return arresteeMedicalInformation;
    }

    public String getArrestDetails() {
        return arrestDetails;
    }

    public String getOfficerRank() {
        return officerRank;
    }

    public String getOfficerName() {
        return officerName;
    }

    public String getOfficerNumber() {
        return officerNumber;
    }

    public String getOfficerDivision() {
        return officerDivision;
    }

    public String getOfficerAgency() {
        return officerAgency;
    }
}
