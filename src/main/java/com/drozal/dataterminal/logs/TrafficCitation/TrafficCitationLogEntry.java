package com.drozal.dataterminal.logs.TrafficCitation;

// Data model for a log entry
public class TrafficCitationLogEntry {
    public String citationNumber;
    public String citationDate;
    public String citationTime;
    public String citationCharges;
    public String citationCounty;
    public String citationArea;
    public String citationStreet;
    public String offenderName;
    public String offenderGender;
    public String offenderAge;
    public String offenderHomeAddress;
    public String offenderDescription;
    public String offenderVehicleModel;
    public String offenderVehicleColor;
    public String offenderVehicleType;
    public String offenderVehiclePlate;
    public String offenderVehicleOther;
    public String officerRank;
    public String officerName;
    public String officerNumber;
    public String officerDivision;
    public String officerAgency;
    public String citationComments;
    public TrafficCitationLogEntry() {
    }

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

    public String getOffenderHomeAddress() {
        return offenderHomeAddress;
    }

    public String getCitationNumber() {
        return citationNumber;
    }

    public String getCitationDate() {
        return citationDate;
    }

    public String getCitationTime() {
        return citationTime;
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

    public String getOffenderAge() {
        return offenderAge;
    }

    public String getOffenderDescription() {
        return offenderDescription;
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

    public String getCitationCharges() {
        return citationCharges;
    }

    public String getOfficerAgency() {
        return officerAgency;
    }

    public String getCitationComments() {
        return citationComments;
    }
}
