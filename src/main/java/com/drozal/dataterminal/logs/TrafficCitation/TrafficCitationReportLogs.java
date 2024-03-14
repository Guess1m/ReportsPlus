package com.drozal.dataterminal.logs.TrafficCitation;

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
public class TrafficCitationReportLogs {
    private List<TrafficCitationLogEntry> logs;

    public TrafficCitationReportLogs() {
    }

    public static List<TrafficCitationLogEntry> extractLogEntries(String filePath) {
        List<TrafficCitationLogEntry> logEntries = new ArrayList<>();

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
                    TrafficCitationLogEntry logEntry = new TrafficCitationLogEntry();
                    logEntry.citationNumber = getTagValue(logsElement, "citationNumber");
                    logEntry.citationDate = getTagValue(logsElement, "citationDate");
                    logEntry.citationTime = getTagValue(logsElement, "citationTime");
                    logEntry.citationCounty = getTagValue(logsElement, "citationCounty");
                    logEntry.citationArea = getTagValue(logsElement, "citationArea");
                    logEntry.citationStreet = getTagValue(logsElement, "citationStreet");
                    logEntry.offenderName = getTagValue(logsElement, "offenderName");
                    logEntry.offenderGender = getTagValue(logsElement, "offenderGender");
                    logEntry.offenderEthnicity = getTagValue(logsElement, "offenderEthnicity");
                    logEntry.offenderAge = getTagValue(logsElement, "offenderAge");
                    logEntry.offenderDescription = getTagValue(logsElement, "offenderDescription");
                    logEntry.offenderVehicleMake = getTagValue(logsElement, "offenderVehicleMake");
                    logEntry.offenderVehicleModel = getTagValue(logsElement, "offenderVehicleModel");
                    logEntry.offenderVehicleColor = getTagValue(logsElement, "offenderVehicleColor");
                    logEntry.offenderVehicleType = getTagValue(logsElement, "offenderVehicleType");
                    logEntry.offenderVehiclePlate = getTagValue(logsElement, "offenderVehiclePlate");
                    logEntry.offenderVehicleOther = getTagValue(logsElement, "offenderVehicleOther");
                    logEntry.offenderViolations = getTagValue(logsElement, "offenderViolations");
                    logEntry.offenderActionsTaken = getTagValue(logsElement, "offenderActionsTaken");
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
            e.printStackTrace();
        }
        return logEntries;
    }

    public static void addLogEntryToGrid(GridPane gridPane, TrafficCitationLogEntry logEntry, int rowIndex) {
        // Create labels for each log entry field
        Label citationNumberLabel = createLabel(logEntry.citationNumber);
        Label citationDateLabel = createLabel(logEntry.citationDate);
        Label citationTimeLabel = createLabel(logEntry.citationTime);
        Label citationCountyLabel = createLabel(logEntry.citationCounty);
        Label citationAreaLabel = createLabel(logEntry.citationArea);
        Label citationStreetLabel = createLabel(logEntry.citationStreet);
        Label offenderNameLabel = createLabel(logEntry.offenderName);
        Label offenderGenderLabel = createLabel(logEntry.offenderGender);
        Label offenderEthnicityLabel = createLabel(logEntry.offenderEthnicity);
        Label offenderAgeLabel = createLabel(logEntry.offenderAge);
        Label offenderDescriptionLabel = createLabel(logEntry.offenderDescription);
        Label offenderVehicleMakeLabel = createLabel(logEntry.offenderVehicleMake);
        Label offenderVehicleModelLabel = createLabel(logEntry.offenderVehicleModel);
        Label offenderVehicleColorLabel = createLabel(logEntry.offenderVehicleColor);
        Label offenderVehicleTypeLabel = createLabel(logEntry.offenderVehicleType);
        Label offenderVehiclePlateLabel = createLabel(logEntry.offenderVehiclePlate);
        Label offenderVehicleOtherLabel = createLabel(logEntry.offenderVehicleOther);
        Label offenderViolationsLabel = createLabel(logEntry.offenderViolations);
        Label offenderActionsTakenLabel = createLabel(logEntry.offenderActionsTaken);
        Label officerRankLabel = createLabel(logEntry.officerRank);
        Label officerNameLabel = createLabel(logEntry.officerName);
        Label officerNumberLabel = createLabel(logEntry.officerNumber);
        Label officerDivisionLabel = createLabel(logEntry.officerDivision);
        Label officerAgencyLabel = createLabel(logEntry.officerAgency);
        Label citationCommentsLabel = createLabel(logEntry.citationComments);

// Add labels to the GridPane
        gridPane.addRow(rowIndex, citationNumberLabel, citationDateLabel, citationTimeLabel, citationCountyLabel, citationAreaLabel,
                citationStreetLabel, offenderNameLabel, offenderGenderLabel, offenderEthnicityLabel, offenderAgeLabel,
                offenderDescriptionLabel, offenderVehicleMakeLabel, offenderVehicleModelLabel, offenderVehicleColorLabel,
                offenderVehicleTypeLabel, offenderVehiclePlateLabel, offenderVehicleOtherLabel, offenderViolationsLabel,
                offenderActionsTakenLabel, officerRankLabel, officerNameLabel, officerNumberLabel, officerDivisionLabel,
                officerAgencyLabel, citationCommentsLabel);
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

    public static List<TrafficCitationLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get(stringUtil.trafficCitationLogURL);
            if (!Files.exists(filePath)) {
                return new ArrayList<>(); // Return an empty list if file doesn't exist
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(TrafficCitationReportLogs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            TrafficCitationReportLogs logList = (TrafficCitationReportLogs) unmarshaller.unmarshal(filePath.toFile());
            return logList.getLogs();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if loading fails
        }
    }

    // Save logs to XML
    public static void saveLogsToXML(List<TrafficCitationLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TrafficCitationReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            TrafficCitationReportLogs logList = new TrafficCitationReportLogs();
            logList.setLogs(logs);

            // Use try-with-resources to ensure FileOutputStream is closed properly
            try (FileOutputStream fos = new FileOutputStream(stringUtil.trafficCitationLogURL)) {
                marshaller.marshal(logList, fos);
            }
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<TrafficCitationLogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<TrafficCitationLogEntry> logs) {
        this.logs = logs;
    }

}