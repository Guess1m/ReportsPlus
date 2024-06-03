package com.drozal.dataterminal.logs.TrafficStop;

public class TrafficStopLogEntry {
    public String PlateNumber;
    public String Color;
    public String Type;
    public String StopNumber;
    public String ResponseModel;
    public String ResponseOtherInfo;
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

    public String operatorName;
    public String operatorAge;
    public String operatorDescription;
    public String operatorAddress;
    public String operatorGender;

    public TrafficStopLogEntry() {

    }

    public TrafficStopLogEntry(String date, String time, String model, String otherInfo, String oName, String oAge, String oAddress, String oDescription, String oGender, String name, String rank, String number, String division, String agency, String sNumber, String comments, String street, String county, String area, String plateN, String color, String type) {
        this.Date = date;
        this.Time = time;
        this.ResponseModel = model;
        this.ResponseOtherInfo = otherInfo;
        this.operatorName = oName;
        this.operatorAge = oAge;
        this.operatorAddress = oAddress;
        this.operatorDescription = oDescription;
        this.operatorGender = oGender;
        this.Name = name;
        this.Rank = rank;
        this.Number = number;
        this.Division = division;
        this.Agency = agency;
        this.StopNumber = sNumber;
        this.CommentsTextArea = comments;
        this.Street = street;
        this.County = county;
        this.Area = area;
        this.PlateNumber = plateN;
        this.Color = color;
        this.Type = type;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getOperatorDescription() {
        return operatorDescription;
    }

    public String getOperatorAddress() {
        return operatorAddress;
    }

    public String getOperatorGender() {
        return operatorGender;
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

    public String getCommentsTextArea() {
        return CommentsTextArea;
    }

    public String getOperatorAge() {
        return operatorAge;
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

    public String getResponseModel() {
        return ResponseModel;
    }

    public String getResponseOtherInfo() {
        return ResponseOtherInfo;
    }

}

