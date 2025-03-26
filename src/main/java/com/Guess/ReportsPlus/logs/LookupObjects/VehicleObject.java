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
		
		String vin = vehDataMap.getOrDefault("vin", "no value provided");
		String insexp = vehDataMap.getOrDefault("insexp", "no value provided");
		String regexp = vehDataMap.getOrDefault("regexp", "no value provided");
		String driver = vehDataMap.getOrDefault("driver", "no value provided");
		
		String ownerModel = vehDataMap.getOrDefault("ownermodel", "no value provided");
		String ownerAddress = vehDataMap.getOrDefault("owneraddress", "no value provided");
		String ownerGender = vehDataMap.getOrDefault("ownergender", "no value provided");
		
		String model = vehDataMap.getOrDefault("model", "no value provided");
		String isStolen = vehDataMap.getOrDefault("isstolen", "no value provided");
		String isPolice = vehDataMap.getOrDefault("ispolice", "no value provided");
		String registration = vehDataMap.getOrDefault("registration", "no value provided");
		String insurance = vehDataMap.getOrDefault("insurance", "no value provided");
		String owner = vehDataMap.getOrDefault("owner", "no value provided");
		String plate = vehDataMap.getOrDefault("licenseplate", "no value provided");
		String color = getColorFromRGB(vehDataMap.getOrDefault("color", "no value provided"));
		
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
