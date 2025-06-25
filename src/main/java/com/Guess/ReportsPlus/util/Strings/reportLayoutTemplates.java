package com.Guess.ReportsPlus.util.Strings;

public class reportLayoutTemplates {

	public static String calloutReport = "{\n" +
			"  \"layout\": [\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Officer information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Name\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"OFFICER_NAME\",\n" +
			"              \"populateKey\": \"officername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Rank\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_RANK\",\n" +
			"              \"populateKey\": \"rank\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Num\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_NUMBER\",\n" +
			"              \"populateKey\": \"officernumber\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callsign\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_CALLSIGN\",\n" +
			"              \"populateKey\": \"callsign\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Agency\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_AGENCY\",\n" +
			"              \"populateKey\": \"agency\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Division\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_DIVISION\",\n" +
			"              \"populateKey\": \"Division\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Callout information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Time\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Date\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callout Number\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"NUMBER_FIELD\",\n" +
			"              \"populateKey\": \"num\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Respond Code\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"code\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"County\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COUNTY_FIELD\",\n" +
			"              \"populateKey\": \"county\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Area\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_AREA\",\n" +
			"              \"populateKey\": \"area\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Street\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"COMBO_BOX_STREET\",\n" +
			"              \"populateKey\": \"street\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Description\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": null,\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Notes\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Notes\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"notes\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}";
	public static String patrolReport = "{\n" +
			"  \"layout\": [\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Officer information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"OFFICER_NAME\",\n" +
			"              \"populateKey\": \"officername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Rank\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_RANK\",\n" +
			"              \"populateKey\": \"rank\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Num\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_NUMBER\",\n" +
			"              \"populateKey\": \"officernumber\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callsign\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_CALLSIGN\",\n" +
			"              \"populateKey\": \"callsign\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Agency\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_AGENCY\",\n" +
			"              \"populateKey\": \"agency\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Division\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_DIVISION\",\n" +
			"              \"populateKey\": \"division\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Shift Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Start Time\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"starttime\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"End Time\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"endtime\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Length\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"length\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Date\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Patrol Number\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"NUMBER_FIELD\",\n" +
			"              \"populateKey\": \"number\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Activity\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Traffic Stops\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"stops\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Citations\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"citations\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callouts\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"callouts\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Arrests\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"arrests\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Notes\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Notes\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"notes\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Vehicle Info\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"vehicleinfo\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}\n" +
			"";
	public static String incidentReport = "{\n" +
			"  \"layout\": [\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Officer information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"OFFICER_NAME\",\n" +
			"              \"populateKey\": \"officername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Rank\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_RANK\",\n" +
			"              \"populateKey\": \"rank\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Num\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_NUMBER\",\n" +
			"              \"populateKey\": \"officernumber\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callsign\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_CALLSIGN\",\n" +
			"              \"populateKey\": \"callsign\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Agency\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_AGENCY\",\n" +
			"              \"populateKey\": \"agency\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Division\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_DIVISION\",\n" +
			"              \"populateKey\": \"division\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Timestamp / Location Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Date\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Time\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Incident Num\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"NUMBER_FIELD\",\n" +
			"              \"populateKey\": \"number\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Street\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"COMBO_BOX_STREET\",\n" +
			"              \"populateKey\": \"street\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Area\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_AREA\",\n" +
			"              \"populateKey\": \"area\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"County\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COUNTY_FIELD\",\n" +
			"              \"populateKey\": \"county\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Suspect Information (If Applicable)\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"name\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"age\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"gender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"address\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"description\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Statement\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"suspectstatement\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Victim Information (If Applicable)\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimname\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimage\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimgender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimaddress\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"victimdescription\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Statement\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"victimstatement\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Summary / Notes\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Summary\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"summary\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Notes\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"notes\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}";
	public static String deathReport = "{\n" +
			"  \"layout\": [\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Reporting Officer\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"OFFICER_NAME\",\n" +
			"              \"populateKey\": \"officername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Rank\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_RANK\",\n" +
			"              \"populateKey\": \"rank\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Num\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_NUMBER\",\n" +
			"              \"populateKey\": \"officernumber\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callsign\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_CALLSIGN\",\n" +
			"              \"populateKey\": \"callsign\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Agency\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_AGENCY\",\n" +
			"              \"populateKey\": \"agency\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Division\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_DIVISION\",\n" +
			"              \"populateKey\": \"division\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Timestamp Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Date\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Time\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Report Num\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"NUMBER_FIELD\",\n" +
			"              \"populateKey\": \"number\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Deceased Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"name\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"age\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"gender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"address\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"description\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Other\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"other\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Death Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Street\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"COMBO_BOX_STREET\",\n" +
			"              \"populateKey\": \"street\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Area\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_AREA\",\n" +
			"              \"populateKey\": \"area\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"County\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COUNTY_FIELD\",\n" +
			"              \"populateKey\": \"county\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Time Of Death\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Date Of Death\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Identification Method\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"idmethod\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Cause Of Death\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"causeofdeath\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Mode Of Death\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"modeofdeath\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Body Position\",\n" +
			"              \"size\": 8,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"bodyposition\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Signs Of Struggle\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"signsofstruggle\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Visible Injuries\",\n" +
			"              \"size\": 7,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"injuries\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Body Condition\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"bodycondition\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Witness Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Witness Name\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"witnessname\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Witness Statement\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"statement\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Summary / Notes\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Summary\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"summary\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Notes\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"notes\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}\n" +
			"";
	public static String accidentReport = "{\n" +
			"  \"layout\": [\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Reporting Officer\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"OFFICER_NAME\",\n" +
			"              \"populateKey\": \"officername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Rank\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_RANK\",\n" +
			"              \"populateKey\": \"rank\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Num\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_NUMBER\",\n" +
			"              \"populateKey\": \"officernumber\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callsign\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_CALLSIGN\",\n" +
			"              \"populateKey\": \"callsign\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Agency\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_AGENCY\",\n" +
			"              \"populateKey\": \"agency\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Division\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_DIVISION\",\n" +
			"              \"populateKey\": \"division\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Timestamp Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Date\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Time\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Report Num\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"NUMBER_FIELD\",\n" +
			"              \"populateKey\": \"number\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Suspect Details\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"suspectname\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"suspectage\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"suspectgender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"suspectaddress\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"suspectdescription\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Other\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"suspectother\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Blankone\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"BLANK_SPACE\",\n" +
			"              \"populateKey\": null,\n" +
			"              \"nodeType\": \"BLANK_SPACE\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Plate\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"suspectplate\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Model\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"suspectmodel\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Type\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_TYPE\",\n" +
			"              \"populateKey\": \"suspecttype\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Color\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COMBO_BOX_COLOR\",\n" +
			"              \"populateKey\": \"suspectcolor\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Estimated Speed\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"suspectspeed\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Vehicle Drivable\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"suspectdrivable\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Other Info\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"suspectotherinfo\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Suspect Damanges\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"suspectdamages\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Victim Details\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimname\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimage\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimgender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimaddress\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"victimdescription\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Other\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"victimother\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Blanktwo\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"BLANK_SPACE\",\n" +
			"              \"populateKey\": null,\n" +
			"              \"nodeType\": \"BLANK_SPACE\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Plate\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimplate\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Model\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimmodel\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Type\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_TYPE\",\n" +
			"              \"populateKey\": \"victimtype\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Color\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COMBO_BOX_COLOR\",\n" +
			"              \"populateKey\": \"victimcolor\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Estimated Speed\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"victimspeed\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Vehicle Drivable\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"victimdrivable\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Other Info\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"victimotherinfo\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Victim Damanges\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"victimdamages\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Accident Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Street\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"COMBO_BOX_STREET\",\n" +
			"              \"populateKey\": \"street\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Area\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_AREA\",\n" +
			"              \"populateKey\": \"area\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"County\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COUNTY_FIELD\",\n" +
			"              \"populateKey\": \"county\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Time Of Crash\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Date Of Crash\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Fatal\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"fatal\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Weather Conditions\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"weatherconditions\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Roadconditions\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"roadconditions\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Witness Information (If Applicable)\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Witness Name\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"witnessname\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Witness Statement\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"statement\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Summary / Notes\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Summary\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"summary\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Notes\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"notes\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}";
	public static String searchReport = "{\n" +
			"  \"layout\": [\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Officer information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"OFFICER_NAME\",\n" +
			"              \"populateKey\": \"officername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Rank\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_RANK\",\n" +
			"              \"populateKey\": \"rank\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Num\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_NUMBER\",\n" +
			"              \"populateKey\": \"officernumber\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callsign\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_CALLSIGN\",\n" +
			"              \"populateKey\": \"callsign\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Agency\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_AGENCY\",\n" +
			"              \"populateKey\": \"agency\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Division\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_DIVISION\",\n" +
			"              \"populateKey\": \"division\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Timestamp / Location Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Date\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Time\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Report Num\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"NUMBER_FIELD\",\n" +
			"              \"populateKey\": \"number\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Street\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"COMBO_BOX_STREET\",\n" +
			"              \"populateKey\": \"street\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Area\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_AREA\",\n" +
			"              \"populateKey\": \"area\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"County\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COUNTY_FIELD\",\n" +
			"              \"populateKey\": \"county\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Vehicle Search Information (If Applicable)\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Plate Number\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"plate\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Model\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"model\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Type\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_TYPE\",\n" +
			"              \"populateKey\": \"type\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Color\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COMBO_BOX_COLOR\",\n" +
			"              \"populateKey\": \"color\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Vehicle Search Method\",\n" +
			"              \"size\": 9,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"searchmethod\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Other Vehicle Info\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"othervehicleinfo\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Seized Vehicle Items\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"seizedvehicleitems\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Personal Search Information (If Applicable)\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"name\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"age\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"gender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"address\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"description\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Search Method\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"searchmethod\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Other Info\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"otherinfo\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Seized Items\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"seizeditems\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Summary / Notes\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Summary\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"summary\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Notes\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"notes\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}";
	public static String impoundReport = "{\n" +
			"  \"layout\": [\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Reporting Officer\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"OFFICER_NAME\",\n" +
			"              \"populateKey\": \"officername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Rank\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_RANK\",\n" +
			"              \"populateKey\": \"rank\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Num\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_NUMBER\",\n" +
			"              \"populateKey\": \"officernumber\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callsign\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_CALLSIGN\",\n" +
			"              \"populateKey\": \"callsign\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Agency\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_AGENCY\",\n" +
			"              \"populateKey\": \"agency\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Division\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_DIVISION\",\n" +
			"              \"populateKey\": \"division\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Timestamp / Location Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Street\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"COMBO_BOX_STREET\",\n" +
			"              \"populateKey\": \"street\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Area\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_AREA\",\n" +
			"              \"populateKey\": \"area\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"County\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COUNTY_FIELD\",\n" +
			"              \"populateKey\": \"county\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Time\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Date\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Report Num\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"NUMBER_FIELD\",\n" +
			"              \"populateKey\": \"number\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Owner / Vehicle Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Owner Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"name\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Owner Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"age\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Owner Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"gender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Owner Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"address\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Owner Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"description\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Owner Other Details\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"other\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Blankone\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"BLANK_SPACE\",\n" +
			"              \"populateKey\": null,\n" +
			"              \"nodeType\": \"BLANK_SPACE\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Plate Num\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"plate\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Model\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"model\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Type\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_TYPE\",\n" +
			"              \"populateKey\": \"type\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Color\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_COLOR\",\n" +
			"              \"populateKey\": \"color\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Vehicle Drivable\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"vehicledrivable\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Search Conducted\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"searchconducted\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Driver Information (If Applicable)\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Driver Is Owner\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"driverisowner\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Blanktwo\",\n" +
			"              \"size\": 8,\n" +
			"              \"fieldType\": \"BLANK_SPACE\",\n" +
			"              \"populateKey\": null,\n" +
			"              \"nodeType\": \"BLANK_SPACE\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Driver Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"drivername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Driver Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"driverage\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Driver Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"drivergender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Driver Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"driveraddress\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Driver Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"driverdescription\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Driver Other Details\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"driverother\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Summary / Notes (If Applicable)\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Summary\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"summary\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Notes\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"notes\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}";
	public static String trafficStopReport = "{\n" +
			"  \"layout\": [\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Reporting Officer\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"OFFICER_NAME\",\n" +
			"              \"populateKey\": \"officername\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Rank\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_RANK\",\n" +
			"              \"populateKey\": \"rank\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Officer Num\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"OFFICER_NUMBER\",\n" +
			"              \"populateKey\": \"officernumber\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Callsign\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"OFFICER_CALLSIGN\",\n" +
			"              \"populateKey\": \"callsign\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Agency\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_AGENCY\",\n" +
			"              \"populateKey\": \"agency\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Division\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"OFFICER_DIVISION\",\n" +
			"              \"populateKey\": \"division\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Timestamp / Location Information\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Date\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"DATE_FIELD\",\n" +
			"              \"populateKey\": \"date\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Time\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TIME_FIELD\",\n" +
			"              \"populateKey\": \"time\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Report Num\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"NUMBER_FIELD\",\n" +
			"              \"populateKey\": \"number\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Street\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"COMBO_BOX_STREET\",\n" +
			"              \"populateKey\": \"street\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Area\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_AREA\",\n" +
			"              \"populateKey\": \"area\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"County\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"COUNTY_FIELD\",\n" +
			"              \"populateKey\": \"county\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Suspect Details\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Name\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"name\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Age\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"age\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Gender\",\n" +
			"              \"size\": 2,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"gender\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Address\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"address\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Description\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"description\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Other Info\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"other\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Vehicle Details\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Plate Number\",\n" +
			"              \"size\": 3,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"plate\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Model\",\n" +
			"              \"size\": 5,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"model\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Type\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_TYPE\",\n" +
			"              \"populateKey\": \"type\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Color\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"COMBO_BOX_COLOR\",\n" +
			"              \"populateKey\": \"color\",\n" +
			"              \"nodeType\": \"COMBO_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Other Vehicle Info\",\n" +
			"              \"size\": 8,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"othervehicleinfo\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Stop Information (Where Applicable)\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Reason For Stop\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"reasonforstop\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Actions Taken\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"actionstaken\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Evidence\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"TEXT_FIELD\",\n" +
			"              \"populateKey\": \"evidence\",\n" +
			"              \"nodeType\": \"TEXT_FIELD\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Blank\",\n" +
			"              \"size\": 12,\n" +
			"              \"fieldType\": \"BLANK_SPACE\",\n" +
			"              \"populateKey\": null,\n" +
			"              \"nodeType\": \"BLANK_SPACE\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        },\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Warning Issued\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"warningissued\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Citation Issued\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"citationissued\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Arrest Made\",\n" +
			"              \"size\": 4,\n" +
			"              \"fieldType\": \"CHECK_BOX\",\n" +
			"              \"populateKey\": \"arrestmade\",\n" +
			"              \"nodeType\": \"CHECK_BOX\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    },\n" +
			"    {\n" +
			"      \"sectionTitle\": \"Outcome / Notes\",\n" +
			"      \"rowConfigs\": [\n" +
			"        {\n" +
			"          \"fieldConfigs\": [\n" +
			"            {\n" +
			"              \"fieldName\": \"Outcome\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"outcome\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            },\n" +
			"            {\n" +
			"              \"fieldName\": \"Notes\",\n" +
			"              \"size\": 6,\n" +
			"              \"fieldType\": \"TEXT_AREA\",\n" +
			"              \"populateKey\": \"notes\",\n" +
			"              \"nodeType\": \"TEXT_AREA\",\n" +
			"              \"dropdownType\": null\n" +
			"            }\n" +
			"          ]\n" +
			"        }\n" +
			"      ]\n" +
			"    }\n" +
			"  ]\n" +
			"}\n" +
			"\n" +
			"";
}