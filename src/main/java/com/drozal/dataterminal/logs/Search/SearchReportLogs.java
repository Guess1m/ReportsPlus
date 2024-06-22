package com.drozal.dataterminal.logs.Search;

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
public class SearchReportLogs {
	private List<SearchLogEntry> logs;
	
	public SearchReportLogs() {
	}
	
	public static List<SearchLogEntry> extractLogEntries(String filePath) {
		List<SearchLogEntry> logEntries = new ArrayList<>();
		
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
					SearchLogEntry logEntry = new SearchLogEntry();
					logEntry.SearchNumber = getTagValue(logsElement, "SearchNumber");
					logEntry.searchedPersons = getTagValue(logsElement, "searchedPersons");
					logEntry.searchDate = getTagValue(logsElement, "searchDate");
					logEntry.searchTime = getTagValue(logsElement, "searchTime");
					logEntry.searchSeizedItems = getTagValue(logsElement, "searchSeizedItems");
					logEntry.searchGrounds = getTagValue(logsElement, "searchGrounds");
					logEntry.searchType = getTagValue(logsElement, "searchType");
					logEntry.searchMethod = getTagValue(logsElement, "searchMethod");
					logEntry.searchWitnesses = getTagValue(logsElement, "searchWitnesses");
					logEntry.officerRank = getTagValue(logsElement, "officerRank");
					logEntry.officerName = getTagValue(logsElement, "officerName");
					logEntry.officerNumber = getTagValue(logsElement, "officerNumber");
					logEntry.officerAgency = getTagValue(logsElement, "officerAgency");
					logEntry.officerDivision = getTagValue(logsElement, "officerDivision");
					logEntry.searchStreet = getTagValue(logsElement, "searchStreet");
					logEntry.searchArea = getTagValue(logsElement, "searchArea");
					logEntry.searchCounty = getTagValue(logsElement, "searchCounty");
					logEntry.searchComments = getTagValue(logsElement, "searchComments");
					logEntry.testsConducted = getTagValue(logsElement, "testsConducted");
					logEntry.testResults = getTagValue(logsElement, "testResults");
					logEntry.breathalyzerBACMeasure = getTagValue(logsElement, "breathalyzerBACMeasure");
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
			
			List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();
			
			return logs.size();
		} catch (Exception e) {
			logError("Exception", e);
			return -1;
		}
	}
	
	public static List<SearchLogEntry> loadLogsFromXML() {
		try {
			Path filePath = Paths.get(stringUtil.searchLogURL);
			if (!Files.exists(filePath)) {
				return new ArrayList<>();
			}
			
			JAXBContext jaxbContext = JAXBContext.newInstance(SearchReportLogs.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			SearchReportLogs logList = (SearchReportLogs) unmarshaller.unmarshal(filePath.toFile());
			return logList.getLogs();
		} catch (JAXBException e) {
			logError("Exception", e);
			return new ArrayList<>();
		}
	}
	
	public static void saveLogsToXML(List<SearchLogEntry> logs) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SearchReportLogs.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SearchReportLogs logList = new SearchReportLogs();
			logList.setLogs(logs);
			
			try (FileOutputStream fos = new FileOutputStream(stringUtil.searchLogURL)) {
				marshaller.marshal(logList, fos);
			}
		} catch (JAXBException | IOException e) {
			logError("Exception", e);
		}
	}
	
	public List<SearchLogEntry> getLogs() {
		return logs;
	}
	
	public void setLogs(List<SearchLogEntry> logs) {
		this.logs = logs;
	}
	
}