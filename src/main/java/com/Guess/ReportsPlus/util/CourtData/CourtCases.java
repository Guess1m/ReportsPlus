package com.Guess.ReportsPlus.util.CourtData;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CourtCases")
@XmlAccessorType(XmlAccessType.FIELD)
public class CourtCases {
	@XmlElement(name = "Case")
	private List<Case> caseList;

	public List<Case> getCaseList() {
		return caseList;
	}

	public void setCaseList(List<Case> caseList) {
		this.caseList = caseList;
	}
}
