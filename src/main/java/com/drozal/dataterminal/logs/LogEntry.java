package com.drozal.dataterminal.logs;

// Data model for a log entry
class LogEntry {
    private String level;
    private String message;

    public LogEntry() {}

    public LogEntry(String level, String message) {
        this.level = level;
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}