package com.Guess.ReportsPlus.util.CourtData;

import com.Guess.ReportsPlus.config.ConfigReader;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.courtDataURL;

public class CourtUtils {
	private static final ScheduledExecutorService courtPendingChargesExecutor = Executors.newScheduledThreadPool(1);
	
	public static String generateCaseNumber(String number) {
		StringBuilder caseNumber = new StringBuilder("CN-");
		caseNumber.append(number);
		logDebug("Generated Case#: " + caseNumber.toString());
		return caseNumber.toString();
	}
	
	public static CourtCases loadCourtCases() throws JAXBException {
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
	
	public static void saveCourtCases(CourtCases courtCases) throws JAXBException {
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
		
		boolean exists = courtCases.getCaseList().stream().anyMatch(e -> e.getCaseNumber().equals(courtCase.getCaseNumber()));
		
		if (!exists) {
			courtCases.getCaseList().add(courtCase);
			saveCourtCases(courtCases);
		} else {
			logWarn("Court Case with number " + courtCase.getCaseNumber() + " already exists.");
		}
	}
	
	public static void deleteCase(String casenumber) throws JAXBException, IOException {
		CourtCases courtCases = loadCourtCases();
		
		if (courtCases.getCaseList() != null) {
			courtCases.getCaseList().removeIf(e -> e.getCaseNumber().equals(casenumber));
			saveCourtCases(courtCases);
			showNotificationInfo("Court Case Manager", "Deleted Court Case#: " + casenumber);
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
			if (isTraffic.equalsIgnoreCase("true")) {
				isTrafficCharge = true;
			}
		}
		return calculateOutcomes(isTrafficCharge, outcomeMin, outcomeMax, outcomeTime, outcomeProbChance, outcomeSuspChance, outcomeMinSusp, outcomeMaxSusp, outcomeRevokeChance, outcomeFine);
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
			int minFine = maxFine / 3;
			int randomFine = minFine + random.nextInt(maxFine - minFine + 1);
			result.append("Fined: " + randomFine + ". ");
		}
		
		boolean onlyProbation = outcomeTime.equalsIgnoreCase("months") && random.nextInt(100) < 10;
		
		if (outcomeTime.equalsIgnoreCase("years")) {
			onlyProbation = false;
		}
		if (maxJailTime.equalsIgnoreCase("life")) {
			if (random.nextBoolean()) {
				result.append("Jail Time: Life sentence. ");
			} else {
				result.append("Jail Time: ").append(minJailTime).append(" years. ");
			}
		} else {
			if (onlyProbation) {
				result.append("Probation: Granted. ");
				result.append("Probation Time: ").append(Math.round(Integer.parseInt(minJailTime) + (Integer.parseInt(maxJailTime) - Integer.parseInt(minJailTime)) * random.nextDouble())).append(" months. ");
				result.append("Jail Time: Dismissed. ");
			} else {
				boolean probationGranted = random.nextInt(100) < Integer.parseInt(probChance);
				
				double jailTime = 0;
				if (probationGranted) {
					jailTime = Integer.parseInt(minJailTime) + (Integer.parseInt(maxJailTime) - Integer.parseInt(minJailTime)) * random.nextDouble();
					jailTime = jailTime / 3;
					result.append("Probation: Granted. ");
					result.append("Probation Time: ").append(Math.round(jailTime * 2)).append(" months. ");
				} else {
					jailTime = Integer.parseInt(minJailTime) + (Integer.parseInt(maxJailTime) - Integer.parseInt(minJailTime)) * random.nextDouble();
					result.append("Probation: Denied. ");
				}
				
				if (outcomeTime.equalsIgnoreCase("years")) {
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
					int randomSuspTime = random.nextInt(Integer.parseInt(maxSuspTime) - Integer.parseInt(minSuspTime) + 1) + Integer.parseInt(minSuspTime);
					result.append("License: Suspended.");
					result.append("License Suspension Time: ").append(randomSuspTime).append(" months. ");
				} else {
					result.append("License: Valid.");
				}
			}
		}
		
		return result.toString();
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
	
	public static Optional<Case> findCaseByNumber(String caseNum) {
		CourtCases cases = null;
		try {
			cases = loadCourtCases();
		} catch (JAXBException e) {
			logError("JAXB error while trying to loadCourtCases() by number: ", e);
		}
		
		if (cases.getCaseList() != null) {
			return cases.getCaseList().stream().filter(e -> e.getCaseNumber().equals(caseNum)).findFirst();
		}
		
		return Optional.empty();
	}
	
	public static void scheduleOutcomeRevealForSingleCase(String caseNumber) throws JAXBException, IOException {
		long delayInSeconds = Long.parseLong(ConfigReader.configRead("pedHistory", "courtTrialDelay"));
		Random random = new Random();
		
		long minSec = delayInSeconds / 3;
		long randomSec = minSec + random.nextLong(delayInSeconds - minSec + 1);
		
		CourtCases courtCases = loadCourtCases();
		Case caseToUpdate = courtCases.getCaseList().stream().filter(c -> caseNumber.equals(c.getCaseNumber())).findFirst().orElse(null);
		
		if (caseToUpdate != null) {
			logDebug("Scheduled: " + caseToUpdate.getCaseNumber() + " for court, pending trial: " + randomSec + " Sec");
			
			Runnable revealTask = () -> {
				try {
					synchronized (caseToUpdate) {
						caseToUpdate.setStatus("Closed");
						modifyCase(caseToUpdate.getCaseNumber(), caseToUpdate);
						logDebug("Case: #" + caseToUpdate.getCaseNumber() + " has been closed");
						showNotificationInfo("Court Manager", "Case: #" + caseToUpdate.getCaseNumber() + ", " + caseToUpdate.getName() + ", has been closed");
					}
				} catch (JAXBException | IOException e) {
					logError("Error processing case: #" + caseToUpdate.getCaseNumber() + ":  ", e);
				}
			};
			
			courtPendingChargesExecutor.schedule(revealTask, randomSec, TimeUnit.SECONDS);
		} else {
			logError("Case with number " + caseNumber + " not found.");
		}
	}
}