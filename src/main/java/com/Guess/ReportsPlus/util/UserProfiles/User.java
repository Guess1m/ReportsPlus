package com.Guess.ReportsPlus.util.UserProfiles;

import com.Guess.ReportsPlus.util.Misc.LogUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.File;
import java.util.Optional;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.URLStrings.currentUserFileURL;

@XmlAccessorType(XmlAccessType.FIELD)
public class User {
	
	@XmlElement(name = "Name")
	private String name;
	
	@XmlElement(name = "Agency")
	private String agency;
	
	@XmlElement(name = "Division")
	private String division;
	
	@XmlElement(name = "Rank")
	private String rank;
	
	@XmlElement(name = "Number")
	private String number;
	
	@XmlElement(name = "Callsign")
	private String callsign;
	
	public User(String name, String agency, String division, String rank, String number, String callsign) {
		this.name = name;
		this.agency = agency;
		this.division = division;
		this.rank = rank;
		this.number = number;
		this.callsign = callsign;
	}
	
	public User() {
	}
	
	public static void deleteUser(String name) throws JAXBException {
		Profiles userProfiles = loadUserProfiles();
		
		if (userProfiles.getUserList() != null) {
			userProfiles.getUserList().removeIf(e -> e.getName().equals(name));
			saveUserProfiles(userProfiles);
			log("SearchReport with number " + name + " deleted.", LogUtils.Severity.INFO);
		}
	}
	
	public static Profiles loadUserProfiles() throws JAXBException {
		File file = new File(currentUserFileURL);
		
		if (!file.exists() || file.length() == 0) {
			return new Profiles();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(Profiles.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Profiles) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading Profiles: ", e);
			throw e;
		}
	}
	
	private static void saveUserProfiles(Profiles Profiles) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Profiles.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		File file = new File(currentUserFileURL);
		marshaller.marshal(Profiles, file);
	}
	
	public static void addUser(User User) throws JAXBException {
		Profiles Profiles = loadUserProfiles();
		
		if (Profiles.getUserList() == null) {
			Profiles.setUserList(new java.util.ArrayList<>());
		}
		
		Optional<User> existingReport = Profiles.getUserList().stream().filter(
				e -> e.getName().equals(User.getName())).findFirst();
		
		if (existingReport.isPresent()) {
			Profiles.getUserList().remove(existingReport.get());
			Profiles.getUserList().add(User);
			log("User with name " + User.getName() + " updated.", LogUtils.Severity.INFO);
		} else {
			Profiles.getUserList().add(User);
			log("User with name " + User.getName() + " added.", LogUtils.Severity.INFO);
		}
		saveUserProfiles(Profiles);
	}
	
	public String getAgency() {
		return agency;
	}
	
	public void setAgency(String agency) {
		this.agency = agency;
	}
	
	public String getCallsign() {
		return callsign;
	}
	
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	
	public String getDivision() {
		return division;
	}
	
	public void setDivision(String division) {
		this.division = division;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getRank() {
		return rank;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	@Override
	public String toString() {
		return getName() + ", " + getAgency() + ", " + getRank() + ", " + getNumber() + ", " + getDivision() + ", " + getCallsign();
	}
}
