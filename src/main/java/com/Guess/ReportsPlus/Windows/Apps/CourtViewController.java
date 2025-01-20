package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.CourtData.Case;
import com.Guess.ReportsPlus.util.CourtData.CourtCases;
import com.Guess.ReportsPlus.util.CourtData.CourtUtils;
import com.Guess.ReportsPlus.util.CourtData.CustomCaseCell;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import jakarta.xml.bind.JAXBException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.util.CourtData.CourtUtils.*;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationWarning;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.*;

public class CourtViewController {
	
	private static final ScheduledExecutorService courtPendingChargesExecutor = Executors.newScheduledThreadPool(2);
	public static SimpleIntegerProperty needCourtRefresh = new SimpleIntegerProperty();
	public static CourtViewController courtViewController;
	@javafx.fxml.FXML
	private TextField caseAgeField;
	@javafx.fxml.FXML
	private TextField caseStreetField;
	@javafx.fxml.FXML
	private AnchorPane courtInfoPane;
	@javafx.fxml.FXML
	private Label caselbl12;
	@javafx.fxml.FXML
	private Label caselbl11;
	@javafx.fxml.FXML
	private Label caselbl10;
	@javafx.fxml.FXML
	private Button revealOutcomeBtn;
	@javafx.fxml.FXML
	private Label caseSec1;
	@javafx.fxml.FXML
	private Label caseSec2;
	@javafx.fxml.FXML
	private Label caseTotalProbationLabel;
	@javafx.fxml.FXML
	private Label caselbl4;
	@javafx.fxml.FXML
	private Label caselbl2;
	@javafx.fxml.FXML
	private TextField caseAreaField;
	@javafx.fxml.FXML
	private Label caselbl1;
	@javafx.fxml.FXML
	private Label noCourtCaseSelectedlbl;
	@javafx.fxml.FXML
	private TextArea caseNotesField;
	@javafx.fxml.FXML
	private AnchorPane blankCourtInfoPane;
	@javafx.fxml.FXML
	private ListView caseList;
	@javafx.fxml.FXML
	private TextField caseCourtDateField;
	@javafx.fxml.FXML
	private Label caseSuspensionDurationlbl;
	@javafx.fxml.FXML
	private TextField caseAddressField;
	@javafx.fxml.FXML
	private Label caseSuspensionDuration;
	@javafx.fxml.FXML
	private Button deleteCaseBtn;
	@javafx.fxml.FXML
	private Label caselbl9;
	@javafx.fxml.FXML
	private Label caselbl8;
	@javafx.fxml.FXML
	private Label caselbl7;
	@javafx.fxml.FXML
	private Label caselbl6;
	@javafx.fxml.FXML
	private Label caseTotalLabel;
	@javafx.fxml.FXML
	private Label caseLicenseStatLabel;
	@javafx.fxml.FXML
	private Label casePrim1;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private TextField caseNumField;
	@javafx.fxml.FXML
	private Label casesec4;
	@javafx.fxml.FXML
	private Label casesec3;
	@javafx.fxml.FXML
	private ListView caseOutcomesListView;
	@javafx.fxml.FXML
	private Label casesec2;
	@javafx.fxml.FXML
	private Label casesec1;
	@javafx.fxml.FXML
	private TextField caseCountyField;
	@javafx.fxml.FXML
	private Label caseTotalJailTimeLabel;
	@javafx.fxml.FXML
	private TextField caseGenderField;
	@javafx.fxml.FXML
	private AnchorPane courtPane;
	@javafx.fxml.FXML
	private Label caseprim2;
	@javafx.fxml.FXML
	private ListView caseOffencesListView;
	@javafx.fxml.FXML
	private Label caseprim3;
	@javafx.fxml.FXML
	private Label caseprim1;
	@javafx.fxml.FXML
	private TextField caseNameField;
	
	public static String getNextIndex(CourtCases courtCases) {
		int highestIndex = 0;
		
		if (courtCases.getCaseList() != null) {
			for (Case c : courtCases.getCaseList()) {
				String indexString = c.getIndex();
				if (indexString != null && !indexString.isEmpty()) {
					try {
						int index = Integer.parseInt(indexString);
						highestIndex = Math.max(highestIndex, index);
					} catch (NumberFormatException e) {
						logError("Invalid index format: " + indexString, e);
					}
				}
			}
		}
		return String.valueOf(highestIndex + 1);
	}
	
