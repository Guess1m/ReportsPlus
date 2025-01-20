package com.Guess.ReportsPlus.logs.Callout;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class CalloutReport {
	
	@XmlElement(name = "CalloutNumber")
	private String calloutNumber;
	
	@XmlElement(name = "CalloutStatus")
	private String calloutStatus;
	
	@XmlElement(name = "NotesTextArea")
	private String notesTextArea;
	
	@XmlElement(name = "ResponseGrade")
	private String responseGrade;
	
	@XmlElement(name = "ResponseType")
	private String responseType;
	
	@XmlElement(name = "Time")
	private String time;
	
	@XmlElement(name = "Date")
	private String date;
	
	@XmlElement(name = "Division")
	private String division;
	
	@XmlElement(name = "Agency")
	private String agency;
	
	@XmlElement(name = "Number")
	private String number;
	
	@XmlElement(name = "Rank")
	private String rank;
	
	@XmlElement(name = "Name")
	private String name;
	
	@XmlElement(name = "Address")
	private String address;
	
	@XmlElement(name = "County")
	private String county;
	
	@XmlElement(name = "Area")
	private String area;
	
	public String getOfficerName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAgency() {
		return agency;
	}
	
	public void setAgency(String agency) {
		this.agency = agency;
	}
	
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getCalloutNumber() {
		return calloutNumber;
	}
	
	public void setCalloutNumber(String calloutNumber) {
		this.calloutNumber = calloutNumber;
	}
	
	public String getCounty() {
		return county;
	}
	
	public void setCounty(String county) {
		this.county = county;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDivision() {
		return division;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
	
	public String getNotesTextArea() {
		return notesTextArea;
	}
	
	public void setNotesTextArea(String notesTextArea) {
		this.notesTextArea = notesTextArea;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getResponseGrade() {
		return responseGrade;
	}
	
	public void setResponseGrade(String responseGrade) {
		this.responseGrade = responseGrade;
	}
	
	public String getResponseType() {
		return responseType;
	}
	
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getStatus() {
		return calloutStatus != null ? calloutStatus : "Closed";
	}
	
	public void setStatus(String calloutStatus) {
		this.calloutStatus = calloutStatus;
	}
}
