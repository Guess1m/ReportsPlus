package com.Guess.ReportsPlus.util.UserProfiles;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.currentUserFileURL;

import java.io.File;
import java.util.Optional;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

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
	@XmlElement(name = "Username")
	private String username;
	@XmlElement(name = "Password")
	private String password;

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
			logInfo("SearchReport with number " + name + " deleted.");
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
		Optional<User> existingReport = Profiles.getUserList().stream().filter(e -> e.getName().equals(User.getName()))
				.findFirst();
		if (existingReport.isPresent()) {
			Profiles.getUserList().remove(existingReport.get());
			Profiles.getUserList().add(User);
			logInfo("User with name " + User.getName() + " updated.");
		} else {
			Profiles.getUserList().add(User);
			logInfo("User with name " + User.getName() + " added.");
		}
		saveUserProfiles(Profiles);
	}

	public static User getUser(String name) {
		Profiles userProfiles = null;
		try {
			userProfiles = loadUserProfiles();
		} catch (JAXBException e) {
			logError("Error loading Profiles [2]: ", e);
		}
		if (userProfiles.getUserList() != null) {
			for (User user : userProfiles.getUserList()) {
				if (user.getName().equals(name)) {
					return user;
				}
			}
		}
		return null;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return getName() + ", " + getAgency() + ", " + getRank() + ", " + getNumber() + ", " + getDivision() + ", "
				+ getCallsign();
	}
}
