package com.drozal.dataterminal.logs.Arrest;

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
public class ArrestReportLogs {
	private List<ArrestLogEntry> logs;
	
	public ArrestReportLogs() {
	}
	
	public static List<ArrestLogEntry> extractLogEntries(String filePath) {
		List<ArrestLogEntry> logEntries = new ArrayList<>();
		
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
					ArrestLogEntry logEntry = new ArrestLogEntry();
					logEntry.arrestNumber = getTagValue(logsElement, "arrestNumber");
					logEntry.arresteeName = getTagValue(logsElement, "arresteeName");
					logEntry.arrestDate = getTagValue(logsElement, "arrestDate");
					logEntry.arrestTime = getTagValue(logsElement, "arrestTime");
					logEntry.arrestCharges = getTagValue(logsElement, "arrestCharges");
					logEntry.arrestCounty = getTagValue(logsElement, "arrestCounty");
					logEntry.arrestArea = getTagValue(logsElement, "arrestArea");
					logEntry.arrestStreet = getTagValue(logsElement, "arrestStreet");
					logEntry.arresteeAge = getTagValue(logsElement, "arresteeAge");
					logEntry.arresteeGender = getTagValue(logsElement, "arresteeGender");
					logEntry.arresteeDescription = getTagValue(logsElement, "arresteeDescription");
					logEntry.ambulanceYesNo = getTagValue(logsElement, "ambulanceYesNo");
					logEntry.TaserYesNo = getTagValue(logsElement, "TaserYesNo");
					logEntry.arresteeMedicalInformation = getTagValue(logsElement, "arresteeMedicalInformation");
					logEntry.arresteeHomeAddress = getTagValue(logsElement, "arresteeHomeAddress");
					logEntry.arrestDetails = getTagValue(logsElement, "arrestDetails");
					logEntry.officerRank = getTagValue(logsElement, "officerRank");
					logEntry.officerName = getTagValue(logsElement, "officerName");
					logEntry.officerNumber = getTagValue(logsElement, "officerNumber");
					logEntry.officerAgency = getTagValue(logsElement, "officerAgency");
					logEntry.officerDivision = getTagValue(logsElement, "officerDivision");
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
			
			List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static List<ArrestLogEntry> loadLogsFromXML() {
		try {
			Path filePath = Paths.get(stringUtil.arrestLogURL);
			if (!Files.exists(filePath)) {
				return new ArrayList<>();
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(ArrestReportLogs.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ArrestReportLogs logList = (ArrestReportLogs) unmarshaller.unmarshal(filePath.toFile());
			return logList.getLogs();
		} catch (JAXBException e) {
			logError("Exception", e);
			return new ArrayList<>();
		}
	}
	
	public static void saveLogsToXML(List<ArrestLogEntry> logs) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ArrestReportLogs.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ArrestReportLogs logList = new ArrestReportLogs();
			logList.setLogs(logs);
			
			try (FileOutputStream fos = new FileOutputStream(stringUtil.arrestLogURL)) {
				marshaller.marshal(logList, fos);
			}
		} catch (JAXBException | IOException e) {
			logError("Exception", e);
		}
	}
	
	public List<ArrestLogEntry> getLogs() {
		return logs;
	}
	
	public void setLogs(List<ArrestLogEntry> logs) {
		this.logs = logs;
	}
	
}