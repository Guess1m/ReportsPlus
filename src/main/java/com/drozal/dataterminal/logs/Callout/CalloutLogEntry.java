package com.drozal.dataterminal.logs.Callout;

import java.util.Arrays;
import java.util.List;

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
	
	public List<String> getAllValues() {
		return Arrays.asList(getDate(), getTime(), getName(), getRank(), getNumber(), getDivision(), getAgency(),
		                     getResponeType(), getResponseGrade(), getCalloutNumber(), getNotesTextArea(), getAddress(),
		                     getCounty(), getArea());
	}
	
	public String getCalloutNumber() {
		return CalloutNumber;
	}
	
	public String getNotesTextArea() {
		return NotesTextArea;
	}
	
	public String getResponseGrade() {
		return ResponseGrade;
	}
	
	public String getResponeType() {
		return ResponeType;
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
	
	public String getAddress() {
		return Address;
	}
	
	public String getCounty() {
		return County;
	}
	
	public String getArea() {
		return Area;
	}
}

