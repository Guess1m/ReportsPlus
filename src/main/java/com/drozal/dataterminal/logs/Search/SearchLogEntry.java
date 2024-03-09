package com.drozal.dataterminal.logs.Search;

// Data model for a log entry
public class SearchLogEntry {
    public String SearchNumber;
    public String searchDate;
    public String searchTime;
    public String searchSeizedItems;
    public String searchGrounds;
    public String searchType;
    public String searchMethod;
    public String searchWitnesses;
    public String officerRank;
    public String officerName;
    public String officerNumber;
    public String officerAgency;
    public String officerDivision;
    public String searchStreet;
    public String searchArea;
    public String searchCounty;
    public String searchComments;
    public String searchedPersons;

    public SearchLogEntry() {
    }

    public String getSearchNumber() {
        return SearchNumber;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public String getSearchSeizedItems() {
        return searchSeizedItems;
    }

    public String getSearchGrounds() {
        return searchGrounds;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getSearchMethod() {
        return searchMethod;
    }

    public String getSearchWitnesses() {
        return searchWitnesses;
    }

    public String getOfficerRank() {
        return officerRank;
    }

    public String getOfficerName() {
        return officerName;
    }

    public String getOfficerNumber() {
        return officerNumber;
    }

    public String getOfficerAgency() {
        return officerAgency;
    }

    public String getOfficerDivision() {
        return officerDivision;
    }

    public String getSearchStreet() {
        return searchStreet;
    }

    public String getSearchArea() {
        return searchArea;
    }

    public String getSearchCounty() {
        return searchCounty;
    }

    public String getSearchComments() {
        return searchComments;
    }

    public String getSearchedPersons() {
        return searchedPersons;
    }

    public SearchLogEntry(String searchNumber, String searchedPersons, String searchDate, String searchTime, String searchSeizedItems, String searchGrounds, String searchType, String searchMethod, String searchWitnesses, String officerRank, String officerName, String officerNumber, String officerAgency, String officerDivision, String searchStreet, String searchArea, String searchCounty, String searchComments) {
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
    }
}