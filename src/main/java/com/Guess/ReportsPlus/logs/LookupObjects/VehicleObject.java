package com.Guess.ReportsPlus.logs.LookupObjects;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
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
	
	String driver;
	
	public VehicleObject(String licensePlate) {
		Map<String, String> vehDataMap = Map.of();
		try {
			vehDataMap = grabVehicleData(
					getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldCars.data",
					licensePlate);
		} catch (IOException e) {
			logError("Failed to load ServerWorldCars: ", e);
		}
		
		String vin = vehDataMap.getOrDefault("vin", "Not Found");
		String insexp = vehDataMap.getOrDefault("insexp", "Not Found");
		String regexp = vehDataMap.getOrDefault("regexp", "Not Found");
		String driver = vehDataMap.getOrDefault("driver", "Not Found");
		
		String model = vehDataMap.getOrDefault("model", "Not Found");
		String isStolen = vehDataMap.getOrDefault("isStolen", "Not Found");
		String isPolice = vehDataMap.getOrDefault("isPolice", "Not Found");
		String registration = vehDataMap.getOrDefault("registration", "Not Found");
		String insurance = vehDataMap.getOrDefault("insurance", "Not Found");
		String owner = vehDataMap.getOrDefault("owner", "Not Found");
		String plate = vehDataMap.getOrDefault("licensePlate", "Not Found");
		String color = getColorFromRGB(vehDataMap.getOrDefault("color", "Not Found"));
		
		//TODO: driver not implemented yet
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
