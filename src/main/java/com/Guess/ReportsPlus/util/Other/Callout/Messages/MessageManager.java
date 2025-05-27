package com.Guess.ReportsPlus.util.Other.Callout.Messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

public class MessageManager {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static String serializeMessages(List<StoredMessage> messages) {
		if (messages == null || messages.isEmpty()) {
			return "[]";
		}
		try {
			return objectMapper.writeValueAsString(messages);
		} catch (JsonProcessingException e) {
			logError("Error serializing messages to JSON: ", e);
			return "[]";
		}
	}
	
	public static List<StoredMessage> deserializeMessages(String messagesJson) {
		if (messagesJson == null || messagesJson.trim().isEmpty() || messagesJson.trim().equals("null")) {
			return new ArrayList<>();
		}
		try {
			return objectMapper.readValue(messagesJson, new TypeReference<List<StoredMessage>>() {
			});
		} catch (IOException e) {
			logError("Error deserializing messages from (JSON: " + " " + messagesJson + ")", e);
			return new ArrayList<>();
		}
	}
}