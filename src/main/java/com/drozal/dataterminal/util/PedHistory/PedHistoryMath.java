package com.drozal.dataterminal.util.PedHistory;

import com.drozal.dataterminal.util.Misc.LogUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.NotificationManager.showNotificationError;

public class PedHistoryMath {
	
	public static boolean calculateTrueFalseProbability(String percentage) {
		int percentage1 = Integer.parseInt(percentage);
		if (percentage1 < 0 || percentage1 > 100) {
			String message = "Check Config: Percentage must be between 0 and 100, it is: " + percentage + " using default 50% chance.";
			log(message, LogUtils.Severity.ERROR);
			showNotificationError("Error", message, mainRT);
			percentage1 = 50;
		}
		
		Random random = new Random();
		return random.nextInt(100) < percentage1;
	}
	
	public static Set<String> getPermitTypeBasedOnChances(int chanceConcealed, int chanceOpen, int chanceBoth) {
		int totalChance = chanceConcealed + chanceOpen + chanceBoth;
		if (totalChance != 100) {
			String message = "Check Config: Gun permitType chances do not add up to equal 100 they equal: " + totalChance + ". Concealed: " + chanceConcealed + " Open: " + chanceOpen + " Both: " + chanceBoth;
			log(message, LogUtils.Severity.ERROR);
			showNotificationError("Error", message, mainRT);
			//todo put the default values here once I figure out good ones:
			chanceConcealed = 33;
			chanceOpen = 33;
			chanceBoth = 34;
		}
		
		Random random = new Random();
		int roll = random.nextInt(100) + 1;
		
		Set<String> result = new HashSet<>();
		
		if (roll <= chanceConcealed) {
			result.add("concealed");
		} else if (roll <= chanceConcealed + chanceOpen) {
			result.add("open");
		} else if (roll <= chanceConcealed + chanceOpen + chanceBoth) {
			result.add("both");
		}
		
		return result;
	}
	
	public static Set<String> getPermitClassBasedOnChances(int chanceHandgun, int chanceShotgun, int chanceLonggun) {
		int totalChance = chanceHandgun + chanceShotgun + chanceLonggun;
		if (totalChance != 100) {
			String message = "Check Config: Permit chances do not add up to 100. They equal: " + totalChance + ". Handgun: " + chanceHandgun + " Shotgun: " + chanceShotgun + " Longgun: " + chanceLonggun;
			log(message, LogUtils.Severity.ERROR);
			showNotificationError("Error", message, mainRT);
			//todo put the default values here once I figure out good ones:
			chanceHandgun = 50;
			chanceShotgun = 22;
			chanceLonggun = 28;
		}
		
		Random random = new Random();
		Set<String> result = new HashSet<>();
		
		boolean hasHandgun = random.nextInt(100) < chanceHandgun;
		boolean hasShotgun = random.nextInt(100) < chanceShotgun;
		boolean hasLonggun = random.nextInt(100) < chanceLonggun;
		
		if (hasHandgun) {
			result.add("Handguns");
		}
		if (hasShotgun) {
			result.add("Shotguns");
		}
		if (hasLonggun) {
			result.add("Longguns");
		}
		
		if (result.isEmpty()) {
			int roll = random.nextInt(3);
			switch (roll) {
				case 0:
					result.add("Handguns");
					break;
				case 1:
					result.add("Shotguns");
					break;
				case 2:
					result.add("Longguns");
					break;
			}
		}
		
		return result;
	}
	
	public static int calculateTotalStops(int baseValue) {
		Random random = new Random();
		int increase = 0;
		if (random.nextDouble() * 100 < 50) {
			increase = random.nextInt(3 + 1);
		}
		
		return baseValue + increase;
	}
	
	public static List<String> getRandomCharges(String filePath, double noChargesProbability, double oneToTwoChargesProbability, double twoToThreeChargesProbability, double threeToFiveChargesProbability) throws IOException, ParserConfigurationException, SAXException {
		List<String> charges = new ArrayList<>();
		
		double totalProbability = noChargesProbability + oneToTwoChargesProbability + twoToThreeChargesProbability + threeToFiveChargesProbability;
		if (totalProbability != 100.0) {
			String message = "Check Config: Probabilities do not add up to 100. They equal: " + totalProbability + ". No charges: " + noChargesProbability + " 1-2 charges: " + oneToTwoChargesProbability + " 2-3 charges: " + twoToThreeChargesProbability + " 3-5 charges: " + threeToFiveChargesProbability;
			log(message, LogUtils.Severity.ERROR);
			showNotificationError("Error", message, mainRT);
			//todo put the default values here once I figure out good ones:
			noChargesProbability = 60;
			oneToTwoChargesProbability = 35;
			twoToThreeChargesProbability = 20;
			threeToFiveChargesProbability = 5;
		}
		
		File file = new File(filePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);
		
		NodeList chargeNodes = document.getElementsByTagName("Charge");
		List<Element> chargeElements = new ArrayList<>();
		for (int i = 0; i < chargeNodes.getLength(); i++) {
			chargeElements.add((Element) chargeNodes.item(i));
		}
		
		Random random = new Random();
		
		int numberOfCharges = determineNumberOfCharges(noChargesProbability, oneToTwoChargesProbability,
		                                               twoToThreeChargesProbability, threeToFiveChargesProbability,
		                                               random);
		
		for (int i = 0; i < numberOfCharges && !chargeElements.isEmpty(); i++) {
			int index = random.nextInt(chargeElements.size());
			Element chargeElement = chargeElements.remove(index);
			charges.add(chargeElement.getAttribute("name"));
		}
		
		return charges;
	}
	
