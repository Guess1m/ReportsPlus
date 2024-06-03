package com.drozal.dataterminal.logs.Patrol;

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
 * The type Patrol report logs.
 */
@XmlRootElement
public class PatrolReportLogs {
    private List<PatrolLogEntry> logs;

    /**
     * Instantiates a new Patrol report logs.
     */
    public PatrolReportLogs() {
    }

    /**
     * Extract log entries list.
     *
     * @param filePath the file path
     * @return the list
     */
    public static List<PatrolLogEntry> extractLogEntries(String filePath) {
        List<PatrolLogEntry> logEntries = new ArrayList<>();

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                return logEntries;
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
            return nodeList.item(0).getTextContent();
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

            List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();

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
    public static List<PatrolLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get(stringUtil.patrolLogURL);
            if (!Files.exists(filePath)) {
                return new ArrayList<>();
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(PatrolReportLogs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            PatrolReportLogs logList = (PatrolReportLogs) unmarshaller.unmarshal(filePath.toFile());
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
    public static void saveLogsToXML(List<PatrolLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PatrolReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            PatrolReportLogs logList = new PatrolReportLogs();
            logList.setLogs(logs);

            try (FileOutputStream fos = new FileOutputStream(stringUtil.patrolLogURL)) {
                marshaller.marshal(logList, fos);
            }
        } catch (JAXBException | IOException e) {
            logError("Exception", e);
        }
    }

    /**
     * Gets logs.
     *
     * @return the logs
     */
    public List<PatrolLogEntry> getLogs() {
        return logs;
    }

    /**
     * Sets logs.
     *
     * @param logs the logs
     */
    public void setLogs(List<PatrolLogEntry> logs) {
        this.logs = logs;
    }

}