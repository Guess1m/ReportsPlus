package com.Guess.ReportsPlus.util.Other;

public class NoteTab {
    private String tabName;
    private String noteText;

    public NoteTab(String tabName, String noteText) {
        this.tabName = tabName;
        this.noteText = noteText;
    }

    public NoteTab(String tabName) {
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}