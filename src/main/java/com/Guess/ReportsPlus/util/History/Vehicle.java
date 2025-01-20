package com.Guess.ReportsPlus.util.History;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.URLStrings.vehicleHistoryURL;

@XmlRootElement(name = "vehicle")
@XmlAccessorType(XmlAccessType.FIELD)
public class Vehicle {
	
	@XmlElement(name = "PlateNumber")
	private String plateNumber;
	
	@XmlElement(name = "Color")
	private String color;
	
	@XmlElement(name = "Model")
	private String model;
	
	@XmlElement(name = "Stolen")
	private String stolenStatus;
	
	@XmlElement(name = "Police")
	private String policeStatus;
	
	@XmlElement(name = "Owner")
	private String owner;
	
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
	
	@XmlElement(name = "Type")
	private String type;
	
	@XmlElement(name = "Vin")
	private String vin;
	
	@XmlElement(name = "Inspection")
	private String inspection;
	
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
		
		@XmlElement(name = "Vehicle")
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
			
			Optional<Vehicle> existingReport = Vehicles.getVehicleList().stream().filter(
					e -> e.getPlateNumber().equals(Vehicle.getPlateNumber())).findFirst();
			
			if (existingReport.isPresent()) {
				Vehicles.getVehicleList().remove(existingReport.get());
				Vehicles.getVehicleList().add(Vehicle);
				log("Vehicle with plate number " + Vehicle.getPlateNumber() + " updated.", LogUtils.Severity.INFO);
			} else {
				Vehicles.getVehicleList().add(Vehicle);
				log("Vehicle with plate number " + Vehicle.getPlateNumber() + " added.", LogUtils.Severity.INFO);
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
			
			if (Vehicles.getVehicleList() != null) {
				return Vehicles.getVehicleList().stream().filter(
						e -> e.getPlateNumber().equalsIgnoreCase(Vehiclenumber)).findFirst();
			}
			
			return Optional.empty();
		}
		
	}
	
}