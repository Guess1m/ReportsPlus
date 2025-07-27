package com.Guess.ReportsPlus.logs.LookupObjects;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabVehicleData;

import java.io.IOException;
import java.util.Map;

public class VehicleObject {
	String plate;
	String model;
	String make;
	String registrationDate;
	String insuranceDate;
	String isStolen;
	String isPolice;
	String owner;
	String registration;
	String insurance;
	String coverage;
	String color;
	String vin;
	String ownerModel;
	String ownerAddress;
	String ownerGender;
	String ownerdob;
	String owneriswanted;
	String ownerlicensenumber;
	String ownerlicensestate;
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
		String ownerdob = vehDataMap.getOrDefault("ownerdob", "no value provided");
		String owneriswanted = vehDataMap.getOrDefault("owneriswanted", "no value provided");
		String ownerlicensenumber = vehDataMap.getOrDefault("ownerlicensenumber", "no value provided");
		String ownerlicensestate = vehDataMap.getOrDefault("ownerlicensestate", "no value provided");
		String model = vehDataMap.getOrDefault("model", "no value provided");
		String make = vehDataMap.getOrDefault("make", "no value provided");
		String isStolen = vehDataMap.getOrDefault("isstolen", "no value provided");
		String isPolice = vehDataMap.getOrDefault("ispolice", "no value provided");
		String registration = vehDataMap.getOrDefault("registration", "no value provided");
		String insurance = vehDataMap.getOrDefault("insurance", "no value provided");
		String coverage = vehDataMap.getOrDefault("coverage", "no value provided");
		String owner = vehDataMap.getOrDefault("owner", "no value provided");
		String plate = vehDataMap.getOrDefault("licenseplate", "no value provided");
		String color = getColorFromRGB(vehDataMap.getOrDefault("color", "no value provided"));
		this.driver = driver;
		this.vin = vin;
		this.registrationDate = regexp;
		this.insuranceDate = insexp;
		this.coverage = coverage;
		this.color = color;
		this.plate = plate;
		this.model = model;
		this.make = make;
		this.isStolen = isStolen;
		this.isPolice = isPolice;
		this.registration = registration;
		this.insurance = insurance;
		this.owner = owner;
		this.ownerModel = ownerModel;
		this.ownerAddress = ownerAddress;
		this.ownerGender = ownerGender;
		this.ownerAddress = ownerAddress;
		this.ownerGender = ownerGender;
		this.ownerdob = ownerdob;
		this.owneriswanted = owneriswanted;
		this.ownerlicensenumber = ownerlicensenumber;
		this.ownerlicensestate = ownerlicensestate;
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

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public void setInsuranceDate(String insuranceDate) {
		this.insuranceDate = insuranceDate;
	}

	public void setIsStolen(String isStolen) {
		this.isStolen = isStolen;
	}

	public void setIsPolice(String isPolice) {
		this.isPolice = isPolice;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public void setOwnerModel(String ownerModel) {
		this.ownerModel = ownerModel;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public void setOwnerGender(String ownerGender) {
		this.ownerGender = ownerGender;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getOwnerdob() {
		return ownerdob;
	}

	public String getOwneriswanted() {
		return owneriswanted;
	}

	public String getOwnerlicensenumber() {
		return ownerlicensenumber;
	}

	public String getOwnerlicensestate() {
		return ownerlicensestate;
	}

	public void setOwnerdob(String ownerdob) {
		this.ownerdob = ownerdob;
	}

	public void setOwneriswanted(String owneriswanted) {
		this.owneriswanted = owneriswanted;
	}

	public void setOwnerlicensenumber(String ownerlicensenumber) {
		this.ownerlicensenumber = ownerlicensenumber;
	}

	public void setOwnerlicensestate(String ownerlicensestate) {
		this.ownerlicensestate = ownerlicensestate;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
}
