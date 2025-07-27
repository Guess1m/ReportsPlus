package com.Guess.ReportsPlus.logs.Search;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "searchReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchReports {
	@XmlElement(name = "logs")
	private List<SearchReport> SearchReportList;
	public List<SearchReport> getSearchReportList() {
		return SearchReportList;
	}
	public void setSearchReportList(List<SearchReport> SearchReportList) {
		this.SearchReportList = SearchReportList;
	}
}
