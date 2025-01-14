package com.Guess.ReportsPlus.logs.LookupObjects;

import java.io.IOException;
import java.util.Map;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabPedData;

public class PedObject {
	String name;
	String licenseNumber;
	String modelName;
	String birthday;
	String gender;
	String address;
	String isWanted;
	String licenseStatus;
	String licenseExp;
	String weaponPermitType;
	String weaponPermitStatus;
	String weaponPermitExpiration;
	String fishPermitStatus;
	String fishPermitExpiration;
	String timesStopped;
	String huntPermitStatus;
	String huntPermitExpiration;
	String isOnParole;
	String isOnProbation;
	
	public PedObject(String filePath, String licensePlate) {
		Map<String, String> pedData = Map.of();
		try {
			pedData = grabPedData(filePath, licensePlate);
		} catch (IOException e) {
			logError("Failed to load ServerWorldCars: ", e);
		}
		
		this.name = pedData.getOrDefault("name", "Not Found");
		this.licenseNumber = pedData.getOrDefault("licenseNumber", "Not Found");
		this.modelName = pedData.getOrDefault("pedModel", "Not Found");
		this.birthday = pedData.getOrDefault("birthday", "Not Found");
		this.gender = pedData.getOrDefault("gender", "Not Found");
		this.address = pedData.getOrDefault("address", "Not Found");
		this.isWanted = pedData.getOrDefault("isWanted", "Not Found");
		this.licenseStatus = pedData.getOrDefault("licenseStatus", "Not Found");
		this.licenseExp = pedData.getOrDefault("licenseExpiration", "Not Found");
		this.weaponPermitType = pedData.getOrDefault("weaponPermitType", "Not Found");
		this.weaponPermitStatus = pedData.getOrDefault("weaponPermitStatus", "Not Found");
		this.weaponPermitExpiration = pedData.getOrDefault("weaponPermitExpiration", "Not Found");
		this.fishPermitStatus = pedData.getOrDefault("fishPermitStatus", "Not Found");
		this.timesStopped = pedData.getOrDefault("timesStopped", "Not Found");
		this.fishPermitExpiration = pedData.getOrDefault("fishPermitExpiration", "Not Found");
		this.huntPermitStatus = pedData.getOrDefault("huntPermitStatus", "Not Found");
		this.huntPermitExpiration = pedData.getOrDefault("huntPermitExpiration", "Not Found");
		this.isOnParole = pedData.getOrDefault("isOnParole", "Not Found");
		this.isOnProbation = pedData.getOrDefault("isOnProbation", "Not Found");
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public String getFishPermitExpiration() {
		return fishPermitExpiration;
	}
	
	public String getFishPermitStatus() {
		return fishPermitStatus;
	}
	
	public String getGender() {
		return gender;
	}
	
	public String getHuntPermitExpiration() {
		return huntPermitExpiration;
	}
	
	public String getHuntPermitStatus() {
		return huntPermitStatus;
	}
	
	public String getIsOnParole() {
		return isOnParole;
	}
	
	public String getIsOnProbation() {
		return isOnProbation;
	}
	
	public String getIsWanted() {
		return isWanted;
	}
	
	public String getLicenseExp() {
		return licenseExp;
	}
	
	public String getLicenseNumber() {
		return licenseNumber;
	}
	
	public String getLicenseStatus() {
		return licenseStatus;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTimesStopped() {
		return timesStopped;
	}
	
	public String getWeaponPermitExpiration() {
		return weaponPermitExpiration;
	}
	
	public String getWeaponPermitStatus() {
		return weaponPermitStatus;
	}
	
	public String getWeaponPermitType() {
		return weaponPermitType;
	}
}
