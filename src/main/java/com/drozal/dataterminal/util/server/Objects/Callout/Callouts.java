package com.drozal.dataterminal.util.server.Objects.Callout;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * The type Callouts.
 */
@XmlRootElement(name = "Callouts")
@XmlAccessorType(XmlAccessType.FIELD)
public class Callouts {

    @XmlElement(name = "Callout")
    private List<Callout> calloutList;

    /**
     * Gets callout list.
     *
     * @return the callout list
     */
    public List<Callout> getCalloutList() {
        return calloutList;
    }

    /**
     * Sets callout list.
     *
     * @param calloutList the callout list
     */
    public void setCalloutList(List<Callout> calloutList) {
        this.calloutList = calloutList;
    }
}
