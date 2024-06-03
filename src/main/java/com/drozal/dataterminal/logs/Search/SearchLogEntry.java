package com.drozal.dataterminal.logs.Search;

/**
 * The type Search log entry.
 */
public class SearchLogEntry {
	/**
	 * The Search number.
	 */
	public String SearchNumber;
	/**
	 * The Search date.
	 */
	public String searchDate;
	/**
	 * The Search time.
	 */
	public String searchTime;
	/**
	 * The Search seized items.
	 */
	public String searchSeizedItems;
	/**
	 * The Search grounds.
	 */
	public String searchGrounds;
	/**
	 * The Search type.
	 */
	public String searchType;
	/**
	 * The Search method.
	 */
	public String searchMethod;
	/**
	 * The Search witnesses.
	 */
	public String searchWitnesses;
	/**
	 * The Officer rank.
	 */
	public String officerRank;
	/**
	 * The Officer name.
	 */
	public String officerName;
	/**
	 * The Officer number.
	 */
	public String officerNumber;
	/**
	 * The Officer agency.
	 */
	public String officerAgency;
	/**
	 * The Officer division.
	 */
	public String officerDivision;
	/**
	 * The Search street.
	 */
	public String searchStreet;
	/**
	 * The Search area.
	 */
	public String searchArea;
	/**
	 * The Search county.
	 */
	public String searchCounty;
	/**
	 * The Search comments.
	 */
	public String searchComments;
	/**
	 * The Searched persons.
	 */
	public String searchedPersons;
	/**
	 * The Tests conducted.
	 */
	public String testsConducted;
	/**
	 * The Test results.
	 */
	public String testResults;
	/**
	 * The Breathalyzer bac measure.
	 */
	public String breathalyzerBACMeasure;
	
	/**
	 * Instantiates a new Search log entry.
	 */
	public SearchLogEntry() {
	}
	
	/**
	 * Instantiates a new Search log entry.
	 *
	 * @param searchNumber           the search number
	 * @param searchedPersons        the searched persons
	 * @param searchDate             the search date
	 * @param searchTime             the search time
	 * @param searchSeizedItems      the search seized items
	 * @param searchGrounds          the search grounds
	 * @param searchType             the search type
	 * @param searchMethod           the search method
	 * @param searchWitnesses        the search witnesses
	 * @param officerRank            the officer rank
	 * @param officerName            the officer name
	 * @param officerNumber          the officer number
	 * @param officerAgency          the officer agency
	 * @param officerDivision        the officer division
	 * @param searchStreet           the search street
	 * @param searchArea             the search area
	 * @param searchCounty           the search county
	 * @param searchComments         the search comments
	 * @param testsConducted         the tests conducted
	 * @param testResults            the test results
	 * @param breathalyzerBACMeasure the breathalyzer bac measure
	 */
	public SearchLogEntry(String searchNumber, String searchedPersons, String searchDate, String searchTime, String searchSeizedItems, String searchGrounds, String searchType, String searchMethod, String searchWitnesses, String officerRank, String officerName, String officerNumber, String officerAgency, String officerDivision, String searchStreet, String searchArea, String searchCounty, String searchComments, String testsConducted, String testResults, String breathalyzerBACMeasure) {
		this.SearchNumber = searchNumber;
		this.searchedPersons = searchedPersons;
		this.searchDate = searchDate;
		this.searchTime = searchTime;
		this.searchSeizedItems = searchSeizedItems;
		this.searchGrounds = searchGrounds;
		this.searchType = searchType;
		this.searchMethod = searchMethod;
		this.searchWitnesses = searchWitnesses;
		this.officerRank = officerRank;
		this.officerName = officerName;
		this.officerNumber = officerNumber;
		this.officerAgency = officerAgency;
		this.officerDivision = officerDivision;
		this.searchStreet = searchStreet;
		this.searchArea = searchArea;
		this.searchCounty = searchCounty;
		this.searchComments = searchComments;
		this.testsConducted = testsConducted;
		this.testResults = testResults;
		this.breathalyzerBACMeasure = breathalyzerBACMeasure;
	}
	
	/**
	 * Gets search number.
	 *
	 * @return the search number
	 */
	public String getSearchNumber() {
		return SearchNumber;
	}
	
	/**
	 * Gets search date.
	 *
	 * @return the search date
	 */
	public String getSearchDate() {
		return searchDate;
	}
	
	/**
	 * Gets search time.
	 *
	 * @return the search time
	 */
	public String getSearchTime() {
		return searchTime;
	}
	
	/**
	 * Gets search seized items.
	 *
	 * @return the search seized items
	 */
	public String getSearchSeizedItems() {
		return searchSeizedItems;
	}
	
	/**
	 * Gets search grounds.
	 *
	 * @return the search grounds
	 */
	public String getSearchGrounds() {
		return searchGrounds;
	}
	
	/**
	 * Gets search type.
	 *
	 * @return the search type
	 */
	public String getSearchType() {
		return searchType;
	}
	
	/**
	 * Gets search method.
	 *
	 * @return the search method
	 */
	public String getSearchMethod() {
		return searchMethod;
	}
	
	/**
	 * Gets search witnesses.
	 *
	 * @return the search witnesses
	 */
	public String getSearchWitnesses() {
		return searchWitnesses;
	}
	
	/**
	 * Gets officer rank.
	 *
	 * @return the officer rank
	 */
	public String getOfficerRank() {
		return officerRank;
	}
	
	/**
	 * Gets officer name.
	 *
	 * @return the officer name
	 */
	public String getOfficerName() {
		return officerName;
	}
	
	/**
	 * Gets officer number.
	 *
	 * @return the officer number
	 */
	public String getOfficerNumber() {
		return officerNumber;
	}
	
	/**
	 * Gets officer agency.
	 *
	 * @return the officer agency
	 */
	public String getOfficerAgency() {
		return officerAgency;
	}
	
	/**
	 * Gets officer division.
	 *
	 * @return the officer division
	 */
	public String getOfficerDivision() {
		return officerDivision;
	}
	
	/**
	 * Gets search street.
	 *
	 * @return the search street
	 */
	public String getSearchStreet() {
		return searchStreet;
	}
	
	/**
	 * Gets search area.
	 *
	 * @return the search area
	 */
	public String getSearchArea() {
		return searchArea;
	}
	
	/**
	 * Gets search county.
	 *
	 * @return the search county
	 */
	public String getSearchCounty() {
		return searchCounty;
	}
	
	/**
	 * Gets search comments.
	 *
	 * @return the search comments
	 */
	public String getSearchComments() {
		return searchComments;
	}
	
	/**
	 * Gets searched persons.
	 *
	 * @return the searched persons
	 */
	public String getSearchedPersons() {
		return searchedPersons;
	}
	
	/**
	 * Gets tests conducted.
	 *
	 * @return the tests conducted
	 */
	public String getTestsConducted() {
		return testsConducted;
	}
	
	/**
	 * Gets test results.
	 *
	 * @return the test results
	 */
	public String getTestResults() {
		return testResults;
	}
	
	/**
	 * Gets breathalyzer bac measure.
	 *
	 * @return the breathalyzer bac measure
	 */
	public String getBreathalyzerBACMeasure() {
		return breathalyzerBACMeasure;
	}
}