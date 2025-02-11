package com.Guess.ReportsPlus.logs.TrafficStop;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrafficStopReport {
    @XmlElement(name = "PlateNumber")
    private String PlateNumber;

    @XmlElement(name = "Color")
    private String Color;

    @XmlElement(name = "stopStatus")
    private String stopStatus;

    @XmlElement(name = "Type")
    private String Type;

    @XmlElement(name = "StopNumber")
    private String StopNumber;

    @XmlElement(name = "ResponseModel")
    private String ResponseModel;

    @XmlElement(name = "ResponseOtherInfo")
    private String ResponseOtherInfo;

    @XmlElement(name = "CommentsTextArea")
    private String CommentsTextArea;

    @XmlElement(name = "Time")
    private String Time;

    @XmlElement(name = "Date")
    private String Date;

    @XmlElement(name = "Division")
    private String Division;

    @XmlElement(name = "Agency")
    private String Agency;

    @XmlElement(name = "Number")
    private String Number;

    @XmlElement(name = "Rank")
    private String Rank;

    @XmlElement(name = "Name")
    private String Name;

    @XmlElement(name = "County")
    private String County;

    @XmlElement(name = "Area")
    private String Area;

    @XmlElement(name = "Street")
    private String Street;

    @XmlElement(name = "operatorName")
    private String operatorName;

    @XmlElement(name = "operatorAge")
    private String operatorAge;

    @XmlElement(name = "operatorDescription")
    private String operatorDescription;

    @XmlElement(name = "operatorAddress")
    private String operatorAddress;

    @XmlElement(name = "operatorGender")
    private String operatorGender;

    public String getAgency() {
        return Agency;
    }

    public void setAgency(String agency) {
        Agency = agency;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getCommentsTextArea() {
        return CommentsTextArea;
    }

    public void setCommentsTextArea(String commentsTextArea) {
        CommentsTextArea = commentsTextArea;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return stopStatus != null ? stopStatus : "Closed";
    }

    public void setStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getOfficerName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getOperatorAddress() {
        return operatorAddress;
    }

    public void setOperatorAddress(String operatorAddress) {
        this.operatorAddress = operatorAddress;
    }

    public String getOperatorAge() {
        return operatorAge;
    }

    public void setOperatorAge(String operatorAge) {
        this.operatorAge = operatorAge;
    }

    public String getOperatorDescription() {
        return operatorDescription;
    }

    public void setOperatorDescription(String operatorDescription) {
        this.operatorDescription = operatorDescription;
    }

    public String getOperatorGender() {
        return operatorGender;
    }

    public void setOperatorGender(String operatorGender) {
        this.operatorGender = operatorGender;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getResponseModel() {
        return ResponseModel;
    }

    public void setResponseModel(String responseModel) {
        ResponseModel = responseModel;
    }

    public String getResponseOtherInfo() {
        return ResponseOtherInfo;
    }

    public void setResponseOtherInfo(String responseOtherInfo) {
        ResponseOtherInfo = responseOtherInfo;
    }

    public String getStopNumber() {
        return StopNumber;
    }

    public void setStopNumber(String stopNumber) {
        StopNumber = stopNumber;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
