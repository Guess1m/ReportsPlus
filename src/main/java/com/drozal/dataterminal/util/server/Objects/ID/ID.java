package com.drozal.dataterminal.util.server.Objects.ID;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * The type Id.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ID {
	
	@XmlElement(name = "Name")
	private String name;
	
	@XmlElement(name = "Birthday")
	private String birthday;
	
	@XmlElement(name = "Gender")
	private String gender;
	
	@XmlElement(name = "Address")
	private String address;
	
	@XmlElement(name = "Index")
	private int index;
	
	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		if (name != null && !name.isEmpty()) {
			String[] parts = name.split(" ");
			return parts[0];
		}
		return "";
	}
	
	/**
	 * Gets last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		if (name != null && !name.isEmpty()) {
			String[] parts = name.split(" ");
			if (parts.length > 1) {
				return parts[parts.length - 1];
			}
		}
		return "";
	}
	
	/**
	 * Gets birthday.
	 *
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}
	
	/**
	 * Sets birthday.
	 *
	 * @param birthday the birthday
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	/**
	 * Gets gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	
	/**
	 * Sets gender.
	 *
	 * @param gender the gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	/**
	 * Gets index.
	 *
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Sets index.
	 *
	 * @param index the index
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * Gets address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets address.
	 *
	 * @param address the address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
}
