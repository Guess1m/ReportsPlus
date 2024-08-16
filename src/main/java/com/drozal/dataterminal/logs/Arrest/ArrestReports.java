package com.drozal.dataterminal.logs.Arrest;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "arrestReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArrestReports {

    @XmlElement(name = "logs")
    private List<ArrestReport> ArrestReportList;

    public List<ArrestReport> getArrestReportList() {
        return ArrestReportList;
    }

    public void setArrestReportList(List<ArrestReport> ArrestReportList) {
        this.ArrestReportList = ArrestReportList;
    }
}
