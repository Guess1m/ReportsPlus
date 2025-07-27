package com.Guess.ReportsPlus.util.Server;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Guess.ReportsPlus.util.History.Vehicle;

public class recordUtils {
	public static List<Vehicle> getAllVehicles() {
		List<Vehicle> vehiclesList = new ArrayList<>();
		String filePath = getServerDataFolderPath() + "ServerWorldCars.data";
		final Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			return vehiclesList;
		}
		try {
			String data = new String(Files.readAllBytes(path));
			String[] vehicleRecords = data.split("\\|");
			for (String record : vehicleRecords) {
				if (record.trim().isEmpty())
					continue;
				Vehicle vehicle = new Vehicle();
				String[] attributes = record.split("&");
				for (String attribute : attributes) {
					String[] keyValue = attribute.split("=");
					if (keyValue.length > 1) {
						String key = keyValue[0].trim().toLowerCase();
						String value = keyValue[1].trim();
						switch (key) {
							case "licenseplate":
								vehicle.setPlateNumber(value);
								break;
							case "model":
								vehicle.setModel(value);
								break;
							case "regexp":
								vehicle.setRegistrationExpiration(value);
								break;
							case "insexp":
								vehicle.setInspection(value);
								break;
							case "vin":
								vehicle.setVin(value);
								break;
							case "isstolen":
								vehicle.setStolenStatus(value);
								break;
							case "ispolice":
								vehicle.setPoliceStatus(value);
								break;
							case "owner":
								vehicle.setOwner(value);
								break;
							case "registration":
								vehicle.setRegistration(value);
								break;
							case "insurance":
								vehicle.setInsurance(value);
								break;
							case "color":
								vehicle.setColor(value);
								break;
						}
					}
				}
				if (vehicle.getPlateNumber() != null && !vehicle.getPlateNumber().isEmpty()) {
					vehiclesList.add(vehicle);
				}
			}
		} catch (IOException e) {
			logError("Failed to read vehicle data from file: " + filePath, e);
		}
		return vehiclesList;
	}

	public static List<String> getAllVehicleOwners(String filePath) {
		List<String> owners = new ArrayList<>();
		final Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			return owners;
		}
		try {
			byte[] encodedBytes = Files.readAllBytes(path);
			String data = new String(encodedBytes);
			String[] vehicles = data.split("\\|");
			for (String vehicle : vehicles) {
				String[] attributes = vehicle.split("&");
				for (String attribute : attributes) {
					String[] keyValue = attribute.split("=");
					if (keyValue.length > 1 && "owner".equalsIgnoreCase(keyValue[0].trim())) {
						String ownerName = keyValue[1].trim();
						if (!ownerName.isEmpty()) {
							owners.add(ownerName);
						}
						break;
					}
				}
			}
		} catch (IOException e) {
			logError("Failed to read vehicle owners from file: " + filePath, e);
		}
		return owners;
	}

	public static Map<String, String> grabPedData(String filePath, String pedName) throws IOException {
		final Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			logError("File does not exist: " + filePath);
			return new HashMap<>();
		}
		byte[] encodedBytes = Files.readAllBytes(path);
		String data = new String(encodedBytes);
		String[] pedestrians = data.split("\\|");
		for (String pedestrian : pedestrians) {
			Map<String, String> attributesMap = new HashMap<>();
			String[] attributes = pedestrian.split("&");
			for (String attribute : attributes) {
				String[] keyValue = attribute.split("=");
				if (keyValue.length > 1) {
					attributesMap.put(keyValue[0].toLowerCase(), keyValue[1].trim());
				} else {
					attributesMap.put(keyValue[0].toLowerCase(), "No value provided");
				}
			}
			if (attributesMap.containsKey("name") && attributesMap.get("name").equalsIgnoreCase(pedName)) {
				return attributesMap;
			} else if (attributesMap.containsKey("owner") && attributesMap.get("owner").equalsIgnoreCase(pedName)) {
				return attributesMap;
			}
		}
		Map<String, String> notFoundMap = new HashMap<>();
		notFoundMap.put("Error", "Pedestrian not found");
		return notFoundMap;
	}

	public static Map<String, String> grabVehicleData(String filePath, String licensePlate) throws IOException {
		if (!Files.exists(Paths.get(filePath))) {
			logError("File does not exist: " + filePath);
			return new HashMap<>();
		}
		String normalizedLicensePlate = licensePlate.toLowerCase();
		byte[] encodedBytes = Files.readAllBytes(Paths.get(filePath));
		String data = new String(encodedBytes);
		String[] vehicles = data.split("\\|");
		for (String vehicle : vehicles) {
			Map<String, String> attributesMap = new HashMap<>();
			String[] attributes = vehicle.split("&");
			for (String attribute : attributes) {
				String[] keyValue = attribute.split("=");
				if (keyValue.length > 1) {
					attributesMap.put(keyValue[0], keyValue[1].trim());
				} else {
					attributesMap.put(keyValue[0], "no value provided");
				}
			}
			if (attributesMap.getOrDefault("licenseplate", "").toLowerCase().equals(normalizedLicensePlate)) {
				return attributesMap;
			}
		}
		Map<String, String> notFoundMap = new HashMap<>();
		notFoundMap.put("error", "vehicle not found");
		return notFoundMap;
	}

	public static Map<String, String> grabTrafficStop(String filePath) throws IOException {
		if (!Files.exists(Paths.get(filePath))) {
			logError("File does not exist: " + filePath);
			return new HashMap<>();
		}
		byte[] encodedBytes = Files.readAllBytes(Paths.get(filePath));
		String data = new String(encodedBytes);
		String[] vehicles = data.split("\\|");
		for (String vehicle : vehicles) {
			Map<String, String> attributesMap = new HashMap<>();
			String[] attributes = vehicle.split("&");
			for (String attribute : attributes) {
				String[] keyValue = attribute.split("=");
				if (keyValue.length > 1) {
					attributesMap.put(keyValue[0], keyValue[1].trim());
				} else {
					attributesMap.put(keyValue[0], "no value provided");
				}
			}
			if (attributesMap.getOrDefault("licenseplate", "") != null) {
				return attributesMap;
			}
		}
		Map<String, String> notFoundMap = new HashMap<>();
		notFoundMap.put("error", "vehicle not found");
		return notFoundMap;
	}

	public static String extractValueByKey(String text, String keyToSearch) {
		String[] parts = text.split("\\|");
		for (String part : parts) {
			String[] keyValue = part.split("=", 2);
			if (keyValue.length == 2) {
				String key = keyValue[0].trim();
				String value = keyValue[1].trim();
				if (key.equalsIgnoreCase(keyToSearch)) {
					return value;
				}
			}
		}
		return null;
	}
}
