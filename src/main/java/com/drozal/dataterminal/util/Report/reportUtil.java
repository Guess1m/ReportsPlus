package com.drozal.dataterminal.util.Report;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.ChargesData;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.util.Misc.AutoCompleteComboBoxListener;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.dropdownInfo;
import com.drozal.dataterminal.util.Window.ResizeHelper;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Report.treeViewUtils.*;
import static com.drozal.dataterminal.util.Window.windowUtils.centerStageOnMainApp;
import static com.drozal.dataterminal.util.Window.windowUtils.toggleWindowedFullscreen;

public class reportUtil {
	static double windowX = 0;
	static double windowY = 0;
	static double windowWidth = 0;
	static double windowHeight = 0;
	private static double xOffset;
	private static double yOffset;
	
	private static String getPrimaryColor() {
		String primaryColor;
		try {
			primaryColor = ConfigReader.configRead("reportSettings", "reportSecondary");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return primaryColor;
	}
	
	private static String getSecondaryColor() {
		String secondaryColor;
		try {
			secondaryColor = ConfigReader.configRead("reportSettings", "reportAccent");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return secondaryColor;
	}
	
	private static String getAccentColor() {
		String accentColor;
		try {
			accentColor = ConfigReader.configRead("reportSettings", "reportBackground");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return accentColor;
	}
	
	public static AnchorPane createTitleBar(String titleText) {
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setSaturation(-1.0);
		colorAdjust.setBrightness(-0.45);
		
		Label titleLabel = new Label(titleText);
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
		titleLabel.setAlignment(Pos.CENTER);
		AnchorPane.setLeftAnchor(titleLabel, (double) 0);
		AnchorPane.setRightAnchor(titleLabel, (double) 0);
		AnchorPane.setTopAnchor(titleLabel, (double) 0);
		AnchorPane.setBottomAnchor(titleLabel, (double) 0);
		titleLabel.setEffect(colorAdjust);
		titleLabel.setMouseTransparent(true);
		
		AnchorPane titleBar = new AnchorPane(titleLabel);
		titleBar.setMinHeight(30);
		titleBar.setStyle("-fx-background-color: #383838;");
		
		Image placeholderImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"));
		ImageView placeholderImageView = new ImageView(placeholderImage);
		placeholderImageView.setFitWidth(49);
		placeholderImageView.setFitHeight(49);
		AnchorPane.setLeftAnchor(placeholderImageView, 0.0);
		AnchorPane.setTopAnchor(placeholderImageView, -10.0);
		AnchorPane.setBottomAnchor(placeholderImageView, -10.0);
		placeholderImageView.setEffect(colorAdjust);
		
		Image closeImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/cross.png"));
		ImageView closeImageView = new ImageView(closeImage);
		closeImageView.setFitWidth(15);
		closeImageView.setFitHeight(15);
		AnchorPane.setRightAnchor(closeImageView, 15.0);
		AnchorPane.setTopAnchor(closeImageView, 7.0);
		closeImageView.setEffect(colorAdjust);
		
		Image maximizeImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/maximize.png"));
		ImageView maximizeImageView = new ImageView(maximizeImage);
		maximizeImageView.setFitWidth(15);
		maximizeImageView.setFitHeight(15);
		AnchorPane.setRightAnchor(maximizeImageView, 42.5);
		AnchorPane.setTopAnchor(maximizeImageView, 7.0);
		maximizeImageView.setEffect(colorAdjust);
		
		Image minimizeImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/minimize.png"));
		ImageView minimizeImageView = new ImageView(minimizeImage);
		minimizeImageView.setFitWidth(15);
		minimizeImageView.setFitHeight(15);
		AnchorPane.setRightAnchor(minimizeImageView, 70.0);
		AnchorPane.setTopAnchor(minimizeImageView, 7.0);
		minimizeImageView.setEffect(colorAdjust);
		
		Rectangle closeRect = new Rectangle(20, 20);
		Rectangle maximizeRect = new Rectangle(20, 20);
		Rectangle minimizeRect = new Rectangle(20, 20);
		
		closeRect.setFill(Color.TRANSPARENT);
		minimizeRect.setFill(Color.TRANSPARENT);
		maximizeRect.setFill(Color.TRANSPARENT);
		
		closeRect.setOnMouseClicked(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			stage.close();
		});
		
		minimizeRect.setOnMouseClicked(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			stage.setIconified(true);
		});
		maximizeRect.setOnMouseClicked(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			toggleWindowedFullscreen(stage, 850, 750);
		});
		
		titleBar.setOnMouseDragged(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			stage.setX(event.getScreenX() - xOffset);
			stage.setY(event.getScreenY() - yOffset);
		});
		
