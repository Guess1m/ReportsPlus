package com.Guess.ReportsPlus.util.Report;

import java.util.Arrays;
import java.util.List;

public class nestedReportUtils {
	public enum FieldType {
		//TODO: make base reports use DATE_FIELD, TIME_FIELD, OFFICER_NAME, OFFICER_RANK, OFFICER_DIVISION, OFFICER_AGENCY, OFFICER_NUMBER, OFFICER_CALLSIGN
		TEXT_FIELD, DATE_FIELD, TIME_FIELD, OFFICER_NAME, OFFICER_RANK, OFFICER_DIVISION, OFFICER_AGENCY, OFFICER_NUMBER, OFFICER_CALLSIGN, TEXT_AREA, COMBO_BOX_STREET, COMBO_BOX_AREA, COMBO_BOX_COLOR, COMBO_BOX_TYPE, COMBO_BOX_SEARCH_TYPE, COMBO_BOX_SEARCH_METHOD, CITATION_TREE_VIEW, CHARGES_TREE_VIEW, TRANSFER_BUTTON
	}
	
	public static class SectionConfig {
		private final String sectionTitle;
		
		private final List<RowConfig> rowConfigs;
		
		public SectionConfig(String sectionTitle, RowConfig... rowConfigs) {
			this.sectionTitle = sectionTitle;
			this.rowConfigs = Arrays.asList(rowConfigs);
		}
		
		public String getSectionTitle() {
			return sectionTitle;
		}
		
		public List<RowConfig> getRowConfigs() {
			return rowConfigs;
		}
		
	}
	
	public static class TransferConfig {
		private final String title;
		private final List<RowConfig> rowConfigs;
		
		public TransferConfig(String title, RowConfig... rowConfigs) {
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
		
		private final List<FieldConfig> fieldConfigs;
		
		public RowConfig(FieldConfig... fieldConfigs) {
			this.fieldConfigs = Arrays.asList(fieldConfigs);
		}
		
		public List<FieldConfig> getFieldConfigs() {
			return fieldConfigs;
		}
		
	}
	
	public static class FieldConfig {
		
		private final String fieldName;
		private String populateKey;
		private final int size;
		private final FieldType fieldType;
		
		public FieldConfig(String fieldName, int size, FieldType fieldType) {
			this.fieldName = fieldName;
			this.size = size;
			this.fieldType = fieldType;
		}
		
		public FieldConfig(String fieldName, int size, FieldType fieldType, String populateKey) {
			this.fieldName = fieldName;
			this.populateKey = populateKey;
			this.size = size;
			this.fieldType = fieldType;
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
	}
}
