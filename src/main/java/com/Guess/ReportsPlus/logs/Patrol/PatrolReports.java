package com.Guess.ReportsPlus.logs.Patrol;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "patrolReportLogs")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatrolReports {

    @XmlElement(name = "logs")
    private List<PatrolReport> patrolReportList;

    public List<PatrolReport> getPatrolReportList() {
        return patrolReportList;
    }

    public void setPatrolReportList(List<PatrolReport> patrolReportList) {
        this.patrolReportList = patrolReportList;
    }
}
