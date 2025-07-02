package com.Guess.ReportsPlus.logs.LookupObjects;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabPedData;

import java.io.IOException;
import java.util.Map;

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
			logError("Failed to load ServerWorldPeds: ", e);
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

	public PedObject() {
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

	public void setName(String name) {
		this.name = name;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setIsWanted(String isWanted) {
		this.isWanted = isWanted;
	}

	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

	public void setLicenseExp(String licenseExp) {
		this.licenseExp = licenseExp;
	}

	public void setWeaponPermitType(String weaponPermitType) {
		this.weaponPermitType = weaponPermitType;
	}

	public void setWeaponPermitStatus(String weaponPermitStatus) {
		this.weaponPermitStatus = weaponPermitStatus;
	}

	public void setWeaponPermitExpiration(String weaponPermitExpiration) {
		this.weaponPermitExpiration = weaponPermitExpiration;
	}

	public void setFishPermitStatus(String fishPermitStatus) {
		this.fishPermitStatus = fishPermitStatus;
	}

	public void setFishPermitExpiration(String fishPermitExpiration) {
		this.fishPermitExpiration = fishPermitExpiration;
	}

	public void setTimesStopped(String timesStopped) {
		this.timesStopped = timesStopped;
	}

	public void setHuntPermitStatus(String huntPermitStatus) {
		this.huntPermitStatus = huntPermitStatus;
	}

	public void setHuntPermitExpiration(String huntPermitExpiration) {
		this.huntPermitExpiration = huntPermitExpiration;
	}

	public void setIsOnParole(String isOnParole) {
		this.isOnParole = isOnParole;
	}

	public void setIsOnProbation(String isOnProbation) {
		this.isOnProbation = isOnProbation;
	}

}
