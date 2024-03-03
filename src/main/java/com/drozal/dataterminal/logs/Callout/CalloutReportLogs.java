package com.drozal.dataterminal.logs.Callout;

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
public class CalloutReportLogs {
    private List<CalloutLogEntry> logs;

    public CalloutReportLogs() {
    }

    public List<CalloutLogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<CalloutLogEntry> logs) {
        this.logs = logs;
    }

    public static List<CalloutLogEntry> extractLogEntries(String filePath) {
        List<CalloutLogEntry> logEntries = new ArrayList<>();

        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList logsList = doc.getElementsByTagName("logs");

            for (int i = 0; i < logsList.getLength(); i++) {
                Node logsNode = logsList.item(i);
                if (logsNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element logsElement = (Element) logsNode;
                    CalloutLogEntry logEntry = new CalloutLogEntry();
                    logEntry.CalloutNumber = getTagValue(logsElement, "CalloutNumber");
                    logEntry.Date = getTagValue(logsElement, "Date");
                    logEntry.Time = getTagValue(logsElement, "Time");
                    logEntry.ResponeType = getTagValue(logsElement, "ResponeType");
                    logEntry.ResponseGrade = getTagValue(logsElement, "ResponseGrade");
                    logEntry.Address = getTagValue(logsElement, "Address");
                    logEntry.County = getTagValue(logsElement, "County");
                    logEntry.Area = getTagValue(logsElement, "Area");
                    logEntry.Rank = getTagValue(logsElement, "Rank");
                    logEntry.Name = getTagValue(logsElement, "Name");
                    logEntry.Number = getTagValue(logsElement, "Number");
                    logEntry.Agency = getTagValue(logsElement, "Agency");
                    logEntry.Division = getTagValue(logsElement, "Division");
                    logEntry.NotesTextArea = getTagValue(logsElement, "NotesTextArea");
                    logEntries.add(logEntry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logEntries;
    }

    public static void addLogEntryToGrid(GridPane gridPane, CalloutLogEntry logEntry, int rowIndex) {
        // Create labels for each log entry field
        Label calloutNumberLabel = createLabel(logEntry.CalloutNumber);
        Label notesLabel = createLabel(logEntry.NotesTextArea);
        Label responseGradeLabel = createLabel(logEntry.ResponseGrade);
        Label responseTypeLabel = createLabel(logEntry.ResponeType);
        Label timeLabel = createLabel(logEntry.Time);
        Label dateLabel = createLabel(logEntry.Date);
        Label divisionLabel = createLabel(logEntry.Division);
        Label agencyLabel = createLabel(logEntry.Agency);
        Label numberLabel = createLabel(logEntry.Number);
        Label rankLabel = createLabel(logEntry.Rank);
        Label nameLabel = createLabel(logEntry.Name);
        Label addressLabel = createLabel(logEntry.Address);
        Label countyLabel = createLabel(logEntry.County);
        Label areaLabel = createLabel(logEntry.Area);

        // Add labels to the GridPane
        gridPane.addRow(rowIndex, calloutNumberLabel, notesLabel, responseGradeLabel, responseTypeLabel, timeLabel, dateLabel,
                divisionLabel, agencyLabel, numberLabel, rankLabel, nameLabel, addressLabel, countyLabel, areaLabel);


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

    public static List<CalloutLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get(stringUtil.calloutLogURL);
            if (!Files.exists(filePath)) {
                return new ArrayList<>(); // Return an empty list if file doesn't exist
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(CalloutReportLogs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CalloutReportLogs logList = (CalloutReportLogs) unmarshaller.unmarshal(filePath.toFile());
            return logList.getLogs();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if loading fails
        }
    }

    // Save logs to XML
    public static void saveLogsToXML(List<CalloutLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CalloutReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            CalloutReportLogs logList = new CalloutReportLogs();
            logList.setLogs(logs);
            marshaller.marshal(logList, new FileOutputStream(stringUtil.calloutLogURL));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}