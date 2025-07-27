package com.Guess.ReportsPlus.logs.Incident;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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
