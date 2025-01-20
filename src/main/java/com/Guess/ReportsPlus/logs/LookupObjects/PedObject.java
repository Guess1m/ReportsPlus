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
		
		this.name = pedData.getOrDefault("name", null);
		this.licenseNumber = pedData.getOrDefault("licensenumber", null);
		this.modelName = pedData.getOrDefault("pedmodel", null);
		this.birthday = pedData.getOrDefault("birthday", null);
		this.gender = pedData.getOrDefault("gender", null);
		this.address = pedData.getOrDefault("address", null);
		this.isWanted = pedData.getOrDefault("iswanted", null);
		this.licenseStatus = pedData.getOrDefault("licensestatus", null);
		this.licenseExp = pedData.getOrDefault("licenseexpiration", null);
		this.weaponPermitType = pedData.getOrDefault("weaponpermittype", null);
		this.weaponPermitStatus = pedData.getOrDefault("weaponpermitstatus", null);
		this.weaponPermitExpiration = pedData.getOrDefault("weaponpermitexpiration", null);
		this.fishPermitStatus = pedData.getOrDefault("fishpermitstatus", null);
		this.timesStopped = pedData.getOrDefault("timesstopped", null);
		this.fishPermitExpiration = pedData.getOrDefault("fishpermitexpiration", null);
		this.huntPermitStatus = pedData.getOrDefault("huntpermitstatus", null);
		this.huntPermitExpiration = pedData.getOrDefault("huntpermitexpiration", null);
		this.isOnParole = pedData.getOrDefault("isonparole", null);
		this.isOnProbation = pedData.getOrDefault("isonprobation", null);
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
