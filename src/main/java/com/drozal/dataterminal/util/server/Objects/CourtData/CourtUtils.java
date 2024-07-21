package com.drozal.dataterminal.util.server.Objects.CourtData;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Optional;

import static com.drozal.dataterminal.util.Misc.stringUtil.courtDataURL;

public class CourtUtils {
	private static final int CASE_NUMBER_LENGTH = 7;
	
	public static String generateCaseNumber() {
		StringBuilder caseNumber = new StringBuilder("CN-");
		for (int i = 0; i < CASE_NUMBER_LENGTH; i++) {
			SecureRandom RANDOM = new SecureRandom();
			int digit = RANDOM.nextInt(10); // Generate a digit between 0 and 9
			caseNumber.append(digit);
		}
		return caseNumber.toString();
	}
	
	public static CourtCases loadCourtCases() throws JAXBException, IOException {
		File file = new File(courtDataURL);
		if (!file.exists()) {
			return new CourtCases();
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(CourtCases.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (CourtCases) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			System.err.println("Error loading courtcases: " + e.getMessage());
			throw e;
		}
	}
	
	private static void saveCourtCases(CourtCases courtCases) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(CourtCases.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		File file = new File(courtDataURL);
		marshaller.marshal(courtCases, file);
	}
	
	public static void addCase(Case courtCase) throws JAXBException, IOException {
		CourtCases courtCases = loadCourtCases();
		
		if (courtCases.getCaseList() == null) {
			courtCases.setCaseList(new java.util.ArrayList<>());
		}
		
		boolean exists = courtCases.getCaseList().stream().anyMatch(
				e -> e.getCaseNumber() == courtCase.getCaseNumber());
		
		if (!exists) {
			courtCases.getCaseList().add(courtCase);
			saveCourtCases(courtCases);
		} else {
			System.err.println("Court Case with number " + courtCase.getCaseNumber() + " already exists.");
		}
	}
	
	public static void deleteCase(String casenumber) throws JAXBException, IOException {
		CourtCases courtCases = loadCourtCases();
		
		if (courtCases.getCaseList() != null) {
			courtCases.getCaseList().removeIf(e -> e.getCaseNumber().equals(casenumber));
			saveCourtCases(courtCases);
		}
	}
	
	public static Optional<Case> findCaseByNumber(String casenumber) throws JAXBException, IOException {
		CourtCases courtCases = loadCourtCases();
		
		if (courtCases.getCaseList() != null) {
			return courtCases.getCaseList().stream().filter(e -> e.getCaseNumber().equals(casenumber)).findFirst();
		}
		
		return Optional.empty();
	}
	
	public static void modifyEmployee(String number, Case updatedCase) throws JAXBException, IOException {
		CourtCases courtCases = loadCourtCases();
		
		if (courtCases.getCaseList() != null) {
			for (int i = 0; i < courtCases.getCaseList().size(); i++) {
				Case e = courtCases.getCaseList().get(i);
				if (e.getCaseNumber().equals(number)) {
					courtCases.getCaseList().set(i, updatedCase);
					saveCourtCases(courtCases);
					return;
				}
			}
		}
	}
	
}
