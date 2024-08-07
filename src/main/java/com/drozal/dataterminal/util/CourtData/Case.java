package com.drozal.dataterminal.util.CourtData;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Case {

    @XmlElement(name = "CaseNumber")
    private String caseNumber;

    @XmlElement(name = "CaseTime")
    private String caseTime;

    @XmlElement(name = "CourtDate")
    private String courtDate;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "OffenceDate")
    private String offenceDate;

    @XmlElement(name = "Age")
    private String age;

    @XmlElement(name = "Address")
    private String address;

    @XmlElement(name = "Gender")
    private String gender;

    @XmlElement(name = "Street")
    private String street;

    @XmlElement(name = "Area")
    private String area;

    @XmlElement(name = "County")
    private String county;

    @XmlElement(name = "Notes")
    private String notes;

    @XmlElement(name = "Offences")
    private String offences;

    @XmlElement(name = "Outcomes")
    private String outcomes;

    @XmlElement(name = "Status")
    private String status;

    public String getFirstName() {
        if (name != null && !name.isEmpty()) {
            String[] parts = name.split(" ");
            return parts[0];
        }
        return "";
    }

    public String getLastName() {
        if (name != null && !name.isEmpty()) {
            String[] parts = name.split(" ");
            if (parts.length > 1) {
                return parts[parts.length - 1];
            }
        }
        return "";
    }

    public String getCaseTime() {
        return caseTime;
    }

    public void setCaseTime(String caseTime) {
        this.caseTime = caseTime;
    }

    public String getOffences() {
        return offences;
    }

    public void setOffences(String offences) {
        this.offences = offences;
    }

    public String getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(String outcomes) {
        this.outcomes = outcomes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCourtDate() {
        return courtDate;
    }

    public void setCourtDate(String courtDate) {
        this.courtDate = courtDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOffenceDate() {
        return offenceDate;
    }

    public void setOffenceDate(String offenceDate) {
        this.offenceDate = offenceDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}