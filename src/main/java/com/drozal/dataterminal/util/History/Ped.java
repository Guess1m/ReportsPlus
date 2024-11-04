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
import static com.drozal.dataterminal.util.Misc.controllerUtils.calculateAge;
import static com.drozal.dataterminal.util.Misc.stringUtil.pedHistoryURL;

@XmlRootElement(name = "ped")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ped {
	
	@XmlElement(name = "Name")
	private String name;
	
	@XmlElement(name = "LicenseNumber")
	private String licenseNumber;
	
	@XmlElement(name = "Model")
	private String model;
	
	@XmlElement(name = "Aliases")
	private String aliases;
	
	@XmlElement(name = "Flags")
	private String flags;
	
	@XmlElement(name = "Description")
	private String description;
	
	@XmlElement(name = "Affiliations")
	private String affiliations;
	
	@XmlElement(name = "Gender")
	private String gender;
	
	@XmlElement(name = "Birthday")
	private String birthday;
	
	@XmlElement(name = "Address")
	private String address;
	
	@XmlElement(name = "WantedStatus")
	private String wantedStatus;
	
	@XmlElement(name = "OutstandingWarrants")
	private String outstandingWarrants;
	
	@XmlElement(name = "LicenseStatus")
	private String licenseStatus;
	
	@XmlElement(name = "ParoleStatus")
	private String paroleStatus;
	
	@XmlElement(name = "ProbationStatus")
	private String probationStatus;
	
	@XmlElement(name = "TimesStopped")
	private String timesStopped;
	
	@XmlElement(name = "GunLicenseStatus")
	private String gunLicenseStatus;
	
	@XmlElement(name = "GunLicenseType")
	private String gunLicenseType;
	
	@XmlElement(name = "GunLicenseClass")
	private String gunLicenseClass;
	
	@XmlElement(name = "FishingLicenseStatus")
	private String fishingLicenseStatus;
	
	@XmlElement(name = "BoatingLicenseStatus")
	private String boatingLicenseStatus;
	
	@XmlElement(name = "HuntingLicenseStatus")
	private String huntingLicenseStatus;
	
	@XmlElement(name = "ArrestPriors")
	private String arrestPriors;
	
	@XmlElement(name = "CitationPriors")
	private String citationPriors;
	
	@XmlElement(name = "VehiclePlateNum")
	private String vehiclePlateNum;
	
	public String getFirstName() {
		if (name != null && !name.isEmpty()) {
			String[] parts = name.split(" ");
			return parts[0];
		}
		return "";
	}
	
	public String getLastName() {
		if (name != null && !name.isEmpty()) {
			String[] parts = name.split(" ");
			if (parts.length > 1) {
				return parts[parts.length - 1];
			}
		}
		return "";
	}
	
	public String getAge() {
		if (birthday != null && !birthday.isEmpty()) {
			return calculateAge(birthday);
		}
		return "";
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getVehiclePlateNum() {
		return vehiclePlateNum;
	}
	
	public void setVehiclePlateNum(String vehiclePlateNum) {
		this.vehiclePlateNum = vehiclePlateNum;
	}
	
	public String getGunLicenseClass() {
		return gunLicenseClass;
	}
	
	public void setGunLicenseClass(String gunLicenseClass) {
		this.gunLicenseClass = gunLicenseClass;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAffiliations() {
		return affiliations;
	}
	
	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}
	
	public String getFlags() {
		return flags;
	}
	
	public void setFlags(String flags) {
		this.flags = flags;
	}
	
	public String getAliases() {
		return aliases;
	}
	
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	
	public String getArrestPriors() {
		return arrestPriors;
	}
	
	public void setArrestPriors(String arrestPriors) {
		this.arrestPriors = arrestPriors;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getBoatingLicenseStatus() {
		return boatingLicenseStatus;
	}
	
	public void setBoatingLicenseStatus(String boatingLicenseStatus) {
		this.boatingLicenseStatus = boatingLicenseStatus;
	}
	
	public String getCitationPriors() {
		return citationPriors;
	}
	
	public void setCitationPriors(String citationPriors) {
		this.citationPriors = citationPriors;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFishingLicenseStatus() {
		return fishingLicenseStatus;
	}
	
	public void setFishingLicenseStatus(String fishingLicenseStatus) {
		this.fishingLicenseStatus = fishingLicenseStatus;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getGunLicenseStatus() {
		return gunLicenseStatus;
	}
	
	public void setGunLicenseStatus(String gunLicenseStatus) {
		this.gunLicenseStatus = gunLicenseStatus;
	}
	
	public String getLicenseNumber() {
		return licenseNumber;
	}
	
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	
	public String getLicenseStatus() {
		return licenseStatus;
	}
	
	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOutstandingWarrants() {
		return outstandingWarrants;
	}
	
	public void setOutstandingWarrants(String outstandingWarrants) {
		this.outstandingWarrants = outstandingWarrants;
	}
	
	public String getParoleStatus() {
		return paroleStatus;
	}
	
	public void setParoleStatus(String paroleStatus) {
		this.paroleStatus = paroleStatus;
	}
	
	public String getProbationStatus() {
		return probationStatus;
	}
	
	public void setProbationStatus(String probationStatus) {
		this.probationStatus = probationStatus;
	}
	
	public String getTimesStopped() {
		return timesStopped;
	}
	
	public void setTimesStopped(String timesStopped) {
		this.timesStopped = timesStopped;
	}
	
	public String getWantedStatus() {
		return wantedStatus;
	}
	
	public void setWantedStatus(String wantedStatus) {
		this.wantedStatus = wantedStatus;
	}
	
	public String getGunLicenseType() {
		return gunLicenseType;
	}
	
	public void setGunLicenseType(String gunLicenseType) {
		this.gunLicenseType = gunLicenseType;
	}
	
	public String getHuntingLicenseStatus() {
		return huntingLicenseStatus;
	}
	
	public void setHuntingLicenseStatus(String huntingLicenseStatus) {
		this.huntingLicenseStatus = huntingLicenseStatus;
	}
	
	@XmlRootElement(name = "PedDatabase")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Peds {
		
		@XmlElement(name = "Ped")
		private List<Ped> pedList;
		
		public List<Ped> getPedList() {
			return pedList;
		}
		
		public void setPedList(List<Ped> pedList) {
			this.pedList = pedList;
		}
	}
	
	public static class PedHistoryUtils {
		
		public static Peds loadPeds() throws JAXBException {
			File file = new File(pedHistoryURL);
			if (!file.exists()) {
				return new Peds();
			}
			
			try {
				JAXBContext context = JAXBContext.newInstance(Peds.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				return (Peds) unmarshaller.unmarshal(file);
			} catch (JAXBException e) {
				logError("Error loading Peds: ", e);
				throw e;
			}
		}
		
		private static void savePeds(Peds Peds) throws JAXBException {
			JAXBContext context = JAXBContext.newInstance(Peds.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			File file = new File(pedHistoryURL);
			marshaller.marshal(Peds, file);
		}
		
		public static void addPed(Ped Ped) throws JAXBException {
			Peds Peds = loadPeds();
			
			if (Peds.getPedList() == null) {
				Peds.setPedList(new java.util.ArrayList<>());
			}
			
			Optional<Ped> existingReport = Peds.getPedList().stream().filter(
					e -> e.getLicenseNumber().equals(Ped.getLicenseNumber())).findFirst();
			
			if (existingReport.isPresent()) {
				Peds.getPedList().remove(existingReport.get());
				Peds.getPedList().add(Ped);
				log("Ped with license number " + Ped.getLicenseNumber() + " updated.", LogUtils.Severity.INFO);
			} else {
				Peds.getPedList().add(Ped);
				log("Ped with license number " + Ped.getLicenseNumber() + " added.", LogUtils.Severity.INFO);
			}
			
			savePeds(Peds);
		}
		
		public static void deletePed(String Pednumber) throws JAXBException {
			Peds Peds = loadPeds();
			
			if (Peds.getPedList() != null) {
				Peds.getPedList().removeIf(e -> e.getLicenseNumber().equals(Pednumber));
				savePeds(Peds);
				log("Ped with license number " + Pednumber + " deleted.", LogUtils.Severity.INFO);
			}
		}
		
		public static Optional<Ped> findPedByNumber(String Pednumber) {
			Peds Peds = null;
			try {
				Peds = loadPeds();
			} catch (JAXBException e) {
				logError("JAXB error while trying to loadPeds() by number: ", e);
			}
			
			if (Peds.getPedList() != null) {
				return Peds.getPedList().stream().filter(e -> e.getLicenseNumber().equals(Pednumber)).findFirst();
			}
			
			return Optional.empty();
		}
		
		public static Optional<Ped> findPedByName(String pedname) {
			Peds Peds = null;
			try {
				Peds = loadPeds();
			} catch (JAXBException e) {
				logError("JAXB error while trying to loadPeds() by name: ", e);
			}
			
			if (Peds.getPedList() != null) {
				return Peds.getPedList().stream().filter(e -> e.getName().equalsIgnoreCase(pedname)).findFirst();
			}
			
			return Optional.empty();
		}
		
		public static void modifyPed(String number, Ped updatedPed) throws JAXBException {
			Peds Peds = loadPeds();
			
			if (Peds.getPedList() != null) {
				for (int i = 0; i < Peds.getPedList().size(); i++) {
					Ped e = Peds.getPedList().get(i);
					if (e.getLicenseNumber().equals(number)) {
						Peds.getPedList().set(i, updatedPed);
						savePeds(Peds);
						return;
					}
				}
			}
		}
		
	}
	
}