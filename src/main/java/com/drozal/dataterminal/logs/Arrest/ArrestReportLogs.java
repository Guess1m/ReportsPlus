package com.drozal.dataterminal.logs.Arrest;

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
public class ArrestReportLogs {
    private List<ArrestLogEntry> logs;

    public ArrestReportLogs() {
    }

    public static List<ArrestLogEntry> extractLogEntries(String filePath) {
        List<ArrestLogEntry> logEntries = new ArrayList<>();

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
                    ArrestLogEntry logEntry = new ArrestLogEntry();
                    logEntry.arrestNumber = getTagValue(logsElement, "arrestNumber");
                    logEntry.arresteeName = getTagValue(logsElement, "arresteeName");
                    logEntry.arrestDate = getTagValue(logsElement, "arrestDate");
                    logEntry.arrestTime = getTagValue(logsElement, "arrestTime");
                    logEntry.arrestCounty = getTagValue(logsElement, "arrestCounty");
                    logEntry.arrestArea = getTagValue(logsElement, "arrestArea");
                    logEntry.arrestStreet = getTagValue(logsElement, "arrestStreet");
                    logEntry.arresteeAge = getTagValue(logsElement, "arresteeAge");
                    logEntry.arresteeGender = getTagValue(logsElement, "arresteeGender");
                    logEntry.arresteeEthnicity = getTagValue(logsElement, "arresteeEthnicity");
                    logEntry.arresteeDescription = getTagValue(logsElement, "arresteeDescription");
                    logEntry.arresteeMedicalInformation = getTagValue(logsElement, "arresteeMedicalInformation");
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
            e.printStackTrace();
        }
        return logEntries;
    }

    public static void addLogEntryToGrid(GridPane gridPane, ArrestLogEntry logEntry, int rowIndex) {
        // Create labels for each log entry field
        Label arrestNumberLabel = createLabel(logEntry.arrestNumber);
        Label arrestDateLabel = createLabel(logEntry.arrestDate);
        Label arrestTimeLabel = createLabel(logEntry.arrestTime);
        Label arrestCountyLabel = createLabel(logEntry.arrestCounty);
        Label arrestAreaLabel = createLabel(logEntry.arrestArea);
        Label arrestStreetLabel = createLabel(logEntry.arrestStreet);
        Label arresteeNameLabel = createLabel(logEntry.arresteeName);
        Label arresteeAgeLabel = createLabel(logEntry.arresteeAge);
        Label arresteeGenderLabel = createLabel(logEntry.arresteeGender);
        Label arresteeEthnicityLabel = createLabel(logEntry.arresteeEthnicity);
        Label arresteeDescriptionLabel = createLabel(logEntry.arresteeDescription);
        Label arresteeMedicalInformationLabel = createLabel(logEntry.arresteeMedicalInformation);
        Label arrestDetailsLabel = createLabel(logEntry.arrestDetails);
        Label officerRankLabel = createLabel(logEntry.officerRank);
        Label officerNameLabel = createLabel(logEntry.officerName);
        Label officerNumberLabel = createLabel(logEntry.officerNumber);
        Label officerDivisionLabel = createLabel(logEntry.officerDivision);
        Label officerAgencyLabel = createLabel(logEntry.officerAgency);


// Add labels to the GridPane
        gridPane.addRow(rowIndex, arrestNumberLabel, arrestDateLabel, arrestTimeLabel, arrestCountyLabel, arrestAreaLabel, arrestStreetLabel,
                arresteeNameLabel, arresteeAgeLabel, arresteeGenderLabel, arresteeEthnicityLabel, arresteeDescriptionLabel,
                arresteeMedicalInformationLabel, arrestDetailsLabel, officerRankLabel, officerNameLabel, officerNumberLabel,
                officerAgencyLabel, officerDivisionLabel);

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

    public static List<ArrestLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get(stringUtil.arrestLogURL);
            if (!Files.exists(filePath)) {
                return new ArrayList<>(); // Return an empty list if file doesn't exist
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(ArrestReportLogs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ArrestReportLogs logList = (ArrestReportLogs) unmarshaller.unmarshal(filePath.toFile());
            return logList.getLogs();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if loading fails
        }
    }

    // Save logs to XML
    public static void saveLogsToXML(List<ArrestLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ArrestReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ArrestReportLogs logList = new ArrestReportLogs();
            logList.setLogs(logs);

            // Use try-with-resources to ensure FileOutputStream is closed properly
            try (FileOutputStream fos = new FileOutputStream(stringUtil.arrestLogURL)) {
                marshaller.marshal(logList, fos);
            }
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<ArrestLogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<ArrestLogEntry> logs) {
        this.logs = logs;
    }

}