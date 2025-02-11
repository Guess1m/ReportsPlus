package com.Guess.ReportsPlus.logs.Death;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "DeathReports")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeathReports {

    @XmlElement(name = "DeathReport")
    private List<DeathReport> deathReportList;

    public List<DeathReport> getDeathReportList() {
        return deathReportList;
    }

    public void setDeathReportList(List<DeathReport> deathReportList) {
        this.deathReportList = deathReportList;
    }
}
