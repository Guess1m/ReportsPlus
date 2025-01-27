package com.Guess.ReportsPlus.util.Strings;

import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.customizationURL;

public class customizationDataLoader {
	
	public static List<String> divisions = new ArrayList<>();
	public static List<String> agencies = new ArrayList<>();
	public static List<String> ranks = new ArrayList<>();
	public static List<String> vehicleTypes = new ArrayList<>();
	public static List<String> carColors = new ArrayList<>();
	public static List<String> searchTypes = new ArrayList<>();
	public static List<String> searchMethods = new ArrayList<>();
	public static List<String> areaList = new ArrayList<>();
	
	static {
		try {
			log("Loading data from Json...", LogUtils.Severity.INFO);
			loadDataFromJson();
		} catch (IOException e) {
			logError("Error Loading data from Json: ", e);
		}
	}
	
	private static void loadDataFromJson() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(new File(customizationURL));
		
		JsonNode vehicleTypesNode = rootNode.get("vehicleTypes");
		for (JsonNode node : vehicleTypesNode) {
			vehicleTypes.add(node.asText());
		}
		
		JsonNode carColorsNode = rootNode.get("carColors");
		for (JsonNode node : carColorsNode) {
			carColors.add(node.asText());
		}
		
		JsonNode searchTypesNode = rootNode.get("searchTypes");
		for (JsonNode node : searchTypesNode) {
			searchTypes.add(node.asText());
		}
		
		JsonNode searchMethodsNode = rootNode.get("searchMethods");
		for (JsonNode node : searchMethodsNode) {
			searchMethods.add(node.asText());
		}
		
		JsonNode areaListNode = rootNode.get("areaList");
		for (JsonNode node : areaListNode) {
			areaList.add(node.asText());
		}
		
		JsonNode divisionsNode = rootNode.get("divisions");
		for (JsonNode node : divisionsNode) {
			divisions.add(node.asText());
		}
		
		JsonNode agenciesNode = rootNode.get("agencies");
		for (JsonNode node : agenciesNode) {
			agencies.add(node.asText());
		}
		
		JsonNode ranksNode = rootNode.get("ranks");
		for (JsonNode node : ranksNode) {
			ranks.add(node.asText());
		}
		log("loaded values from Json.", LogUtils.Severity.INFO);
	}
	
}
