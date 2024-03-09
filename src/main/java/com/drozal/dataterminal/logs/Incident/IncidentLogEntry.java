package com.drozal.dataterminal.logs.Incident;

// Data model for a log entry
public class IncidentLogEntry {
    public String incidentNumber;
    public String incidentDate;
    public String incidentTime;
    public String incidentStatement;
    public String incidentWitnesses;
    public String incidentVictims;
    public String officerName;
    public String officerRank;
    public String officerNumber;
    public String officerAgency;
    public String officerDivision;
    public String incidentStreet;
    public String incidentArea;
    public String incidentCounty;
    public String incidentActionsTaken;
    public String incidentComments;

    public IncidentLogEntry() {
    }

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

    public String getIncidentNumber() {
        return incidentNumber;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public String getIncidentTime() {
        return incidentTime;
    }

    public String getIncidentStatement() {
        return incidentStatement;
    }

    public String getIncidentWitnesses() {
        return incidentWitnesses;
    }

    public String getIncidentVictims() {
        return incidentVictims;
    }

    public String getOfficerName() {
        return officerName;
    }

    public String getOfficerRank() {
        return officerRank;
    }

    public String getOfficerNumber() {
        return officerNumber;
    }

    public String getOfficerAgency() {
        return officerAgency;
    }

    public String getOfficerDivision() {
        return officerDivision;
    }

    public String getIncidentStreet() {
        return incidentStreet;
    }

    public String getIncidentArea() {
        return incidentArea;
    }

    public String getIncidentCounty() {
        return incidentCounty;
    }

    public String getIncidentActionsTaken() {
        return incidentActionsTaken;
    }

    public String getIncidentComments() {
        return incidentComments;
    }
}
