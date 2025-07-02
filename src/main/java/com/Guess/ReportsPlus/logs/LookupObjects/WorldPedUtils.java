package com.Guess.ReportsPlus.logs.LookupObjects;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldPedUtils {

    public static List<Map<String, String>> grabAllPedData(String filePath) throws IOException {
        List<Map<String, String>> allPeds = new ArrayList<>();
        final Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return allPeds;
        }

        byte[] encodedBytes = Files.readAllBytes(path);
        String data = new String(encodedBytes);

        String[] pedestrians = data.split("\\|");
        for (String pedestrian : pedestrians) {
            Map<String, String> attributesMap = new HashMap<>();
            String[] attributes = pedestrian.split("&");
            for (String attribute : attributes) {
                String[] keyValue = attribute.split("=", 2);
                if (keyValue.length > 1) {
                    attributesMap.put(keyValue[0].toLowerCase(), keyValue[1].trim());
                } else {
                    attributesMap.put(keyValue[0].toLowerCase(), "");
                }
            }
            if (!attributesMap.isEmpty() && attributesMap.containsKey("name")) {
                allPeds.add(attributesMap);
            }
        }
        return allPeds;
    }

    public static List<PedObject> getAllWorldPeds(String filePath) {
        List<PedObject> allPeds = new ArrayList<>();
        try {
            List<Map<String, String>> allPedData = grabAllPedData(filePath);

            for (Map<String, String> pedData : allPedData) {
                PedObject ped = new PedObject();
                ped.setName(pedData.getOrDefault("name", null));
                ped.setLicenseNumber(pedData.getOrDefault("licensenumber", null));
                ped.setModelName(pedData.getOrDefault("pedmodel", null));
                ped.setBirthday(pedData.getOrDefault("birthday", null));
                ped.setGender(pedData.getOrDefault("gender", null));
                ped.setAddress(pedData.getOrDefault("address", null));
                ped.setIsWanted(pedData.getOrDefault("iswanted", null));
                ped.setLicenseStatus(pedData.getOrDefault("licensestatus", null));
                ped.setLicenseExp(pedData.getOrDefault("licenseexpiration", null));
                ped.setWeaponPermitType(pedData.getOrDefault("weaponpermittype", null));
                ped.setWeaponPermitStatus(pedData.getOrDefault("weaponpermitstatus", null));
                ped.setWeaponPermitExpiration(pedData.getOrDefault("weaponpermitexpiration", null));
                ped.setFishPermitStatus(pedData.getOrDefault("fishpermitstatus", null));
                ped.setFishPermitExpiration(pedData.getOrDefault("fishpermitexpiration", null));
                ped.setTimesStopped(pedData.getOrDefault("timesstopped", null));
                ped.setHuntPermitStatus(pedData.getOrDefault("huntpermitstatus", null));
                ped.setHuntPermitExpiration(pedData.getOrDefault("huntpermitexpiration", null));
                ped.setIsOnParole(pedData.getOrDefault("isonparole", null));
                ped.setIsOnProbation(pedData.getOrDefault("isonprobation", null));
                allPeds.add(ped);
            }
        } catch (IOException e) {
            logError("Failed to read and parse ServerWorldPeds.data: ", e);
        }
        return allPeds;
    }
}