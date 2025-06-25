package com.Guess.ReportsPlus.util.Strings;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationWarning;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.customDropdownURL;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.customizationURL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class customizationDataLoader {

	public static final Set<String> DEFAULT_FIELDS = new HashSet<>(Arrays.asList("agencies", "streets", "ranks",
			"divisions", "areaList", "searchMethods", "searchTypes", "carColors", "vehicleTypes"));
	private static final List<String> DEFAULT_STREETS = Arrays.asList("N/A", "Abattoir Avenue", "Abe Milton Parkway",
			"Ace Jones Drive", "Adam's Apple Boulevard", "Aguja Street", "Alta Place", "Alhambra Drive", "Alta Street",
			"Algonquin Boulevard", "Amarillo Vista", "Amarillo Way", "Americano Way",
			"Atlee Street", "Autopia Parkway", "Banham Canyon Drive", "Barbareno Road", "Bay City Avenue",
			"Bay City Incline", "Baytree Canyon Road", "Boulevard Del Perro", "Bridge Street", "Brouge Avenue",
			"Buccaneer Way", "Buen Vino Road",
			"Caesars Place", "Calais Avenue", "Calafia Road", "Capital Boulevard", "Carcer Way", "Carson Avenue",
			"Cascabel Avenue", "Cassidy Trail", "Cat-Claw Avenue", "Chianski Passage", "Cholla Road",
			"Cholla Springs Avenue",
			"Chum Street", "Chupacabra Street", "Clinton Avenue", "Cockingend Drive", "Conquistador Street",
			"Cortes Street", "Cougar Avenue", "Covenant Avenue", "Cox Way", "Crusade Road", "Davis Avenue",
			"Decker Street", "Del Perro Freeway",
			"Didion Drive", "Dorset Drive", "Dorset Place", "Dry Dock Street", "Duluoz Avenue", "Dunstable Drive",
			"Dunstable Lane", "Dutch London Street", "East Galileo Avenue", "East Joshua Road", "East Mirror Drive",
			"Eastbourne Way",
			"Eclipse Boulevard", "Edwood Way", "El Burro Boulevard", "El Rancho Boulevard", "Elgin Avenue",
			"Elysian Fields Freeway", "Equality Way", "Exceptionalists Way", "Fantastic Place", "Fenwell Place",
			"Fort Zancudo Approach Road",
			"Forum Drive", "Fudge Lane", "Galileo Road", "Gentry Lane", "Ginger Street", "Glory Way", "Goma Street",
			"Great Ocean Highway", "Grapeseed Avenue", "Grapeseed Main Street", "Greenwich Parkway", "Greenwich Place",
			"Greenwich Way",
			"Grove Street", "Hanger Way", "Hangman Avenue", "Hardy Way", "Hawick Avenue", "Heritage Way",
			"Hillcrest Avenue", "Hillcrest Ridge Access Road", "Imagination Court", "Industry Passage", "Ineseno Road",
			"Integrity Way",
			"Interstate 1", "Interstate 2", "Interstate 4", "Interstate 5", "Invention Court", "Innocence Boulevard",
			"Jamestown Street", "Joad Lane", "Joshua Road", "Kimble Hill Drive", "Kortz Drive", "Labor Place",
			"Laguna Place",
			"Lake Vinewood Drive", "La Puerta Freeway", "Las Lagunas Boulevard", "Lesbos Lane", "Liberty Street",
			"Lindsay Circus", "Little Bighorn Avenue", "Lolita Avenue", "Los Santos Freeway", "Low Power Street",
			"Macdonald Street",
			"Mad Wayne Thunder Drive", "Magellan Avenue", "Marathon Avenue", "Marina Drive", "Marlowe Drive",
			"Melanoma Street", "Meringue Lane", "Meteor Street", "Milton Road", "Mirror Park Boulevard", "Mirror Place",
			"Morningwood Boulevard", "Mount Haan Drive", "Mount Haan Road", "Mount Vinewood Drive",
			"Mountain View Drive", "Movie Star Way", "Mutiny Road", "New Empire Way", "Nikola Avenue", "Nikola Place",
			"Niland Avenue",
			"North Archer Avenue", "North Calafia Way", "North Conker Avenue", "North Rockford Drive",
			"North Sheldon Avenue", "Nowhere Road", "O'Neil Way", "Occupation Avenue", "Olympic Freeway",
			"Orchardville Avenue", "Paleto Boulevard",
			"Palomino Avenue", "Palomino Freeway", "Panorama Drive", "Peaceful Street", "Perth Street",
			"Picture Perfect Drive", "Plaice Place", "Playa Vista", "Popular Street", "Portola Drive", "Power Street",
			"Procopio Drive",
			"Procopio Promenade", "Prosperity Street", "Prosperity Street Promenade", "Pyrite Avenue", "Raton Pass",
			"Red Desert Avenue", "Richman Street", "Rockford Drive", "Route 1", "Route 11", "Route 13", "Route 14",
			"Route 15",
			"Route 16", "Route 17", "Route 18", "Route 19", "Route 20", "Route 22", "Route 23", "Route 68",
			"Route 68 Approach", "Rub Street", "Sam Austin Drive", "San Andreas Avenue", "San Vitus Boulevard",
			"Sandcastle Way", "Seaview Road",
			"Senora Freeway", "Senora Road", "Senora Way", "Shank Street", "Signal Street", "Sinner Street",
			"Sinners Passage", "Smoke Tree Road", "South Arsenal Street", "South Boulevard Del Perro",
			"South Mo Milton Drive",
			"South Rockford Drive", "South Shambles Street", "Spanish Avenue", "Steele Way", "Strangeways Drive",
			"Strawberry Avenue", "Supply Street", "Sustancia Road", "Swiss Street", "Tackle Street", "Tangerine Street",
			"Tongva Drive",
			"Tower Way", "Tug Street", "Union Road", "Utopia Gardens", "Vespucci Boulevard", "Vinewood Boulevard",
			"Vinewood Park Drive", "Vitus Street", "Voodoo Place", "West Eclipse Boulevard", "West Galileo Avenue",
			"West Mirror Drive",
			"Whispymound Drive", "Wild Oats Drive", "York Street", "Zancudo Avenue", "Zancudo Barranca", "Zancudo Road",
			"Zancudo Trail");
	private static final List<String> DEFAULT_AGENCIES = Arrays.asList("Los Santos Police Department [LSPD]",
			"Los Santos Sheriff's Department [LSSD]", "Blaine County Sheriff's Office [BCSO]",
			"San Andreas Highway Patrol [SAHP]", "Federal Investigation Bureau [FIB]",
			"International Affairs Agency [IAA]", "San Andreas State Parks [SASP]", "Drug Obeservation Agency [DOA]",
			"Burea of Alcohol, Tobacco, Firearms and Explosives [ATF]", "Patriotism and Immigration Authority [PIA]",
			"United States Marshal Service [USMS]");
	private static final List<String> DEFAULT_RANKS = Arrays.asList("POLICE DEPARTMENT", "Police Officer",
			"Senior Police Officer", "Sergeant", "Lieutenant", "Captain", "Chief of Police", "", "SHERIFF'S OFFICE ",
			"Deputy", "Senior Deputy", "Sergeant", "Lieutenant", "Captain", "Sheriff", "",
			"HIGHWAY PATROL", "State Trooper", "Senior State Trooper", "Sergeant", "Lieutenant", "Captain", "",
			"FEDERAL AGENCIES", "Special Agent", "Supervisory Special Agent", "Border Patrol Agent",
			"Supervisory Border Patrol Agent",
			"Deputy U.S. Marshal", "Supervisory Deputy U.S. Marshal", "", "PARKS SERVICES", "Ranger",
			"Senior Park Ranger", "Ranger Corporal", "Ranger Sergeant", "Ranger Lieutenant", "Game Warden");
	private static final List<String> DEFAULT_DIVISIONS = Arrays.asList("PATROL OPERATIONS", "Central Patrol Division",
			"Northern Patrol Division", "Southern Patrol Division", "Eastern Patrol Division",
			"Western Patrol Division", "Harbor/Port Patrol Division", "", "CRIMINAL INVESTIGATIONS",
			"Major Crimes Division", "Homicide & Violent Crimes Section", "Narcotics & Gang Enforcement Section",
			"Special Victims Unit", "Forensic Science Section", "Financial Crimes Section", "", "SPECIAL OPERATIONS",
			"SWAT/Tactical Division", "K-9 Unit", "Air Support Division", "Marine Enforcement Division",
			"Mounted Patrol Unit", "Traffic Enforcement Division", "", "ADMINISTRATIVE SERVICES",
			"Professional Standards Division",
			"Training & Development Section", "Public Affairs Unit", "Records Management Bureau", "",
			"TECHNICAL SERVICES", "Crime Analysis Division", "Communications Unit", "Fleet Management Section", "",
			"CUSTODY OPERATIONS",
			"Detention Services Division", "Inmate Management Bureau", "Correctional Facilities Unit", "",
			"REGIONAL COMMANDS", "Metro Region Command", "Northern Region Command", "Southern Region Command",
			"Border Security Command", "",
			"INTERAGENCY OPERATIONS", "Joint Terrorism Task Force", "Multi-Agency Gang Unit",
			"Emergency Management Section");
	private static final List<String> DEFAULT_AREALIST = Arrays.asList("N/A", "Alamo Sea", "Alta", "Backlot City",
			"Banham Canyon", "Banning", "Baytree Canyon", "Bolingbroke Penitentiary", "Burton", "Cape Catfish",
			"Cayo Perico", "Chamberlain Hills", "Chiliad Mountain State Wilderness", "Chumash",
			"Cypress Flats", "Davis", "Davis Quartz", "Del Perro", "Del Perro Beach", "Dignity Village",
			"Downtown Los Santos", "Downtown Vinewood", "East Los Santos", "East Vinewood", "El Burro Heights",
			"Elysian Island", "Fort Zancudo",
			"Galilee", "Galileo Park", "Grand Senora Desert", "Grapeseed", "Great Chaparral", "Great Ocean Highway",
			"GWC and Golfing Society", "Harmony", "Heart Attacks Beach", "La Mesa", "La Puerta", "Lago Zancudo",
			"Lake Vinewood",
			"Lake Vinewood Estates", "Land Act Reservoir", "Legion Square", "Little Seoul",
			"Los Santos International Airport", "Ludendorff", "Marlowe Vineyards", "Mirror Park",
			"Mirror Park Railyard", "Mission Row", "Morningwood",
			"Mount Chiliad", "Mount Gordo", "Mount Josiah", "Murrieta Heights", "North Chumash", "North Point",
			"North Yankton (Ludendorff)", "Pacific Bluffs", "Paleto Bay", "Paleto Cove", "Palomino Highlands",
			"Pillbox Hill",
			"Port of Los Santos", "Procopio Beach", "Puerto Del Sol", "Rancho", "Redwood Lights Track", "Richman",
			"Richman Glen", "RON Alternates Wind Farm", "Rockford Hills", "Sandy Shores", "San Chianski Mountain Range",
			"San Andreas",
			"Stab City", "Strawberry", "Tataviam Mountains", "Terminal", "Textile City", "Tongva Hills",
			"Tongva Valley", "Vespucci Beach", "Vespucci Canals", "Vinewood Hills", "Vinewood Racetrack",
			"West Vinewood");
	private static final List<String> DEFAULT_SEARCH_METHODS = Arrays.asList("N/A", "Search Warrant", "Probable Cause",
			"Consent Search", "Exigent Circumstances", "Plain View Doctrine", "Incident to Arrest", "Stop and Frisk",
			"Terry Frisk", "K-9 Search", "Electronic Surveillance",
			"Field Sobriety Test");
	private static final List<String> DEFAULT_SEARCH_TYPES = Arrays.asList("N/A", "Vehicle Search", "Personal Search",
			"Premises Search", "Pat-down Search", "Baggage Search", "Strip Search", "Digital Search", "Consent Search",
			"Inventory Search");
	private static final List<String> DEFAULT_CAR_COLORS = Arrays.asList("N/A", "Black", "White", "Silver", "Gray",
			"Red", "Blue", "Green", "Yellow", "Orange", "Brown", "Purple", "Pink", "Teal", "Magenta", "Turquoise",
			"Gold", "Bronze", "Cyan", "Lime");
	private static final List<String> DEFAULT_VEHICLE_TYPES = Arrays.asList("N/A", "Car", "Truck", "Motorcycle", "Bus",
			"Van", "SUV", "Pickup Truck", "Box Truck", "Tractor Trailer", "Flatbed", "Bicycle", "Tractor", "RV", "Taxi",
			"Ambulance", "Fire Truck", "Police Car", "Limousine", "Train", "Boat",
			"Airplane", "Helicopter", "Scooter", "Jet Ski", "Golf Cart", "Go-Kart");
	public static List<String> divisions = new ArrayList<>();
	public static List<String> agencies = new ArrayList<>();
	public static List<String> ranks = new ArrayList<>();
	public static List<String> vehicleTypes = new ArrayList<>();
	public static List<String> carColors = new ArrayList<>();
	public static List<String> searchTypes = new ArrayList<>();
	public static List<String> searchMethods = new ArrayList<>();
	public static List<String> areaList = new ArrayList<>();
	public static List<String> streets = new ArrayList<>();

	public static void loadJsonData() {
		try {
			logInfo("Loading data from Json...");
			loadDataFromJson();
			processCustomFields();
		} catch (IOException e) {
			logError("Error Loading data from Json: ", e);
		}
	}

	private static void processCustomFields() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File customFile = new File(customDropdownURL);

		if (!customFile.exists()) {
			return;
		}

		ObjectNode rootNode;
		try {
			rootNode = (ObjectNode) mapper.readTree(customFile);
		} catch (IOException e) {
			logError("Error reading custom dropdown file: ", e);
			throw e;
		}

		boolean modified = false;

		Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			JsonNode node = field.getValue();

			if (node.isArray()) {
				ArrayNode arrayNode = (ArrayNode) node;
				boolean hasNA = false;
				int naIndex = -1;

				for (int i = 0; i < arrayNode.size(); i++) {
					String value = arrayNode.get(i).asText();
					if ("N/A".equalsIgnoreCase(value)) {
						hasNA = true;
						naIndex = i;
						break;
					}
				}

				if (hasNA) {
					if (naIndex != 0) {
						arrayNode.remove(naIndex);
						arrayNode.insert(0, "N/A");
						modified = true;
					}
				} else {
					arrayNode.insert(0, "N/A");
					modified = true;
				}

				List<Integer> duplicates = new ArrayList<>();
				for (int i = 1; i < arrayNode.size(); i++) {
					String value = arrayNode.get(i).asText();
					if ("N/A".equalsIgnoreCase(value)) {
						duplicates.add(i);
					}
				}
				for (int i = duplicates.size() - 1; i >= 0; i--) {
					int index = duplicates.get(i);
					arrayNode.remove(index);
					modified = true;
				}
			}
		}

		if (modified) {
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(customFile, rootNode);
				logInfo("Updated custom fields to ensure 'N/A' is first.");
			} catch (IOException e) {
				logError("Failed to update custom fields file: ", e);
				throw e;
			}
		}
	}

	private static void loadDataFromJson() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(customizationURL);

		Map<String, List<String>> predefinedSections = new HashMap<>();
		predefinedSections.put("agencies", DEFAULT_AGENCIES);
		predefinedSections.put("streets", DEFAULT_STREETS);
		predefinedSections.put("ranks", DEFAULT_RANKS);
		predefinedSections.put("divisions", DEFAULT_DIVISIONS);
		predefinedSections.put("areaList", DEFAULT_AREALIST);
		predefinedSections.put("searchMethods", DEFAULT_SEARCH_METHODS);
		predefinedSections.put("searchTypes", DEFAULT_SEARCH_TYPES);
		predefinedSections.put("carColors", DEFAULT_CAR_COLORS);
		predefinedSections.put("vehicleTypes", DEFAULT_VEHICLE_TYPES);

		ObjectNode rootNode;
		boolean fileExists = file.exists();

		if (fileExists) {
			rootNode = (ObjectNode) mapper.readTree(file);
		} else {
			rootNode = mapper.createObjectNode();
		}

		boolean modified = false;

		for (Map.Entry<String, List<String>> entry : predefinedSections.entrySet()) {
			String section = entry.getKey();
			List<String> defaults = entry.getValue();

			if (!rootNode.has(section)) {
				ArrayNode arrayNode = mapper.createArrayNode();
				defaults.forEach(arrayNode::add);
				rootNode.set(section, arrayNode);
				modified = true;
				logInfo("Added missing section '" + section + "' to customization.json");
			} else {
				JsonNode sectionNode = rootNode.get(section);
				if (!sectionNode.isArray() || sectionNode.size() == 0) {
					ArrayNode arrayNode = mapper.createArrayNode();
					defaults.forEach(arrayNode::add);
					rootNode.set(section, arrayNode);
					modified = true;
					logInfo("Replaced empty/invalid section '" + section + "' in customization.json");
				}
			}
		}

		Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			String fieldName = field.getKey();
			JsonNode node = field.getValue();

			if (node.isArray()) {
				ArrayNode arrayNode = (ArrayNode) node;
				if (arrayNode.size() == 0) {
					if (predefinedSections.containsKey(fieldName)) {
						List<String> defaults = predefinedSections.get(fieldName);
						arrayNode.removeAll();
						defaults.forEach(arrayNode::add);
						modified = true;
						logInfo("Reset empty predefined section '" + fieldName + "'");
					} else {
						arrayNode.add("Default Option");
						modified = true;
						logInfo("Added default to custom section '" + fieldName + "'");
					}
				}
			}
		}

		if (modified) {
			logInfo("Customization.json Modified");
			try {
				file.getParentFile().mkdirs();
				mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
				logInfo("Updated customization.json");
			} catch (IOException e) {
				logError("Failed to save customization.json: ", e);
				throw e;
			}
		}

		loadSection(rootNode, "agencies", agencies);
		loadSection(rootNode, "ranks", ranks);
		loadSection(rootNode, "streets", streets);
		loadSection(rootNode, "divisions", divisions);
		loadSection(rootNode, "vehicleTypes", vehicleTypes);
		loadSection(rootNode, "carColors", carColors);
		loadSection(rootNode, "searchTypes", searchTypes);
		loadSection(rootNode, "searchMethods", searchMethods);
		loadSection(rootNode, "areaList", areaList);
		logInfo("Loaded values from customization.json.");
	}

	public static boolean addCustomField(String fieldName, List<String> values) throws IOException {
		if (fieldName == null || fieldName.trim().isEmpty()) {
			logError("Field name is null or empty");
			showNotificationWarning("Customization Utility", "Field name is null or empty");
			return false;
		}
		if (values == null || values.isEmpty()) {
			logError("Field Values list is null or empty");
			showNotificationWarning("Customization Utility", "Field Values list is null or empty");
			return false;
		}
		if (DEFAULT_FIELDS.contains(fieldName)) {
			logError("Field name '" + fieldName + "' is a default field and cannot be added as custom.");
			showNotificationWarning("Customization Utility", "Cannot add default field as custom.");
			return false;
		}

		ObjectMapper mapper = new ObjectMapper();
		File customFile = new File(customDropdownURL);
		ObjectNode customRoot;

		if (customFile.exists()) {
			try {
				customRoot = (ObjectNode) mapper.readTree(customFile);
			} catch (Exception e) {
				logError("Error parsing custom dropdown file: ", e);
				showNotificationError("Customization Utility", "Error parsing custom dropdown file: " + e);
				return false;
			}
		} else {
			customRoot = mapper.createObjectNode();
		}

		if (customRoot.has(fieldName)) {
			logInfo("Field '" + fieldName + "' already exists in custom dropdown file.");
			showNotificationWarning("Customization Utility", "Field '" + fieldName + "' already exists.");
			return false;
		}

		ArrayNode arrayNode = mapper.createArrayNode();
		Set<String> addedValues = new HashSet<>();

		arrayNode.add("N/A");
		addedValues.add("N/A");

		for (String value : values) {
			if (value == null || value.trim().isEmpty()) {
				continue;
			}
			String trimmedValue = value.trim();
			if ("N/A".equalsIgnoreCase(trimmedValue)) {
				continue;
			}
			if (!addedValues.contains(trimmedValue)) {
				arrayNode.add(trimmedValue);
				addedValues.add(trimmedValue);
			}
		}
		if (arrayNode.size() == 0) {
			arrayNode.add("N/A");
		}
		customRoot.set(fieldName, arrayNode);

		try {
			File parentDir = customFile.getParentFile();
			if (parentDir != null) {
				parentDir.mkdirs();
			}
			mapper.writerWithDefaultPrettyPrinter().writeValue(customFile, customRoot);
			logInfo("Added custom field '" + fieldName + "' to custom dropdown file.");
			showNotificationInfo("Customization Utility", "Added custom field '" + fieldName + "'.");
			return true;
		} catch (IOException e) {
			logError("Failed to write custom dropdown file: ", e);
			showNotificationError("Customization Utility", "Failed to write custom dropdown file: " + e);
			throw e;
		}
	}

	public static List<String> getValuesForField(String fieldName) {
		switch (fieldName) {
			case "agencies":
				return new ArrayList<>(agencies);
			case "streets":
				return new ArrayList<>(streets);
			case "ranks":
				return new ArrayList<>(ranks);
			case "divisions":
				return new ArrayList<>(divisions);
			case "areaList":
				return new ArrayList<>(areaList);
			case "searchMethods":
				return new ArrayList<>(searchMethods);
			case "searchTypes":
				return new ArrayList<>(searchTypes);
			case "carColors":
				return new ArrayList<>(carColors);
			case "vehicleTypes":
				return new ArrayList<>(vehicleTypes);
			default:
				ObjectMapper mapper = new ObjectMapper();
				File customFile = new File(customDropdownURL);
				if (!customFile.exists()) {
					logWarn("Custom dropdown file does not exist");
					return Collections.emptyList();
				}
				try {
					JsonNode rootNode = mapper.readTree(customFile);
					JsonNode fieldNode = rootNode.get(fieldName);
					if (fieldNode != null && fieldNode.isArray()) {
						List<String> values = new ArrayList<>();
						fieldNode.forEach(node -> values.add(node.asText()));
						return values;
					} else {
						logError("Field '" + fieldName + "' not found in custom dropdown file.");
						showNotificationWarning("Customization Utility",
								"Field '" + fieldName + "' not found in custom dropdown file.");
						return Collections.emptyList();
					}
				} catch (IOException e) {
					logError("Error reading custom dropdown file: ", e);
					return Collections.emptyList();
				}
		}
	}

	public static List<String> getCustomizationFields() {
		try {
			List<String> fields = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			File customFile = new File(customDropdownURL);
			if (!customFile.exists()) {
				return fields;
			}
			JsonNode rootNode = mapper.readTree(customFile);
			if (rootNode.isObject()) {
				Iterator<String> fieldNames = rootNode.fieldNames();
				while (fieldNames.hasNext()) {
					fields.add(fieldNames.next());
				}
			} else {
				logError("Custom dropdown file is not a valid JSON object");
			}
			return fields;
		} catch (Exception e) {
			logError("Error loading fields from custom dropdown file: ", e);
			return Collections.emptyList();
		}
	}

	public static boolean deleteCustomField(String fieldName) throws IOException {
		if (DEFAULT_FIELDS.contains(fieldName)) {
			logError("Cannot delete default field '" + fieldName + "'.");
			return false;
		}

		ObjectMapper mapper = new ObjectMapper();
		File customFile = new File(customDropdownURL);

		if (!customFile.exists()) {
			return false;
		}

		ObjectNode root;
		try {
			root = (ObjectNode) mapper.readTree(customFile);
		} catch (IOException e) {
			logError("Error reading custom dropdown file: ", e);
			throw e;
		}

		if (!root.has(fieldName)) {
			return false;
		}

		root.remove(fieldName);
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(customFile, root);
			logInfo("Deleted custom field '" + fieldName + "' from custom dropdown file.");
			return true;
		} catch (IOException e) {
			logError("Failed to delete field from custom dropdown file: ", e);
			throw e;
		}
	}

	private static void loadSection(JsonNode rootNode, String section, List<String> target) {
		target.clear();
		JsonNode node = rootNode.get(section);
		if (node != null && node.isArray()) {
			node.elements().forEachRemaining(element -> target.add(element.asText()));
		}
	}
}
