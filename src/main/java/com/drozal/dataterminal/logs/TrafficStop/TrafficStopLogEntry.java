package com.drozal.dataterminal.logs.TrafficStop;

// Data model for a log entry
public class TrafficStopLogEntry {
    public String PlateNumber;
    public String Color;
    public String Type;
    public String StopNumber;
    public String ViolationsTextArea;
    public String ActionsTextArea;
    public String CommentsTextArea;
    public String Time;
    public String Date;
    public String Division;
    public String Agency;
    public String Number;
    public String Rank;
    public String Name;
    public String County;
    public String Area;
    public String Street;

    public TrafficStopLogEntry() {

    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public String getColor() {
        return Color;
    }

    public String getType() {
        return Type;
    }

    public String getStopNumber() {
        return StopNumber;
    }

    public String getViolationsTextArea() {
        return ViolationsTextArea;
    }

    public String getActionsTextArea() {
        return ActionsTextArea;
    }

    public String getCommentsTextArea() {
        return CommentsTextArea;
    }

    public String getTime() {
        return Time;
    }

    public String getDate() {
        return Date;
    }

    public String getDivision() {
        return Division;
    }

    public String getAgency() {
        return Agency;
    }

    public String getNumber() {
        return Number;
    }

    public String getRank() {
        return Rank;
    }

    public String getName() {
        return Name;
    }

    public String getCounty() {
        return County;
    }

    public String getArea() {
        return Area;
    }

    public String getStreet() {
        return Street;
    }

    public TrafficStopLogEntry(String date, String time, String name, String rank, String number, String division, String agency, String sNumber, String violation, String comments, String actions, String street, String county, String area, String plateN, String color, String type) {
        this.Date = date;
        this.Time = time;
        this.Name = name;
        this.Rank = rank;
        this.Number = number;
        this.Division = division;
        this.Agency = agency;
        this.StopNumber = sNumber;
        this.ViolationsTextArea = violation;
        this.CommentsTextArea = comments;
        this.ActionsTextArea = actions;
        this.Street = street;
        this.County = county;
        this.Area = area;
        this.PlateNumber = plateN;
        this.Color = color;
        this.Type = type;
    }


}

