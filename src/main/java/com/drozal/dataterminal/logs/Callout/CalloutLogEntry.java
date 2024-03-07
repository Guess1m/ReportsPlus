package com.drozal.dataterminal.logs.Callout;

import javafx.beans.value.ObservableValue;

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

    public String getNotesTextArea() {
        return NotesTextArea;
    }

    public void setNotesTextArea(String notesTextArea) {
        NotesTextArea = notesTextArea;
    }

    public String getResponseGrade() {
        return ResponseGrade;
    }

    public void setResponseGrade(String responseGrade) {
        ResponseGrade = responseGrade;
    }

    public String getResponeType() {
        return ResponeType;
    }

    public void setResponeType(String responeType) {
        ResponeType = responeType;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getAgency() {
        return Agency;
    }

    public void setAgency(String agency) {
        Agency = agency;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getCalloutNumber() {
        return CalloutNumber;
    }

    public void setCalloutNumber(String calloutNumber) {
        CalloutNumber = calloutNumber;
    }

}

