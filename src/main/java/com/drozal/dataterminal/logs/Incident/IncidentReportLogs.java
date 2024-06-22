package com.drozal.dataterminal.logs.Incident;

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
public class IncidentReportLogs {
	private List<IncidentLogEntry> logs;
	
	public IncidentReportLogs() {
	}
	
	public static List<IncidentLogEntry> extractLogEntries(String filePath) {
		List<IncidentLogEntry> logEntries = new ArrayList<>();
		
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
					IncidentLogEntry logEntry = new IncidentLogEntry();
					logEntry.incidentNumber = getTagValue(logsElement, "incidentNumber");
					logEntry.incidentDate = getTagValue(logsElement, "incidentDate");
					logEntry.incidentTime = getTagValue(logsElement, "incidentTime");
					logEntry.incidentStatement = getTagValue(logsElement, "incidentStatement");
					logEntry.incidentWitnesses = getTagValue(logsElement, "incidentWitnesses");
					logEntry.incidentVictims = getTagValue(logsElement, "incidentVictims");
					logEntry.officerName = getTagValue(logsElement, "officerName");
					logEntry.officerRank = getTagValue(logsElement, "officerRank");
					logEntry.officerNumber = getTagValue(logsElement, "officerNumber");
					logEntry.officerAgency = getTagValue(logsElement, "officerAgency");
					logEntry.officerDivision = getTagValue(logsElement, "officerDivision");
					logEntry.incidentStreet = getTagValue(logsElement, "incidentStreet");
					logEntry.incidentArea = getTagValue(logsElement, "incidentArea");
					logEntry.incidentCounty = getTagValue(logsElement, "incidentCounty");
					logEntry.incidentActionsTaken = getTagValue(logsElement, "incidentActionsTaken");
					logEntry.incidentComments = getTagValue(logsElement, "incidentComments");
					
					logEntries.add(logEntry);
				}
			}
		} catch (Exception e) {
			logError("Exception", e);
		}
		
		return logEntries;
	}
	
	@SuppressWarnings("ConstantValue")
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
			
			List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static List<IncidentLogEntry> loadLogsFromXML() {
		try {
			Path filePath = Paths.get(stringUtil.incidentLogURL);
			if (!Files.exists(filePath)) {
				return new ArrayList<>();
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(IncidentReportLogs.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			IncidentReportLogs logList = (IncidentReportLogs) unmarshaller.unmarshal(filePath.toFile());
			return logList.getLogs();
		} catch (JAXBException e) {
			logError("Exception", e);
			return new ArrayList<>();
		}
	}
	
	public static void saveLogsToXML(List<IncidentLogEntry> logs) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(IncidentReportLogs.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			IncidentReportLogs logList = new IncidentReportLogs();
			logList.setLogs(logs);
			
			try (FileOutputStream fos = new FileOutputStream(stringUtil.incidentLogURL)) {
				marshaller.marshal(logList, fos);
			}
		} catch (JAXBException | IOException e) {
			logError("Exception", e);
		}
	}
	
	public List<IncidentLogEntry> getLogs() {
		return logs;
	}
	
	public void setLogs(List<IncidentLogEntry> logs) {
		this.logs = logs;
	}
	
}