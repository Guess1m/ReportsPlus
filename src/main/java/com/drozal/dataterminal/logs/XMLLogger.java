package com.drozal.dataterminal.logs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class XMLLogger {

    public static void main(String[] args) {
        // Sample data
        List<LogEntry> logs = new ArrayList<>();
        logs.add(new LogEntry("Info", "Application started"));
        logs.add(new LogEntry("Warning", "Memory usage high"));

        List<LogEntry> log2 = new ArrayList<>();
        log2.add(new LogEntry("Info", "Log2 started"));
        log2.add(new LogEntry("Error", "Log2 encountered an error"));

        // Serialize to XML
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CalloutReportLogs.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            CalloutReportLogs logList = new CalloutReportLogs();
            logList.setLogs(logs);
            logList.setLog2(log2); // Set the new log section
            marshaller.marshal(logList, new FileOutputStream("logs.xml"));
            System.out.println("CalloutReportLogs saved to logs.xml");
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}



