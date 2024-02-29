package com.drozal.dataterminal.logs;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

// Data model for a collection of log entries
@XmlRootElement
class CalloutReportLogs {
    private List<LogEntry> logs;
    private List<LogEntry> log2; // New section

    public CalloutReportLogs() {}

    public List<LogEntry> getLogs() {
        return logs;
    }

    public void setLogs(List<LogEntry> logs) {
        this.logs = logs;
    }

    public List<LogEntry> getLog2() {
        return log2;
    }

    public void setLog2(List<LogEntry> log2) {
        this.log2 = log2;
    }
}