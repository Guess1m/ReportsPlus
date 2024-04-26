package com.drozal.dataterminal.util;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ID {

    @XmlElement(name = "Name")
    private String name; // This will represent both first and last names

    @XmlElement(name = "Birthday")
    private String birthday;

    @XmlElement(name = "Gender")
    private String gender;

    @XmlElement(name = "Index")
    private int index;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Split the name into first and last names
    public String getFirstName() {
        if (name != null && !name.isEmpty()) {
            String[] parts = name.split(" ");
            return parts[0]; // First name
        }
        return "";
    }

    public String getLastName() {
        if (name != null && !name.isEmpty()) {
            String[] parts = name.split(" ");
            if (parts.length > 1) {
                return parts[parts.length - 1]; // Last name
            }
        }
        return "";
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
