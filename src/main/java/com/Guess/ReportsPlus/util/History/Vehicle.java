package com.Guess.ReportsPlus.util.History;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.vehicleHistoryURL;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.Guess.ReportsPlus.config.ConfigReader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vehicle")
@XmlAccessorType(XmlAccessType.FIELD)
public class Vehicle {

	@XmlElement(name = "PlateNumber")
	private String plateNumber;

	@XmlElement(name = "Color")
	private String color;

	@XmlElement(name = "Model")
	private String model;

	@XmlElement(name = "Make")
	private String make;

	@XmlElement(name = "Stolen")
	private String stolenStatus;

	@XmlElement(name = "Police")
	private String policeStatus;

	@XmlElement(name = "Owner")
	private String owner;

	@XmlElement(name = "OwnerAddress")
	private String ownerAddress;

	@XmlElement(name = "OwnerModel")
	private String ownerModel;

	@XmlElement(name = "OwnerDOB")
	private String ownerDob;

	@XmlElement(name = "OwnerGender")
	private String ownerGender;

	@XmlElement(name = "OwnerIsWanted")
	private String ownerIsWanted;

	@XmlElement(name = "OwnerLicenseState")
	private String ownerLicenseState;

	@XmlElement(name = "OwnerLicenseNumber")
	private String ownerLicenseNumber;

	@XmlElement(name = "Registration")
	private String registration;

	@XmlElement(name = "RegistrationNumber")
	private String registrationNumber;

	@XmlElement(name = "RegistrationExpiration")
	private String registrationExpiration;

	@XmlElement(name = "Insurance")
	private String insurance;

	@XmlElement(name = "InsuranceNumber")
	private String insuranceNumber;

	@XmlElement(name = "InsuranceExpiration")
	private String insuranceExpiration;

	@XmlElement(name = "Coverage")
	private String coverage;

	@XmlElement(name = "Type")
	private String type;

	@XmlElement(name = "Vin")
	private String vin;

	@XmlElement(name = "Inspection")
	private String inspection;

