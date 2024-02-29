package com.drozal.dataterminal.logs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class XMLLogger {

    public static void main(String[] args) throws JAXBException, FileNotFoundException {
        // Sample data
        List<CalloutLogEntry> logs = new ArrayList<>();
        //logs.add(new CalloutLogEntry());

        List<CalloutLogEntry> log2 = new ArrayList<>();
        //log2.add(new CalloutLogEntry("Info", "Log2 started"));
        //log2.add(new CalloutLogEntry("Error", "Log2 encountered an error"));

        // Serialize to XML
        JAXBContext jaxbContext = JAXBContext.newInstance(CalloutReportLogs.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        CalloutReportLogs logList = new CalloutReportLogs();
        logList.setLogs(logs);
        marshaller.marshal(logList, new FileOutputStream("logs.xml"));
        System.out.println("CalloutReportLogs saved to logs.xml");
    }
}



