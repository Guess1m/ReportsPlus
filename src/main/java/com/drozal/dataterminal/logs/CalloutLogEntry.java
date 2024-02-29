package com.drozal.dataterminal.logs;

import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

// Data model for a log entry
public class CalloutLogEntry {
    public String CalloutNumber;
    public String NotesTextArea;
    public String ResponseGrade;
    public String ResponeType;
    public String Time;
    public String Date;
    public String Division;
    public String Agency;
    public String Number;
    public String Rank;
    public String Name;

    public CalloutLogEntry(){

    }

    public CalloutLogEntry(String date, String time, String name, String rank, String number, String division, String agency, String responseType, String responseGrade, String cNumber, String notes) {
        this.Date=date;
        this.Time=time;
        this.Name=name;
        this.Rank=rank;
        this.Number=number;
        this.Division=division;
        this.Agency=agency;
        this.ResponeType=responseType;
        this.ResponseGrade=responseGrade;
        this.CalloutNumber=cNumber;
        this.NotesTextArea=notes;
    }
}

