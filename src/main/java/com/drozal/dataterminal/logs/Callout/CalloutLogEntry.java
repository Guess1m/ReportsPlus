package com.drozal.dataterminal.logs.Callout;

// Data model for a log entry
public class CalloutLogEntry {
    public String CalloutNumber;
    public String NotesTextArea;
    public String ResponseGrade;
    public String ResponeType;
    public String Time;
    public String Date;
    public String Division;
    public String Agency;
    public String Number;
    public String Rank;
    public String Name;
    public String Address;
    public String County;
    public String Area;

    public CalloutLogEntry() {

    }

    public CalloutLogEntry(String date, String time, String name, String rank, String number, String division, String agency, String responseType, String responseGrade, String cNumber, String notes, String address, String county, String area) {
        this.Date = date;
        this.Time = time;
        this.Name = name;
        this.Rank = rank;
        this.Number = number;
        this.Division = division;
        this.Agency = agency;
        this.ResponeType = responseType;
        this.ResponseGrade = responseGrade;
        this.CalloutNumber = cNumber;
        this.NotesTextArea = notes;
        this.Address = address;
        this.County = county;
        this.Area = area;
    }
}

