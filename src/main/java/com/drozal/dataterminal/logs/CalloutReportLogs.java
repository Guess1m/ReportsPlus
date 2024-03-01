package com.drozal.dataterminal.logs;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;

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

    // Load existing logs from XML
    public List<CalloutLogEntry> loadLogsFromXML() {
        try {
            Path filePath = Paths.get("DataLogs/logs.xml");
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
    public void saveLogsToXML(List<CalloutLogEntry> logs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CalloutReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            CalloutReportLogs logList = new CalloutReportLogs();
            logList.setLogs(logs);
            marshaller.marshal(logList, new FileOutputStream("DataLogs/logs.xml"));
            System.out.println("CalloutReportLogs appended to DataLogs/logs.xml");
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}