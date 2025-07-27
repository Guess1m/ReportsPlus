package com.Guess.ReportsPlus.logs.Callout;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "calloutReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class CalloutReports {
	@XmlElement(name = "logs")
	private List<CalloutReport> calloutReportList;

	public List<CalloutReport> getCalloutReportList() {
		return calloutReportList;
	}

	public void setCalloutReportList(List<CalloutReport> calloutReportList) {
		this.calloutReportList = calloutReportList;
	}
}