	private static void revealOutcomes(Case case1) {
		if (courtViewController != null) {
			List<String> licenseStatusList = parseCharges(case1.getOutcomes(), "License");
			String outcomeSuspension = calculateTotalTime(case1.getOutcomes(), "License Suspension Time");
			String outcomeProbation = calculateTotalTime(case1.getOutcomes(), "Probation Time");
			List<String> jailTimeList = parseCharges(case1.getOutcomes(), "Jail Time");
			String totalJailTime = calculateTotalTime(case1.getOutcomes(), "Jail Time");
			if (jailTimeList.contains("Life sentence")) {
				totalJailTime = "Life Sentence";
			}
			
			boolean areTrafficChargesPresent = !licenseStatusList.isEmpty() || !outcomeSuspension.isEmpty();
			String licenseStatus = "";
			if (licenseStatusList.contains("OK")) {
				licenseStatus = "N/A";
				courtViewController.caseLicenseStatLabel.setStyle("-fx-text-fill: gray;");
			} else if (licenseStatusList.contains("Suspended")) {
				licenseStatus = "Suspended";
				courtViewController.caseLicenseStatLabel.setStyle("-fx-text-fill: #cc5200;");
			} else if (licenseStatusList.contains("Revoked")) {
				licenseStatus = "Revoked";
				courtViewController.caseLicenseStatLabel.setStyle("-fx-text-fill: red;");
			}
			
			if (!totalJailTime.isEmpty()) {
				if (totalJailTime.contains("years") && Integer.parseInt(extractInteger(totalJailTime)) >= 10) {
					courtViewController.caseTotalJailTimeLabel.setStyle("-fx-text-fill: red;");
				} else if (totalJailTime.contains("months")) {
					courtViewController.caseTotalJailTimeLabel.setStyle("-fx-text-fill: black;");
				} else if (totalJailTime.contains("Life")) {
					courtViewController.caseTotalJailTimeLabel.setStyle("-fx-text-fill: red;");
				}
				courtViewController.caseTotalJailTimeLabel.setText(totalJailTime);
			} else {
				courtViewController.caseTotalJailTimeLabel.setStyle("-fx-text-fill: gray;");
				courtViewController.caseTotalJailTimeLabel.setText("None");
			}
			
			if (!outcomeProbation.isEmpty()) {
				if (outcomeProbation.contains("years")) {
					courtViewController.caseTotalProbationLabel.setStyle("-fx-text-fill: red;");
				} else if (outcomeProbation.contains("months") && Integer.parseInt(
						extractInteger(outcomeProbation)) >= 7) {
					courtViewController.caseTotalProbationLabel.setStyle("-fx-text-fill: #cc5200;");
				} else {
					courtViewController.caseTotalProbationLabel.setStyle("-fx-text-fill: black;");
				}
				courtViewController.caseTotalProbationLabel.setText(outcomeProbation);
			} else {
				courtViewController.caseTotalProbationLabel.setStyle("-fx-text-fill: gray;");
				courtViewController.caseTotalProbationLabel.setText("None");
			}
			
			if (areTrafficChargesPresent) {
				courtViewController.caseLicenseStatLabel.setText(licenseStatus);
				if (!outcomeSuspension.isEmpty() && !licenseStatusList.contains("Revoked")) {
					if (outcomeSuspension.contains("years") && Integer.parseInt(
							extractInteger(outcomeSuspension)) >= 2) {
						courtViewController.caseSuspensionDuration.setStyle("-fx-text-fill: red;");
					} else {
						courtViewController.caseSuspensionDuration.setStyle("-fx-text-fill: #cc5200;");
					}
					courtViewController.caseSuspensionDuration.setText(outcomeSuspension);
				} else {
					courtViewController.caseSuspensionDuration.setStyle("-fx-text-fill: #cc5200;");
					courtViewController.caseSuspensionDuration.setText("License Revoked");
				}
			} else {
				courtViewController.caseLicenseStatLabel.setStyle("-fx-text-fill: gray;");
				courtViewController.caseLicenseStatLabel.setText("N/A");
				courtViewController.caseSuspensionDuration.setStyle("-fx-text-fill: gray;");
				courtViewController.caseSuspensionDuration.setText("None");
			}
			
			int fineTotal = calculateFineTotal(case1.getOutcomes());
			if (fineTotal > 1500) {
				courtViewController.caseTotalLabel.setStyle("-fx-text-fill: red;");
				courtViewController.caseTotalLabel.setText("$" + fineTotal + ".00");
			} else if (fineTotal > 700) {
				courtViewController.caseTotalLabel.setStyle("-fx-text-fill: #cc5200;");
				courtViewController.caseTotalLabel.setText("$" + fineTotal + ".00");
			} else if (fineTotal > 0) {
				courtViewController.caseTotalLabel.setStyle("-fx-text-fill: black;");
				courtViewController.caseTotalLabel.setText("$" + fineTotal + ".00");
			} else {
				courtViewController.caseTotalLabel.setStyle("-fx-text-fill: gray;");
				courtViewController.caseTotalLabel.setText("$0.00");
			}
			
			String offences = case1.getOffences() != null ? case1.getOffences() : "";
			Pattern pattern = Pattern.compile("MaxFine:\\S+");
			Matcher matcher = pattern.matcher(offences);
			String updatedOffences = matcher.replaceAll("").trim();
			
			ObservableList<Label> offenceLabels = createLabels(updatedOffences);
			ObservableList<Label> outcomeLabels = createLabels(case1.getOutcomes());
			
			courtViewController.caseOutcomesListView.setItems(outcomeLabels);
			courtViewController.caseOffencesListView.setItems(offenceLabels);
			
			setCellFactory(courtViewController.caseOutcomesListView);
			setCellFactory(courtViewController.caseOffencesListView);
		}
	}
	
