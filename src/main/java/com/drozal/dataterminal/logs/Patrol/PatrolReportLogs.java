package com.drozal.dataterminal.logs.Patrol;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class PatrolReportLogs {
    private List<PatrolLogEntry> logs;

    public PatrolReportLogs() {
    }

    public static List<PatrolLogEntry> extractLogEntries(String filePath) {
        List<PatrolLogEntry> logEntries = new ArrayList<>();

        try {
            File file = new File(filePath);

            // Check if the file exists
            if (!file.exists()) {
                System.err.println("File not found: " + filePath);
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
                    PatrolLogEntry logEntry = new PatrolLogEntry();
                    logEntry.patrolNumber = getTagValue(logsElement, "patrolNumber");
                    logEntry.patrolDate = getTagValue(logsElement, "patrolDate");
                    logEntry.patrolLength = getTagValue(logsElement, "patrolLength");
                    logEntry.patrolStartTime = getTagValue(logsElement, "patrolStartTime");
                    logEntry.patrolStopTime = getTagValue(logsElement, "patrolStopTime");
                    logEntry.officerRank = getTagValue(logsElement, "officerRank");
                    logEntry.officerName = getTagValue(logsElement, "officerName");
                    logEntry.officerNumber = getTagValue(logsElement, "officerNumber");
                    logEntry.officerDivision = getTagValue(logsElement, "officerDivision");
                    logEntry.officerAgency = getTagValue(logsElement, "officerAgency");
                    logEntry.officerVehicle = getTagValue(logsElement, "officerVehicle");
                    logEntry.patrolComments = getTagValue(logsElement, "patrolComments");
                    logEntries.add(logEntry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logEntries;
    }

    public static void addLogEntryToGrid(GridPane gridPane, PatrolLogEntry logEntry, int rowIndex) {
        // Create labels for each log entry field
        Label patrolNumberLabel = createLabel(logEntry.patrolNumber);
        Label patrolDateLabel = createLabel(logEntry.patrolDate);
        Label patrolTimeLabel = createLabel(logEntry.patrolLength);
        Label patrolStartTimeLabel = createLabel(logEntry.patrolStartTime);
        Label patrolStopTimeLabel = createLabel(logEntry.patrolStopTime);
        Label officerRankLabel = createLabel(logEntry.officerRank);
        Label officerNameLabel = createLabel(logEntry.officerName);
        Label officerNumberLabel = createLabel(logEntry.officerNumber);
        Label officerDivisionLabel = createLabel(logEntry.officerDivision);
        Label officerAgencyLabel = createLabel(logEntry.officerAgency);
        Label officerVehicleLabel = createLabel(logEntry.officerVehicle);
        Label patrolCommentsLabel = createLabel(logEntry.patrolComments);

// Add labels to the GridPane
        gridPane.addRow(rowIndex, patrolNumberLabel, patrolDateLabel, patrolTimeLabel, patrolStartTimeLabel, patrolStopTimeLabel,
                officerRankLabel, officerNameLabel, officerNumberLabel, officerAgencyLabel, officerDivisionLabel, officerVehicleLabel, patrolCommentsLabel);

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

    public static List<PatrolLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get(stringUtil.patrolLogURL);
            if (!Files.exists(filePath)) {
                return new ArrayList<>(); // Return an empty list if file doesn't exist
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(PatrolReportLogs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            PatrolReportLogs logList = (PatrolReportLogs) unmarshaller.unmarshal(filePath.toFile());
            return logList.getLogs();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if loading fails
        }
    }

    // Save logs to XML
    public static void saveLogsToXML(List<PatrolLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PatrolReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            PatrolReportLogs logList = new PatrolReportLogs();
            logList.setLogs(logs);
            marshaller.marshal(logList, new FileOutputStream(stringUtil.patrolLogURL));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<PatrolLogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<PatrolLogEntry> logs) {
        this.logs = logs;
    }

}