package com.Guess.ReportsPlus.util.Report;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

public class nestedReportUtils {
	public enum FieldType {
		TEXT_FIELD("TEXT_FIELD"), NUMBER_FIELD("TEXT_FIELD"), DATE_FIELD("TEXT_FIELD"), TIME_FIELD("TEXT_FIELD"), OFFICER_NAME("TEXT_FIELD"), OFFICER_RANK("TEXT_FIELD"), OFFICER_DIVISION("TEXT_FIELD"), OFFICER_AGENCY("TEXT_FIELD"), OFFICER_NUMBER("TEXT_FIELD"), OFFICER_CALLSIGN(
				"TEXT_FIELD"), TEXT_AREA("TEXT_AREA"), COMBO_BOX_STREET("COMBO_BOX"), COMBO_BOX_AREA("COMBO_BOX"), COUNTY_FIELD("TEXT_FIELD"), COMBO_BOX_COLOR("COMBO_BOX"), COMBO_BOX_TYPE("COMBO_BOX"), COMBO_BOX_SEARCH_TYPE("COMBO_BOX"), COMBO_BOX_SEARCH_METHOD("COMBO_BOX"), CITATION_TREE_VIEW(
				"TREE_VIEW"), CHARGES_TREE_VIEW("TREE_VIEW"), TRANSFER_BUTTON("BUTTON"), CUSTOM_DROPDOWN("COMBO_BOX"), CHECK_BOX("CHECK_BOX"), BLANK_SPACE("BLANK_SPACE");
		
		private final String value;
		
		FieldType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
	}
	
	public static class SectionConfig {
		private final String sectionTitle;
		private final RowConfig[] rowConfigs;
		private boolean hasButton;
		
		@JsonCreator
		public SectionConfig(@JsonProperty("sectionTitle") String sectionTitle, @JsonProperty("rowConfigs") RowConfig... rowConfigs) {
			this.sectionTitle = sectionTitle;
			this.rowConfigs = rowConfigs;
			this.hasButton = false;
		}
		
		public void setHasButton(boolean hasButton) {
			this.hasButton = hasButton;
		}
		
		public boolean hasButton() {
			return hasButton;
		}
		
		public String getSectionTitle() {
			return sectionTitle;
		}
		
		public RowConfig[] getRowConfigs() {
			return rowConfigs;
		}
	}
	
	public static class TransferConfig {
		private final String title;
		private final List<RowConfig> rowConfigs;
		
		@JsonCreator
		public TransferConfig(@JsonProperty("title") String title, @JsonProperty("rowConfigs") RowConfig... rowConfigs) {
			this.title = title;
			this.rowConfigs = Arrays.asList(rowConfigs);
		}
		
		public String getTitle() {
			return title;
		}
		
		public List<RowConfig> getRowConfigs() {
			return rowConfigs;
		}
	}
	
	public static class RowConfig {
		private final FieldConfig[] fieldConfigs;
		
		@JsonCreator
		public RowConfig(@JsonProperty("fieldConfigs") FieldConfig... fieldConfigs) {
			this.fieldConfigs = fieldConfigs;
		}
		
		public FieldConfig[] getFieldConfigs() {
			return fieldConfigs;
		}
	}
	
	public static class FieldConfig {
		private final String fieldName;
		private final int size;
		private final FieldType fieldType;
		private String populateKey;
		private String nodeType;
		private String dropdownType;
		
		@JsonCreator
		public FieldConfig(@JsonProperty("fieldName") String fieldName, @JsonProperty("size") int size, @JsonProperty("fieldType") FieldType fieldType) {
			this.fieldName = fieldName;
			this.size = size;
			this.fieldType = fieldType;
			this.populateKey = populateKey;
			this.nodeType = nodeType;
			this.dropdownType = dropdownType;
		}
		
		public String getDropdownType() {
			return dropdownType;
		}
		
		public void setDropdownType(String dropdownType) {
			this.dropdownType = dropdownType;
		}
		
		public String getNodeType() {
			return nodeType;
		}
		
		public void setNodeType(String nodeType) {
			this.nodeType = nodeType;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public int getSize() {
			return size;
		}
		
		public FieldType getFieldType() {
			return fieldType;
		}
		
		public String getPopulateKey() {
			return populateKey;
		}
		
		public void setPopulateKey(String populateKey) {
			this.populateKey = populateKey;
		}
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class FullLayoutConfig {
		@JsonProperty("layout")
		private List<SectionConfig> layout;
		
		@JsonProperty("transfer")
		private TransferConfig transfer;
		
		public FullLayoutConfig() {
		}
		
		public FullLayoutConfig(List<SectionConfig> layout, TransferConfig transfer) {
			this.layout = layout;
			this.transfer = transfer;
		}
		
		public List<SectionConfig> getLayout() {
			return layout;
		}
		
		public void setLayout(List<SectionConfig> layout) {
			this.layout = layout;
		}
		
		public TransferConfig getTransfer() {
			return transfer;
		}
		
		public void setTransfer(TransferConfig transfer) {
			this.transfer = transfer;
		}
	}
	
}