		titleBar.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		
		AnchorPane.setRightAnchor(closeRect, 12.5);
		AnchorPane.setTopAnchor(closeRect, 6.3);
		AnchorPane.setRightAnchor(minimizeRect, 70.0);
		AnchorPane.setTopAnchor(minimizeRect, 6.3);
		AnchorPane.setRightAnchor(maximizeRect, 42.5);
		AnchorPane.setTopAnchor(maximizeRect, 6.3);
		
		titleBar.getChildren().addAll(placeholderImageView, closeRect, maximizeRect, minimizeRect, closeImageView,
		                              maximizeImageView, minimizeImageView);
		Platform.runLater(() -> {
			Stage stage1 = (Stage) titleBar.getScene().getWindow();
			ResizeHelper.addResizeListener(stage1);
		});
		closeRect.toFront();
		minimizeRect.toFront();
		maximizeRect.toFront();
		return titleBar;
	}
	
	public static AnchorPane createSimpleTitleBar(String titleText, boolean resizable) {
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setSaturation(-1.0);
		colorAdjust.setBrightness(-0.45);
		
		Label titleLabel = new Label(titleText);
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
		titleLabel.setAlignment(Pos.CENTER);
		AnchorPane.setLeftAnchor(titleLabel, (double) 0);
		AnchorPane.setRightAnchor(titleLabel, (double) 0);
		AnchorPane.setTopAnchor(titleLabel, (double) 0);
		AnchorPane.setBottomAnchor(titleLabel, (double) 0);
		titleLabel.setEffect(colorAdjust);
		titleLabel.setMouseTransparent(true);
		
		AnchorPane titleBar = new AnchorPane(titleLabel);
		titleBar.setMinHeight(30);
		titleBar.setStyle("-fx-background-color: #383838;");
		
		Image placeholderImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"));
		ImageView placeholderImageView = new ImageView(placeholderImage);
		placeholderImageView.setFitWidth(49);
		placeholderImageView.setFitHeight(49);
		AnchorPane.setLeftAnchor(placeholderImageView, 0.0);
		AnchorPane.setTopAnchor(placeholderImageView, -10.0);
		AnchorPane.setBottomAnchor(placeholderImageView, -10.0);
		placeholderImageView.setEffect(colorAdjust);
		
		Image closeImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/cross.png"));
		ImageView closeImageView = new ImageView(closeImage);
		closeImageView.setFitWidth(15);
		closeImageView.setFitHeight(15);
		AnchorPane.setRightAnchor(closeImageView, 15.0);
		AnchorPane.setTopAnchor(closeImageView, 7.0);
		closeImageView.setEffect(colorAdjust);
		
		Image minimizeImage = new Image(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/minimize.png"));
		ImageView minimizeImageView = new ImageView(minimizeImage);
		minimizeImageView.setFitWidth(15);
		minimizeImageView.setFitHeight(15);
		AnchorPane.setRightAnchor(minimizeImageView, 42.5);
		AnchorPane.setTopAnchor(minimizeImageView, 7.0);
		minimizeImageView.setEffect(colorAdjust);
		
		Rectangle closeRect = new Rectangle(20, 20);
		Rectangle minimizeRect = new Rectangle(20, 20);
		
		closeRect.setFill(Color.TRANSPARENT);
		minimizeRect.setFill(Color.TRANSPARENT);
		
		closeRect.setOnMouseClicked(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			stage.close();
		});
		
		minimizeRect.setOnMouseClicked(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			stage.setIconified(true);
		});
		
		AnchorPane.setRightAnchor(closeRect, 12.5);
		AnchorPane.setTopAnchor(closeRect, 6.3);
		AnchorPane.setRightAnchor(minimizeRect, 42.5);
		AnchorPane.setTopAnchor(minimizeRect, 6.3);
		
		titleBar.getChildren().addAll(placeholderImageView, closeRect, minimizeRect, closeImageView, minimizeImageView);
		closeRect.toFront();
		minimizeRect.toFront();
		
		titleBar.setOnMouseDragged(event -> {
			Stage stage = (Stage) titleBar.getScene().getWindow();
			stage.setX(event.getScreenX() - xOffset);
			stage.setY(event.getScreenY() - yOffset);
		});
		
		titleBar.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		
		if (resizable) {
			Platform.runLater(() -> {
				Stage stage1 = (Stage) titleBar.getScene().getWindow();
				ResizeHelper.addResizeListener(stage1);
			});
		}
		
		return titleBar;
	}
	
	public static Map<String, Object> createReportWindow(String reportName, int numWidthUnits, int numHeightUnits, nestedReportUtils.TransferConfig transferConfig, nestedReportUtils.SectionConfig... sectionConfigs) {
		String placeholder;
		try {
			placeholder = ConfigReader.configRead("reportSettings", "reportHeading");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Screen screen = Screen.getPrimary();
		double screenWidth = screen.getVisualBounds().getWidth();
		double screenHeight = screen.getVisualBounds().getHeight();
		
		double preferredWidth = screenWidth / 12 * numWidthUnits;
		double preferredHeight = screenHeight / 12 * numHeightUnits;
		
		BorderPane borderPane = new BorderPane();
		borderPane.setStyle("-fx-border-color: black; -fx-border-width: 1.5;");
		
		AnchorPane titleBar = createTitleBar("Report Manager");
		
		borderPane.setTop(titleBar);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		Label warningLabel = new Label("Please fill out the form");
		warningLabel.setVisible(false);
		warningLabel.setStyle(
				"-fx-text-fill: red; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14;");
		
		for (int i = 0; i < 12; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(100 / 12.0);
			gridPane.getColumnConstraints().add(column);
		}
		
		Label mainHeaderLabel = new Label("New " + reportName);
		mainHeaderLabel.setStyle(
				"-fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: " + placeholder + "; -fx-font-family: Segoe UI Black;");
		mainHeaderLabel.setAlignment(Pos.CENTER);
		GridPane.setColumnSpan(mainHeaderLabel, 12);
		gridPane.add(mainHeaderLabel, 0, 0);
		
		Map<String, Object> fieldsMap = new HashMap<>();
		int rowIndex = 1;
		
		for (nestedReportUtils.SectionConfig sectionConfig : sectionConfigs) {
			
			Label sectionLabel = new Label(sectionConfig.getSectionTitle());
			sectionLabel.setFont(Font.font("Segoe UI Black"));
			sectionLabel.setStyle(
					"-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + placeholder + "; -fx-background-color: transparent; -fx-padding: 0px 40px;");
			gridPane.add(sectionLabel, 0, rowIndex, 12, 1);
			rowIndex++;
			
			for (nestedReportUtils.RowConfig rowConfig : sectionConfig.getRowConfigs()) {
				addRowToGridPane(gridPane, rowConfig, rowIndex, fieldsMap);
				rowIndex++;
			}
			
			if (sectionConfig != sectionConfigs[sectionConfigs.length - 1]) {
				Separator separator = new Separator();
				separator.setMaxWidth(Double.MAX_VALUE);
				
				StackPane separatorPane = new StackPane(separator);
				separatorPane.setPadding(new Insets(20, 0, 0, 0));
				
				GridPane.setColumnSpan(separatorPane, 12);
				gridPane.add(separatorPane, 0, rowIndex);
				rowIndex++;
			}
			
			if (sectionConfig.getSectionTitle().equals("Citation Treeview")) {
				rowIndex += 5;
			} else {
				rowIndex += 2;
			}
		}
		
		Button submitBtn = new Button("Collect Values");
		submitBtn.getStyleClass().add("incidentformButton");
		submitBtn.setStyle("-fx-padding: 15;");
		submitBtn.setStyle("-fx-background-color: " + getPrimaryColor());
		submitBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				submitBtn.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
			} else {
				submitBtn.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
			}
		});
		
		MenuButton pullNotesBtn = new MenuButton("Pull From Notes");
		pullNotesBtn.setMinWidth(Region.USE_PREF_SIZE);
		pullNotesBtn.getStyleClass().add("incidentformButton");
		pullNotesBtn.setStyle("-fx-padding: 15;");
		pullNotesBtn.setStyle("-fx-background-color: " + getPrimaryColor());
		pullNotesBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				pullNotesBtn.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
			} else {
				pullNotesBtn.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
			}
		});
		
		Button delBtn = new Button("Delete Report");
		delBtn.setVisible(false);
		delBtn.setDisable(true);
		delBtn.getStyleClass().add("incidentformButton");
		delBtn.setStyle("-fx-padding: 15; -fx-border-color:red; -fx-border-width: 1;");
		delBtn.setStyle("-fx-background-color: " + getPrimaryColor() + "; -fx-border-color:red; -fx-border-width: 1;");
		delBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				delBtn.setStyle(
						"-fx-background-color: " + getSecondaryColor() + "; -fx-border-color:red; -fx-border-width: 1;");
			} else {
				delBtn.setStyle(
						"-fx-background-color: " + getPrimaryColor() + "; -fx-border-color:red; -fx-border-width: 1;");
			}
		});
		
		HBox buttonBox = new HBox(10, delBtn, pullNotesBtn, warningLabel, submitBtn);
		buttonBox.setAlignment(Pos.BASELINE_RIGHT);
		VBox root = new VBox(10, mainHeaderLabel, gridPane);
		
		if (transferConfig != null) {
			TitledPane titledPane = new TitledPane();
			titledPane.setExpanded(false);
			titledPane.setText(transferConfig.getTitle());
			titledPane.getStyleClass().add("paneoptions");
			
			GridPane paneGrid = new GridPane();
			paneGrid.setHgap(10);
			paneGrid.setVgap(10);
			
			paneGrid.getColumnConstraints().clear();
			for (int i = 0; i < 12; i++) {
				ColumnConstraints columnConstraints = new ColumnConstraints();
				columnConstraints.setPercentWidth(100.0 / 12);
				paneGrid.getColumnConstraints().add(columnConstraints);
			}
			
			int rowIndex1 = 0;
			for (nestedReportUtils.RowConfig rowConfig : transferConfig.getRowConfigs()) {
				addRowToGridPane(paneGrid, rowConfig, rowIndex1++, fieldsMap);
			}
			
			titledPane.setContent(paneGrid);
			
			Pane spacerPane1 = new Pane();
			spacerPane1.setMinHeight(20);
			spacerPane1.setPrefHeight(20);
			Pane spacerPane2 = new Pane();
			spacerPane2.setMinHeight(20);
			spacerPane1.setPrefHeight(20);
			
			Accordion accordion = new Accordion();
			accordion.setStyle("-fx-box-border: transparent;");
			accordion.getPanes().add(titledPane);
			accordion.setMaxWidth(Double.MAX_VALUE);
			accordion.setMinHeight(Region.USE_PREF_SIZE);
			accordion.setPrefHeight(Region.USE_COMPUTED_SIZE);
			accordion.setMaxHeight(Region.USE_PREF_SIZE);
			
			paneGrid.setStyle(
					"-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");
			accordion.setStyle(
					"-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");
			titledPane.setStyle(
					"-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");
			
			root.getChildren().addAll(spacerPane1, titledPane, spacerPane2);
		}
		
		root.getChildren().add(buttonBox);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: " + getAccentColor() + ";");
		Insets insets = new Insets(20, 25, 15, 25);
		root.setPadding(insets);
		
		ScrollPane scrollPane = new ScrollPane(root);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		borderPane.setCenter(scrollPane);
		
		Stage stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		
		Scene scene = new Scene(borderPane);
		
		try {
			if (ConfigReader.configRead("reportSettings", "reportWindowDarkMode").equals("true")) {
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/light/formFields.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/light/formTextArea.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/light/formButton.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/light/formComboBox.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/light/Logscrollpane.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/light/tableCss.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/light/formTitledPane.css").toExternalForm());
			} else {
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/dark/formFields.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/dark/formTextArea.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/dark/formButton.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/dark/formComboBox.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/dark/Logscrollpane.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/dark/tableCss.css").toExternalForm());
				scene.getStylesheets().add(Launcher.class.getResource(
						"/com/drozal/dataterminal/css/form/dark/formTitledPane.css").toExternalForm());
			}
		} catch (IOException e) {
			logError("Could not add stylesheets to reports: ", e);
		}
		
		scrollPane.getStyleClass().add("formPane");
		scrollPane.setStyle(
				"-fx-background-color: " + getAccentColor() + "; " + "-fx-focus-color: " + getAccentColor() + ";");
		
		stage.setScene(scene);
		stage.setTitle(reportName);
		
		titleBar.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				toggleWindowedFullscreen(stage, preferredWidth, preferredHeight);
			}
		});
		
		stage.setOnHidden(event -> {
			windowX = stage.getX();
			windowY = stage.getY();
			windowWidth = stage.getWidth();
			windowHeight = stage.getHeight();
		});
		
		Map<String, Object> result = new HashMap<>();
		result.put(reportName + " Map", fieldsMap);
		result.put("delBtn", delBtn);
		result.put("pullNotesBtn", pullNotesBtn);
		result.put("warningLabel", warningLabel);
		result.put("submitBtn", submitBtn);
		result.put("root", borderPane);
		
		stage.setAlwaysOnTop(true); //todo remove
		
		stage.setWidth(preferredWidth);
		stage.setHeight(preferredHeight);
		centerStageOnMainApp(stage);
		stage.show();
		
		return result;
	}
	
	private static void addRowToGridPane(GridPane gridPane, nestedReportUtils.RowConfig rowConfig, int rowIndex, Map<String, Object> fieldsMap) {
		String placeholder;
		try {
			if (ConfigReader.configRead("reportSettings", "reportWindowDarkMode").equals("true")) {
				placeholder = "black";
			} else {
				placeholder = "white";
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		int totalSize = 0;
		int columnIndex = 0;
		for (nestedReportUtils.FieldConfig fieldConfig : rowConfig.getFieldConfigs()) {
			if (fieldConfig.getSize() <= 0 || totalSize + fieldConfig.getSize() > 12) {
				log(fieldConfig.getFieldName(), LogUtils.Severity.ERROR);
				log(String.valueOf(fieldConfig.getSize()), LogUtils.Severity.ERROR);
				throw new IllegalArgumentException("Invalid field size configuration");
			}
			totalSize += fieldConfig.getSize();
			switch (fieldConfig.getFieldType()) {
				case TEXT_FIELD:
					TextField textField = new TextField();
					textField.getStyleClass().add("formField3");
					textField.setStyle("-fx-background-color: " + getPrimaryColor());
					textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							textField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							textField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					textField.textProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue != null) {
							textField.setText(newValue.toUpperCase());
						}
					});
					textField.setPromptText(fieldConfig.getFieldName().toUpperCase());
					textField.setPrefWidth(200);
					gridPane.add(textField, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), textField);
					break;
				case TEXT_AREA:
					TextArea textArea = new TextArea();
					textArea.setWrapText(true);
					textArea.getStyleClass().add("othertextarea");
					textArea.setStyle("-fx-background-color: " + getPrimaryColor());
					textArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							textArea.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							textArea.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					textArea.setPromptText(fieldConfig.getFieldName().toUpperCase());
					textArea.setPrefRowCount(5);
					textArea.setMaxWidth(Double.MAX_VALUE);
					textArea.setMinHeight(150);
					textArea.setPrefHeight(150);
					textArea.setMaxHeight(Region.USE_COMPUTED_SIZE);
					gridPane.add(textArea, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), textArea);
					
					GridPane.setHgrow(textArea, Priority.ALWAYS);
					GridPane.setVgrow(textArea, Priority.ALWAYS);
					break;
				case COMBO_BOX_AREA:
					ComboBox<String> comboBoxArea = new ComboBox<>();
					new AutoCompleteComboBoxListener<>(comboBoxArea);
					comboBoxArea.getStyleClass().add("comboboxnew");
					comboBoxArea.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
					comboBoxArea.getEditor().setStyle("-fx-background-color: " + getPrimaryColor() + ";");
					comboBoxArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							comboBoxArea.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							comboBoxArea.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					comboBoxArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							comboBoxArea.getEditor().setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							comboBoxArea.getEditor().setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					comboBoxArea.getItems().addAll(dropdownInfo.areaList);
					comboBoxArea.setPromptText(fieldConfig.getFieldName().toUpperCase());
					comboBoxArea.setButtonCell(new ListCell() {
						@Override
						protected void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
							} else {
								setStyle("-fx-text-fill: " + placeholder + ";");
								comboBoxArea.getEditor().setStyle("-fx-text-fill: " + placeholder + ";");
								setText(item.toString());
							}
						}
					});
					comboBoxArea.setMaxWidth(Double.MAX_VALUE);
					gridPane.add(comboBoxArea, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), comboBoxArea);
					break;
				case COMBO_BOX_STREET:
					ComboBox<String> comboBoxStreet = new ComboBox<>();
					new AutoCompleteComboBoxListener<>(comboBoxStreet);
					comboBoxStreet.getStyleClass().add("comboboxnew");
					comboBoxStreet.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
					comboBoxStreet.getEditor().setStyle("-fx-background-color: " + getPrimaryColor() + ";");
					comboBoxStreet.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							comboBoxStreet.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							comboBoxStreet.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					comboBoxStreet.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							comboBoxStreet.getEditor().setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							comboBoxStreet.getEditor().setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					comboBoxStreet.getItems().addAll(dropdownInfo.streetList);
					comboBoxStreet.setPromptText(fieldConfig.getFieldName().toUpperCase());
					comboBoxStreet.setButtonCell(new ListCell() {
						@Override
						protected void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
							} else {
								setStyle("-fx-text-fill: " + placeholder + ";");
								comboBoxStreet.getEditor().setStyle("-fx-text-fill: " + placeholder + ";");
								setText(item.toString());
							}
						}
					});
					comboBoxStreet.setMaxWidth(Double.MAX_VALUE);
					gridPane.add(comboBoxStreet, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), comboBoxStreet);
					break;
				case COMBO_BOX_COLOR:
					ComboBox<String> comboBoxColor = new ComboBox<>();
					comboBoxColor.getStyleClass().add("comboboxnew");
					comboBoxColor.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
					comboBoxColor.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							comboBoxColor.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							comboBoxColor.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					comboBoxColor.getItems().addAll(dropdownInfo.carColors);
					comboBoxColor.setPromptText(fieldConfig.getFieldName().toUpperCase());
					comboBoxColor.setButtonCell(new ListCell() {
						@Override
						protected void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
							} else {
								setStyle("-fx-text-fill: " + placeholder + ";");
								setText(item.toString());
							}
						}
					});
					comboBoxColor.setMaxWidth(Double.MAX_VALUE);
					gridPane.add(comboBoxColor, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), comboBoxColor);
					break;
				case COMBO_BOX_TYPE:
					ComboBox<String> comboBoxType = new ComboBox<>();
					comboBoxType.getStyleClass().add("comboboxnew");
					comboBoxType.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
					comboBoxType.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							comboBoxType.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							comboBoxType.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					comboBoxType.getItems().addAll(dropdownInfo.vehicleTypes);
					comboBoxType.setPromptText(fieldConfig.getFieldName().toUpperCase());
					comboBoxType.setButtonCell(new ListCell() {
						
						@Override
						protected void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
							} else {
								setStyle("-fx-text-fill: " + placeholder + ";");
								setText(item.toString());
							}
						}
						
					});
					comboBoxType.setMaxWidth(Double.MAX_VALUE);
					gridPane.add(comboBoxType, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), comboBoxType);
					break;
				case COMBO_BOX_SEARCH_TYPE:
					ComboBox<String> comboBoxSearchType = new ComboBox<>();
					comboBoxSearchType.getStyleClass().add("comboboxnew");
					comboBoxSearchType.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
					comboBoxSearchType.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							comboBoxSearchType.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							comboBoxSearchType.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					comboBoxSearchType.getItems().addAll(dropdownInfo.searchTypes);
					comboBoxSearchType.setPromptText(fieldConfig.getFieldName().toUpperCase());
					comboBoxSearchType.setButtonCell(new ListCell() {
						
						@Override
						protected void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
							} else {
								setStyle("-fx-text-fill: " + placeholder + ";");
								setText(item.toString());
							}
						}
						
					});
					comboBoxSearchType.setMaxWidth(Double.MAX_VALUE);
					gridPane.add(comboBoxSearchType, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), comboBoxSearchType);
					break;
				case COMBO_BOX_SEARCH_METHOD:
					ComboBox<String> comboBoxSearchMethod = new ComboBox<>();
					comboBoxSearchMethod.getStyleClass().add("comboboxnew");
					comboBoxSearchMethod.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
					comboBoxSearchMethod.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							comboBoxSearchMethod.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							comboBoxSearchMethod.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					comboBoxSearchMethod.getItems().addAll(dropdownInfo.searchMethods);
					comboBoxSearchMethod.setPromptText(fieldConfig.getFieldName().toUpperCase());
					comboBoxSearchMethod.setButtonCell(new ListCell() {
						
						@Override
						protected void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
							} else {
								setStyle("-fx-text-fill: " + placeholder + ";");
								setText(item.toString());
							}
						}
						
					});
					comboBoxSearchMethod.setMaxWidth(Double.MAX_VALUE);
					gridPane.add(comboBoxSearchMethod, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), comboBoxSearchMethod);
					break;
				case CITATION_TREE_VIEW:
					TreeView<String> treeView = new TreeView<>();
					treeView.setPrefHeight(350);
					treeView.setMinHeight(350);
					treeView.setMaxHeight(350);
					
					File file = new File(getJarPath() + "/data/Citations.xml");
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					Document document = null;
					try {
						document = factory.newDocumentBuilder().parse(file);
					} catch (SAXException | IOException | ParserConfigurationException e) {
						throw new RuntimeException(e);
					}
					
					Element root = document.getDocumentElement();
					
					TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());
					
					parseTreeXML(root, rootItem);
					treeView.setRoot(rootItem);
					expandTreeItem(rootItem);
					
					TextField citationNameField = new TextField();
					citationNameField.setPromptText("Citation Name");
					TextField citationFineField = new TextField();
					citationFineField.setPromptText("Citation Maximum Fine");
					
					Button addButton = new Button("Add");
					Button removeButton = new Button("Remove");
					
					Label citationInfoLabel = new Label("Citation Information");
					citationInfoLabel.setAlignment(Pos.CENTER);
					
					TableView<CitationsData> citationTableView = new TableView<>();
					citationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
					citationTableView.getStyleClass().add("calloutTABLE");
					
					TableColumn<CitationsData, String> citationColumn = new TableColumn<>("Citation");
					citationColumn.setCellValueFactory(new PropertyValueFactory<>("citation"));
					citationTableView.setTableMenuButtonVisible(false);
					
					citationTableView.getColumns().add(citationColumn);
					gridPane.add(treeView, columnIndex, rowIndex, fieldConfig.getSize(), 5);
					
					int additionalColumnIndex = columnIndex + fieldConfig.getSize();
					
					int remainingColumns = gridPane.getColumnCount() - additionalColumnIndex;
					
					gridPane.add(citationInfoLabel, additionalColumnIndex, rowIndex, remainingColumns, 1);
					GridPane.setHalignment(citationInfoLabel, HPos.CENTER);
					gridPane.add(citationNameField, additionalColumnIndex, rowIndex + 1, remainingColumns, 1);
					gridPane.add(citationFineField, additionalColumnIndex, rowIndex + 2, remainingColumns, 1);
					
					HBox buttonBox = new HBox(40, addButton, removeButton);
					buttonBox.setAlignment(Pos.CENTER);
					gridPane.add(buttonBox, additionalColumnIndex, rowIndex + 3, remainingColumns, 1);
					
					gridPane.add(citationTableView, additionalColumnIndex, rowIndex + 4, remainingColumns, 1);
					
					fieldsMap.put("CitationNameField", citationNameField);
					fieldsMap.put("CitationFineField", citationFineField);
					fieldsMap.put("AddButton", addButton);
					fieldsMap.put("RemoveButton", removeButton);
					fieldsMap.put("CitationTableView", citationTableView);
					fieldsMap.put(fieldConfig.getFieldName(), treeView);
					
					citationInfoLabel.setStyle(
							"-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + placeholder + "; -fx-background-color: transparent; -fx-padding: 0px 40px;");
					citationInfoLabel.setFont(Font.font("Segoe UI Black"));
					addButton.getStyleClass().add("incidentformButton");
					addButton.setStyle("-fx-padding: 15;");
					addButton.setStyle("-fx-background-color: " + getPrimaryColor());
					addButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							addButton.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							addButton.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					removeButton.getStyleClass().add("incidentformButton");
					removeButton.setStyle("-fx-padding: 15;");
					removeButton.setStyle("-fx-background-color: " + getPrimaryColor());
					removeButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							removeButton.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							removeButton.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					citationTableView.setStyle("-fx-background-color: " + getPrimaryColor());
					citationTableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							citationTableView.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							citationTableView.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					citationNameField.getStyleClass().add("formField3");
					citationNameField.setStyle("-fx-background-color: " + getPrimaryColor());
					citationNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							citationNameField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							citationNameField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					citationFineField.getStyleClass().add("formField3");
					citationFineField.setStyle("-fx-background-color: " + getPrimaryColor());
					citationFineField.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							citationFineField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							citationFineField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					treeView.setOnMouseClicked(event -> {
						TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
						if (selectedItem != null && selectedItem.isLeaf()) {
							citationNameField.setText(selectedItem.getValue());
							citationFineField.setText(
									findXMLValue(selectedItem.getValue(), "fine", "data/Citations.xml"));
						} else {
							citationNameField.setText("");
							citationFineField.setText("");
						}
					});
					addButton.setOnMouseClicked(event -> {
						String citation = citationNameField.getText();
						if (!(citation.isBlank() || citation.isEmpty())) {
							CitationsData formData = null;
							
							String fine = findXMLValue(citation, "fine", "data/Citations.xml");
							if (fine != null) {
								formData = new CitationsData(citation);
							} else {
								log("Added Ciation via Custom Citation Value: " + citation + " fine: " + citationFineField.getText(),
								    LogUtils.Severity.DEBUG);
								formData = new CitationsData(citation + " MaxFine:" + citationFineField.getText());
							}
							
							citationTableView.getItems().add(formData);
						}
					});
					removeButton.setOnMouseClicked(event -> {
						CitationsData selectedItem = citationTableView.getSelectionModel().getSelectedItem();
						if (selectedItem != null) {
							citationTableView.getItems().remove(selectedItem);
						}
					});
					rowIndex += 6;
					break;
				case TRANSFER_BUTTON:
					Button transferButton = new Button("Transfer Information");
					transferButton.setMaxWidth(Double.MAX_VALUE);
					GridPane.setHgrow(transferButton, Priority.ALWAYS);
					GridPane.setVgrow(transferButton, Priority.NEVER);
					
					transferButton.setMinHeight(Button.USE_PREF_SIZE);
					transferButton.setPrefHeight(Button.USE_COMPUTED_SIZE);
					transferButton.setMaxHeight(Button.USE_PREF_SIZE);
					
					transferButton.getStyleClass().add("incidentformButton");
					transferButton.setStyle("-fx-background-color: " + getAccentColor());
					transferButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							transferButton.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						} else {
							transferButton.setStyle("-fx-background-color: " + getAccentColor() + ";");
						}
					});
					gridPane.add(transferButton, columnIndex, rowIndex, fieldConfig.getSize(), 1);
					fieldsMap.put(fieldConfig.getFieldName(), transferButton);
					break;
				case CHARGES_TREE_VIEW:
					TreeView<String> chargestreeView = new TreeView<>();
					chargestreeView.setPrefHeight(350);
					chargestreeView.setMinHeight(350);
					chargestreeView.setMaxHeight(350);
					
					File file2 = new File(getJarPath() + "/data/Charges.xml");
					DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
					Document document2 = null;
					try {
						document2 = factory2.newDocumentBuilder().parse(file2);
					} catch (SAXException | IOException | ParserConfigurationException e) {
						throw new RuntimeException(e);
					}
					
					Element root2 = document2.getDocumentElement();
					
					TreeItem<String> rootItem2 = new TreeItem<>(root2.getNodeName());
					
					parseTreeXML(root2, rootItem2);
					chargestreeView.setRoot(rootItem2);
					expandTreeItem(rootItem2);
					
					TextField chargeNameField = new TextField();
					chargeNameField.setEditable(false);
					chargeNameField.setPromptText("Charge Name");
					
					Button addButton2 = new Button("Add");
					Button removeButton2 = new Button("Remove");
					
					Label chargeInfoLabel = new Label("Charge Information");
					chargeInfoLabel.setAlignment(Pos.CENTER);
					
					TableView<ChargesData> chargeTableView = new TableView<>();
					chargeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
					chargeTableView.getStyleClass().add("calloutTABLE");
					
					TableColumn<ChargesData, String> chargeColumn = new TableColumn<>("Charge");
					chargeColumn.setCellValueFactory(new PropertyValueFactory<>("charge"));
					chargeTableView.setTableMenuButtonVisible(false);
					
					chargeTableView.getColumns().add(chargeColumn);
					gridPane.add(chargestreeView, columnIndex, rowIndex, fieldConfig.getSize(), 5);
					
					int additionalColumnIndex2 = columnIndex + fieldConfig.getSize();
					
					int remainingColumns2 = gridPane.getColumnCount() - additionalColumnIndex2;
					
					gridPane.add(chargeInfoLabel, additionalColumnIndex2, rowIndex, remainingColumns2, 1);
					GridPane.setHalignment(chargeInfoLabel, HPos.CENTER);
					gridPane.add(chargeNameField, additionalColumnIndex2, rowIndex + 1, remainingColumns2, 1);
					
					HBox buttonBox2 = new HBox(40, addButton2, removeButton2);
					buttonBox2.setAlignment(Pos.CENTER);
					gridPane.add(buttonBox2, additionalColumnIndex2, rowIndex + 3, remainingColumns2, 1);
					
					gridPane.add(chargeTableView, additionalColumnIndex2, rowIndex + 4, remainingColumns2, 1);
					
					fieldsMap.put("ChargeNameField", chargeNameField);
					fieldsMap.put("AddButton", addButton2);
					fieldsMap.put("RemoveButton", removeButton2);
					fieldsMap.put("ChargeTableView", chargeTableView);
					fieldsMap.put(fieldConfig.getFieldName(), chargestreeView);
					
					chargeInfoLabel.setStyle(
							"-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + placeholder + "; -fx-background-color: transparent; -fx-padding: 0px 40px;");
					chargeInfoLabel.setFont(Font.font("Segoe UI Black"));
					addButton2.getStyleClass().add("incidentformButton");
					addButton2.setStyle("-fx-padding: 15;");
					addButton2.setStyle("-fx-background-color: " + getPrimaryColor());
					addButton2.hoverProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							addButton2.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							addButton2.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					removeButton2.getStyleClass().add("incidentformButton");
					removeButton2.setStyle("-fx-padding: 15;");
					removeButton2.setStyle("-fx-background-color: " + getPrimaryColor());
					removeButton2.hoverProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							removeButton2.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							removeButton2.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					chargeTableView.setStyle("-fx-background-color: " + getPrimaryColor());
					chargeTableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							chargeTableView.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							chargeTableView.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					chargeNameField.getStyleClass().add("formField3");
					chargeNameField.setStyle("-fx-background-color: " + getPrimaryColor());
					chargeNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							chargeNameField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
						} else {
							chargeNameField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
						}
					});
					chargestreeView.setOnMouseClicked(event -> {
						TreeItem<String> selectedItem = chargestreeView.getSelectionModel().getSelectedItem();
						if (selectedItem != null && selectedItem.isLeaf()) {
							chargeNameField.setText(selectedItem.getValue());
						} else {
							chargeNameField.setText("");
						}
					});
					addButton2.setOnMouseClicked(event -> {
						String charge = chargeNameField.getText();
						if (!(charge.isBlank() || charge.isEmpty())) {
							ChargesData formData = new ChargesData(charge);
							chargeTableView.getItems().add(formData);
						}
					});
					removeButton2.setOnMouseClicked(event -> {
						ChargesData selectedItem = chargeTableView.getSelectionModel().getSelectedItem();
						if (selectedItem != null) {
							chargeTableView.getItems().remove(selectedItem);
						}
					});
					rowIndex += 6;
					break;
				
			}
			columnIndex += fieldConfig.getSize();
			
		}
	}
	
	public static String extractMaxFine(String citation) {
		if (citation == null) {
			citation = "";
		}
		Pattern pattern = Pattern.compile("MaxFine:(\\S+)");
		Matcher matcher = pattern.matcher(citation);
		
		if (matcher.find()) {
			return matcher.group(1);
		}
		
		return null;
	}
	
	public static String generateReportNumber() {
		int num_length = 7;
		StringBuilder DeathReportNumber = new StringBuilder();
		for (int i = 0; i < num_length; i++) {
			SecureRandom RANDOM = new SecureRandom();
			int digit = RANDOM.nextInt(10);
			DeathReportNumber.append(digit);
		}
		return DeathReportNumber.toString();
	}
}
