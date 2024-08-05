package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.util.Misc.LogUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;

public class recordUtils {
	
	public static Map<String, String> grabPedData(String filePath, String pedName) throws IOException {
		final Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			log("File does not exist: " + filePath, LogUtils.Severity.ERROR);
			return new HashMap<>();
		}
		
		byte[] encodedBytes = Files.readAllBytes(path);
		String data = new String(encodedBytes);
		
		String[] pedestrians = data.split(",");
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
			log("File does not exist: " + filePath, LogUtils.Severity.ERROR);
			return new HashMap<>();
		}
		
		String normalizedLicensePlate = licensePlate.toLowerCase();
		byte[] encodedBytes = Files.readAllBytes(Paths.get(filePath));
		String data = new String(encodedBytes);
		
		String[] vehicles = data.split(",");
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
			
			if (attributesMap.getOrDefault("licensePlate", "").toLowerCase().equals(normalizedLicensePlate)) {
				return attributesMap;
			}
		}
		
		Map<String, String> notFoundMap = new HashMap<>();
		notFoundMap.put("error", "vehicle not found");
		return notFoundMap;
	}
	
}
