package com.Guess.ReportsPlus.util.Strings;

public class reportLayoutTemplates {
	public static String calloutReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Officer information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Name\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"officername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Rank\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Num\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"officernumber\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callsign\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Agency\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Division\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"Division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Callout information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Time\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callout Number\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Respond Code\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"code\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"County\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\r\n" +
			"        \"populateKey\" : \"county\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Area\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\r\n" +
			"        \"populateKey\" : \"area\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Street\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\r\n" +
			"        \"populateKey\" : \"street\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Description\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : null,\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Notes\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String patrolReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Officer information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"officername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Rank\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Num\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"officernumber\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callsign\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Agency\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Division\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Shift Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Start Time\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"starttime\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"End Time\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"endtime\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Length\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"length\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Patrol Number\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Activity\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Traffic Stops\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"stops\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Citations\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"citations\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callouts\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"callouts\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Arrests\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"arrests\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Notes\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Vehicle Info\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"vehicleinfo\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String incidentReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Officer information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Officer Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"officername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Rank\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Num\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"officernumber\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callsign\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Agency\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Division\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Timestamp / Location Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Date\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Time\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Incident Num\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Street\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\r\n" +
			"        \"populateKey\" : \"street\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Area\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\r\n" +
			"        \"populateKey\" : \"area\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"County\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\r\n" +
			"        \"populateKey\" : \"county\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Suspect Information (If Applicable)\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Suspect Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"name\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect DOB\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"dob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"gender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"address\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Suspect Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"description\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Statement\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"suspectstatement\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Victim Information (If Applicable)\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Victim Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim DOB\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimdob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimgender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimaddress\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Victim Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"victimdescription\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Statement\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"victimstatement\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Summary / Notes\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Summary\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"summary\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String deathReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Reporting Officer\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Officer Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"officername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Rank\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Num\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"officernumber\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callsign\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Agency\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Division\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Timestamp Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Date\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Time\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Report Num\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Deceased Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"name\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date of Birth\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"dob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"gender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"address\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"description\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Other\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"other\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Death Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Street\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\r\n" +
			"        \"populateKey\" : \"street\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Area\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\r\n" +
			"        \"populateKey\" : \"area\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"County\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\r\n" +
			"        \"populateKey\" : \"county\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Time Of Death\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date Of Death\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Identification Method\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"idmethod\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Cause Of Death\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"causeofdeath\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Mode Of Death\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"modeofdeath\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Body Position\",\r\n" +
			"        \"size\" : 8,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"bodyposition\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Signs Of Struggle\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"signsofstruggle\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Visible Injuries\",\r\n" +
			"        \"size\" : 7,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"injuries\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Body Condition\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"bodycondition\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Witness Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Witness Name\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"witnessname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Witness Statement\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"statement\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Summary / Notes\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Summary\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"summary\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String accidentReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Reporting Officer\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Officer Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"officername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Rank\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Num\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"officernumber\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callsign\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Agency\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Division\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Timestamp Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Date\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Time\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Report Num\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Suspect Details\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Suspect Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"suspectname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect DOB\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"suspectdob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"suspectgender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"suspectaddress\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Suspect Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"suspectdescription\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Other\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"suspectother\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"blank_805919\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"BLANK_SPACE\",\r\n" +
			"        \"populateKey\" : null,\r\n" +
			"        \"nodeType\" : \"BLANK_SPACE\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Suspect Plate\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"suspectplate\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"PLATE_NUMBER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Model\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"suspectmodel\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"MODEL\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Type\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_TYPE\",\r\n" +
			"        \"populateKey\" : \"suspecttype\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"VEHICLE_TYPE\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Suspect Color\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_COLOR\",\r\n" +
			"        \"populateKey\" : \"suspectcolor\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Estimated Speed\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"suspectspeed\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Vehicle Drivable\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"suspectdrivable\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Suspect Other Info\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"suspectotherinfo\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Suspect Damanges\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"suspectdamages\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Both\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Victim Details\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Victim Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim DOB\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimdob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimgender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimaddress\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Victim Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"victimdescription\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Other\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"victimother\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"blank_926579\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"BLANK_SPACE\",\r\n" +
			"        \"populateKey\" : null,\r\n" +
			"        \"nodeType\" : \"BLANK_SPACE\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Victim Plate\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimplate\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"PLATE_NUMBER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Model\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimmodel\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"MODEL\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Type\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_TYPE\",\r\n" +
			"        \"populateKey\" : \"victimtype\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"VEHICLE_TYPE\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Victim Color\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_COLOR\",\r\n" +
			"        \"populateKey\" : \"victimcolor\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Estimated Speed\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"victimspeed\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Vehicle Drivable\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"victimdrivable\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Victim Other Info\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"victimotherinfo\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Victim Damanges\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"victimdamages\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Both\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Accident Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Street\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\r\n" +
			"        \"populateKey\" : \"street\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Area\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\r\n" +
			"        \"populateKey\" : \"area\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"County\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\r\n" +
			"        \"populateKey\" : \"county\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Time Of Crash\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date Of Crash\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Fatal\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"fatal\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Weather Conditions\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"weatherconditions\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Roadconditions\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"roadconditions\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Witness Information (If Applicable)\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Witness Name\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"witnessname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Witness Statement\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"statement\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Summary / Notes\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Summary\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"summary\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String searchReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Officer information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Officer Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"officername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Rank\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Num\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"officernumber\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callsign\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Agency\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Division\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Timestamp / Location Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Date\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Time\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Report Num\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Street\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\r\n" +
			"        \"populateKey\" : \"street\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Area\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\r\n" +
			"        \"populateKey\" : \"area\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"County\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\r\n" +
			"        \"populateKey\" : \"county\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Vehicle Search Information (If Applicable)\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Plate Number\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"plate\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"PLATE_NUMBER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Model\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"model\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"MODEL\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Type\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_TYPE\",\r\n" +
			"        \"populateKey\" : \"type\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"VEHICLE_TYPE\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Color\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_COLOR\",\r\n" +
			"        \"populateKey\" : \"color\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Vehicle Search Method\",\r\n" +
			"        \"size\" : 9,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"searchmethod\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Other Vehicle Info\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"othervehicleinfo\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Seized Vehicle Items\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"seizedvehicleitems\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Vehicle\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Personal Search Information (If Applicable)\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"name\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date of Birth\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"dob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"gender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"address\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"description\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Search Method\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"searchmethod\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Other Info\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"otherinfo\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Seized Items\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"seizeditems\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Summary / Notes\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Summary\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"summary\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String impoundReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Reporting Officer\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Officer Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"officername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Rank\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Num\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"officernumber\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callsign\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Agency\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Division\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Timestamp / Location Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Street\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\r\n" +
			"        \"populateKey\" : \"street\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Area\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\r\n" +
			"        \"populateKey\" : \"area\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"County\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\r\n" +
			"        \"populateKey\" : \"county\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Time\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Report Num\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Owner / Vehicle Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Owner Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"name\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Owner Age\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"dob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"AGE\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Owner Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"gender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Owner Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"address\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Owner Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"description\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Owner Other Details\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"other\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"blank_389065\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"BLANK_SPACE\",\r\n" +
			"        \"populateKey\" : null,\r\n" +
			"        \"nodeType\" : \"BLANK_SPACE\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Plate Num\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"plate\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"PLATE_NUMBER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Model\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"model\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"MODEL\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Type\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_TYPE\",\r\n" +
			"        \"populateKey\" : \"type\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"VEHICLE_TYPE\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Color\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_COLOR\",\r\n" +
			"        \"populateKey\" : \"color\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Vehicle Drivable\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"vehicledrivable\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Search Conducted\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"searchconducted\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Both\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Driver Information (If Applicable)\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Driver Is Owner\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"driverisowner\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"blank_121274\",\r\n" +
			"        \"size\" : 8,\r\n" +
			"        \"fieldType\" : \"BLANK_SPACE\",\r\n" +
			"        \"populateKey\" : null,\r\n" +
			"        \"nodeType\" : \"BLANK_SPACE\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Driver Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"drivername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Driver DOB\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"dob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Driver Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"drivergender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Driver Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"driveraddress\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Driver Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"driverdescription\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Driver Other Details\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"driverother\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Summary / Notes (If Applicable)\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Summary\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"summary\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String trafficStopReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Reporting Officer\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Officer Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"officername\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Rank\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Num\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"officernumber\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Callsign\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Agency\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Division\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Timestamp / Location Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Date\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Time\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Report Num\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Street\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\r\n" +
			"        \"populateKey\" : \"street\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Area\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\r\n" +
			"        \"populateKey\" : \"area\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"County\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\r\n" +
			"        \"populateKey\" : \"county\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Suspect Details\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"name\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date of Birth\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"dob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Gender\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"gender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Address\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"address\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"ADDRESS\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"description\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Other Info\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"other\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Vehicle Details\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Plate Number\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"plate\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"PLATE_NUMBER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Model\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"model\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"MODEL\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Type\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_TYPE\",\r\n" +
			"        \"populateKey\" : \"type\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"VEHICLE_TYPE\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Color\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_COLOR\",\r\n" +
			"        \"populateKey\" : \"color\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Other Vehicle Info\",\r\n" +
			"        \"size\" : 8,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"othervehicleinfo\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Vehicle\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Stop Information (Where Applicable)\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Reason For Stop\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"reasonforstop\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Actions Taken\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"actionstaken\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Evidence\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"evidence\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"blank_831619\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"BLANK_SPACE\",\r\n" +
			"        \"populateKey\" : null,\r\n" +
			"        \"nodeType\" : \"BLANK_SPACE\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Warning Issued\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"warningissued\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Citation Issued\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"citationissued\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Arrest Made\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"arrestmade\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Outcome / Notes\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Outcome\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"outcome\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String useOfForceReport = "{\r\n" +
			"  \"layout\" : [ {\r\n" +
			"    \"sectionTitle\" : \"Incident Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Incident Number\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\r\n" +
			"        \"populateKey\" : \"number\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Date of Incident\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\r\n" +
			"        \"populateKey\" : \"date\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Time of Incident\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\r\n" +
			"        \"populateKey\" : \"time\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Street\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\r\n" +
			"        \"populateKey\" : \"street\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Area\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\r\n" +
			"        \"populateKey\" : \"area\",\r\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"County\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\r\n" +
			"        \"populateKey\" : \"county\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Reporting Officer Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Officer Number\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\r\n" +
			"        \"populateKey\" : \"onum\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Name\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\r\n" +
			"        \"populateKey\" : \"oname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Rank\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\r\n" +
			"        \"populateKey\" : \"rank\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Officer Agency\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\r\n" +
			"        \"populateKey\" : \"agency\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Division\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\r\n" +
			"        \"populateKey\" : \"division\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Officer Callsign\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\r\n" +
			"        \"populateKey\" : \"callsign\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Subject Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Subject First Name\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"firstname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FIRST_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Subject Last Name\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"lastname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"LAST_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Subject DOB\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"dob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Subject Gender\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"gender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Subject Description\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"description\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Subject Other Info\",\r\n" +
			"        \"size\" : 6,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"otherinfo\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Use of Force Details\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Type of Force\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"typeofforce\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Level of Force\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"levelofforce\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Injury Level\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"injurylevel\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Weapon Used\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"weaponused\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Subject Behavior\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"subjectbehavior\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Force Justification\",\r\n" +
			"        \"size\" : 5,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"forcejustification\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Force Effective\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\r\n" +
			"        \"populateKey\" : \"forceeffective\",\r\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Reason for Force\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"reason\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Subject Injuries\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"injuries\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Witness Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Witness Name\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"witnessname\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"FULL_NAME\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Witness DOB\",\r\n" +
			"        \"size\" : 2,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"witnessdob\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DOB\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Witness Gender\",\r\n" +
			"        \"size\" : 3,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"witnessgender\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"GENDER\"\r\n" +
			"      }, {\r\n" +
			"        \"fieldName\" : \"Witness Description\",\r\n" +
			"        \"size\" : 4,\r\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\r\n" +
			"        \"populateKey\" : \"witnessdesc\",\r\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"DESCRIPTION\"\r\n" +
			"      } ]\r\n" +
			"    }, {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Witness Other Info\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"witnessotherinfo\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : \"None\"\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : true,\r\n" +
			"    \"lookupType\" : \"Ped\"\r\n" +
			"  }, {\r\n" +
			"    \"sectionTitle\" : \"Additional Information\",\r\n" +
			"    \"rowConfigs\" : [ {\r\n" +
			"      \"fieldConfigs\" : [ {\r\n" +
			"        \"fieldName\" : \"Notes\",\r\n" +
			"        \"size\" : 12,\r\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\r\n" +
			"        \"populateKey\" : \"notes\",\r\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\r\n" +
			"        \"dropdownType\" : null,\r\n" +
			"        \"lookupValue\" : null\r\n" +
			"      } ]\r\n" +
			"    } ],\r\n" +
			"    \"pullFromLookup\" : false,\r\n" +
			"    \"lookupType\" : null\r\n" +
			"  } ]\r\n" +
			"}";
	public static String arrestReport = "{\n" +
			"  \"layout\" : [ {\n" +
			"    \"sectionTitle\" : \"Reporting Officer\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Officer Name\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\n" +
			"        \"populateKey\" : \"officername\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Rank\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\n" +
			"        \"populateKey\" : \"rank\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Officer Num\",\n" +
			"        \"size\" : 2,\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\n" +
			"        \"populateKey\" : \"officernumber\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Callsign\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\n" +
			"        \"populateKey\" : \"callsign\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Agency\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\n" +
			"        \"populateKey\" : \"agency\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Division\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\n" +
			"        \"populateKey\" : \"division\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Timestamp / Location Information\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Date\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\n" +
			"        \"populateKey\" : \"date\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Time\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\n" +
			"        \"populateKey\" : \"time\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Report Num\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\n" +
			"        \"populateKey\" : \"number\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Street\",\n" +
			"        \"size\" : 5,\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\n" +
			"        \"populateKey\" : \"street\",\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Area\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\n" +
			"        \"populateKey\" : \"area\",\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"County\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\n" +
			"        \"populateKey\" : \"county\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Suspect Details\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Name\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"name\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"FULL_NAME\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Date of Birth\",\n" +
			"        \"size\" : 2,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"dob\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"DOB\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Gender\",\n" +
			"        \"size\" : 2,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"gender\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"GENDER\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Address\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"address\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"ADDRESS\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Description\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"description\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"DESCRIPTION\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Other Info\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"other\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"None\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : true,\n" +
			"    \"lookupType\" : \"Ped\"\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Medical Details (Where Applicable)\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Use of Force Applied\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\n" +
			"        \"populateKey\" : \"useofforceapplied\",\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Weapons Utilized\",\n" +
			"        \"size\" : 8,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"weaponutilized\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Other Information\",\n" +
			"        \"size\" : 12,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"otherinformation\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"blank_481360\",\n" +
			"        \"size\" : 12,\n" +
			"        \"fieldType\" : \"BLANK_SPACE\",\n" +
			"        \"populateKey\" : null,\n" +
			"        \"nodeType\" : \"BLANK_SPACE\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Injury Type\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"injurytype\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Refused Medical\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\n" +
			"        \"populateKey\" : \"refusedmedical\",\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"EMS Transport\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\n" +
			"        \"populateKey\" : \"emstransport\",\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Injury Details\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"injurydetails\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Transport Details\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"transportdetails\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Arrest Details\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"charges_tree_view_385381\",\n" +
			"        \"size\" : 12,\n" +
			"        \"fieldType\" : \"CHARGES_TREE_VIEW\",\n" +
			"        \"populateKey\" : null,\n" +
			"        \"nodeType\" : \"TREE_VIEW\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : {\n" +
			"          \"SUSPECT_NAME\" : \"Name\",\n" +
			"          \"NOTES\" : \"Notes\",\n" +
			"          \"OFFENSE_TIME\" : \"Time\",\n" +
			"          \"SUSPECT_AGE\" : \"Date of Birth\",\n" +
			"          \"SUSPECT_GENDER\" : \"Gender\",\n" +
			"          \"AREA\" : \"Area\",\n" +
			"          \"CASE_NUMBER\" : \"Report Num\",\n" +
			"          \"SUSPECT_ADDRESS\" : \"Address\",\n" +
			"          \"OFFENSE_DATE\" : \"Date\",\n" +
			"          \"STREET\" : \"Street\",\n" +
			"          \"COUNTY\" : \"County\"\n" +
			"        }\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Self Transported\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"CHECK_BOX\",\n" +
			"        \"populateKey\" : \"selftransported\",\n" +
			"        \"nodeType\" : \"CHECK_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Transporting Officer\",\n" +
			"        \"size\" : 8,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"transportingofficer\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Notes / Comments\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Notes\",\n" +
			"        \"size\" : 12,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"notes\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  } ]\n" +
			"}";
	public static String citationReport = "{\n" +
			"  \"layout\" : [ {\n" +
			"    \"sectionTitle\" : \"Reporting Officer\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Officer Name\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"OFFICER_NAME\",\n" +
			"        \"populateKey\" : \"officername\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Rank\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"OFFICER_RANK\",\n" +
			"        \"populateKey\" : \"rank\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Officer Num\",\n" +
			"        \"size\" : 2,\n" +
			"        \"fieldType\" : \"OFFICER_NUMBER\",\n" +
			"        \"populateKey\" : \"officernumber\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Callsign\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"OFFICER_CALLSIGN\",\n" +
			"        \"populateKey\" : \"callsign\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Agency\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"OFFICER_AGENCY\",\n" +
			"        \"populateKey\" : \"agency\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Division\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"OFFICER_DIVISION\",\n" +
			"        \"populateKey\" : \"division\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Timestamp / Location Information\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Date\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"DATE_FIELD\",\n" +
			"        \"populateKey\" : \"date\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Time\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"TIME_FIELD\",\n" +
			"        \"populateKey\" : \"time\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Report Num\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"NUMBER_FIELD\",\n" +
			"        \"populateKey\" : \"number\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Street\",\n" +
			"        \"size\" : 5,\n" +
			"        \"fieldType\" : \"COMBO_BOX_STREET\",\n" +
			"        \"populateKey\" : \"street\",\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Area\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"COMBO_BOX_AREA\",\n" +
			"        \"populateKey\" : \"area\",\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"County\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"COUNTY_FIELD\",\n" +
			"        \"populateKey\" : \"county\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Suspect Details\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Name\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"name\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"FULL_NAME\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Date of Birth\",\n" +
			"        \"size\" : 2,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"dob\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"DOB\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Gender\",\n" +
			"        \"size\" : 2,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"gender\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"GENDER\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Address\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"address\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"ADDRESS\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Description\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"description\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"DESCRIPTION\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Other Info\",\n" +
			"        \"size\" : 6,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"other\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"None\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : true,\n" +
			"    \"lookupType\" : \"Ped\"\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Vehicle Details\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Plate Number\",\n" +
			"        \"size\" : 3,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"plate\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"PLATE_NUMBER\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Model\",\n" +
			"        \"size\" : 5,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"model\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"MODEL\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Type\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"COMBO_BOX_TYPE\",\n" +
			"        \"populateKey\" : \"type\",\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"VEHICLE_TYPE\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Color\",\n" +
			"        \"size\" : 4,\n" +
			"        \"fieldType\" : \"COMBO_BOX_COLOR\",\n" +
			"        \"populateKey\" : \"color\",\n" +
			"        \"nodeType\" : \"COMBO_BOX\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"None\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      }, {\n" +
			"        \"fieldName\" : \"Other Vehicle Info\",\n" +
			"        \"size\" : 8,\n" +
			"        \"fieldType\" : \"TEXT_FIELD\",\n" +
			"        \"populateKey\" : \"othervehicleinfo\",\n" +
			"        \"nodeType\" : \"TEXT_FIELD\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : \"None\",\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : true,\n" +
			"    \"lookupType\" : \"Vehicle\"\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Citation Details\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"citation_tree_view_362791\",\n" +
			"        \"size\" : 12,\n" +
			"        \"fieldType\" : \"CITATION_TREE_VIEW\",\n" +
			"        \"populateKey\" : null,\n" +
			"        \"nodeType\" : \"TREE_VIEW\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : {\n" +
			"          \"SUSPECT_NAME\" : \"Name\",\n" +
			"          \"NOTES\" : \"Notes\",\n" +
			"          \"OFFENSE_TIME\" : \"Time\",\n" +
			"          \"SUSPECT_AGE\" : \"Date of Birth\",\n" +
			"          \"AREA\" : \"Area\",\n" +
			"          \"SUSPECT_GENDER\" : \"Gender\",\n" +
			"          \"CASE_NUMBER\" : \"Report Num\",\n" +
			"          \"SUSPECT_ADDRESS\" : \"Address\",\n" +
			"          \"OFFENSE_DATE\" : \"Date\",\n" +
			"          \"STREET\" : \"Street\",\n" +
			"          \"PLATE_NUMBER\" : \"Plate Number\",\n" +
			"          \"COUNTY\" : \"County\"\n" +
			"        }\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  }, {\n" +
			"    \"sectionTitle\" : \"Notes / Comments\",\n" +
			"    \"rowConfigs\" : [ {\n" +
			"      \"fieldConfigs\" : [ {\n" +
			"        \"fieldName\" : \"Notes\",\n" +
			"        \"size\" : 12,\n" +
			"        \"fieldType\" : \"TEXT_AREA\",\n" +
			"        \"populateKey\" : \"notes\",\n" +
			"        \"nodeType\" : \"TEXT_AREA\",\n" +
			"        \"dropdownType\" : null,\n" +
			"        \"lookupValue\" : null,\n" +
			"        \"courtCaseMapping\" : null\n" +
			"      } ]\n" +
			"    } ],\n" +
			"    \"pullFromLookup\" : false,\n" +
			"    \"lookupType\" : null\n" +
			"  } ]\n" +
			"}";
}