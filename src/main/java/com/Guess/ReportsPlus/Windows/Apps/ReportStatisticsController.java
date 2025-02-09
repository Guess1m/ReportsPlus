package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import jakarta.xml.bind.JAXBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.*;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UIDarkColor;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UILightColor;
import static com.Guess.ReportsPlus.logs.Accident.AccidentReportUtils.loadAccidentReports;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.loadArrestReports;
import static com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils.loadCalloutReports;
import static com.Guess.ReportsPlus.logs.Death.DeathReportUtils.loadDeathReports;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.loadImpoundReports;
import static com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils.loadIncidentReports;
import static com.Guess.ReportsPlus.logs.Patrol.PatrolReportUtils.loadPatrolReports;
import static com.Guess.ReportsPlus.logs.Search.SearchReportUtils.loadSearchReports;
import static com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils.loadTrafficStopReports;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.hexToRgba;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.rgbToHexString;

public class ReportStatisticsController {
	public static ReportStatisticsController reportStatisticsController;
	public static NumberAxis yAxis = null;
	public static CategoryAxis xAxis = null;
	public static AreaChart<String, Number> chart = null;
	@FXML
	private BorderPane borderPane;
	@FXML
	private BorderPane root;
	@FXML
	private AnchorPane mainPane;
	@FXML
	private ComboBox reportsByCombobox;
	@FXML
	private Label reportsByLabel;
	
	public static NumberAxis getyAxis() {
		return yAxis;
	}
	
	public static AreaChart<String, Number> getChart() {
		return chart;
	}
	
	public static CategoryAxis getxAxis() {
		return xAxis;
	}
	
	private <T> HashMap<String, Integer> parseReport(List<T> reports, String getMethod) {
		HashMap<String, Integer> counts = new HashMap<>();
		if (reports != null) {
			for (T report : reports) {
				try {
					String value = (String) report.getClass().getMethod(getMethod).invoke(report);
					if (value != null && !value.equalsIgnoreCase("n/a") && !value.equalsIgnoreCase("")) {
						value = value.replaceAll("(?i)county", "").trim();
						counts.put(value, counts.getOrDefault(value, 0) + 1);
					}
				} catch (Exception e) {
					log("[ReportStats] Report: [" + report.getClass().getSimpleName() + "] missing method: " + getMethod, LogUtils.Severity.WARN);
				}
			}
		}
		return counts;
	}
	
	private HashMap<String, Integer> parseLogReports(String getMethod) {
		HashMap<String, Integer> counts = new HashMap<>();
		try {
			log("[ReportStats] Starting to parse all reports for method: [" + getMethod + "]", LogUtils.Severity.INFO);
			mergeCounts(counts, parseReport(loadAccidentReports().getAccidentReportList(), getMethod));
			mergeCounts(counts, parseReport(loadArrestReports().getArrestReportList(), getMethod));
			mergeCounts(counts, parseReport(loadCalloutReports().getCalloutReportList(), getMethod));
			mergeCounts(counts, parseReport(loadDeathReports().getDeathReportList(), getMethod));
			mergeCounts(counts, parseReport(loadImpoundReports().getImpoundReportList(), getMethod));
			mergeCounts(counts, parseReport(loadIncidentReports().getIncidentReportList(), getMethod));
			mergeCounts(counts, parseReport(loadPatrolReports().getPatrolReportList(), getMethod));
			mergeCounts(counts, parseReport(loadSearchReports().getSearchReportList(), getMethod));
			mergeCounts(counts, parseReport(TrafficCitationUtils.loadTrafficCitationReports().getTrafficCitationReportList(), getMethod));
			mergeCounts(counts, parseReport(loadTrafficStopReports().getTrafficStopReportList(), getMethod));
			
		} catch (JAXBException e) {
			logError("Error parsing reports: ", e);
		}
		log("[ReportStats] Finished parsing all reports for [" + getMethod + "]; " + counts, LogUtils.Severity.INFO);
		return counts;
	}
	
