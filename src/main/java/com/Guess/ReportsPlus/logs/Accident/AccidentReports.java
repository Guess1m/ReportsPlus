package com.Guess.ReportsPlus.logs.Accident;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "accidentReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccidentReports {

    @XmlElement(name = "logs")
    private List<AccidentReport> AccidentReportList;

    public List<AccidentReport> getAccidentReportList() {
        return AccidentReportList;
    }

    public void setAccidentReportList(List<AccidentReport> AccidentReportList) {
        this.AccidentReportList = AccidentReportList;
    }
}
