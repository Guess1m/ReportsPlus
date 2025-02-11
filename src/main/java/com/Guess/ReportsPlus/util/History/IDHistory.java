package com.Guess.ReportsPlus.util.History;

import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Server.Objects.ID.ID;
import com.Guess.ReportsPlus.util.Server.Objects.ID.IDs;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.IDHistoryURL;

public class IDHistory {

    public static IDs loadHistoryIDs() throws JAXBException {
        File file = new File(IDHistoryURL);
        if (!file.exists()) {
            return new IDs();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(IDs.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (IDs) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            logError("Error loading IDs: ", e);
            throw e;
        }
    }

    private static void saveHistoryIDs(IDs IDs) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(IDs.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        File file = new File(IDHistoryURL);
        marshaller.marshal(IDs, file);
    }

    public static boolean searchIDHisForName(String name) {
        IDs IDs = null;
        try {
            IDs = loadHistoryIDs();
        } catch (JAXBException e) {
            logError("Error loading IDs from searchIDHisForName: ", e);
        }

        if (IDs.getIdList() == null) {
            IDs.setIdList(new java.util.ArrayList<>());
        }

        Optional<ID> existingReport = IDs.getIdList().stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst();
        return existingReport.isPresent();
    }

    public static ID getHistoryIDFromName(String name) {
        IDs IDs = null;
        try {
            IDs = loadHistoryIDs();
        } catch (JAXBException e) {
            logError("Error loading IDs from searchIDHisForName: ", e);
        }

        if (IDs.getIdList() == null) {
            IDs.setIdList(new java.util.ArrayList<>());
        }

        Optional<ID> existingReport = IDs.getIdList().stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst();
        return existingReport.orElse(null);
    }

    public static void addHistoryID(ID ID) throws JAXBException {
        IDs IDs = loadHistoryIDs();

        if (IDs.getIdList() == null) {
            IDs.setIdList(new java.util.ArrayList<>());
        }

        Optional<ID> existingReport = IDs.getIdList().stream().filter(e -> e.getName().equals(ID.getName())).findFirst();

        if (existingReport.isPresent()) {
            IDs.getIdList().remove(existingReport.get());
            IDs.getIdList().add(ID);
            log("HistoryID with name " + ID.getName() + " updated.", LogUtils.Severity.INFO);
        } else {
            IDs.getIdList().add(ID);
            log("HistoryID with name " + ID.getName() + " added.", LogUtils.Severity.INFO);
        }

        saveHistoryIDs(IDs);
    }

    public static void addServerIDToHistoryIfNotExists(ID serverID) throws JAXBException {
        IDs historyIDs = loadHistoryIDs();

        if (historyIDs.getIdList() == null) {
            historyIDs.setIdList(new ArrayList<>());
        }
        Optional<ID> existingID = historyIDs.getIdList().stream().filter(id -> id.getName().equals(serverID.getName())).findFirst();

        if (existingID.isEmpty()) {
            if (historyIDs.getIdList() == null) {
                historyIDs.setIdList(new java.util.ArrayList<>());
            }
            historyIDs.getIdList().add(serverID);
            saveHistoryIDs(historyIDs);
            log("ServerID with name " + serverID.getName() + " added to history.", LogUtils.Severity.INFO);
        } else {
            log("ServerID with name " + serverID.getName() + " already exists in history.", LogUtils.Severity.INFO);
        }
    }

    public static boolean checkAllHistoryIDsClosed() throws JAXBException {
        List<ID> historyIDs = IDHistory.loadHistoryIDs().getIdList();
        if (historyIDs == null || historyIDs.isEmpty()) {
            log("No history IDs found", LogUtils.Severity.INFO);
            return false;
        }

        boolean allClosed = historyIDs.stream().allMatch(id -> "closed".equalsIgnoreCase(id.getStatus()));

        if (allClosed) {
            log("All history IDs are closed", LogUtils.Severity.INFO);
        } else {
            log("Not all history IDs are closed", LogUtils.Severity.INFO);
        }

        return allClosed;
    }

}
