package com.drozal.dataterminal.util.server.Objects.CourtData;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Case {
	
	@XmlElement(name = "CaseNumber")
	private String caseNumber;
	
	@XmlElement(name = "CourtDate")
	private String courtDate;
	
	@XmlElement(name = "Name")
	private String name;
	
	@XmlElement(name = "OffenceDate")
	private String offenceDate;
	
	@XmlElement(name = "Age")
	private String age;
	
	@XmlElement(name = "OffenceLocation")
	private String offenceLocation;
	
	@XmlElement(name = "Notes")
	private String notes;
	
	@XmlElement(name = "Offences")
	private String offences;
	
	@XmlElement(name = "Outcomes")
	private String outcomes;
	
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
	
	public String getOffenceLocation() {
		return offenceLocation;
	}
	
	public void setOffenceLocation(String offenceLocation) {
		this.offenceLocation = offenceLocation;
	}
	
}