	private static void setCellFactory(ListView<Label> listView) {
		listView.setCellFactory(new Callback<>() {
			@Override
			public ListCell<Label> call(ListView<Label> param) {
				return new ListCell<>() {
					@Override
					protected void updateItem(Label item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							setGraphic(item);
						}
					}
				};
			}
		});
	}
	
	public static void scheduleOutcomeRevealsForPendingCases() throws JAXBException, IOException {
		long delayInSeconds = Long.parseLong(ConfigReader.configRead("pedHistory", "courtTrialDelay"));
		Random random = new Random();
		
		long minSec = delayInSeconds / 3;
		CourtCases courtCases = loadCourtCases();
		
		if (courtCases.getCaseList() != null) {
			List<Case> pendingCases = courtCases.getCaseList().stream().filter(
					c -> "pending".equalsIgnoreCase(c.getStatus())).collect(Collectors.toList());
			
			for (Case pendingCase : pendingCases) {
				long randomSec = minSec + random.nextLong(delayInSeconds - minSec + 1);
				log("Scheduled: " + pendingCase.getCaseNumber() + " for court, pending trial: " + randomSec + " Sec",
				    LogUtils.Severity.DEBUG);
				
				Runnable revealTask = () -> {
					
					revealOutcomes(pendingCase);
					pendingCase.setStatus("Closed");
					
					try {
						modifyCase(pendingCase.getCaseNumber(), pendingCase);
						log("Case: #" + pendingCase.getCaseNumber() + " has been closed", LogUtils.Severity.DEBUG);
						showNotificationInfo("Court Manager",
						                     "Case: #" + pendingCase.getCaseNumber() + ", " + pendingCase.getName() + ", has been closed");
					} catch (JAXBException | IOException e) {
						logError("Error modifying case from scheduleOutcomeReveals: ", e);
						
					}
				};
				
				courtPendingChargesExecutor.schedule(revealTask, randomSec, TimeUnit.SECONDS);
			}
			
		}
	}
	
	public void initialize() {
		blankCourtInfoPane.setVisible(true);
		courtInfoPane.setVisible(false);
		
		needCourtRefresh.set(0);
		needCourtRefresh.addListener((obs, oldValue, newValue) -> {
			if (newValue.equals(1)) {
				loadCaseLabels(caseList);
				needCourtRefresh.set(0);
			}
		});
		
		loadCaseLabels(caseList);
		caseList.getSelectionModel().clearSelection();
		
		casePrim1.setText(localization.getLocalizedMessage("CourtView.MainHeaderLabel", "COURT CASE REPORT"));
		caselbl1.setText(localization.getLocalizedMessage("CourtView.CaseNumLabel", "CASE#:"));
		caselbl2.setText(localization.getLocalizedMessage("CourtView.CaseDateLabel", "COURT DATE:"));
		
		caseSec1.setText(localization.getLocalizedMessage("CourtView.DefendantInfoLbl", "Defendant Information:"));
		caselbl4.setText(localization.getLocalizedMessage("CourtView.DefendantNameLbl", "NAME:"));
		caselbl6.setText(localization.getLocalizedMessage("CourtView.DefendantAgeLbl", "AGE/DOB:"));
		caselbl8.setText(localization.getLocalizedMessage("CourtView.DefendantAddressLbl", "ADDRESS:"));
		caselbl7.setText(localization.getLocalizedMessage("CourtView.DefendantGenderLbl", "GENDER:"));
		
		caseSec2.setText(localization.getLocalizedMessage("CourtView.OffenseInfoLbl", "Offence Information:"));
		caselbl9.setText(localization.getLocalizedMessage("CourtView.OffenseStreetLbl", "STREET:"));
		caselbl10.setText(localization.getLocalizedMessage("CourtView.OffenseAreaLbl", "AREA:"));
		caselbl11.setText(localization.getLocalizedMessage("CourtView.OffenseCountyLbl", "COUNTY:"));
		caselbl12.setText(localization.getLocalizedMessage("CourtView.OffenseNotesLbl", "CASE NOTES:"));
		
		caseprim2.setText(localization.getLocalizedMessage("CourtView.ChargesLabel", "Charges:"));
		caseprim3.setText(localization.getLocalizedMessage("CourtView.OutcomesLabel", "Outcomes:"));
		
		caseprim1.setText(localization.getLocalizedMessage("CourtView.CaseVerdictLabel", "Case Verdict:"));
		casesec1.setText(localization.getLocalizedMessage("CourtView.FineTotalLabel", "Fine Total:"));
		casesec2.setText(localization.getLocalizedMessage("CourtView.TotalJailTimeLabel", "Total Jail Time:"));
		casesec3.setText(localization.getLocalizedMessage("CourtView.TotalProbationLabel", "Total Probation Time:"));
		casesec4.setText(localization.getLocalizedMessage("CourtView.LicenseStatusLabel", "License Status:"));
		caseSuspensionDurationlbl.setText(
				localization.getLocalizedMessage("CourtView.SuspensionDurationLabel", "Suspension Duration:"));
		
		revealOutcomeBtn.setText(localization.getLocalizedMessage("CourtView.ShowOutcomesButton", "Show Outcome(s)"));
		deleteCaseBtn.setText(localization.getLocalizedMessage("CourtView.DeleteCaseButton", "Delete Case"));
		
		noCourtCaseSelectedlbl.setText(
				localization.getLocalizedMessage("CourtView.NoCaseFoundLabel", "No Court Case Selected."));
	}
	
	public void loadCaseLabels(ListView<String> listView) {
		listView.getItems().clear();
		try {
			CourtCases courtCases = loadCourtCases();
			ObservableList<String> caseNames = FXCollections.observableArrayList();
			
			if (courtCases.getCaseList() != null) {
				
				List<Case> sortedCases = courtCases.getCaseList().stream().sorted(Comparator.comparing((Case case1) -> {
					
					String indexString = case1.getIndex();
					if (indexString == null || indexString.isEmpty()) {
						indexString = getNextIndex(courtCases);
						case1.setIndex(indexString);
						try {
							CourtUtils.addCase(case1);
						} catch (JAXBException | IOException e) {
							throw new RuntimeException(e);
						}
					}
					
					int index = Integer.parseInt(indexString);
					
					return index;
				}).reversed()).toList();
				
				for (Case case1 : sortedCases) {
					if (!case1.getName().isEmpty() && !case1.getOffences().isEmpty()) {
						caseNames.add(
								case1.getOffenceDate().replaceAll("-", "/") + " " + case1.getCaseTime().replace(".",
								                                                                                "") + " " + case1.getName() + " " + case1.getCaseNumber());
					}
				}
				
				listView.setItems(caseNames);
				
				listView.setCellFactory(new Callback<>() {
					@Override
					public ListCell<String> call(ListView<String> param) {
						return new ListCell<>() {
							private final CustomCaseCell customCaseCell = new CustomCaseCell();
							
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty || item == null) {
									setGraphic(null);
								} else {
									for (Case case1 : sortedCases) {
										if (item.equals(case1.getOffenceDate().replaceAll("-",
										                                                  "/") + " " + case1.getCaseTime().replace(
												".", "") + " " + case1.getName() + " " + case1.getCaseNumber())) {
											customCaseCell.updateCase(case1);
											break;
										}
									}
									setGraphic(customCaseCell);
								}
							}
						};
					}
				});
				
				listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue != null) {
						blankCourtInfoPane.setVisible(false);
						courtInfoPane.setVisible(true);
						for (Case case1 : sortedCases) {
							if (newValue.equals(
									case1.getOffenceDate().replaceAll("-", "/") + " " + case1.getCaseTime().replace(".",
									                                                                                "") + " " + case1.getName() + " " + case1.getCaseNumber())) {
								updateFields(case1);
								break;
							}
						}
					}
				});
				
				Map<String, Case> caseMap = new HashMap<>();
				for (Case case1 : courtCases.getCaseList()) {
					String dateTime = case1.getOffenceDate() + " " + case1.getCaseTime().replace(".", "");
					caseMap.put(dateTime, case1);
				}
				courtCases.setCaseList(new ArrayList<>(caseMap.values()));
				saveCourtCases(courtCases);
			}
		} catch (JAXBException e) {
			logError("Error loading Case labels: ", e);
		}
	}
	
	private void updateFields(Case case1) {
		if (case1.getStatus() != null) {
			if (case1.getStatus().equalsIgnoreCase("pending")) {
				String pendingLabel = localization.getLocalizedMessage("CourtView.PendingLabel", "Pending");
				caseTotalLabel.setText(pendingLabel);
				caseTotalLabel.setStyle("-fx-text-fill: black;");
				caseTotalJailTimeLabel.setText(pendingLabel);
				caseTotalJailTimeLabel.setStyle("-fx-text-fill: black;");
				caseTotalProbationLabel.setText(pendingLabel);
				caseTotalProbationLabel.setStyle("-fx-text-fill: black;");
				caseLicenseStatLabel.setText(pendingLabel);
				caseLicenseStatLabel.setStyle("-fx-text-fill: black;");
				caseSuspensionDuration.setText(pendingLabel);
				caseSuspensionDuration.setStyle("-fx-text-fill: black;");
				
				String offences = case1.getOffences() != null ? case1.getOffences() : "";
				Pattern pattern = Pattern.compile("MaxFine:\\S+");
				Matcher matcher = pattern.matcher(offences);
				String updatedOffences = matcher.replaceAll("").trim();
				
				ObservableList<Label> offenceLabels = createLabels(updatedOffences);
				ObservableList<Label> outcomeLabels = createPendingLabels(case1.getOutcomes());
				
				caseOutcomesListView.setItems(outcomeLabels);
				caseOffencesListView.setItems(offenceLabels);
				
				setCellFactory(caseOutcomesListView);
				setCellFactory(caseOffencesListView);
			} else {
				revealOutcomes(case1);
			}
		} else {
			log("Could not find a caseStatus for: #" + case1.getCaseNumber(), LogUtils.Severity.ERROR);
			revealOutcomes(case1);
			case1.setStatus("Closed");
			try {
				modifyCase(case1.getCaseNumber(), case1);
				log("Case: #" + case1.getCaseNumber() + " has been set as closed", LogUtils.Severity.DEBUG);
			} catch (JAXBException | IOException e) {
				logError("Error setting case as closed (modifying): ", e);
				
			}
		}
		revealOutcomeBtn.setVisible(case1.getStatus().equalsIgnoreCase("pending"));
		caseAgeField.setText(case1.getAge() != null ? String.valueOf(case1.getAge()) : "");
		caseGenderField.setText(case1.getGender() != null ? String.valueOf(case1.getGender()) : "");
		caseAreaField.setText(case1.getArea() != null ? case1.getArea() : "");
		caseStreetField.setText(case1.getStreet() != null ? case1.getStreet() : "");
		caseCountyField.setText(case1.getCounty() != null ? case1.getCounty() : "");
		caseNotesField.setText(case1.getNotes() != null ? case1.getNotes() : "");
		caseNameField.setText(case1.getName() != null ? case1.getName() : "");
		caseCourtDateField.setText(case1.getCourtDate() != null ? case1.getCourtDate() : "");
		caseNumField.setText(case1.getCaseNumber() != null ? case1.getCaseNumber() : "");
		caseAddressField.setText(case1.getAddress() != null ? case1.getAddress() : "");
	}
	
	@javafx.fxml.FXML
	public void deleteCaseBtnPress(ActionEvent actionEvent) {
		String selectedCaseNum;
		if (!caseNumField.getText().isEmpty() && caseNumField != null) {
			selectedCaseNum = caseNumField.getText();
			try {
				deleteCase(selectedCaseNum);
			} catch (JAXBException e) {
				logError("Could not delete case, JAXBException:", e);
			} catch (IOException e) {
				logError("Could not delete case, IOException:", e);
			}
			blankCourtInfoPane.setVisible(true);
			courtInfoPane.setVisible(false);
			loadCaseLabels(caseList);
		}
	}
	
	@javafx.fxml.FXML
	public void revealOutcomeBtnPress(ActionEvent actionEvent) {
		String selectedCaseNum;
		if (!caseNumField.getText().isEmpty() && caseNumField != null) {
			selectedCaseNum = caseNumField.getText();
			Optional<Case> caseToUpdateOptional = findCaseByNumber(selectedCaseNum);
			if (caseToUpdateOptional.isPresent()) {
				Case caseToUpdate = caseToUpdateOptional.get();
				if (!caseToUpdate.getStatus().equalsIgnoreCase("closed")) {
					try {
						caseToUpdate.setStatus("Closed");
						modifyCase(caseToUpdate.getCaseNumber(), caseToUpdate);
						log("Case: #" + caseToUpdate.getCaseNumber() + " Outcomes Revealed", LogUtils.Severity.INFO);
						updateFields(caseToUpdate);
						loadCaseLabels(caseList);
						
					} catch (JAXBException e) {
						logError("Could not RevealOutcomes case#" + caseToUpdate.getCaseNumber() + ", JAXBException: ",
						         e);
					} catch (IOException e) {
						logError("Could not RevealOutcomes case#" + caseToUpdate.getCaseNumber() + ", IOException: ",
						         e);
					}
				} else {
					log("Case: #" + caseToUpdate.getCaseNumber() + " Outcomes Already Revealed!",
					    LogUtils.Severity.WARN);
					showNotificationWarning("Court Manager",
					                        "Case: #" + caseToUpdate.getCaseNumber() + " Outcomes Already Revealed");
				}
			}
		}
	}
	
	public Label getCaselbl12() {
		return caselbl12;
	}
	
	public Label getCaselbl11() {
		return caselbl11;
	}
	
	public Label getCaselbl10() {
		return caselbl10;
	}
	
	public Label getCaseSec1() {
		return caseSec1;
	}
	
	public Label getCaseSec2() {
		return caseSec2;
	}
	
	public Label getCaselbl4() {
		return caselbl4;
	}
	
	public Label getCaselbl2() {
		return caselbl2;
	}
	
	public Label getCaselbl1() {
		return caselbl1;
	}
	
	public Label getNoCourtCaseSelectedlbl() {
		return noCourtCaseSelectedlbl;
	}
	
	public TextArea getCaseNotesField() {
		return caseNotesField;
	}
	
	public AnchorPane getBlankCourtInfoPane() {
		return blankCourtInfoPane;
	}
	
	public Label getCaseSuspensionDurationlbl() {
		return caseSuspensionDurationlbl;
	}
	
	public Label getCaselbl9() {
		return caselbl9;
	}
	
	public Label getCaselbl8() {
		return caselbl8;
	}
	
	public Label getCaselbl7() {
		return caselbl7;
	}
	
	public Label getCaselbl6() {
		return caselbl6;
	}
	
	public Label getCasePrim1() {
		return casePrim1;
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getCasesec4() {
		return casesec4;
	}
	
	public Label getCasesec3() {
		return casesec3;
	}
	
	public Label getCasesec2() {
		return casesec2;
	}
	
	public Label getCasesec1() {
		return casesec1;
	}
	
	public AnchorPane getCourtPane() {
		return courtPane;
	}
	
	public Label getCaseprim2() {
		return caseprim2;
	}
	
	public Label getCaseprim3() {
		return caseprim3;
	}
	
	public Label getCaseprim1() {
		return caseprim1;
	}
	
}
