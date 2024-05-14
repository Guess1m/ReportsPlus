package com.drozal.dataterminal.util.server;

import com.drozal.dataterminal.util.LogUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.drozal.dataterminal.util.LogUtils.log;

public class recordUtils {

    public static void parseWorldPedData(String data) {
        String[] pedestrians = data.split(",");
        for (String pedestrian : pedestrians) {
            String[] attributes = pedestrian.split("&");
            for (String attribute : attributes) {
                String[] keyValue = attribute.split("=");
                if (keyValue.length > 1) {
                    System.out.println(keyValue[0] + ": " + keyValue[1].trim());
                } else {
                    System.out.println(keyValue[0] + ": " + "No value provided");
                }
            }
            System.out.println();  // Add a blank line between pedestrians for readability
        }
    }

    public static String searchForPedAttribute(String data, String pedName, String requiredAttribute) {
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
            // Check if this pedestrian has the name you're looking for with case-insensitivity
            if (attributesMap.containsKey("name") && attributesMap.get("name").equalsIgnoreCase(pedName)) {
                return attributesMap.getOrDefault(requiredAttribute.toLowerCase(), "Attribute not found");
            }
        }
        return "Pedestrian not found";
    }


    public static void parseWorldVehicleData(String filePath) throws IOException {
        byte[] encodedBytes = Files.readAllBytes(Paths.get(filePath));
        String data = new String(encodedBytes);

        String[] vehicles = data.split(",");
        for (String vehicle : vehicles) {
            String[] attributes = vehicle.split("&");
            for (String attribute : attributes) {
                String[] keyValue = attribute.split("=");
                if (keyValue.length > 1) {
                    System.out.println(keyValue[0] + ": " + keyValue[1].trim());
                } else {
                    System.out.println(keyValue[0] + ": " + "No value provided");
                }
            }
            System.out.println();  // Add a blank line between vehicles for readability
        }
    }

    public static Map<String, String> grabPedData(String filePath, String pedName) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            log("File does not exist: " + filePath, LogUtils.Severity.ERROR);
            return new HashMap<>();
        }

        byte[] encodedBytes = Files.readAllBytes(Paths.get(filePath));
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
            // Perform a case-insensitive comparison by converting both to lower case
            if (attributesMap.containsKey("name") && attributesMap.get("name").equalsIgnoreCase(pedName)) {
                return attributesMap;
            }
        }
        // If pedestrian is not found, return an empty map or a map with a specific message
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
                    attributesMap.put(keyValue[0], keyValue[1].trim());  // Normalize both key and value
                } else {
                    attributesMap.put(keyValue[0], "no value provided");
                }
            }
            // Check for the license plate in a case-insensitive manner
            if (attributesMap.getOrDefault("licensePlate", "").toLowerCase().equals(normalizedLicensePlate)) {
                return attributesMap;
            }
        }
        // If the vehicle is not found, return a map with an error message
        Map<String, String> notFoundMap = new HashMap<>();
        notFoundMap.put("error", "vehicle not found");
        return notFoundMap;
    }


    public static String searchForVehicleAttribute(String data, String licensePlate, String requiredAttribute) {
        String[] vehicles = data.split(",");
        for (String vehicle : vehicles) {
            Map<String, String> attributesMap = new HashMap<>();
            String[] attributes = vehicle.split("&");
            for (String attribute : attributes) {
                String[] keyValue = attribute.split("=");
                if (keyValue.length > 1) {
                    attributesMap.put(keyValue[0].toLowerCase(), keyValue[1].trim());
                } else {
                    attributesMap.put(keyValue[0].toLowerCase(), "No value provided");
                }
            }
            if (attributesMap.getOrDefault("licenseplate", "").equalsIgnoreCase(licensePlate)) {
                return attributesMap.getOrDefault(requiredAttribute.toLowerCase(), "Attribute not found");
            }
        }
        return "Vehicle not found";
    }


}
