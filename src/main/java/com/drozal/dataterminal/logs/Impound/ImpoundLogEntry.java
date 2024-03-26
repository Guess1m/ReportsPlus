package com.drozal.dataterminal.logs.Impound;

// Data model for a log entry
public class ImpoundLogEntry {
    public String impoundNumber;
    public String impoundDate;
    public String impoundTime;
    public String ownerName;
    public String ownerAge;
    public String ownerGender;
    public String ownerAddress;
    public String impoundPlateNumber;
    public String impoundModel;
    public String impoundType;
    public String impoundColor;
    public String impoundComments;
    public String officerRank;
    public String officerName;
    public String officerNumber;
    public String officerDivision;
    public String officerAgency;

    public ImpoundLogEntry() {
    }

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

    public String getImpoundNumber() {
        return impoundNumber;
    }

    public String getImpoundDate() {
        return impoundDate;
    }

    public String getImpoundTime() {
        return impoundTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerAge() {
        return ownerAge;
    }

    public String getOwnerGender() {
        return ownerGender;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public String getImpoundPlateNumber() {
        return impoundPlateNumber;
    }

    public String getImpoundModel() {
        return impoundModel;
    }

    public String getImpoundType() {
        return impoundType;
    }

    public String getImpoundColor() {
        return impoundColor;
    }

    public String getImpoundComments() {
        return impoundComments;
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