	private static int determineNumberOfCharges(double noChargesProbability, double oneToTwoChargesProbability, double twoToThreeChargesProbability, double threeToFiveChargesProbability, Random random) {
		double randomValue = random.nextDouble() * 100;
		
		if (randomValue < noChargesProbability) {
			return 0;
		} else if (randomValue < noChargesProbability + oneToTwoChargesProbability) {
			return 1 + random.nextInt(2);
		} else if (randomValue < noChargesProbability + oneToTwoChargesProbability + twoToThreeChargesProbability) {
			return 2 + random.nextInt(2);
		} else {
			return 3 + random.nextInt(3);
		}
	}
	
	public static List<String> getRandomCitations(String filePath, double noCitationsProbability, double oneToTwoCitationsProbability, double twoToThreeCitationsProbability, double threeToFiveCitationsProbability) throws IOException, ParserConfigurationException, SAXException {
		List<String> citations = new ArrayList<>();
		
		double totalProbability = noCitationsProbability + oneToTwoCitationsProbability + twoToThreeCitationsProbability + threeToFiveCitationsProbability;
		if (totalProbability != 100.0) {
			String message = "Check Config: Probabilities do not add up to 100. They equal: " + totalProbability + ". No cit: " + noCitationsProbability + " 1-2 cit: " + oneToTwoCitationsProbability + " 2-3 cit: " + twoToThreeCitationsProbability + " 3-5 cit: " + threeToFiveCitationsProbability;
			log(message, LogUtils.Severity.ERROR);
			showNotificationError("Error", message, mainRT);
			noCitationsProbability = 60;
			oneToTwoCitationsProbability = 35;
			twoToThreeCitationsProbability = 20;
			threeToFiveCitationsProbability = 5;
		}
		
		File file = new File(filePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);
		
		NodeList citationNodes = document.getElementsByTagName("Citation");
		List<Element> citationElements = new ArrayList<>();
		for (int i = 0; i < citationNodes.getLength(); i++) {
			citationElements.add((Element) citationNodes.item(i));
		}
		
		Random random = new Random();
		
		int numberOfCitations = determineNumberOfCitations(noCitationsProbability, oneToTwoCitationsProbability,
		                                                   twoToThreeCitationsProbability,
		                                                   threeToFiveCitationsProbability, random);
		
		for (int i = 0; i < numberOfCitations && !citationElements.isEmpty(); i++) {
			int index = random.nextInt(citationElements.size());
			Element citationElement = citationElements.remove(index);
			citations.add(citationElement.getAttribute("name"));
		}
		
		return citations;
	}
	
	private static int determineNumberOfCitations(double noCitationsProbability, double oneToTwoCitationsProbability, double twoToThreeCitationsProbability, double threeToFiveCitationsProbability, Random random) {
		double randomValue = random.nextDouble() * 100;
		
		if (randomValue < noCitationsProbability) {
			return 0;
		} else if (randomValue < noCitationsProbability + oneToTwoCitationsProbability) {
			return 1 + random.nextInt(2);
		} else if (randomValue < noCitationsProbability + oneToTwoCitationsProbability + twoToThreeCitationsProbability) {
			return 2 + random.nextInt(2);
		} else {
			return 3 + random.nextInt(3);
		}
	}
	
	public static String getRandomCharge(String filePath) throws IOException, ParserConfigurationException, SAXException {
		List<String> charges = new ArrayList<>();
		
		File file = new File(filePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);
		
		NodeList chargeNodes = document.getElementsByTagName("Charge");
		for (int i = 0; i < chargeNodes.getLength(); i++) {
			Element chargeElement = (Element) chargeNodes.item(i);
			String chargeName = chargeElement.getAttribute("name");
			if (!chargeName.toLowerCase().contains("warrant")) {
				charges.add(chargeName);
			}
		}
		
		if (charges.isEmpty()) {
			return "No Data";
		}
		
		Random random = new Random();
		int index = random.nextInt(charges.size());
		return charges.get(index);
	}
}
