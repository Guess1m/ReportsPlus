package com.drozal.dataterminal.logs.Patrol;

// Data model for a log entry
public class PatrolLogEntry {
    public String patrolNumber;
    public String patrolDate;
    public String patrolLength;
    public String patrolStartTime;
    public String patrolStopTime;
    public String officerRank;
    public String officerName;
    public String officerNumber;
    public String officerDivision;
    public String officerAgency;
    public String officerVehicle;
    public String patrolComments;

    public String getPatrolNumber() {
        return patrolNumber;
    }

    public String getPatrolDate() {
        return patrolDate;
    }

    public String getPatrolLength() {
        return patrolLength;
    }

    public String getPatrolStartTime() {
        return patrolStartTime;
    }

    public String getPatrolStopTime() {
        return patrolStopTime;
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

    public String getOfficerVehicle() {
        return officerVehicle;
    }

    public String getPatrolComments() {
        return patrolComments;
    }

    public PatrolLogEntry() {
    }

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
}
