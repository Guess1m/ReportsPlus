package com.Guess.ReportsPlus.logs.LookupObjects;

import java.io.IOException;
import java.util.Map;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabVehicleData;

public class VehicleObject {
    String plate;
    String model;
    String registrationDate;
    String insuranceDate;
    String isStolen;
    String isPolice;
    String owner;
    String registration;
    String insurance;
    String color;
    String vin;

    String ownerModel;
    String ownerAddress;
    String ownerGender;

    String driver;

    public VehicleObject(String licensePlate) {
        Map<String, String> vehDataMap = Map.of();
        try {
            vehDataMap = grabVehicleData(getServerDataFolderPath() + "ServerWorldCars.data", licensePlate);
        } catch (IOException e) {
            logError("Failed to load ServerWorldCars: ", e);
        }

        String vin = vehDataMap.getOrDefault("vin", "Not Found");
        String insexp = vehDataMap.getOrDefault("insexp", "Not Found");
        String regexp = vehDataMap.getOrDefault("regexp", "Not Found");
        String driver = vehDataMap.getOrDefault("driver", "Not Found");

        String ownerModel = vehDataMap.getOrDefault("ownermodel", "Not Found");
        String ownerAddress = vehDataMap.getOrDefault("owneraddress", "Not Found");
        String ownerGender = vehDataMap.getOrDefault("ownergender", "Not Found");

        String model = vehDataMap.getOrDefault("model", "Not Found");
        String isStolen = vehDataMap.getOrDefault("isstolen", "Not Found");
        String isPolice = vehDataMap.getOrDefault("ispolice", "Not Found");
        String registration = vehDataMap.getOrDefault("registration", "Not Found");
        String insurance = vehDataMap.getOrDefault("insurance", "Not Found");
        String owner = vehDataMap.getOrDefault("owner", "Not Found");
        String plate = vehDataMap.getOrDefault("licenseplate", "Not Found");
        String color = getColorFromRGB(vehDataMap.getOrDefault("color", "Not Found"));

        this.driver = driver;

        this.vin = vin;
        this.registrationDate = regexp;
        this.insuranceDate = insexp;
        this.color = color;
        this.plate = plate;
        this.model = model;
        this.isStolen = isStolen;
        this.isPolice = isPolice;
        this.registration = registration;
        this.insurance = insurance;
        this.owner = owner;
        this.ownerModel = ownerModel;
        this.ownerAddress = ownerAddress;
        this.ownerGender = ownerGender;
    }

    private String getColorFromRGB(String colorValue) {
        String[] rgb = colorValue.split("-");
        if (rgb.length == 3) {
            return "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
        }
        return "Not Found";
    }

    public String getColor() {
        return color;
    }

    public String getInsurance() {
        return insurance;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public String getOwnerGender() {
        return ownerGender;
    }

    public String getOwnerModel() {
        return ownerModel;
    }

    public String getIsPolice() {
        return isPolice;
    }

    public String getIsStolen() {
        return isStolen;
    }

    public String getModel() {
        return model;
    }

    public String getOwner() {
        return owner;
    }

    public String getPlate() {
        return plate;
    }

    public String getRegistration() {
        return registration;
    }

    public String getDriver() {
        return driver;
    }

    public String getInsuranceDate() {
        return insuranceDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getVin() {
        return vin;
    }
}
