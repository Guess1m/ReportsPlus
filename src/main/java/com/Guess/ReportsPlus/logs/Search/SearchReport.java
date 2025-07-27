package com.Guess.ReportsPlus.logs.Search;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logs")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchReport {
	@XmlElement(name = "SearchNumber")
	private String SearchNumber;
	@XmlElement(name = "searchDate")
	private String searchDate;
	@XmlElement(name = "searchStatus")
	private String searchStatus;
	@XmlElement(name = "searchTime")
	private String searchTime;
	@XmlElement(name = "searchSeizedItems")
	private String searchSeizedItems;
	@XmlElement(name = "searchGrounds")
	private String searchGrounds;
	@XmlElement(name = "searchType")
	private String searchType;
	@XmlElement(name = "searchMethod")
	private String searchMethod;
	@XmlElement(name = "searchWitnesses")
	private String searchWitnesses;
	@XmlElement(name = "officerRank")
	private String officerRank;
	@XmlElement(name = "officerName")
	private String officerName;
	@XmlElement(name = "officerNumber")
	private String officerNumber;
	@XmlElement(name = "officerAgency")
	private String officerAgency;
	@XmlElement(name = "officerDivision")
	private String officerDivision;
	@XmlElement(name = "searchStreet")
	private String searchStreet;
	@XmlElement(name = "searchArea")
	private String searchArea;
	@XmlElement(name = "searchCounty")
	private String searchCounty;
	@XmlElement(name = "searchComments")
	private String searchComments;
	@XmlElement(name = "searchedPersons")
	private String searchedPersons;
	@XmlElement(name = "testsConducted")
	private String testsConducted;
	@XmlElement(name = "testResults")
	private String testResults;
	@XmlElement(name = "breathalyzerBACMeasure")
	private String breathalyzerBACMeasure;

	public String getSearchComments() {
		return searchComments;
	}

	public void setSearchComments(String searchComments) {
		this.searchComments = searchComments;
	}

	public String getBreathalyzerBACMeasure() {
		return breathalyzerBACMeasure;
	}

	public void setBreathalyzerBACMeasure(String breathalyzerBACMeasure) {
		this.breathalyzerBACMeasure = breathalyzerBACMeasure;
	}

	public String getOfficerAgency() {
		return officerAgency;
	}

	public void setOfficerAgency(String officerAgency) {
		this.officerAgency = officerAgency;
	}

	public String getOfficerDivision() {
		return officerDivision;
	}

	public void setOfficerDivision(String officerDivision) {
		this.officerDivision = officerDivision;
	}

	public String getOfficerName() {
		return officerName;
	}

	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}

	public String getOfficerNumber() {
		return officerNumber;
	}

	public void setOfficerNumber(String officerNumber) {
		this.officerNumber = officerNumber;
	}

	public String getOfficerRank() {
		return officerRank;
	}

	public void setOfficerRank(String officerRank) {
		this.officerRank = officerRank;
	}

	public String getArea() {
		return searchArea;
	}

	public void setSearchArea(String searchArea) {
		this.searchArea = searchArea;
	}

	public String getCounty() {
		return searchCounty;
	}

	public void setSearchCounty(String searchCounty) {
		this.searchCounty = searchCounty;
	}

	public String getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	public String getSearchedPersons() {
		return searchedPersons;
	}

	public void setSearchedPersons(String searchedPersons) {
		this.searchedPersons = searchedPersons;
	}

	public String getSearchGrounds() {
		return searchGrounds;
	}

	public void setSearchGrounds(String searchGrounds) {
		this.searchGrounds = searchGrounds;
	}

	public String getSearchMethod() {
		return searchMethod;
	}

	public void setSearchMethod(String searchMethod) {
		this.searchMethod = searchMethod;
	}

	public String getSearchNumber() {
		return SearchNumber;
	}

	public void setSearchNumber(String searchNumber) {
		SearchNumber = searchNumber;
	}

	public String getSearchSeizedItems() {
		return searchSeizedItems;
	}

	public void setSearchSeizedItems(String searchSeizedItems) {
		this.searchSeizedItems = searchSeizedItems;
	}

	public String getSearchStreet() {
		return searchStreet;
	}

	public void setSearchStreet(String searchStreet) {
		this.searchStreet = searchStreet;
	}

	public String getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchWitnesses() {
		return searchWitnesses;
	}

	public void setSearchWitnesses(String searchWitnesses) {
		this.searchWitnesses = searchWitnesses;
	}

	public String getTestResults() {
		return testResults;
	}

	public void setTestResults(String testResults) {
		this.testResults = testResults;
	}

	public String getTestsConducted() {
		return testsConducted;
	}

	public void setTestsConducted(String testsConducted) {
		this.testsConducted = testsConducted;
	}

	public String getStatus() {
		return searchStatus != null ? searchStatus : "Closed";
	}

	public void setStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
}
