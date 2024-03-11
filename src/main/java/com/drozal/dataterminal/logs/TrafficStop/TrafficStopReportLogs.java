package com.drozal.dataterminal.logs.TrafficStop;

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
public class TrafficStopReportLogs {
    private List<TrafficStopLogEntry> logs;

    public TrafficStopReportLogs() {
    }

    public static List<TrafficStopLogEntry> extractLogEntries(String filePath) {
        List<TrafficStopLogEntry> logEntries = new ArrayList<>();

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
                    TrafficStopLogEntry logEntry = new TrafficStopLogEntry();
                    logEntry.StopNumber = getTagValue(logsElement, "StopNumber");
                    logEntry.Date = getTagValue(logsElement, "Date");
                    logEntry.Time = getTagValue(logsElement, "Time");
                    logEntry.PlateNumber = getTagValue(logsElement, "PlateNumber");
                    logEntry.Color = getTagValue(logsElement, "Color");
                    logEntry.Type = getTagValue(logsElement, "Type");
                    logEntry.ResponseModel = getTagValue(logsElement, "ResponseModel");
                    logEntry.ResponseMake = getTagValue(logsElement, "ResponseMake");
                    logEntry.ResponseOtherInfo = getTagValue(logsElement, "ResponseOtherInfo");
                    logEntry.operatorName = getTagValue(logsElement, "operatorName");
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
                    logEntry.ViolationsTextArea = getTagValue(logsElement, "ViolationsTextArea");
                    logEntry.ActionsTextArea = getTagValue(logsElement, "ActionsTextArea");
                    logEntry.CommentsTextArea = getTagValue(logsElement, "CommentsTextArea");

                    logEntries.add(logEntry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logEntries;
    }

    public static void addLogEntryToGrid(GridPane gridPane, TrafficStopLogEntry logEntry, int rowIndex) {
        // Create labels for each log entry field
        Label calloutNumberLabel = createLabel(logEntry.StopNumber);
        Label dateLabel = createLabel(logEntry.Date);
        Label timeLabel = createLabel(logEntry.Time);
        Label licensePlateNumber = createLabel(logEntry.PlateNumber);
        Label carColor = createLabel(logEntry.Color);
        Label carType = createLabel(logEntry.Type);
        Label rankLabel = createLabel(logEntry.Rank);
        Label nameLabel = createLabel(logEntry.Name);
        Label divisionLabel = createLabel(logEntry.Division);
        Label agencyLabel = createLabel(logEntry.Agency);
        Label numberLabel = createLabel(logEntry.Number);
        Label streetLabel = createLabel(logEntry.Street);
        Label countyLabel = createLabel(logEntry.County);
        Label areaLabel = createLabel(logEntry.Area);
        Label violations = createLabel(logEntry.ViolationsTextArea);
        Label actions = createLabel(logEntry.ActionsTextArea);
        Label comments = createLabel(logEntry.CommentsTextArea);


        // Add labels to the GridPane
        gridPane.addRow(rowIndex, calloutNumberLabel, violations, actions, carColor, comments, licensePlateNumber, carType, timeLabel, dateLabel,
                divisionLabel, agencyLabel, numberLabel, rankLabel, nameLabel, streetLabel, countyLabel, areaLabel);


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

    public static List<TrafficStopLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get(stringUtil.trafficstopLogURL);
            if (!Files.exists(filePath)) {
                return new ArrayList<>(); // Return an empty list if file doesn't exist
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(TrafficStopReportLogs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            TrafficStopReportLogs logList = (TrafficStopReportLogs) unmarshaller.unmarshal(filePath.toFile());
            return logList.getLogs();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if loading fails
        }
    }

    // Save logs to XML
    public static void saveLogsToXML(List<TrafficStopLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TrafficStopReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            TrafficStopReportLogs logList = new TrafficStopReportLogs();
            logList.setLogs(logs);
            marshaller.marshal(logList, new FileOutputStream(stringUtil.trafficstopLogURL));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<TrafficStopLogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<TrafficStopLogEntry> logs) {
        this.logs = logs;
    }

}