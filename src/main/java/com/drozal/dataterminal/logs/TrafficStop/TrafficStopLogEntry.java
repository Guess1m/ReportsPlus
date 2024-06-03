package com.drozal.dataterminal.logs.TrafficStop;

/**
 * The type Traffic stop log entry.
 */
public class TrafficStopLogEntry {
	/**
	 * The Plate number.
	 */
	public String PlateNumber;
	/**
	 * The Color.
	 */
	public String Color;
	/**
	 * The Type.
	 */
	public String Type;
	/**
	 * The Stop number.
	 */
	public String StopNumber;
	/**
	 * The Response model.
	 */
	public String ResponseModel;
	/**
	 * The Response other info.
	 */
	public String ResponseOtherInfo;
	/**
	 * The Comments text area.
	 */
	public String CommentsTextArea;
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
	 * The County.
	 */
	public String County;
	/**
	 * The Area.
	 */
	public String Area;
	
	/**
	 * The Street.
	 */
	public String Street;
	
	/**
	 * The Operator name.
	 */
	public String operatorName;
	/**
	 * The Operator age.
	 */
	public String operatorAge;
	/**
	 * The Operator description.
	 */
	public String operatorDescription;
	/**
	 * The Operator address.
	 */
	public String operatorAddress;
	/**
	 * The Operator gender.
	 */
	public String operatorGender;
	
	/**
	 * Instantiates a new Traffic stop log entry.
	 */
	public TrafficStopLogEntry() {
	
	}
	
	/**
	 * Instantiates a new Traffic stop log entry.
	 *
	 * @param date         the date
	 * @param time         the time
	 * @param model        the model
	 * @param otherInfo    the other info
	 * @param oName        the o name
	 * @param oAge         the o age
	 * @param oAddress     the o address
	 * @param oDescription the o description
	 * @param oGender      the o gender
	 * @param name         the name
	 * @param rank         the rank
	 * @param number       the number
	 * @param division     the division
	 * @param agency       the agency
	 * @param sNumber      the s number
	 * @param comments     the comments
	 * @param street       the street
	 * @param county       the county
	 * @param area         the area
	 * @param plateN       the plate n
	 * @param color        the color
	 * @param type         the type
	 */
	public TrafficStopLogEntry(String date, String time, String model, String otherInfo, String oName, String oAge, String oAddress, String oDescription, String oGender, String name, String rank, String number, String division, String agency, String sNumber, String comments, String street, String county, String area, String plateN, String color, String type) {
		this.Date = date;
		this.Time = time;
		this.ResponseModel = model;
		this.ResponseOtherInfo = otherInfo;
		this.operatorName = oName;
		this.operatorAge = oAge;
		this.operatorAddress = oAddress;
		this.operatorDescription = oDescription;
		this.operatorGender = oGender;
		this.Name = name;
		this.Rank = rank;
		this.Number = number;
		this.Division = division;
		this.Agency = agency;
		this.StopNumber = sNumber;
		this.CommentsTextArea = comments;
		this.Street = street;
		this.County = county;
		this.Area = area;
		this.PlateNumber = plateN;
		this.Color = color;
		this.Type = type;
	}
	
	/**
	 * Gets operator name.
	 *
	 * @return the operator name
	 */
	public String getOperatorName() {
		return operatorName;
	}
	
	/**
	 * Gets operator description.
	 *
	 * @return the operator description
	 */
	public String getOperatorDescription() {
		return operatorDescription;
	}
	
	/**
	 * Gets operator address.
	 *
	 * @return the operator address
	 */
	public String getOperatorAddress() {
		return operatorAddress;
	}
	
	/**
	 * Gets operator gender.
	 *
	 * @return the operator gender
	 */
	public String getOperatorGender() {
		return operatorGender;
	}
	
	/**
	 * Gets plate number.
	 *
	 * @return the plate number
	 */
	public String getPlateNumber() {
		return PlateNumber;
	}
	
	/**
	 * Gets color.
	 *
	 * @return the color
	 */
	public String getColor() {
		return Color;
	}
	
	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	public String getType() {
		return Type;
	}
	
	/**
	 * Gets stop number.
	 *
	 * @return the stop number
	 */
	public String getStopNumber() {
		return StopNumber;
	}
	
	/**
	 * Gets comments text area.
	 *
	 * @return the comments text area
	 */
	public String getCommentsTextArea() {
		return CommentsTextArea;
	}
	
	/**
	 * Gets operator age.
	 *
	 * @return the operator age
	 */
	public String getOperatorAge() {
		return operatorAge;
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
	
	/**
	 * Gets street.
	 *
	 * @return the street
	 */
	public String getStreet() {
		return Street;
	}
	
	/**
	 * Gets response model.
	 *
	 * @return the response model
	 */
	public String getResponseModel() {
		return ResponseModel;
	}
	
	/**
	 * Gets response other info.
	 *
	 * @return the response other info
	 */
	public String getResponseOtherInfo() {
		return ResponseOtherInfo;
	}
	
}

