package com.drozal.dataterminal.logs.Impound;

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
public class ImpoundReportLogs {
    private List<ImpoundLogEntry> logs;

    public ImpoundReportLogs() {
    }

    public static List<ImpoundLogEntry> extractLogEntries(String filePath) {
        List<ImpoundLogEntry> logEntries = new ArrayList<>();

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
                    ImpoundLogEntry logEntry = new ImpoundLogEntry();
                    logEntry.impoundNumber = getTagValue(logsElement, "impoundNumber");
                    logEntry.impoundDate = getTagValue(logsElement, "impoundDate");
                    logEntry.impoundTime = getTagValue(logsElement, "impoundTime");
                    logEntry.ownerName = getTagValue(logsElement, "ownerName");
                    logEntry.ownerAge = getTagValue(logsElement, "ownerAge");
                    logEntry.ownerGender = getTagValue(logsElement, "ownerGender");
                    logEntry.ownerAddress = getTagValue(logsElement, "ownerAddress");
                    logEntry.impoundPlateNumber = getTagValue(logsElement, "impoundPlateNumber");
                    logEntry.impoundMake = getTagValue(logsElement, "impoundMake");
                    logEntry.impoundModel = getTagValue(logsElement, "impoundModel");
                    logEntry.impoundType = getTagValue(logsElement, "impoundType");
                    logEntry.impoundColor = getTagValue(logsElement, "impoundColor");
                    logEntry.impoundComments = getTagValue(logsElement, "impoundComments");
                    logEntry.officerRank = getTagValue(logsElement, "officerRank");
                    logEntry.officerName = getTagValue(logsElement, "officerName");
                    logEntry.officerNumber = getTagValue(logsElement, "officerNumber");
                    logEntry.officerDivision = getTagValue(logsElement, "officerDivision");
                    logEntry.officerAgency = getTagValue(logsElement, "officerAgency");
                    logEntries.add(logEntry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logEntries;
    }

    public static void addLogEntryToGrid(GridPane gridPane, ImpoundLogEntry logEntry, int rowIndex) {
        // Create labels for each log entry field
        Label impoundNumberLabel = createLabel(logEntry.impoundNumber);
        Label impoundDateLabel = createLabel(logEntry.impoundDate);
        Label impoundTimeLabel = createLabel(logEntry.impoundTime);
        Label ownerNameLabel = createLabel(logEntry.ownerName);
        Label ownerAgeLabel = createLabel(logEntry.ownerAge);
        Label ownerGenderLabel = createLabel(logEntry.ownerGender);
        Label ownerAddressLabel = createLabel(logEntry.ownerAddress);
        Label impoundPlateNumberLabel = createLabel(logEntry.impoundPlateNumber);
        Label impoundMakeLabel = createLabel(logEntry.impoundMake);
        Label impoundModelLabel = createLabel(logEntry.impoundModel);
        Label impoundTypeLabel = createLabel(logEntry.impoundType);
        Label impoundColorLabel = createLabel(logEntry.impoundColor);
        Label impoundCommentsLabel = createLabel(logEntry.impoundComments);
        Label officerRankLabel = createLabel(logEntry.officerRank);
        Label officerNameLabel = createLabel(logEntry.officerName);
        Label officerNumberLabel = createLabel(logEntry.officerNumber);
        Label officerDivisionLabel = createLabel(logEntry.officerDivision);
        Label officerAgencyLabel = createLabel(logEntry.officerAgency);

// Add labels to the GridPane
        gridPane.addRow(rowIndex, impoundNumberLabel, impoundDateLabel, impoundTimeLabel, ownerNameLabel, ownerAgeLabel, ownerGenderLabel,
                ownerAddressLabel, impoundPlateNumberLabel, impoundMakeLabel, impoundModelLabel, impoundTypeLabel, impoundColorLabel,
                impoundCommentsLabel, officerRankLabel, officerNameLabel, officerNumberLabel, officerDivisionLabel, officerAgencyLabel);


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

    public static List<ImpoundLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get(stringUtil.impoundLogURL);
            if (!Files.exists(filePath)) {
                return new ArrayList<>(); // Return an empty list if file doesn't exist
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(ImpoundReportLogs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ImpoundReportLogs logList = (ImpoundReportLogs) unmarshaller.unmarshal(filePath.toFile());
            return logList.getLogs();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list if loading fails
        }
    }

    // Save logs to XML
    public static void saveLogsToXML(List<ImpoundLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ImpoundReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ImpoundReportLogs logList = new ImpoundReportLogs();
            logList.setLogs(logs);
            marshaller.marshal(logList, new FileOutputStream(stringUtil.impoundLogURL));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<ImpoundLogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<ImpoundLogEntry> logs) {
        this.logs = logs;
    }

}