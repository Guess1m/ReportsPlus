package com.Guess.ReportsPlus.logs.TrafficCitation;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "trafficCitationReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrafficCitationReports {
	@XmlElement(name = "logs")
	private List<TrafficCitationReport> TrafficCitationReportList;

	public List<TrafficCitationReport> getTrafficCitationReportList() {
		return TrafficCitationReportList;
	}

	public void setTrafficCitationReportList(List<TrafficCitationReport> TrafficCitationReportList) {
		this.TrafficCitationReportList = TrafficCitationReportList;
	}
}
