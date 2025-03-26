package com.Guess.ReportsPlus.logs.TrafficStop;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "trafficStopReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrafficStopReports {
	
	@XmlElement(name = "logs")
	private List<TrafficStopReport> TrafficStopReportList;
	
	public List<TrafficStopReport> getTrafficStopReportList() {
		return TrafficStopReportList;
	}
	
	public void setTrafficStopReportList(List<TrafficStopReport> TrafficStopReportList) {
		this.TrafficStopReportList = TrafficStopReportList;
	}
}
