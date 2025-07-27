package com.Guess.ReportsPlus.logs.Impound;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "impoundReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImpoundReports {
	@XmlElement(name = "logs")
	private List<ImpoundReport> ImpoundReportList;

	public List<ImpoundReport> getImpoundReportList() {
		return ImpoundReportList;
	}

	public void setImpoundReportList(List<ImpoundReport> ImpoundReportList) {
		this.ImpoundReportList = ImpoundReportList;
	}
}
