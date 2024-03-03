package com.drozal.dataterminal.logs.TrafficStop;

import java.util.List;

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

