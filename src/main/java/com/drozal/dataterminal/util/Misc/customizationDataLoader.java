package com.drozal.dataterminal.util.Misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.stringUtil.customizationURL;

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
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads data (agencies, ranks, and divisions) from the JSON file.
	 *
	 * @throws IOException if an error occurs during file reading
	 */
	private static void loadDataFromJson() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(new File(customizationURL));
		
		
		JsonNode vehicleTypesNode = rootNode.get("vehicleTypes");
		for (JsonNode node : vehicleTypesNode) {
			vehicleTypes.add(node.asText());
		}
		log("loaded vehicleTypes from Json", LogUtils.Severity.INFO);
		
		
		JsonNode carColorsNode = rootNode.get("carColors");
		for (JsonNode node : carColorsNode) {
			carColors.add(node.asText());
		}
		log("loaded carColors from Json", LogUtils.Severity.INFO);
		
		
		JsonNode searchTypesNode = rootNode.get("searchTypes");
		for (JsonNode node : searchTypesNode) {
			searchTypes.add(node.asText());
		}
		log("loaded searchTypes from Json", LogUtils.Severity.INFO);
		
		
		JsonNode searchMethodsNode = rootNode.get("searchMethods");
		for (JsonNode node : searchMethodsNode) {
			searchMethods.add(node.asText());
		}
		log("loaded searchMethods from Json", LogUtils.Severity.INFO);
		
		
		JsonNode areaListNode = rootNode.get("areaList");
		for (JsonNode node : areaListNode) {
			areaList.add(node.asText());
		}
		log("loaded areaList from Json", LogUtils.Severity.INFO);
		
		
		JsonNode divisionsNode = rootNode.get("divisions");
		for (JsonNode node : divisionsNode) {
			divisions.add(node.asText());
		}
		log("loaded divisions from Json", LogUtils.Severity.INFO);
		
		
		JsonNode agenciesNode = rootNode.get("agencies");
		for (JsonNode node : agenciesNode) {
			agencies.add(node.asText());
		}
		log("loaded agencies from Json", LogUtils.Severity.INFO);
		
		
		JsonNode ranksNode = rootNode.get("ranks");
		for (JsonNode node : ranksNode) {
			ranks.add(node.asText());
		}
		log("loaded ranks from Json", LogUtils.Severity.INFO);
	}
	
}