	private void mergeCounts(HashMap<String, Integer> mainCounts, HashMap<String, Integer> additionalCounts) {
		for (Map.Entry<String, Integer> entry : additionalCounts.entrySet()) {
			mainCounts.put(entry.getKey(), mainCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
		}
	}
	
	private AreaChart<String, Number> createChart(String getMethod) {
		
		xAxis = new CategoryAxis();
		
		ObservableList<String> xAxisElements = FXCollections.observableArrayList();
		
		HashMap<String, Integer> logReports = parseLogReports(getMethod);
		if (logReports != null) {
			xAxisElements.addAll(logReports.keySet());
		}
		
		xAxis.setCategories(xAxisElements);
		
		yAxis = new NumberAxis();
		yAxis.setTickLabelFill(Paint.valueOf("#ffffff"));
		
		chart = new AreaChart<>(xAxis, yAxis);
		
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		
		if (logReports != null) {
			for (String area : logReports.keySet()) {
				series.getData().add(new XYChart.Data<>(area, logReports.get(area)));
			}
		}
		
		chart.getData().add(series);
		
		return chart;
	}
	
	public void initialize() {
		reportsByCombobox.getItems().clear();
		reportsByCombobox.getItems().addAll(new ArrayList<>(Arrays.asList("Officer", "Area", "County")));
		
		reportsByCombobox.getSelectionModel().select(1);
		updateChartPane("getArea");
		
		reportsByCombobox.setOnAction(actionEvent -> {
			String choice = reportsByCombobox.getSelectionModel().getSelectedItem().toString().trim();
			switch (choice) {
				case "Officer":
					updateChartPane("getOfficerName");
					log("[ReportStats] Using getOfficerName for chartdata", LogUtils.Severity.INFO);
					break;
				case "County":
					updateChartPane("getCounty");
					log("[ReportStats] Using getCounty for chartdata", LogUtils.Severity.INFO);
					break;
				case "Area":
				default:
					updateChartPane("getArea");
					log("[ReportStats] Using getArea for chartdata", LogUtils.Severity.INFO);
					break;
			}
		});
		
		reportsByLabel.setText(localization.getLocalizedMessage("ReportStatistics.reportsByLabel", "Reports By:"));
	}
	
	private void updateChartPane(String getMethod) {
		log("Running ChartUpdate For: [" + getMethod + "]", LogUtils.Severity.DEBUG);
		ArrayList<Node> nodes = new ArrayList<>();
		for (Node node : borderPane.getChildren()) {
			if (node instanceof AreaChart) {
				log("[ReportStats] Added AreaChart Node to delete list", LogUtils.Severity.DEBUG);
				nodes.add(node);
			}
		}
		if (nodes.size() > 0) {
			borderPane.getChildren().remove(nodes.get(0));
			log("[ReportStats] Removed old AreaChart Node", LogUtils.Severity.DEBUG);
		}
		
		AreaChart<String, Number> chart = createChart(getMethod);
		try {
			updateChart(chart);
		} catch (IOException e) {
			logError("[ReportStats] Error updating chart config: ", e);
		}
		
		chart.setLegendVisible(false);
		borderPane.setCenter(chart);
	}
	
	private void updateChart(AreaChart chart) throws IOException {
		String mainclr = ConfigReader.configRead("uiColors", "mainColor");
		String secclr = ConfigReader.configRead("uiColors", "secondaryColor");
		String accclr = ConfigReader.configRead("uiColors", "accentColor");
		
		Set<Node> symbolNodes = chart.lookupAll(".default-color0.chart-area-symbol");
		for (Node symbolNode : symbolNodes) {
			symbolNode.setStyle("-fx-background-color: " + secclr + ";");
		}
		Node fillNode = chart.lookup(".default-color0.chart-series-area-fill");
		fillNode.setStyle("-fx-fill: " + hexToRgba(mainclr, 0.5) + ";");
		Node lineNode = chart.lookup(".default-color0.chart-series-area-line");
		lineNode.setStyle("-fx-stroke: " + accclr + ";");
		
		if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
			xAxis.setTickLabelFill(rgbToHexString(UIDarkColor));
			yAxis.setTickLabelFill(rgbToHexString(UIDarkColor));
			Node bkgNode = chart.lookup("AreaChart .chart-content .chart-plot-background");
			bkgNode.setStyle("-fx-background-color: rgba(0,0,0,0.05), rgba(0,0,0,0.05);");
		} else {
			xAxis.setTickLabelFill(rgbToHexString(UILightColor));
			yAxis.setTickLabelFill(rgbToHexString(UILightColor));
			Node bkgNode = chart.lookup("AreaChart .chart-content .chart-plot-background");
			bkgNode.setStyle("-fx-background-color: rgba(255,255,255, 0.1), rgba(255,255,255, 0.1);");
		}
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getReportsByLabel() {
		return reportsByLabel;
	}
}