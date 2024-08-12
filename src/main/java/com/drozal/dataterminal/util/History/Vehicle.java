package com.drozal.dataterminal.util.History;

import com.drozal.dataterminal.util.Misc.LogUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.vehicleHistoryURL;

@XmlRootElement(name = "vehicle")
@XmlAccessorType(XmlAccessType.FIELD)
public class Vehicle {
	//
	//	licensePlate=46EEK572&model=0x361ca82c&isStolen=False&isPolice=True&owner=David McReary&driver=John Mckennedy&registration=Valid&insurance=Valid&color=
	
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
	
	@XmlElement(name = "Insurance")
	private String insurance;
	
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
	
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	public String getPlateNumber() {
		return plateNumber;
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
		
		public static void deleteVehicle(String Vehiclenumber) throws JAXBException {
			Vehicles Vehicles = loadVehicles();
			
			if (Vehicles.getVehicleList() != null) {
				Vehicles.getVehicleList().removeIf(e -> e.getPlateNumber().equals(Vehiclenumber));
				saveVehicles(Vehicles);
				log("Vehicle with plate number " + Vehiclenumber + " deleted.", LogUtils.Severity.INFO);
			}
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
						e -> e.getPlateNumber().equals(Vehiclenumber)).findFirst();
			}
			
			return Optional.empty();
		}
		
		public static void modifyVehicle(String number, Vehicle updatedVehicle) throws JAXBException {
			Vehicles Vehicles = loadVehicles();
			
			if (Vehicles.getVehicleList() != null) {
				for (int i = 0; i < Vehicles.getVehicleList().size(); i++) {
					Vehicle e = Vehicles.getVehicleList().get(i);
					if (e.getPlateNumber().equals(number)) {
						Vehicles.getVehicleList().set(i, updatedVehicle);
						saveVehicles(Vehicles);
						return;
					}
				}
			}
		}
		
	}
	
}