package com.Guess.ReportsPlus.logs.Incident;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "incidentReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class IncidentReports {
	
	@XmlElement(name = "logs")
	private List<IncidentReport> IncidentReportList;
	
	public List<IncidentReport> getIncidentReportList() {
		return IncidentReportList;
	}
	
	public void setIncidentReportList(List<IncidentReport> IncidentReportList) {
		this.IncidentReportList = IncidentReportList;
	}
}
