package com.drozal.dataterminal.util.server.Objects.Callout;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The type Callout.
 */
@XmlRootElement(name = "Callout")
@XmlAccessorType(XmlAccessType.FIELD)
public class Callout {

    @XmlElement(name = "Number")
    private String number;

    @XmlElement(name = "Type")
    private String type;

    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "Priority")
    private String priority;

    @XmlElement(name = "Street")
    private String street;

    @XmlElement(name = "Area")
    private String area;

    @XmlElement(name = "County")
    private String county;

    @XmlElement(name = "StartTime")
    private String startTime;

    @XmlElement(name = "StartDate")
    private String startDate;

    /**
     * Gets number.
     *
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets number.
     *
     * @param number the number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets priority.
     *
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets priority.
     *
     * @param priority the priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Gets street.
     *
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets street.
     *
     * @param street the street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Gets area.
     *
     * @return the area
     */
    public String getArea() {
        return area;
    }

    /**
     * Sets area.
     *
     * @param area the area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * Gets county.
     *
     * @return the county
     */
    public String getCounty() {
        return county;
    }

    /**
     * Sets county.
     *
     * @param county the county
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets start date.
     *
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
