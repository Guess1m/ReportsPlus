package com.Guess.ReportsPlus.util.Other.Callout.Messages;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoredMessage {
	private final String sender;
	private final String date;
	private final String time;
	private final String type;
	private final String message;

	@JsonCreator
	public StoredMessage(@JsonProperty("sender") String sender, @JsonProperty("date") String date,
			@JsonProperty("time") String time, @JsonProperty("type") String type,
			@JsonProperty("message") String message) {
		this.sender = sender;
		this.date = date;
		this.time = time;
		this.type = type;
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		StoredMessage that = (StoredMessage) o;
		return Objects.equals(sender, that.sender) && Objects.equals(date, that.date) && Objects.equals(time, that.time)
				&& Objects.equals(type, that.type) && Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sender, date, time, type, message);
	}

	@Override
	public String toString() {
		return "StoredMessage{" + "sender='" + sender + '\'' + ", date='" + date + '\'' + ", time='" + time + '\''
				+ ", type='" + type + '\'' + ", message='" + message + '\'' + '}';
	}
}