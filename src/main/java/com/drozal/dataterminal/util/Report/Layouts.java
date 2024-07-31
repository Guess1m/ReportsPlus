package com.drozal.dataterminal.util.Report;

import com.drozal.dataterminal.util.Report.nestedReportUtils.*;

import java.util.Map;

import static com.drozal.dataterminal.util.Report.reportUtil.createReportWindow;

public class Layouts {
	
	public static Map<String, Object> impoundLayout() {
		Map<String, Object> impoundReport = createReportWindow("Impound Report", 7, 9, null,
		                                                       new SectionConfig("Officer Information", true,
		                                                                         new RowConfig(
				                                                                         new FieldConfig("name", 5,
				                                                                                         FieldType.TEXT_FIELD),
				                                                                         new FieldConfig("rank", 5,
				                                                                                         FieldType.TEXT_FIELD),
				                                                                         new FieldConfig("number", 2,
				                                                                                         FieldType.TEXT_FIELD)),
		                                                                         new RowConfig(
				                                                                         new FieldConfig("division", 6,
				                                                                                         FieldType.TEXT_FIELD),
				                                                                         new FieldConfig("agency", 6,
				                                                                                         FieldType.TEXT_FIELD))),
		                                                       new SectionConfig("Location / Timestamp Information",
		                                                                         true, new RowConfig(
				                                                       new FieldConfig("date", 5, FieldType.TEXT_FIELD),
				                                                       new FieldConfig("time", 5, FieldType.TEXT_FIELD),
				                                                       new FieldConfig("citation number", 2,
				                                                                       FieldType.TEXT_FIELD))),
		                                                       new SectionConfig("Offender Information", true,
		                                                                         new RowConfig(new FieldConfig(
				                                                                         "offender name", 4,
				                                                                         FieldType.TEXT_FIELD),
		                                                                                       new FieldConfig(
				                                                                                       "offender age",
				                                                                                       4,
				                                                                                       FieldType.TEXT_FIELD),
		                                                                                       new FieldConfig(
				                                                                                       "offender gender",
				                                                                                       4,
				                                                                                       FieldType.TEXT_FIELD)),
		                                                                         new RowConfig(new FieldConfig(
				                                                                         "offender address", 12,
				                                                                         FieldType.TEXT_FIELD))),
		                                                       new SectionConfig("Offender Vehicle Information", true,
		                                                                         new RowConfig(
				                                                                         new FieldConfig("model", 6,
				                                                                                         FieldType.TEXT_FIELD),
				                                                                         new FieldConfig("plate number",
				                                                                                         6,
				                                                                                         FieldType.TEXT_FIELD)),
		                                                                         new RowConfig(
				                                                                         new FieldConfig("type", 7,
				                                                                                         FieldType.COMBO_BOX_TYPE),
				                                                                         new FieldConfig("color", 5,
				                                                                                         FieldType.COMBO_BOX_COLOR))),
		                                                       new SectionConfig("Citation Notes", true, new RowConfig(
				                                                       new FieldConfig("notes", 12,
				                                                                       FieldType.TEXT_AREA))));
		return impoundReport;
	}
	
