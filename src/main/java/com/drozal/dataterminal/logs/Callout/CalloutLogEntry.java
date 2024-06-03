package com.drozal.dataterminal.logs.Callout;

import java.util.Arrays;
import java.util.List;

/**
 * The type Callout log entry.
 */
public class CalloutLogEntry {
	/**
	 * The Callout number.
	 */
	public String CalloutNumber;
	/**
	 * The Notes text area.
	 */
	public String NotesTextArea;
	/**
	 * The Response grade.
	 */
	public String ResponseGrade;
	/**
	 * The Respone type.
	 */
	public String ResponeType;
	/**
	 * The Time.
	 */
	public String Time;
	/**
	 * The Date.
	 */
	public String Date;
	/**
	 * The Division.
	 */
	public String Division;
	/**
	 * The Agency.
	 */
	public String Agency;
	/**
	 * The Number.
	 */
	public String Number;
	/**
	 * The Rank.
	 */
	public String Rank;
	/**
	 * The Name.
	 */
	public String Name;
	/**
	 * The Address.
	 */
	public String Address;
	/**
	 * The County.
	 */
	public String County;
	/**
	 * The Area.
	 */
	public String Area;
	
	/**
	 * Instantiates a new Callout log entry.
	 */
	public CalloutLogEntry() {
	
	}
	
	/**
	 * Instantiates a new Callout log entry.
	 *
	 * @param date          the date
	 * @param time          the time
	 * @param name          the name
	 * @param rank          the rank
	 * @param number        the number
	 * @param division      the division
	 * @param agency        the agency
	 * @param responseType  the response type
	 * @param responseGrade the response grade
	 * @param cNumber       the c number
	 * @param notes         the notes
	 * @param address       the address
	 * @param county        the county
	 * @param area          the area
	 */
	public CalloutLogEntry(String date, String time, String name, String rank, String number, String division, String agency, String responseType, String responseGrade, String cNumber, String notes, String address, String county, String area) {
		this.Date = date;
		this.Time = time;
		this.Name = name;
		this.Rank = rank;
		this.Number = number;
		this.Division = division;
		this.Agency = agency;
		this.ResponeType = responseType;
		this.ResponseGrade = responseGrade;
		this.CalloutNumber = cNumber;
		this.NotesTextArea = notes;
		this.Address = address;
		this.County = county;
		this.Area = area;
	}
	
	/**
	 * Gets all values.
	 *
	 * @return the all values
	 */
	public List<String> getAllValues() {
		return Arrays.asList(getDate(), getTime(), getName(), getRank(), getNumber(), getDivision(), getAgency(), getResponeType(), getResponseGrade(), getCalloutNumber(), getNotesTextArea(), getAddress(), getCounty(), getArea());
	}
	
	/**
	 * Gets callout number.
	 *
	 * @return the callout number
	 */
	public String getCalloutNumber() {
		return CalloutNumber;
	}
	
	/**
	 * Gets notes text area.
	 *
	 * @return the notes text area
	 */
	public String getNotesTextArea() {
		return NotesTextArea;
	}
	
	/**
	 * Gets response grade.
	 *
	 * @return the response grade
	 */
	public String getResponseGrade() {
		return ResponseGrade;
	}
	
	/**
	 * Gets respone type.
	 *
	 * @return the respone type
	 */
	public String getResponeType() {
		return ResponeType;
	}
	
	/**
	 * Gets time.
	 *
	 * @return the time
	 */
	public String getTime() {
		return Time;
	}
	
	/**
	 * Gets date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return Date;
	}
	
	/**
	 * Gets division.
	 *
	 * @return the division
	 */
	public String getDivision() {
		return Division;
	}
	
	/**
	 * Gets agency.
	 *
	 * @return the agency
	 */
	public String getAgency() {
		return Agency;
	}
	
	/**
	 * Gets number.
	 *
	 * @return the number
	 */
	public String getNumber() {
		return Number;
	}
	
	/**
	 * Gets rank.
	 *
	 * @return the rank
	 */
	public String getRank() {
		return Rank;
	}
	
	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	
	/**
	 * Gets address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return Address;
	}
	
	/**
	 * Gets county.
	 *
	 * @return the county
	 */
	public String getCounty() {
		return County;
	}
	
	/**
	 * Gets area.
	 *
	 * @return the area
	 */
	public String getArea() {
		return Area;
	}
}

