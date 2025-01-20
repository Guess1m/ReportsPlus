package com.Guess.ReportsPlus.util.Server.Objects.ID;

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
import static com.Guess.ReportsPlus.util.Misc.URLStrings.currentIDFileURL;

@XmlAccessorType(XmlAccessType.FIELD)
public class ID {
	
	@XmlElement(name = "Name")
	private String name;
	
	@XmlElement(name = "Birthday")
	private String birthday;
	
	@XmlElement(name = "Gender")
	private String gender;
	
	@XmlElement(name = "Address")
	private String address;
	
	@XmlElement(name = "PedModel")
	private String pedModel;
	
	@XmlElement(name = "LicenseNumber")
	private String licenseNumber;
	
	@XmlElement(name = "Status")
	private String status;
	
	public static IDs loadServerIDs() throws JAXBException {
		File file = new File(currentIDFileURL);
		
		if (!file.exists() || file.length() == 0) {
			return new IDs();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(IDs.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (IDs) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logError("Error loading IDs: ", e);
			throw e;
		}
	}
	
	private static void saveServerIDs(IDs IDs) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(IDs.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		File file = new File(currentIDFileURL);
		marshaller.marshal(IDs, file);
	}
	
	public static void addServerID(ID ID) throws JAXBException {
		IDs IDs = loadServerIDs();
		
		if (IDs.getIdList() == null) {
			IDs.setIdList(new java.util.ArrayList<>());
		}
		
		Optional<ID> existingReport = IDs.getIdList().stream().filter(
				e -> e.getName().equals(ID.getName())).findFirst();
		
		if (existingReport.isPresent()) {
			IDs.getIdList().remove(existingReport.get());
			IDs.getIdList().add(ID);
			log("ServerID with name " + ID.getName() + " updated.", LogUtils.Severity.INFO);
		} else {
			IDs.getIdList().add(ID);
			log("ServerID with name " + ID.getName() + " added.", LogUtils.Severity.INFO);
		}
		
		saveServerIDs(IDs);
	}
	
	public String getName() {
		return name;
	}
	
	public String getPedModel() {
		return pedModel;
	}
	
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
	
	public String getBirthday() {
		return birthday;
	}
	
	public String getLicenseNumber() {
		return licenseNumber;
	}
	
	public String getGender() {
		return gender;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
}