	public static Map<String, Object> citationLayout() {
		Map<String, Object> citationReport = createReportWindow("Citation Report", 7, 9,
		                                                        new TransferConfig("Transfer Information To New Report",
		                                                                           new RowConfig(new FieldConfig(
				                                                                           "transferimpoundbtn", 12,
				                                                                           FieldType.TRANSFER_BUTTON))),
		                                                        new SectionConfig("Officer Information", true,
		                                                                          new RowConfig(
				                                                                          new FieldConfig("name", 5,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig("rank", 5,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig("number", 2,
				                                                                                          FieldType.TEXT_FIELD)),
		                                                                          new RowConfig(
				                                                                          new FieldConfig("division", 6,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig("agency", 6,
				                                                                                          FieldType.TEXT_FIELD))),
		                                                        new SectionConfig("Location / Timestamp Information",
		                                                                          true, new RowConfig(
				                                                        new FieldConfig("street", 4,
				                                                                        FieldType.TEXT_FIELD),
				                                                        new FieldConfig("area", 4,
				                                                                        FieldType.COMBO_BOX_AREA),
				                                                        new FieldConfig("county", 4,
				                                                                        FieldType.TEXT_FIELD)),
		                                                                          new RowConfig(
				                                                                          new FieldConfig("date", 5,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig("time", 5,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig(
						                                                                          "citation number", 2,
						                                                                          FieldType.TEXT_FIELD))),
		                                                        new SectionConfig("Offender Information", true,
		                                                                          new RowConfig(new FieldConfig(
				                                                                          "offender name", 4,
				                                                                          FieldType.TEXT_FIELD),
		                                                                                        new FieldConfig(
				                                                                                        "offender age",
				                                                                                        4,
				                                                                                        FieldType.TEXT_FIELD),
		                                                                                        new FieldConfig(
				                                                                                        "offender gender",
				                                                                                        4,
				                                                                                        FieldType.TEXT_FIELD)),
		                                                                          new RowConfig(new FieldConfig(
				                                                                          "offender address", 6,
				                                                                          FieldType.TEXT_FIELD),
		                                                                                        new FieldConfig(
				                                                                                        "offender description",
				                                                                                        6,
				                                                                                        FieldType.TEXT_FIELD))),
		                                                        new SectionConfig(
				                                                        "(If Applicable) Offender Vehicle Information",
				                                                        false, new RowConfig(new FieldConfig("model", 4,
				                                                                                             FieldType.TEXT_FIELD),
				                                                                             new FieldConfig(
						                                                                             "plate number", 4,
						                                                                             FieldType.TEXT_FIELD),
				                                                                             new FieldConfig("color", 4,
				                                                                                             FieldType.COMBO_BOX_COLOR)),
				                                                        new RowConfig(new FieldConfig("type", 4,
				                                                                                      FieldType.COMBO_BOX_TYPE),
				                                                                      new FieldConfig("other info", 8,
				                                                                                      FieldType.TEXT_FIELD))),
		                                                        new SectionConfig("Citation Notes", true, new RowConfig(
				                                                        new FieldConfig("notes", 12,
				                                                                        FieldType.TEXT_AREA))),
		                                                        new SectionConfig("Citation(s)", true, new RowConfig(
				                                                        new FieldConfig("citationview", 6,
				                                                                        FieldType.CITATION_TREE_VIEW))));
		return citationReport;
	}
	
	public static Map<String, Object> incidentLayout() {
		Map<String, Object> incidentReport = createReportWindow("Incident Report", 5, 7, null,
		                                                        new SectionConfig("Officer Information", true,
		                                                                          new RowConfig(
				                                                                          new FieldConfig("name", 5,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig("rank", 5,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig("number", 2,
				                                                                                          FieldType.TEXT_FIELD)),
		                                                                          new RowConfig(
				                                                                          new FieldConfig("division", 6,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig("agency", 6,
				                                                                                          FieldType.TEXT_FIELD))),
		                                                        new SectionConfig("Timestamp / Location Information",
		                                                                          true, new RowConfig(
				                                                        new FieldConfig("date", 3,
				                                                                        FieldType.TEXT_FIELD),
				                                                        new FieldConfig("time", 4,
				                                                                        FieldType.TEXT_FIELD),
				                                                        new FieldConfig("incident num", 5,
				                                                                        FieldType.TEXT_FIELD)),
		                                                                          new RowConfig(
				                                                                          new FieldConfig("street", 5,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig("area", 4,
				                                                                                          FieldType.COMBO_BOX_AREA),
				                                                                          new FieldConfig("county", 3,
				                                                                                          FieldType.TEXT_FIELD))),
		                                                        new SectionConfig("Parties Involved", false,
		                                                                          new RowConfig(
				                                                                          new FieldConfig("suspect(s)",
				                                                                                          6,
				                                                                                          FieldType.TEXT_FIELD),
				                                                                          new FieldConfig(
						                                                                          "victim(s) / witness(s)",
						                                                                          6,
						                                                                          FieldType.TEXT_FIELD)),
		                                                                          new RowConfig(
				                                                                          new FieldConfig("statement",
				                                                                                          12,
				                                                                                          FieldType.TEXT_AREA))),
		                                                        new SectionConfig("Notes / Summary", true,
		                                                                          new RowConfig(
				                                                                          new FieldConfig("summary", 12,
				                                                                                          FieldType.TEXT_AREA)),
		                                                                          new RowConfig(
				                                                                          new FieldConfig("notes", 12,
				                                                                                          FieldType.TEXT_AREA))));
		return incidentReport;
	}
	
	public static Map<String, Object> searchLayout() {
		Map<String, Object> searchReport = createReportWindow("Search Report", 5, 7, null,
		                                                      new SectionConfig("Officer Information", true,
		                                                                        new RowConfig(new FieldConfig("name", 5,
		                                                                                                      FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig("rank", 5,
		                                                                                                      FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig("number",
		                                                                                                      2,
		                                                                                                      FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(
				                                                                        new FieldConfig("division", 6,
				                                                                                        FieldType.TEXT_FIELD),
				                                                                        new FieldConfig("agency", 6,
				                                                                                        FieldType.TEXT_FIELD))),
		                                                      new SectionConfig("Timestamp / Location Information",
		                                                                        true, new RowConfig(
				                                                      new FieldConfig("date", 3, FieldType.TEXT_FIELD),
				                                                      new FieldConfig("time", 4, FieldType.TEXT_FIELD),
				                                                      new FieldConfig("search num", 5,
				                                                                      FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(
				                                                                        new FieldConfig("street", 5,
				                                                                                        FieldType.TEXT_FIELD),
				                                                                        new FieldConfig("area", 4,
				                                                                                        FieldType.COMBO_BOX_AREA),
				                                                                        new FieldConfig("county", 3,
				                                                                                        FieldType.TEXT_FIELD))),
		                                                      new SectionConfig("Search Information", true,
		                                                                        new RowConfig(new FieldConfig(
				                                                                        "grounds for search", 6,
				                                                                        FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig(
				                                                                                      "witness(s)", 6,
				                                                                                      FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(new FieldConfig(
				                                                                        "searched individual", 12,
				                                                                        FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(
				                                                                        new FieldConfig("search type",
				                                                                                        6,
				                                                                                        FieldType.COMBO_BOX_SEARCH_TYPE),
				                                                                        new FieldConfig("search method",
				                                                                                        6,
				                                                                                        FieldType.COMBO_BOX_SEARCH_METHOD))),
		                                                      new SectionConfig(
				                                                      "Field Sobriety Information (If Applicable)",
				                                                      false, new RowConfig(
				                                                      new FieldConfig("test(s) conducted", 4,
				                                                                      FieldType.TEXT_FIELD),
				                                                      new FieldConfig("result", 4,
				                                                                      FieldType.TEXT_FIELD),
				                                                      new FieldConfig("bac measurement", 4,
				                                                                      FieldType.TEXT_FIELD))),
		                                                      new SectionConfig("Notes / Summary", true, new RowConfig(
				                                                      new FieldConfig("seized item(s)", 12,
				                                                                      FieldType.TEXT_AREA)),
		                                                                        new RowConfig(
				                                                                        new FieldConfig("comments", 12,
				                                                                                        FieldType.TEXT_AREA))));
		return searchReport;
	}
	
	public static Map<String, Object> arrestLayout() {
		Map<String, Object> arrestReport = createReportWindow("Arrest Report", 7, 9,
		                                                      new TransferConfig("Transfer Information To New Report",
		                                                                         new RowConfig(new FieldConfig(
				                                                                         "transferimpoundbtn", 4,
				                                                                         FieldType.TRANSFER_BUTTON),
		                                                                                       new FieldConfig(
				                                                                                       "transferincidentbtn",
				                                                                                       4,
				                                                                                       FieldType.TRANSFER_BUTTON),
		                                                                                       new FieldConfig(
				                                                                                       "transfersearchbtn",
				                                                                                       4,
				                                                                                       FieldType.TRANSFER_BUTTON))),
		                                                      new SectionConfig("Officer Information", true,
		                                                                        new RowConfig(new FieldConfig("name", 5,
		                                                                                                      FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig("rank", 5,
		                                                                                                      FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig("number",
		                                                                                                      2,
		                                                                                                      FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(
				                                                                        new FieldConfig("division", 6,
				                                                                                        FieldType.TEXT_FIELD),
				                                                                        new FieldConfig("agency", 6,
				                                                                                        FieldType.TEXT_FIELD))),
		                                                      new SectionConfig("Location / Timestamp Information",
		                                                                        true, new RowConfig(
				                                                      new FieldConfig("street", 4,
				                                                                      FieldType.TEXT_FIELD),
				                                                      new FieldConfig("area", 4,
				                                                                      FieldType.COMBO_BOX_AREA),
				                                                      new FieldConfig("county", 4,
				                                                                      FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(new FieldConfig("date", 5,
		                                                                                                      FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig("time", 5,
		                                                                                                      FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig(
				                                                                                      "arrest number",
				                                                                                      2,
				                                                                                      FieldType.TEXT_FIELD))),
		                                                      new SectionConfig("Offender Information", true,
		                                                                        new RowConfig(
				                                                                        new FieldConfig("offender name",
				                                                                                        4,
				                                                                                        FieldType.TEXT_FIELD),
				                                                                        new FieldConfig("offender age",
				                                                                                        4,
				                                                                                        FieldType.TEXT_FIELD),
				                                                                        new FieldConfig(
						                                                                        "offender gender", 4,
						                                                                        FieldType.TEXT_FIELD)),
		                                                                        new RowConfig(new FieldConfig(
				                                                                        "offender address", 6,
				                                                                        FieldType.TEXT_FIELD),
		                                                                                      new FieldConfig(
				                                                                                      "offender description",
				                                                                                      6,
				                                                                                      FieldType.TEXT_FIELD))),
		                                                      new SectionConfig(
				                                                      "(If Applicable) Offender Medical Information",
				                                                      false, new RowConfig(
				                                                      new FieldConfig("ambulance required (Y/N)", 6,
				                                                                      FieldType.TEXT_FIELD),
				                                                      new FieldConfig("taser deployed (Y/N)", 6,
				                                                                      FieldType.TEXT_FIELD)),
				                                                      new RowConfig(
						                                                      new FieldConfig("other information", 12,
						                                                                      FieldType.TEXT_FIELD))),
		                                                      new SectionConfig("Charge Notes", true, new RowConfig(
				                                                      new FieldConfig("notes", 12,
				                                                                      FieldType.TEXT_AREA))),
		                                                      new SectionConfig("Charge(s)", true, new RowConfig(
				                                                      new FieldConfig("chargeview", 6,
				                                                                      FieldType.CHARGES_TREE_VIEW))));
		return arrestReport;
	}
	
	public static Map<String, Object> trafficStopLayout() {
		Map<String, Object> trafficStopReport = createReportWindow("Traffic Stop Report", 6, 8, new TransferConfig(
				                                                           "Transfer Information To New Report",
				                                                           new RowConfig(new FieldConfig("transferarrestbtn", 6, FieldType.TRANSFER_BUTTON),
				                                                                         new FieldConfig("transfercitationbtn", 6, FieldType.TRANSFER_BUTTON))),
		                                                           new SectionConfig("Officer Information", true,
		                                                                             new RowConfig(
				                                                                             new FieldConfig("name", 5,
				                                                                                             FieldType.TEXT_FIELD),
				                                                                             new FieldConfig("rank", 5,
				                                                                                             FieldType.TEXT_FIELD),
				                                                                             new FieldConfig("number",
				                                                                                             2,
				                                                                                             FieldType.TEXT_FIELD)),
		                                                                             new RowConfig(
				                                                                             new FieldConfig("division",
				                                                                                             6,
				                                                                                             FieldType.TEXT_FIELD),
				                                                                             new FieldConfig("agency",
				                                                                                             6,
				                                                                                             FieldType.TEXT_FIELD))),
		                                                           new SectionConfig("Location / Timestamp Information",
		                                                                             true, new RowConfig(
				                                                           new FieldConfig("street", 4,
				                                                                           FieldType.TEXT_FIELD),
				                                                           new FieldConfig("area", 4,
				                                                                           FieldType.COMBO_BOX_AREA),
				                                                           new FieldConfig("county", 4,
				                                                                           FieldType.TEXT_FIELD)),
		                                                                             new RowConfig(
				                                                                             new FieldConfig("date", 5,
				                                                                                             FieldType.TEXT_FIELD),
				                                                                             new FieldConfig("time", 5,
				                                                                                             FieldType.TEXT_FIELD),
				                                                                             new FieldConfig(
						                                                                             "stop number", 2,
						                                                                             FieldType.TEXT_FIELD))),
		                                                           new SectionConfig("Offender Information", true,
		                                                                             new RowConfig(new FieldConfig(
				                                                                             "offender name", 4,
				                                                                             FieldType.TEXT_FIELD),
		                                                                                           new FieldConfig(
				                                                                                           "offender age",
				                                                                                           4,
				                                                                                           FieldType.TEXT_FIELD),
		                                                                                           new FieldConfig(
				                                                                                           "offender gender",
				                                                                                           4,
				                                                                                           FieldType.TEXT_FIELD)),
		                                                                             new RowConfig(new FieldConfig(
				                                                                             "offender address", 6,
				                                                                             FieldType.TEXT_FIELD),
		                                                                                           new FieldConfig(
				                                                                                           "offender description",
				                                                                                           6,
				                                                                                           FieldType.TEXT_FIELD))),
		                                                           new SectionConfig("Offender Vehicle Information",
		                                                                             true, new RowConfig(
				                                                           new FieldConfig("model", 4,
				                                                                           FieldType.TEXT_FIELD),
				                                                           new FieldConfig("plate number", 4,
				                                                                           FieldType.TEXT_FIELD),
				                                                           new FieldConfig("color", 4,
				                                                                           FieldType.COMBO_BOX_COLOR)),
		                                                                             new RowConfig(
				                                                                             new FieldConfig("type", 4,
				                                                                                             FieldType.COMBO_BOX_TYPE),
				                                                                             new FieldConfig(
						                                                                             "other info", 8,
						                                                                             FieldType.TEXT_FIELD))),
		                                                           new SectionConfig("Comments", true, new RowConfig(
				                                                           new FieldConfig("notes", 12,
				                                                                           FieldType.TEXT_AREA))));
		return trafficStopReport;
	}
	
}
