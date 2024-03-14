package com.drozal.dataterminal.logs.Search;

import com.drozal.dataterminal.util.stringUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
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

@XmlRootElement
public class SearchReportLogs {
    private List<SearchLogEntry> logs;

    public SearchReportLogs() {
    }

    public static List<SearchLogEntry> extractLogEntries(String filePath) {
        List<SearchLogEntry> logEntries = new ArrayList<>();

        try {
            File file = new File(filePath);

            // Check if the file exists
            if (!file.exists()) {
                return logEntries; // Return an empty list
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

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
                    logEntries.add(logEntry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logEntries;
    }

    public static void addLogEntryToGrid(GridPane gridPane, SearchLogEntry logEntry, int rowIndex) {
        // Create labels for each log entry field
        Label spinnerLabel = createLabel(logEntry.SearchNumber);
        Label searchedPersonsLabel = createLabel(logEntry.searchedPersons);
        Label incidentDateLabel = createLabel(logEntry.searchDate);
        Label incidentTimeLabel = createLabel(logEntry.searchTime);
        Label incidentStatementLabel = createLabel(logEntry.searchSeizedItems);
        Label incidentWitnessesLabel = createLabel(logEntry.searchGrounds);
        Label incidentVictimsLabel = createLabel(logEntry.searchType);
        Label officerNameLabel = createLabel(logEntry.searchMethod);
        Label officerRankLabel = createLabel(logEntry.searchWitnesses);
        Label officerNumberLabel = createLabel(logEntry.officerRank);
        Label officerAgencyLabel = createLabel(logEntry.officerName);
        Label officerDivisionLabel = createLabel(logEntry.officerNumber);
        Label incidentStreetLabel = createLabel(logEntry.officerAgency);
        Label incidentAreaLabel = createLabel(logEntry.officerDivision);
        Label incidentCountyLabel = createLabel(logEntry.searchStreet);
        Label incidentActionsTakenLabel = createLabel(logEntry.searchArea);
        Label incidentCommentsLabel = createLabel(logEntry.searchCounty);

// Add labels to the GridPane
        gridPane.addRow(rowIndex, spinnerLabel, searchedPersonsLabel, incidentDateLabel, incidentTimeLabel, incidentStatementLabel,
                incidentWitnessesLabel, incidentVictimsLabel, officerNameLabel, officerRankLabel, officerNumberLabel,
                officerAgencyLabel, officerDivisionLabel, incidentStreetLabel, incidentAreaLabel, incidentCountyLabel,
                incidentActionsTakenLabel, incidentCommentsLabel);
    }

    public static Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", 14));
        label.setWrapText(true); // Allow text wrapping
        label.setMaxWidth(Double.MAX_VALUE); // Allow label to grow horizontally
        return label;
    }

    public static String getTagValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    public static List<SearchLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get(stringUtil.searchLogURL);
            if (!Files.exists(filePath)) {
                return new ArrayList<>(); // Return an empty list if file doesn't exist
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(SearchReportLogs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SearchReportLogs logList = (SearchReportLogs) unmarshaller.unmarshal(filePath.toFile());
            return logList.getLogs();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if loading fails
        }
    }

    // Save logs to XML
    public static void saveLogsToXML(List<SearchLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            SearchReportLogs logList = new SearchReportLogs();
            logList.setLogs(logs);

            // Use try-with-resources to ensure FileOutputStream is closed properly
            try (FileOutputStream fos = new FileOutputStream(stringUtil.searchLogURL)) {
                marshaller.marshal(logList, fos);
            }
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<SearchLogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<SearchLogEntry> logs) {
        this.logs = logs;
    }

}