	@Override
	public String toString() {
		return "plateNumber=[" + plateNumber +
				"]| color=[" + color +
				"]| model=[" + model +
				"]| make=[" + make +
				"]| stolenStatus=[" + stolenStatus +
				"]| policeStatus=[" + policeStatus +
				"]| owner=[" + owner +
				"]| ownerAddress=[" + ownerAddress +
				"]| ownerModel=[" + ownerModel +
				"]| ownerDob=[" + ownerDob +
				"]| ownerGender=[" + ownerGender +
				"]| ownerIsWanted=[" + ownerIsWanted +
				"]| ownerLicenseState=[" + ownerLicenseState +
				"]| ownerLicenseNumber=[" + ownerLicenseNumber +
				"]| registration=[" + registration +
				"]| registrationNumber=[" + registrationNumber +
				"]| registrationExpiration=[" + registrationExpiration +
				"]| insurance=[" + insurance +
				"]| insuranceNumber=[" + insuranceNumber +
				"]| insuranceExpiration=[" + insuranceExpiration +
				"]| coverage=[" + coverage +
				"]| type=[" + type +
				"]| vin=[" + vin +
				"]| inspection=[" + inspection + "]";
	}

	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerAddress() {
		return ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public String getOwnerModel() {
		return ownerModel;
	}

	public void setOwnerModel(String ownerModel) {
		this.ownerModel = ownerModel;
	}

	public String getOwnerDob() {
		return ownerDob;
	}

	public void setOwnerDob(String ownerDob) {
		this.ownerDob = ownerDob;
	}

	public String getOwnerGender() {
		return ownerGender;
	}

	public void setOwnerGender(String ownerGender) {
		this.ownerGender = ownerGender;
	}

	public String getOwnerIsWanted() {
		return ownerIsWanted;
	}

	public void setOwnerIsWanted(String ownerIsWanted) {
		this.ownerIsWanted = ownerIsWanted;
	}

	public String getOwnerLicenseState() {
		return ownerLicenseState;
	}

	public void setOwnerLicenseState(String ownerLicenseState) {
		this.ownerLicenseState = ownerLicenseState;
	}

	public String getOwnerLicenseNumber() {
		return ownerLicenseNumber;
	}

	public void setOwnerLicenseNumber(String ownerLicenseNumber) {
		this.ownerLicenseNumber = ownerLicenseNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getPoliceStatus() {
		return policeStatus;
	}

	public void setPoliceStatus(String policeStatus) {
		this.policeStatus = policeStatus;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getStolenStatus() {
		return stolenStatus;
	}

	public void setStolenStatus(String stolenStatus) {
		this.stolenStatus = stolenStatus;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getInsuranceExpiration() {
		return insuranceExpiration;
	}

	public void setInsuranceExpiration(String insuranceExpiration) {
		this.insuranceExpiration = insuranceExpiration;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public String getRegistrationExpiration() {
		return registrationExpiration;
	}

	public void setRegistrationExpiration(String registrationExpiration) {
		this.registrationExpiration = registrationExpiration;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	@XmlRootElement(name = "VehicleDatabase")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Vehicles {

		@XmlElement(name = "vehicle")
		private List<Vehicle> VehicleList;

		public List<Vehicle> getVehicleList() {
			return VehicleList;
		}

		public void setVehicleList(List<Vehicle> VehicleList) {
			this.VehicleList = VehicleList;
		}
	}

	public static class VehicleHistoryUtils {

		public static String generateInspectionStatus() {
			int validChance = 85;
			try {
				validChance = Integer.parseInt(ConfigReader.configRead("vehicleHistory", "hasValidInspection"));
			} catch (IOException e) {
				logError("Error reading vehicleHistory.hasValidInspection: ", e);
			}
			if (validChance < 0 || validChance > 100) {
				throw new IllegalArgumentException("Chance must be between 0 and 100 for Veh. inspection.");
			}
			int randomValue = (int) (Math.random() * 100) + 1;
			return randomValue <= validChance ? "Valid" : "Invalid";
		}

		public static Vehicles loadVehicles() throws JAXBException {
			File file = new File(vehicleHistoryURL);
			if (!file.exists()) {
				return new Vehicles();
			}

			try {
				JAXBContext context = JAXBContext.newInstance(Vehicles.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				return (Vehicles) unmarshaller.unmarshal(file);
			} catch (JAXBException e) {
				logError("Error loading Vehicles: ", e);
				throw e;
			}
		}

		private static void saveVehicles(Vehicles Vehicles) throws JAXBException {
			JAXBContext context = JAXBContext.newInstance(Vehicles.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			File file = new File(vehicleHistoryURL);
			marshaller.marshal(Vehicles, file);
		}

		public static void addVehicle(Vehicle Vehicle) throws JAXBException {
			Vehicles Vehicles = loadVehicles();

			if (Vehicles.getVehicleList() == null) {
				Vehicles.setVehicleList(new java.util.ArrayList<>());
			}

			Optional<Vehicle> existingReport = Vehicles.getVehicleList().stream()
					.filter(e -> e.getPlateNumber().equals(Vehicle.getPlateNumber())).findFirst();

			if (existingReport.isPresent()) {
				Vehicles.getVehicleList().remove(existingReport.get());
				Vehicles.getVehicleList().add(Vehicle);
				logInfo("Vehicle with plate number " + Vehicle.getPlateNumber() + " updated.");
			} else {
				Vehicles.getVehicleList().add(Vehicle);
				logInfo("Vehicle with plate number " + Vehicle.getPlateNumber() + " added.");
			}

			saveVehicles(Vehicles);
		}

		public static Optional<Vehicle> findVehicleByNumber(String Vehiclenumber) {
			Vehicles Vehicles = null;
			try {
				Vehicles = loadVehicles();
			} catch (JAXBException e) {
				logError("JAXB error while trying to loadVehicles() by number: ", e);
			}

			if (Vehicles != null && Vehicles.getVehicleList() != null) {
				return Vehicles.getVehicleList().stream()
						.filter(e -> e.getPlateNumber().equalsIgnoreCase(Vehiclenumber)).findFirst();
			}

			return Optional.empty();
		}

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