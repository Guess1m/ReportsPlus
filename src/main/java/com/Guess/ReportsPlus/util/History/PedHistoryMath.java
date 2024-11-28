package com.Guess.ReportsPlus.util.History;

import com.Guess.ReportsPlus.util.Misc.LogUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;

public class PedHistoryMath {
	
	private static final List<String> maleNames = new ArrayList<>(
			Arrays.asList("James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph", "Thomas",
			              "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Mark", "Donald", "Steven", "Paul",
			              "Andrew", "Joshua", "Kenneth", "Kevin", "Brian", "George", "Edward", "Henry", "Peter", "Jack",
			              "Ryan", "Harry", "Frank", "Gary", "Raymond", "Albert", "Arthur"));
	private static final List<String> femaleNames = new ArrayList<>(
			Arrays.asList("Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah",
			              "Karen", "Nancy", "Lisa", "Margaret", "Betty", "Sandra", "Ashley", "Dorothy", "Kimberly",
			              "Emily", "Donna", "Michelle", "Carol", "Amanda", "Melissa", "Deborah", "Laura", "Stephanie",
			              "Rebecca", "Sharon", "Cynthia", "Kathleen", "Helen", "Amy", "Angela", "Anna"));
	private static final List<String> lastNames = new ArrayList<>(
			Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez",
			              "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor",
			              "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez",
			              "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright",
			              "Scott", "Torres", "Nguyen", "Hill", "Flores", "Green", "Adams", "Nelson", "Baker", "Hall",
			              "Rivera", "Campbell", "Mitchell", "Carter", "Roberts"));
	private static final Map<String, String> pedAddresses = new HashMap<>();
	private static final List<String> losSantosAddresses = new ArrayList<>(
			List.of("Abattoir Avenue", "Abe Milton Parkway", "Ace Jones Drive", "Adam's Apple Boulevard",
			        "Aguja Street", "Alta Place", "Alta Street", "Amarillo Vista", "Amarillo Way", "Americano Way",
			        "Atlee Street", "Autopia Parkway", "Banham Canyon Drive", "Barbareno Road", "Bay City Avenue",
			        "Bay City Incline", "Baytree Canyon Road", "Boulevard Del Perro", "Bridge Street", "Brouge Avenue",
			        "Buccaneer Way", "Buen Vino Road", "Caesars Place", "Calais Avenue", "Capital Boulevard",
			        "Carcer Way", "Carson Avenue", "Chum Street", "Chupacabra Street", "Clinton Avenue",
			        "Cockingend Drive", "Conquistador Street", "Cortes Street", "Cougar Avenue", "Covenant Avenue",
			        "Cox Way", "Crusade Road", "Davis Avenue", "Decker Street", "Didion Drive", "Dorset Drive",
			        "Dorset Place", "Dry Dock Street", "Dunstable Drive", "Dunstable Lane", "Dutch London Street",
			        "Eastbourne Way", "East Galileo Avenue", "East Mirror Drive", "Eclipse Boulevard", "Edwood Way",
			        "Elgin Avenue", "El Burro Boulevard", "El Rancho Boulevard", "Equality Way", "Exceptionalists Way",
			        "Fantastic Place", "Fenwell Place", "Forum Drive", "Fudge Lane", "Galileo Road", "Gentry Lane",
			        "Ginger Street", "Glory Way", "Goma Street", "Greenwich Parkway", "Greenwich Place",
			        "Greenwich Way", "Grove Street", "Hanger Way", "Hangman Avenue", "Hardy Way", "Hawick Avenue",
			        "Heritage Way", "Hillcrest Avenue", "Hillcrest Ridge Access Road", "Imagination Court",
			        "Industry Passage", "Ineseno Road", "Integrity Way", "Invention Court", "Innocence Boulevard",
			        "Jamestown Street", "Kimble Hill Drive", "Kortz Drive", "Labor Place", "Laguna Place",
			        "Lake Vinewood Drive", "Las Lagunas Boulevard", "Liberty Street", "Lindsay Circus",
			        "Little Bighorn Avenue", "Low Power Street", "Macdonald Street", "Mad Wayne Thunder Drive",
			        "Magellan Avenue", "Marathon Avenue", "Marlowe Drive", "Melanoma Street", "Meteor Street",
			        "Milton Road", "Mirror Park Boulevard", "Mirror Place", "Morningwood Boulevard", "Mount Haan Drive",
			        "Mount Haan Road", "Mount Vinewood Drive", "Movie Star Way", "Mutiny Road", "New Empire Way",
			        "Nikola Avenue", "Nikola Place", "Normandy Drive", "North Archer Avenue", "North Conker Avenue",
			        "North Sheldon Avenue", "North Rockford Drive", "Occupation Avenue", "Orchardville Avenue",
			        "Palomino Avenue", "Peaceful Street", "Perth Street", "Picture Perfect Drive", "Plaice Place",
			        "Playa Vista", "Popular Street", "Portola Drive", "Power Street", "Prosperity Street",
			        "Prosperity Street Promenade", "Red Desert Avenue", "Richman Street", "Rockford Drive",
			        "Roy Lowenstein Boulevard", "Rub Street", "San Andreas Avenue", "Sandcastle Way",
			        "San Vitus Boulevard", "Senora Road", "Shank Street", "Signal Street", "Sinner Street",
			        "Sinners Passage", "South Arsenal Street", "South Boulevard Del Perro", "South Mo Milton Drive",
			        "South Rockford Drive", "South Shambles Street", "Spanish Avenue", "Steele Way",
			        "Strangeways Drive", "Strawberry Avenue", "Supply Street", "Sustancia Road", "Swiss Street",
			        "Tackle Street", "Tangerine Street", "Tongva Drive", "Tower Way", "Tug Street", "Utopia Gardens",
			        "Vespucci Boulevard", "Vinewood Boulevard", "Vinewood Park Drive", "Vitus Street", "Voodoo Place",
			        "West Eclipse Boulevard", "West Galileo Avenue", "West Mirror Drive", "Whispymound Drive",
			        "Wild Oats Drive", "York Street", "Zancudo Barranca"));
	private static final List<String> blaineCountyAddresses = new ArrayList<>(
			List.of("Algonquin Boulevard", "Alhambra Drive", "Armadillo Avenue", "Baytree Canyon Road", "Calafia Road",
			        "Cascabel Avenue", "Cassidy Trail", "Cat-Claw Avenue", "Chianski Passage", "Cholla Road",
			        "Cholla Springs Avenue", "Duluoz Avenue", "East Joshua Road", "Fort Zancudo Approach Road",
			        "Galileo Road", "Grapeseed Avenue", "Grapeseed Main Street", "Joad Lane", "Joshua Road",
			        "Lesbos Lane", "Lolita Avenue", "Marina Drive", "Meringue Lane", "Mount Haan Road",
			        "Mountain View Drive", "Niland Avenue", "North Calafia Way", "Nowhere Road", "O'Neil Way",
			        "Paleto Boulevard", "Panorama Drive", "Procopio Drive", "Procopio Promenade", "Pyrite Avenue",
			        "Raton Pass", "Route 68 Approach", "Seaview Road", "Senora Way", "Smoke Tree Road", "Union Road",
			        "Zancudo Avenue", "Zancudo Road", "Zancudo Trail"));
	
	public static String getRandomDepartment() {
		String[] departments = {"LSPD", "LSSO", "BCSO", "SAHP", "FIB", "IAA"};
		
		int[] weights = {27, 26, 26, 15, 3, 3};
		
		Random random = new Random();
		int randomValue = random.nextInt(100);
		
		int cumulativeWeight = 0;
		for (int i = 0; i < weights.length; i++) {
			cumulativeWeight += weights[i];
			if (randomValue < cumulativeWeight) {
				return departments[i];
			}
		}
		return departments[0];
	}
	
	public static String generateValidLicenseExpirationDate() {
		int maxYears = 4;
		LocalDate currentDate = LocalDate.now();
		
		long minDaysAhead = 0;
		long maxDaysAhead = maxYears * 365L + (maxYears / 4);
		long randomDaysAhead = ThreadLocalRandom.current().nextLong(minDaysAhead, maxDaysAhead + 1);
		
		LocalDate expirationDate = currentDate.plusDays(randomDaysAhead);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		return expirationDate.format(formatter);
	}
	
	public static String generateExpiredLicenseExpirationDate() {
		int maxYearsAgo = 5;
		LocalDate currentDate = LocalDate.now();
		
		long minDaysAgo = 1;
		long maxDaysAgo = maxYearsAgo * 365L + (maxYearsAgo / 4);
		long randomDaysAgo = ThreadLocalRandom.current().nextLong(minDaysAgo, maxDaysAgo + 1);
		
		LocalDate expirationDate = currentDate.minusDays(randomDaysAgo);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		return expirationDate.format(formatter);
	}
	
	public static boolean calculateTrueFalseProbability(String percentage) {
		int percentage1 = Integer.parseInt(percentage);
		if (percentage1 < 0 || percentage1 > 100) {
			String message = "Check Config: Percentage must be between 0 and 100, it is: " + percentage + " using default 50% chance.";
			log(message, LogUtils.Severity.ERROR);
			showNotificationError("Error", message);
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
			showNotificationError("Error", message);
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
	
	public static String calculateLicenseStatus(int chanceValid, int chanceExpired, int chanceSuspended) {
		int totalChance = chanceValid + chanceSuspended + chanceExpired;
		if (totalChance != 100) {
			String message = "License status chances do not add up to 100, they equal: " + totalChance + ". Valid: " + chanceValid + " Suspended: " + chanceSuspended + " Expired: " + chanceExpired;
			log(message, LogUtils.Severity.ERROR);
			showNotificationError("Error", message);
			chanceValid = 55;
			chanceExpired = 22;
			chanceSuspended = 23;
		}
		
		Random random = new Random();
		int roll = random.nextInt(100) + 1;
		
		if (roll <= chanceValid) {
			return "Valid";
		} else if (roll <= chanceValid + chanceSuspended) {
			return "SUSPENDED";
		} else {
			return "EXPIRED";
		}
	}
	
	public static Set<String> getPermitClassBasedOnChances(int chanceHandgun, int chanceShotgun, int chanceLonggun) {
		int totalChance = chanceHandgun + chanceShotgun + chanceLonggun;
		if (totalChance != 100) {
			String message = "Check Config: Permit chances do not add up to 100. They equal: " + totalChance + ". Handgun: " + chanceHandgun + " Shotgun: " + chanceShotgun + " Longgun: " + chanceLonggun;
			log(message, LogUtils.Severity.ERROR);
			showNotificationError("Error", message);
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
			showNotificationError("Error", message);
			noChargesProbability = 60;
			oneToTwoChargesProbability = 25;
			twoToThreeChargesProbability = 10;
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
			showNotificationError("Error", message);
			noCitationsProbability = 60;
			oneToTwoCitationsProbability = 25;
			twoToThreeCitationsProbability = 10;
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
	
	public static String getRandomFullName(boolean isMale) {
		Random random = new Random();
		String firstName;
		if (isMale) {
			firstName = maleNames.get(random.nextInt(maleNames.size()));
		} else {
			firstName = femaleNames.get(random.nextInt(femaleNames.size()));
		}
		String lastName = lastNames.get(random.nextInt(lastNames.size()));
		return firstName + " " + lastName;
	}
	
	public static String generateLicenseNumber() {
		StringBuilder caseNumber = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			SecureRandom RANDOM = new SecureRandom();
			int digit = RANDOM.nextInt(10);
			caseNumber.append(digit);
		}
		return caseNumber.toString();
	}
	
	public static String getRandomAddress() {
		Random random = new Random();
		List<String> chosenList = random.nextInt(2) == 0 ? losSantosAddresses : blaineCountyAddresses;
		String address;
		do {
			int index = random.nextInt(chosenList.size());
			String addressNumber = java.lang.String.format("%03d", random.nextInt(1000));
			address = addressNumber + " " + chosenList.get(index);
		} while (pedAddresses.containsValue(address));
		
		pedAddresses.put(java.lang.String.valueOf(pedAddresses.size() + 1), address);
		return address;
	}
	
	public static String generateBirthday(int maxAge) {
		Random random = new Random();
		LocalDate today = LocalDate.now();
		LocalDate minBirthDate = today.minusYears(maxAge);
		long randomDays = random.nextInt((int) ChronoUnit.DAYS.between(minBirthDate, today) + 1);
		LocalDate birthDate = today.minusDays(randomDays);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
		return birthDate.format(formatter);
	}
}
