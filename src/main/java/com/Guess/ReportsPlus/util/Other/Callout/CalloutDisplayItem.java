package com.Guess.ReportsPlus.util.Other.Callout;

import com.Guess.ReportsPlus.util.Other.Callout.Messages.MessageManager;
import com.Guess.ReportsPlus.util.Other.Callout.Messages.StoredMessage;
import com.Guess.ReportsPlus.util.Server.Objects.Callout.Callout;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CalloutDisplayItem {
	private final StringProperty number;
	private final StringProperty status;
	private final StringProperty type;
	private final StringProperty street;
	private final StringProperty priority;
	private final StringProperty area;
	private final StringProperty description;
	private final StringProperty message;
	private final StringProperty county;
	private final StringProperty startDate;
	private final StringProperty startTime;
	private final StringProperty messages;
	
	private final transient Callout originalCallout;
	
	public CalloutDisplayItem(String number, String status, String type, String street, String priority, String area, String description, String message, String county, String startDate, String startTime, String messagesJson, Callout originalCallout) {
		this.number = new SimpleStringProperty(number);
		this.status = new SimpleStringProperty(status);
		this.type = new SimpleStringProperty(type);
		this.street = new SimpleStringProperty(street);
		this.priority = new SimpleStringProperty(priority);
		this.area = new SimpleStringProperty(area);
		this.description = new SimpleStringProperty(description == null ? "" : description);
		this.message = new SimpleStringProperty(message == null ? "" : message);
		this.county = new SimpleStringProperty(county == null ? "" : county);
		this.startDate = new SimpleStringProperty(startDate == null ? "" : startDate);
		this.startTime = new SimpleStringProperty(startTime == null ? "" : startTime);
		this.messages = new SimpleStringProperty(messagesJson == null || messagesJson.trim().isEmpty() ? "[]" : messagesJson);
		this.originalCallout = originalCallout;
	}
	
	public List<StoredMessage> getStoredMessages() {
		return MessageManager.deserializeMessages(this.messages.get());
	}
	
	public void setStoredMessages(List<StoredMessage> storedMessagesList) {
		this.messages.set(MessageManager.serializeMessages(storedMessagesList));
	}
	
	public void addStoredMessage(StoredMessage newMessage) {
		List<StoredMessage> currentMessages = getStoredMessages();
		if (currentMessages == null) {
			currentMessages = new ArrayList<>();
		}
		currentMessages.add(newMessage);
		setStoredMessages(currentMessages);
	}
	
	public void addStoredMessage(String sender, String date, String time, String type, String content) {
		StoredMessage newMessage = new StoredMessage(sender, date, time, type, content);
		addStoredMessage(newMessage);
	}
	
	public boolean containsStoredMessage(StoredMessage messageToCheck) {
		if (messageToCheck == null || messageToCheck.getType() == null) {
			return false;
		}
		List<StoredMessage> currentMessages = getStoredMessages();
		if (currentMessages == null || currentMessages.isEmpty()) {
			return false;
		}
		for (StoredMessage storedMsg : currentMessages) {
			if (storedMsg != null && Objects.equals(storedMsg.getType(), messageToCheck.getType())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean removeStoredMessage(StoredMessage messageToRemove) {
		if (messageToRemove == null) {
			return false;
		}
		List<StoredMessage> currentMessages = getStoredMessages();
		if (currentMessages == null || currentMessages.isEmpty()) {
			return false;
		}
		
		int indexToRemove = -1;
		for (int i = 0; i < currentMessages.size(); i++) {
			StoredMessage msg = currentMessages.get(i);
			if (msg != null && Objects.equals(msg.getSender(), messageToRemove.getSender()) && Objects.equals(msg.getDate(), messageToRemove.getDate()) && Objects.equals(msg.getTime(), messageToRemove.getTime()) && Objects.equals(msg.getType(), messageToRemove.getType()) && Objects.equals(
					msg.getMessage(), messageToRemove.getMessage())) {
				indexToRemove = i;
				break;
			}
		}
		
		if (indexToRemove != -1) {
			currentMessages.remove(indexToRemove);
			setStoredMessages(currentMessages);
			return true;
		}
		return false;
	}
	
	public StoredMessage getStoredMessageByType(String typeToFind) {
		if (typeToFind == null) {
			return null;
		}
		List<StoredMessage> currentMessages = getStoredMessages();
		if (currentMessages == null || currentMessages.isEmpty()) {
			return null;
		}
		for (StoredMessage storedMsg : currentMessages) {
			if (storedMsg != null && Objects.equals(storedMsg.getType(), typeToFind)) {
				return storedMsg;
			}
		}
		return null;
	}
	
	public boolean containsStoredMessageType(String typeToCheck) {
		if (typeToCheck == null) {
			return false;
		}
		List<StoredMessage> currentMessages = getStoredMessages();
		if (currentMessages == null || currentMessages.isEmpty()) {
			return false;
		}
		for (StoredMessage storedMsg : currentMessages) {
			if (storedMsg != null && Objects.equals(storedMsg.getType(), typeToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	public String getMessages() {
		return messages.get();
	}
	
	public void setMessages(String messagesJson) {
		this.messages.set(messagesJson == null || messagesJson.trim().isEmpty() ? "[]" : messagesJson);
	}
	
	public StringProperty messagesProperty() {
		return messages;
	}
	
	public String getNumber() {
		return number.get();
	}
	
	public void setNumber(String number) {
		this.number.set(number);
	}
	
	public StringProperty numberProperty() {
		return number;
	}
	
	public String getStatus() {
		return status.get();
	}
	
	public void setStatus(String status) {
		this.status.set(status);
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	public String getType() {
		return type.get();
	}
	
	public void setType(String type) {
		this.type.set(type);
	}
	
	public StringProperty typeProperty() {
		return type;
	}
	
	public String getStreet() {
		return street.get();
	}
	
	public void setStreet(String street) {
		this.street.set(street);
	}
	
	public StringProperty streetProperty() {
		return street;
	}
	
	public String getPriority() {
		return priority.get();
	}
	
	public void setPriority(String priority) {
		this.priority.set(priority);
	}
	
	public StringProperty priorityProperty() {
		return priority;
	}
	
	public String getArea() {
		return area.get();
	}
	
	public void setArea(String area) {
		this.area.set(area);
	}
	
	public StringProperty areaProperty() {
		return area;
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public StringProperty descriptionProperty() {
		return description;
	}
	
	public String getMessage() {
		return message.get();
	}
	
	public void setMessage(String message) {
		this.message.set(message);
	}
	
	public StringProperty messageProperty() {
		return message;
	}
	
	public String getCounty() {
		return county.get();
	}
	
	public void setCounty(String county) {
		this.county.set(county);
	}
	
	public StringProperty countyProperty() {
		return county;
	}
	
	public String getStartDate() {
		return startDate.get();
	}
	
	public void setStartDate(String startDate) {
		this.startDate.set(startDate);
	}
	
	public StringProperty startDateProperty() {
		return startDate;
	}
	
	public String getStartTime() {
		return startTime.get();
	}
	
	public void setStartTime(String startTime) {
		this.startTime.set(startTime);
	}
	
	public StringProperty startTimeProperty() {
		return startTime;
	}
	
	public Callout getOriginalCallout() {
		return originalCallout;
	}
}