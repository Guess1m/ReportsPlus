package com.Guess.ReportsPlus.util.Report;

import com.Guess.ReportsPlus.logs.ChargesData;
import com.Guess.ReportsPlus.logs.CitationsData;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;

public class treeViewUtils {
	public static void copyChargeDataFile() throws IOException {
		
		String sourcePathCharges = "/com/Guess/ReportsPlus/data/Charges.xml";
		Path destinationDir = Paths.get(getJarPath(), "data");
		
		if (!Files.exists(destinationDir)) {
			Files.createDirectories(destinationDir);
		}
		
		try (InputStream inputStream = treeViewUtils.class.getResourceAsStream(sourcePathCharges)) {
			if (inputStream != null) {
				
				Path destinationPathCharges = destinationDir.resolve(Paths.get(sourcePathCharges).getFileName());
				
				Files.copy(inputStream, destinationPathCharges, StandardCopyOption.REPLACE_EXISTING);
			} else {
				log("Resource not found: " + sourcePathCharges, LogUtils.Severity.ERROR);
			}
		}
	}
	
	public static void copyCitationDataFile() throws IOException {
		
		String sourcePathCitations = "/com/Guess/ReportsPlus/data/Citations.xml";
		Path destinationDir = Paths.get(getJarPath(), "data");
		
		if (!Files.exists(destinationDir)) {
			Files.createDirectories(destinationDir);
		}
		
		try (InputStream inputStream = treeViewUtils.class.getResourceAsStream(sourcePathCitations)) {
			if (inputStream != null) {
				
				Path destinationPathCitations = destinationDir.resolve(Paths.get(sourcePathCitations).getFileName());
				
				Files.copy(inputStream, destinationPathCitations, StandardCopyOption.REPLACE_EXISTING);
			} else {
				log("Resource not found: " + sourcePathCitations, LogUtils.Severity.ERROR);
			}
		}
	}
	
	public static void copyCustomizationDataFile() throws IOException {
		
		String sourcePathCustomization = "/com/Guess/ReportsPlus/data/customization.json";
		Path destinationDir = Paths.get(getJarPath(), "data");
		
		if (!Files.exists(destinationDir)) {
			Files.createDirectories(destinationDir);
		}
		
		try (InputStream inputStream = treeViewUtils.class.getResourceAsStream(sourcePathCustomization)) {
			if (inputStream != null) {
				
				Path destinationPathCitations = destinationDir.resolve(
						Paths.get(sourcePathCustomization).getFileName());
				
				Files.copy(inputStream, destinationPathCitations, StandardCopyOption.REPLACE_EXISTING);
			} else {
				log("Resource not found: " + sourcePathCustomization, LogUtils.Severity.ERROR);
			}
		}
	}
	
	public static String findXMLValue(String selectedValue, String value, String path) {
		try {
			File file = new File(getJarPath() + "/" + path);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document document = factory.newDocumentBuilder().parse(file);
			
			Element selectedElement = findElementByValue(document.getDocumentElement(), selectedValue);
			if (selectedElement != null) {
				String XMLValue = selectedElement.getAttribute(value);
				return XMLValue.isEmpty() ? null : XMLValue;
			} else {
				log("Element not found for value: " + selectedValue, LogUtils.Severity.WARN);
				return null;
			}
		} catch (ParserConfigurationException | IOException | SAXException e) {
			logError("Find XML value error", e);
			return null;
		}
	}
	
	private static Element findElementByValue(Element parentElement, String value) {
		NodeList childNodes = parentElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;
				if (childElement.getAttribute("name").equals(value)) {
					return childElement;
				}
				
				Element foundElement = findElementByValue(childElement, value);
				if (foundElement != null) {
					return foundElement;
				}
			}
		}
		return null;
	}
	
	public static void parseTreeXML(Element parentElement, TreeItem<String> parentItem) {
		NodeList childNodes = parentElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;
				String nodeName = childElement.getNodeName();
				if (childElement.hasAttribute("name")) {
					nodeName = childElement.getAttribute("name");
				}
				TreeItem<String> item = new TreeItem<>(nodeName);
				parentItem.getChildren().add(item);
				parseTreeXML(childElement, item);
			}
		}
	}
	
	public static void expandTreeItem(TreeItem<String> root) {
		root.setExpanded(true);
	}
	
	public static void addChargesToTable(String chargesString, ObservableList<ChargesData> chargeList) {
		String[] chargesArray = chargesString.split("\\|");
		for (String charge : chargesArray) {
			charge = charge.trim();
			if (!charge.isEmpty()) {
				chargeList.add(new ChargesData(charge));
			}
		}
	}
	
	public static void addCitationsToTable(String citationsString, ObservableList<CitationsData> citList) {
		String[] citationsArray = citationsString.split("\\|");
		for (String cit : citationsArray) {
			cit = cit.trim();
			if (!cit.isEmpty()) {
				citList.add(new CitationsData(cit));
			}
		}
	}
	
}
