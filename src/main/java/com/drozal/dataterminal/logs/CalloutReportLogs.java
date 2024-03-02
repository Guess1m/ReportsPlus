package com.drozal.dataterminal.logs;

import jakarta.xml.bind.annotation.XmlRootElement;

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

}