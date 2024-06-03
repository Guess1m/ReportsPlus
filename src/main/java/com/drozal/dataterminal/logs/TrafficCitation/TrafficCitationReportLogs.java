package com.drozal.dataterminal.logs.TrafficCitation;

import com.drozal.dataterminal.util.Misc.stringUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.drozal.dataterminal.util.Misc.LogUtils.logError;

@XmlRootElement
public class TrafficCitationReportLogs {
	private List<TrafficCitationLogEntry> logs;
	
	public TrafficCitationReportLogs() {
	}
	
	public static List<TrafficCitationLogEntry> extractLogEntries(String filePath) {
		List<TrafficCitationLogEntry> logEntries = new ArrayList<>();
		
		try {
			File file = new File(filePath);
			
			if (!file.exists()) {
				return logEntries;
			}
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement()
			   .normalize();
			
			NodeList logsList = doc.getElementsByTagName("logs");
			
			for (int i = 0; i < logsList.getLength(); i++) {
				Node logsNode = logsList.item(i);
				if (logsNode.getNodeType() == Node.ELEMENT_NODE) {
					Element logsElement = (Element) logsNode;
					TrafficCitationLogEntry logEntry = new TrafficCitationLogEntry();
					logEntry.citationNumber = getTagValue(logsElement, "citationNumber");
					logEntry.citationDate = getTagValue(logsElement, "citationDate");
					logEntry.citationTime = getTagValue(logsElement, "citationTime");
					logEntry.citationCharges = getTagValue(logsElement, "citationCharges");
					logEntry.citationCounty = getTagValue(logsElement, "citationCounty");
					logEntry.citationArea = getTagValue(logsElement, "citationArea");
					logEntry.citationStreet = getTagValue(logsElement, "citationStreet");
					logEntry.offenderName = getTagValue(logsElement, "offenderName");
					logEntry.offenderGender = getTagValue(logsElement, "offenderGender");
					logEntry.offenderAge = getTagValue(logsElement, "offenderAge");
					logEntry.offenderHomeAddress = getTagValue(logsElement, "offenderHomeAddress");
					logEntry.offenderDescription = getTagValue(logsElement, "offenderDescription");
					logEntry.offenderVehicleModel = getTagValue(logsElement, "offenderVehicleModel");
					logEntry.offenderVehicleColor = getTagValue(logsElement, "offenderVehicleColor");
					logEntry.offenderVehicleType = getTagValue(logsElement, "offenderVehicleType");
					logEntry.offenderVehiclePlate = getTagValue(logsElement, "offenderVehiclePlate");
					logEntry.offenderVehicleOther = getTagValue(logsElement, "offenderVehicleOther");
					logEntry.officerRank = getTagValue(logsElement, "officerRank");
					logEntry.officerName = getTagValue(logsElement, "officerName");
					logEntry.officerNumber = getTagValue(logsElement, "officerNumber");
					logEntry.officerDivision = getTagValue(logsElement, "officerDivision");
					logEntry.officerAgency = getTagValue(logsElement, "officerAgency");
					logEntry.citationComments = getTagValue(logsElement, "citationComments");
					logEntries.add(logEntry);
				}
			}
		} catch (Exception e) {
			logError("Exception", e);
		}
		return logEntries;
	}
	
	public static String getTagValue(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList != null && nodeList.getLength() > 0) {
			return nodeList.item(0)
			               .getTextContent();
		}
		return "";
	}
	
	public static int countReports() {
		try {
			
			List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static List<TrafficCitationLogEntry> loadLogsFromXML() {
		try {
			Path filePath = Paths.get(stringUtil.trafficCitationLogURL);
			if (!Files.exists(filePath)) {
				return new ArrayList<>();
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(TrafficCitationReportLogs.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			TrafficCitationReportLogs logList = (TrafficCitationReportLogs) unmarshaller.unmarshal(filePath.toFile());
			return logList.getLogs();
		} catch (JAXBException e) {
			logError("Exception", e);
			return new ArrayList<>();
		}
	}
	
	public static void saveLogsToXML(List<TrafficCitationLogEntry> logs) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TrafficCitationReportLogs.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			TrafficCitationReportLogs logList = new TrafficCitationReportLogs();
			logList.setLogs(logs);
			
			try (FileOutputStream fos = new FileOutputStream(stringUtil.trafficCitationLogURL)) {
				marshaller.marshal(logList, fos);
			}
		} catch (JAXBException | IOException e) {
			logError("Exception", e);
		}
	}
	
	public List<TrafficCitationLogEntry> getLogs() {
		return logs;
	}
	
	public void setLogs(List<TrafficCitationLogEntry> logs) {
		this.logs = logs;
	}
	
}