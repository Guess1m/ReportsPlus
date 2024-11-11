package com.drozal.dataterminal.util.Localization;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommentedProperties extends Properties {
	
	private final LinkedHashMap<String, String> comments = new LinkedHashMap<>();
	
	@Override
	public synchronized Object put(Object key, Object value) {
		if (!containsKey(key)) {
			if (key.toString().contains(".")) {
				String section = getSection(key);
				comments.putIfAbsent(section, "# " + section.substring(0, 1).toUpperCase() + section.substring(1));
			}
		}
		return super.put(key, value);
	}
	
	private Map<String, List<String>> organizeBySections() {
		Map<String, List<String>> sectionedKeys = new TreeMap<>();
		for (String key : stringPropertyNames()) {
			String section = getSection(key);
			sectionedKeys.computeIfAbsent(section, k -> new ArrayList<>()).add(key);
		}
		return sectionedKeys;
	}
	
	public void storeWithComments(OutputStream out, String header) throws IOException {
		try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
			if (header != null) {
				writer.println("# " + header);
			}
			writer.println("# " + new Date());
			writer.println();
			Map<String, List<String>> sectionedKeys = organizeBySections();
			
			for (Map.Entry<String, List<String>> entry : sectionedKeys.entrySet()) {
				if (comments.containsKey(entry.getKey())) {
					writer.println();
					writer.println(comments.get(entry.getKey()));
				}
				for (String key : entry.getValue()) {
					writer.println(key + "=" + getProperty(key));
				}
			}
		}
	}
	
	private String getSection(Object key) {
		return key.toString().contains(".") ? key.toString().split("\\.")[0] : "default";
	}
	
}
