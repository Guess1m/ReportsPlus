package com.Guess.ReportsPlus.util.Server.Objects.Callout;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Callout")
@XmlAccessorType(XmlAccessType.FIELD)
public class Callout {

    @XmlElement(name = "Number")
    private String number;

    @XmlElement(name = "Type")
    private String type;

    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "Message")
    private String message;

    @XmlElement(name = "Priority")
    private String priority;

    @XmlElement(name = "Street")
    private String street;

    @XmlElement(name = "Area")
    private String area;

    @XmlElement(name = "County")
    private String county;

    @XmlElement(name = "StartDate")
    private String startDate;

    @XmlElement(name = "StartTime")
    private String startTime;

    @XmlElement(name = "Status")
    private String status;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
