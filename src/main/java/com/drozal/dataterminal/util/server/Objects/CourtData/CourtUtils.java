package com.drozal.dataterminal.util.server.Objects.CourtData;

import com.drozal.dataterminal.util.Misc.LogUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.courtDataURL;

public class CourtUtils {
	private static final int CASE_NUMBER_LENGTH = 7;
	
	public static String generateCaseNumber() {
		StringBuilder caseNumber = new StringBuilder("CN-");
		for (int i = 0; i < CASE_NUMBER_LENGTH; i++) {
			SecureRandom RANDOM = new SecureRandom();
			int digit = RANDOM.nextInt(10);
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
			logError("Error loading courtcases: ", e);
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
			log("Court Case with number " + courtCase.getCaseNumber() + " already exists.", LogUtils.Severity.WARN);
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
	
	public static void modifyCase(String number, Case updatedCase) throws JAXBException, IOException {
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
	
	public static String parseCourtData(String isTraffic, String probationChance, String minYears, String maxYears, String minMonths, String maxMonths, String suspChance, String minSusp, String maxSusp, String revokeChance, String fine, String finek) {
		String outcomeMin = "";
		String outcomeMax = "";
		String outcomeTime = "";
		String outcomeSuspChance = "";
		String outcomeMinSusp = "";
		String outcomeMaxSusp = "";
		String outcomeProbChance = "";
		String outcomeRevokeChance = "";
		String outcomeFine = "";
		
		if (finek != null && !finek.isEmpty()) {
			outcomeFine = finek + "000";
		} else if (fine != null && !fine.isEmpty()) {
			outcomeFine = fine;
		}
		
		if (minYears != null && !minYears.isEmpty()) {
			outcomeMin = minYears;
			outcomeTime = "years";
		} else if (minMonths != null && !minMonths.isEmpty()) {
			outcomeMin = minMonths;
			outcomeTime = "months";
		}
		
		if (maxYears != null && !maxYears.isEmpty()) {
			outcomeMax = maxYears;
			outcomeTime = "years";
		} else if (maxMonths != null && !maxMonths.isEmpty()) {
			outcomeMax = maxMonths;
			outcomeTime = "months";
		}
		
		if (suspChance != null && !suspChance.isEmpty()) {
			outcomeSuspChance = suspChance;
		}
		
		if (probationChance != null && !probationChance.isEmpty()) {
			outcomeProbChance = probationChance;
		}
		
		if (minSusp != null && !minSusp.isEmpty()) {
			outcomeMinSusp = minSusp;
		}
		
		if (maxSusp != null && !maxSusp.isEmpty()) {
			outcomeMaxSusp = maxSusp;
		}
		
		if (revokeChance != null && !revokeChance.isEmpty()) {
			outcomeRevokeChance = revokeChance;
		}
		
		boolean isTrafficCharge = false;
		if (isTraffic != null) {
			if (isTraffic.equals("true")) {
				isTrafficCharge = true;
			}
		}
		return calculateOutcomes(isTrafficCharge, outcomeMin, outcomeMax, outcomeTime, outcomeProbChance,
		                         outcomeSuspChance, outcomeMinSusp, outcomeMaxSusp, outcomeRevokeChance, outcomeFine);
	}
	
	public static String calculateOutcomes(boolean isTrafficCharge, String outcomeMin, String outcomeMax, String outcomeTime, String probationChance, String outcomeSuspChance, String outcomeMinSusp, String outcomeMaxSusp, String outcomeRevokeChance, String outcomeFine) {
		StringBuilder result = new StringBuilder();
		String minJailTime = String.valueOf(outcomeMin.isEmpty() ? 0 : outcomeMin);
		String maxJailTime = String.valueOf(outcomeMax.isEmpty() ? 0 : outcomeMax);
		String probChance = String.valueOf(probationChance.isEmpty() ? 0 : probationChance);
		String suspChance = String.valueOf(outcomeSuspChance.isEmpty() ? 0 : outcomeSuspChance);
		String minSuspTime = String.valueOf(outcomeMinSusp.isEmpty() ? 0 : outcomeMinSusp);
		String maxSuspTime = String.valueOf(outcomeMaxSusp.isEmpty() ? 0 : outcomeMaxSusp);
		int maxFine = outcomeFine.isEmpty() ? 0 : Integer.parseInt(outcomeFine);
		int revChance = Math.min(outcomeRevokeChance.isEmpty() ? 0 : Integer.parseInt(outcomeRevokeChance), 100);
		
		Random random = new Random();
		
		if (maxFine != 0) {
			random = new Random();
			maxFine = random.nextInt(maxFine + 1);
			result.append("Fined: " + maxFine + ". ");
		}
		
		boolean onlyProbation = outcomeTime.equals("months") && random.nextInt(100) < 10;
		
		if (outcomeTime.equals("years")) {
			onlyProbation = false;
		}
		if (maxJailTime.equals("life")) {
			if (random.nextBoolean()) {
				result.append("Jail Time: Life sentence. ");
			} else {
				result.append("Jail Time: ").append(minJailTime).append(" years. ");
			}
		} else {
			if (onlyProbation) {
				result.append("Probation: Granted. ");
				result.append("Probation Time: ").append(Math.round(
						Integer.parseInt(minJailTime) + (Integer.parseInt(maxJailTime) - Integer.parseInt(
								minJailTime)) * random.nextDouble())).append(" months. ");
				result.append("Jail Time: Dismissed. ");
			} else {
				boolean probationGranted = random.nextInt(100) < Integer.parseInt(probChance);
				
				double jailTime = 0;
				if (probationGranted) {
					jailTime = Integer.parseInt(minJailTime) + (Integer.parseInt(maxJailTime) - Integer.parseInt(
							minJailTime)) * random.nextDouble();
					jailTime = jailTime / 3;
					result.append("Probation: Granted. ");
					result.append("Probation Time: ").append(Math.round(jailTime * 2)).append(" months. ");
				} else {
					jailTime = Integer.parseInt(minJailTime) + (Integer.parseInt(maxJailTime) - Integer.parseInt(
							minJailTime)) * random.nextDouble();
					result.append("Probation: Denied. ");
				}
				
				if (outcomeTime.equals("years")) {
					result.append("Jail Time: ").append(Math.round(jailTime)).append(" years. ");
				} else {
					result.append("Jail Time: ").append(Math.round(jailTime)).append(" months. ");
				}
			}
		}
		boolean revocationGranted = random.nextInt(100) < revChance;
		
		if (isTrafficCharge) {
			if (revocationGranted) {
				result.append("License: Revoked.");
			} else {
				if (random.nextInt(100) < Integer.parseInt(suspChance)) {
					int randomSuspTime = random.nextInt(
							Integer.parseInt(maxSuspTime) - Integer.parseInt(minSuspTime) + 1) + Integer.parseInt(
							minSuspTime);
					result.append("License: Suspended.");
					result.append("License Suspension Time: ").append(randomSuspTime).append(" months. ");
				} else {
					result.append("License: Valid.");
				}
			}
		}
		
		return result.toString();
	}
	
	
}

