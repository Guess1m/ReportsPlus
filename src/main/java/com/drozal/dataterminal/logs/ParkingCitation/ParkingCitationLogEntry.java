package com.drozal.dataterminal.logs.ParkingCitation;

// Data model for a log entry
public class ParkingCitationLogEntry {
    public String citationNumber;
    public String citationDate;
    public String citationTime;
    public String meterNumber;
    public String citationCounty;
    public String citationArea;
    public String citationStreet;

    public String getCitationNumber() {
        return citationNumber;
    }

    public String getCitationDate() {
        return citationDate;
    }

    public String getCitationTime() {
        return citationTime;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public String getCitationCounty() {
        return citationCounty;
    }

    public String getCitationArea() {
        return citationArea;
    }

    public String getCitationStreet() {
        return citationStreet;
    }

    public String getOffenderName() {
        return offenderName;
    }

    public String getOffenderGender() {
        return offenderGender;
    }

    public String getOffenderEthnicity() {
        return offenderEthnicity;
    }

    public String getOffenderAge() {
        return offenderAge;
    }

    public String getOffenderDescription() {
        return offenderDescription;
    }

    public String getOffenderVehicleMake() {
        return offenderVehicleMake;
    }

    public String getOffenderVehicleModel() {
        return offenderVehicleModel;
    }

    public String getOffenderVehicleColor() {
        return offenderVehicleColor;
    }

    public String getOffenderVehicleType() {
        return offenderVehicleType;
    }

    public String getOffenderVehiclePlate() {
        return offenderVehiclePlate;
    }

    public String getOffenderVehicleOther() {
        return offenderVehicleOther;
    }

    public String getOffenderViolations() {
        return offenderViolations;
    }

    public String getOffenderActionsTaken() {
        return offenderActionsTaken;
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

    public String getCitationComments() {
        return citationComments;
    }

    public String offenderName;
    public String offenderGender;
    public String offenderEthnicity;
    public String offenderAge;
    public String offenderDescription;
    public String offenderVehicleMake;
    public String offenderVehicleModel;
    public String offenderVehicleColor;
    public String offenderVehicleType;
    public String offenderVehiclePlate;
    public String offenderVehicleOther;
    public String offenderViolations;
    public String offenderActionsTaken;
    public String officerRank;
    public String officerName;
    public String officerNumber;
    public String officerDivision;
    public String officerAgency;
    public String citationComments;

    public ParkingCitationLogEntry() {
    }

    public ParkingCitationLogEntry(String citationNumber, String citationDate, String citationTime, String meterNumber, String citationCounty, String citationArea, String citationStreet, String offenderName, String offenderGender, String offenderEthnicity, String offenderAge, String offenderDescription, String offenderVehicleMake, String offenderVehicleModel, String offenderVehicleColor, String offenderVehicleType, String offenderVehiclePlate, String offenderVehicleOther, String offenderViolations, String offenderActionsTaken, String officerRank, String officerName, String officerNumber, String officerDivision, String officerAgency, String citationComments) {
        this.citationNumber = citationNumber;
        this.citationDate = citationDate;
        this.citationTime = citationTime;
        this.meterNumber = meterNumber;
        this.citationCounty = citationCounty;
        this.citationArea = citationArea;
        this.citationStreet = citationStreet;
        this.offenderName = offenderName;
        this.offenderGender = offenderGender;
        this.offenderEthnicity = offenderEthnicity;
        this.offenderAge = offenderAge;
        this.offenderDescription = offenderDescription;
        this.offenderVehicleMake = offenderVehicleMake;
        this.offenderVehicleModel = offenderVehicleModel;
        this.offenderVehicleColor = offenderVehicleColor;
        this.offenderVehicleType = offenderVehicleType;
        this.offenderVehiclePlate = offenderVehiclePlate;
        this.offenderVehicleOther = offenderVehicleOther;
        this.offenderViolations = offenderViolations;
        this.offenderActionsTaken = offenderActionsTaken;
        this.officerRank = officerRank;
        this.officerName = officerName;
        this.officerNumber = officerNumber;
        this.officerDivision = officerDivision;
        this.officerAgency = officerAgency;
        this.citationComments = citationComments;
    }
}
