package com.drozal.dataterminal.logs.TrafficStop;

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

/**
 * The type Traffic stop report logs.
 */
@XmlRootElement
public class TrafficStopReportLogs {
	private List<TrafficStopLogEntry> logs;
	
	/**
	 * Instantiates a new Traffic stop report logs.
	 */
	public TrafficStopReportLogs() {
	}
	
	/**
	 * Extract log entries list.
	 *
	 * @param filePath the file path
	 * @return the list
	 */
	public static List<TrafficStopLogEntry> extractLogEntries(String filePath) {
		List<TrafficStopLogEntry> logEntries = new ArrayList<>();
		
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
					TrafficStopLogEntry logEntry = new TrafficStopLogEntry();
					logEntry.StopNumber = getTagValue(logsElement, "StopNumber");
					logEntry.Date = getTagValue(logsElement, "Date");
					logEntry.Time = getTagValue(logsElement, "Time");
					logEntry.PlateNumber = getTagValue(logsElement, "PlateNumber");
					logEntry.Color = getTagValue(logsElement, "Color");
					logEntry.Type = getTagValue(logsElement, "Type");
					logEntry.ResponseModel = getTagValue(logsElement, "ResponseModel");
					logEntry.ResponseOtherInfo = getTagValue(logsElement, "ResponseOtherInfo");
					logEntry.operatorName = getTagValue(logsElement, "operatorName");
					logEntry.operatorAge = getTagValue(logsElement, "operatorAge");
					logEntry.operatorGender = getTagValue(logsElement, "operatorGender");
					logEntry.operatorDescription = getTagValue(logsElement, "operatorDescription");
					logEntry.operatorAddress = getTagValue(logsElement, "operatorAddress");
					logEntry.Rank = getTagValue(logsElement, "Rank");
					logEntry.Name = getTagValue(logsElement, "Name");
					logEntry.Division = getTagValue(logsElement, "Division");
					logEntry.Agency = getTagValue(logsElement, "Agency");
					logEntry.Number = getTagValue(logsElement, "Number");
					logEntry.Street = getTagValue(logsElement, "Street");
					logEntry.County = getTagValue(logsElement, "County");
					logEntry.Area = getTagValue(logsElement, "Area");
					logEntry.CommentsTextArea = getTagValue(logsElement, "CommentsTextArea");
					
					logEntries.add(logEntry);
				}
			}
		} catch (Exception e) {
			logError("Exception", e);
		}
		
		return logEntries;
	}
	
	/**
	 * Gets tag value.
	 *
	 * @param element the element
	 * @param tagName the tag name
	 * @return the tag value
	 */
	public static String getTagValue(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList != null && nodeList.getLength() > 0) {
			return nodeList.item(0)
					.getTextContent();
		}
		return "";
	}
	
	/**
	 * Count reports int.
	 *
	 * @return the int
	 */
	public static int countReports() {
		try {
			
			List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	/**
	 * Load logs from xml list.
	 *
	 * @return the list
	 */
	public static List<TrafficStopLogEntry> loadLogsFromXML() {
		try {
			Path filePath = Paths.get(stringUtil.trafficstopLogURL);
			if (!Files.exists(filePath)) {
				return new ArrayList<>();
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(TrafficStopReportLogs.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			TrafficStopReportLogs logList = (TrafficStopReportLogs) unmarshaller.unmarshal(filePath.toFile());
			return logList.getLogs();
		} catch (JAXBException e) {
			logError("Exception", e);
			return new ArrayList<>();
		}
	}
	
	/**
	 * Save logs to xml.
	 *
	 * @param logs the logs
	 */
	public static void saveLogsToXML(List<TrafficStopLogEntry> logs) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TrafficStopReportLogs.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			TrafficStopReportLogs logList = new TrafficStopReportLogs();
			logList.setLogs(logs);
			
			try (FileOutputStream fos = new FileOutputStream(stringUtil.trafficstopLogURL)) {
				marshaller.marshal(logList, fos);
			}
		} catch (JAXBException | IOException e) {
			logError("SaveLogToXml Exception", e);
		}
	}
	
	/**
	 * Gets logs.
	 *
	 * @return the logs
	 */
	public List<TrafficStopLogEntry> getLogs() {
		return logs;
	}
	
	/**
	 * Sets logs.
	 *
	 * @param logs the logs
	 */
	public void setLogs(List<TrafficStopLogEntry> logs) {
		this.logs = logs;
	}
	
}