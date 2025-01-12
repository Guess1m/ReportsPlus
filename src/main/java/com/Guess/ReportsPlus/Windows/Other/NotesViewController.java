package com.Guess.ReportsPlus.Windows.Other;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.NoteTab;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

public class NotesViewController {
	
	public static List<NoteTab> notesTabList;
	public static String notesText;
	public static NotesViewController notesViewController;
	public static CustomWindow codesWindow;
	@javafx.fxml.FXML
	private TextArea notepadTextArea;
	@javafx.fxml.FXML
	private Button clearbtnnotepad;
	@javafx.fxml.FXML
	private BorderPane borderPane;
	@javafx.fxml.FXML
	private ToggleButton modeToggle;
	@javafx.fxml.FXML
	private Button codesbtnnotepad;
	@javafx.fxml.FXML
	private AnchorPane codeSelectionPane;
	@javafx.fxml.FXML
	private VBox codevbox;
	@javafx.fxml.FXML
	private Label codeSelectionlbl;
	@javafx.fxml.FXML
	private AnchorPane defaultNotesPane;
	@javafx.fxml.FXML
	private TabPane tabPane;
	
	public static void createNoteTabs() throws IOException {
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor") + ";";
		String initialStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";";
		String notepadMode = ConfigReader.configRead("notepad", "notepadMode");
		
		String padding = " -fx-padding: 2 7 2 7;";
		
		notesViewController.getTabPane().getTabs().removeIf(tab -> !tab.getText().equalsIgnoreCase("default"));
		
		for (NoteTab note : notesTabList) {
			
			Tab newTab = new Tab(note.getTabName());
			
			FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Templates/notes-template.fxml"));
			Parent anchorPaneParent;
			try {
				anchorPaneParent = loader.load();
			} catch (IOException e) {
				logError("Could not load note template", e);
				continue;
			}
			
			AnchorPane anchorPane = (AnchorPane) anchorPaneParent;
			
			newTab.setContent(anchorPane);
			
			notesViewController.getTabPane().getTabs().add(newTab);
			
			TextArea noteArea = (TextArea) anchorPane.lookup("#notepadTextArea");
			Button clrBtn = (Button) anchorPane.lookup("#clearbtnnotepad");
			ToggleButton modeToggle = (ToggleButton) anchorPane.lookup("#modeToggle");
			Button codesbtnnotepad = (Button) anchorPane.lookup("#codesbtnnotepad");
			
			newTab.setOnClosed(event -> notesTabList.remove(note));
			
			if (noteArea != null) {
				if (note.getNoteText() != null && !note.getNoteText().isEmpty()) {
					noteArea.setText(note.getNoteText());
				}
				
				noteArea.textProperty().addListener((observable, oldValue, newValue) -> {
					note.setNoteText(newValue);
				});
				
				clrBtn.setStyle(initialStyle + padding);
				clrBtn.setOnMouseEntered(e -> clrBtn.setStyle(hoverStyle + padding));
				clrBtn.setOnMouseExited(e -> clrBtn.setStyle(initialStyle + padding));
				
				codesbtnnotepad.setStyle(initialStyle + padding);
				codesbtnnotepad.setOnMouseEntered(e -> codesbtnnotepad.setStyle(hoverStyle + padding));
				codesbtnnotepad.setOnMouseExited(e -> codesbtnnotepad.setStyle(initialStyle + padding));
				if (notepadMode != null) {
					String notepadStyle;
					String modeToggleStyle;
					if (notepadMode.equals("Dark")) {
						modeToggle.setSelected(true);
						notepadStyle = "-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
						modeToggleStyle = "-fx-background-color: white;";
						noteArea.setStyle(notepadStyle);
						modeToggle.setStyle(modeToggleStyle);
					} else if (notepadMode.equals("Light")) {
						modeToggle.setSelected(false);
						notepadStyle = "-fx-background-color: rgb(0,0,0,0.1); -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
						modeToggleStyle = "-fx-background-color: grey;";
						noteArea.setStyle(notepadStyle);
						modeToggle.setStyle(modeToggleStyle);
					}
				} else {
					noteArea.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
					modeToggle.setStyle("-fx-background-color: grey;");
				}
			}
			if (clrBtn != null) {
				clrBtn.setText(localization.getLocalizedMessage("NotesWindow.ClearNotesButton", "Clear"));
				clrBtn.setOnAction(actionEvent -> {
					if (noteArea != null) {
						noteArea.setText("");
					}
				});
			}
			if (modeToggle != null) {
				modeToggle.setOnAction(actionEvent -> {
					try {
						if (ConfigReader.configRead("notepad", "notepadMode").equals("Light")) {
							ConfigWriter.configwrite("notepad", "notepadMode", "Dark");
							if (noteArea != null) {
								noteArea.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent;-fx-background-radius: 0; -fx-border-radius: 0;");
							}
							modeToggle.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-background-radius: 0; -fx-border-radius: 0;");
							notesViewController.getCodeSelectionPane().setStyle("-fx-background-color: gray;");
							notesViewController.getCodevbox().setStyle("-fx-background-color: rgb(200,200,200,1);");
							notesViewController.getBorderPane().setStyle("-fx-background-color: gray;");
							notesViewController.getCodeSelectionlbl().setStyle("-fx-text-fill: #e2e2e2;");
						} else {
							if (ConfigReader.configRead("notepad", "notepadMode").equals("Dark")) {
								ConfigWriter.configwrite("notepad", "notepadMode", "Light");
								if (noteArea != null) {
									noteArea.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
								}
								modeToggle.setStyle("-fx-background-color: grey;");
								notesViewController.getCodeSelectionPane().setStyle("-fx-background-color: rgb(240,240,240,0.1);");
								notesViewController.getCodevbox().setStyle("-fx-background-color: rgb(210,210,210,0.3);");
								notesViewController.getBorderPane().setStyle("-fx-background-color: white;");
								notesViewController.getCodeSelectionlbl().setStyle("-fx-text-fill: gray;");
							}
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			}
			if (codesbtnnotepad != null) {
				codesbtnnotepad.setText(localization.getLocalizedMessage("NotesWindow.CodesButton", "Codes"));
				codesbtnnotepad.setOnAction(actionEvent -> {
					if (notesViewController.getCodeSelectionPane().isVisible()) {
						
						double toWidth = 0;
						
						Timeline timeline = new Timeline();
						
						KeyValue keyValuePrefHeight = new KeyValue(notesViewController.getCodeSelectionPane().prefWidthProperty(), toWidth);
						KeyValue keyValueMaxHeight = new KeyValue(notesViewController.getCodeSelectionPane().maxWidthProperty(), toWidth);
						KeyValue keyValueMinHeight = new KeyValue(notesViewController.getCodeSelectionPane().minWidthProperty(), toWidth);
						KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValuePrefHeight, keyValueMaxHeight, keyValueMinHeight);
						
						timeline.getKeyFrames().add(keyFrame);
						
						timeline.play();
						notesViewController.getCodeSelectionPane().setVisible(false);
					} else {
						
						double toWidth = 200;
						
						Timeline timeline = new Timeline();
						
						KeyValue keyValuePrefHeight = new KeyValue(notesViewController.getCodeSelectionPane().prefWidthProperty(), toWidth);
						KeyValue keyValueMaxHeight = new KeyValue(notesViewController.getCodeSelectionPane().maxWidthProperty(), toWidth);
						KeyValue keyValueMinHeight = new KeyValue(notesViewController.getCodeSelectionPane().minWidthProperty(), toWidth);
						KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValuePrefHeight, keyValueMaxHeight, keyValueMinHeight);
						
						timeline.getKeyFrames().add(keyFrame);
						
						timeline.play();
						notesViewController.getCodeSelectionPane().setVisible(true);
					}
				});
			}
		}
		
		int tabCount = notesViewController.getTabPane().getTabs().size();
		if (tabCount > 0) {
			notesViewController.getTabPane().getSelectionModel().select(tabCount - 1);
		}
	}
	
	public void initialize() throws IOException {
		notepadTextArea.setText(notesText);
		
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor") + ";";
		String initialStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";";
		String padding = " -fx-padding: 2 7 2 7;";
		
		clearbtnnotepad.setStyle(initialStyle + padding);
		clearbtnnotepad.setOnMouseEntered(e -> clearbtnnotepad.setStyle(hoverStyle + padding));
		clearbtnnotepad.setOnMouseExited(e -> clearbtnnotepad.setStyle(initialStyle + padding));
		
		codesbtnnotepad.setStyle(initialStyle + padding);
		codesbtnnotepad.setOnMouseEntered(e -> codesbtnnotepad.setStyle(hoverStyle + padding));
		codesbtnnotepad.setOnMouseExited(e -> codesbtnnotepad.setStyle(initialStyle + padding));
		
		codeSelectionPane.setPrefWidth(0);
		codeSelectionPane.setMaxWidth(0);
		codeSelectionPane.setMinWidth(0);
		codeSelectionPane.setVisible(false);
		
		if (notesTabList == null) {
			notesTabList = new ArrayList<>();
		}
		
		Platform.runLater(() -> {
			try {
				createNoteTabs();
			} catch (IOException e) {
				logError("CreateNoteTabs; Exception error: ", e);
			}
		});
		
		String notepadMode = null;
		try {
			notepadMode = ConfigReader.configRead("notepad", "notepadMode");
		} catch (IOException e) {
			logError("error getting notepadMode: ", e);
		}
		if (notepadMode != null) {
			String notepadStyle;
			String modeToggleStyle;
			if (notepadMode.equals("Dark")) {
				modeToggle.setSelected(true);
				notepadStyle = "-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
				modeToggleStyle = "-fx-background-color: white;";
				codeSelectionPane.setStyle("-fx-background-color: gray;");
				codevbox.setStyle("-fx-background-color: rgb(200,200,200,1);");
				borderPane.setStyle("-fx-background-color: gray;");
				codeSelectionlbl.setStyle("-fx-text-fill: #f2f2f2;");
				notepadTextArea.setStyle(notepadStyle);
				modeToggle.setStyle(modeToggleStyle);
			} else if (notepadMode.equals("Light")) {
				modeToggle.setSelected(false);
				notepadStyle = "-fx-background-color: rgb(0,0,0,0.1); -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;";
				modeToggleStyle = "-fx-background-color: grey;";
				codeSelectionPane.setStyle("-fx-background-color: rgb(240,240,240,0.1);");
				codevbox.setStyle("-fx-background-color: rgb(210,210,210,0.3);");
				borderPane.setStyle("-fx-background-color: white;");
				codeSelectionlbl.setStyle("-fx-text-fill: gray;");
				notepadTextArea.setStyle(notepadStyle);
				modeToggle.setStyle(modeToggleStyle);
			}
		} else {
			notepadTextArea.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
			modeToggle.setStyle("-fx-background-color: grey;");
		}
		
		clearbtnnotepad.setText(localization.getLocalizedMessage("NotesWindow.ClearNotesButton", "Clear"));
		codesbtnnotepad.setText(localization.getLocalizedMessage("NotesWindow.CodesButton", "Codes"));
		codeSelectionlbl.setText(localization.getLocalizedMessage("NotesWindow.CodeSelectionHeading", "Code Selection Menu"));
	}
	
	public BorderPane getBorderPane() {
		return borderPane;
	}
	
	public Label getCodeSelectionlbl() {
		return codeSelectionlbl;
	}
	
	public VBox getCodevbox() {
		return codevbox;
	}
	
	public AnchorPane getCodeSelectionPane() {
		return codeSelectionPane;
	}
	
	public TabPane getTabPane() {
		return tabPane;
	}
	
	public TextArea getNotepadTextArea() {
		return notepadTextArea;
	}
	
	@javafx.fxml.FXML
	public void onDarkModeToggle() throws IOException {
		if (ConfigReader.configRead("notepad", "notepadMode").equals("Light")) {
			ConfigWriter.configwrite("notepad", "notepadMode", "Dark");
			notepadTextArea.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-border-color: transparent;-fx-background-radius: 0; -fx-border-radius: 0;");
			modeToggle.setStyle("-fx-background-color: white; -fx-background-radius: 0; -fx-border-radius: 0;");
			codeSelectionPane.setStyle("-fx-background-color: gray;");
			codevbox.setStyle("-fx-background-color: rgb(200,200,200,1);");
			borderPane.setStyle("-fx-background-color: gray;");
			codeSelectionlbl.setStyle("-fx-text-fill: #e2e2e2;");
		} else if (ConfigReader.configRead("notepad", "notepadMode").equals("Dark")) {
			ConfigWriter.configwrite("notepad", "notepadMode", "Light");
			notepadTextArea.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-text-fill: black; -fx-border-color: transparent; -fx-background-radius: 0; -fx-border-radius: 0;");
			modeToggle.setStyle("-fx-background-color: grey;");
			codeSelectionPane.setStyle("-fx-background-color: rgb(240,240,240,0.1);");
			codevbox.setStyle("-fx-background-color: rgb(210,210,210,0.3);");
			borderPane.setStyle("-fx-background-color: white;");
			codeSelectionlbl.setStyle("-fx-text-fill: gray;");
		}
	}
	
	@javafx.fxml.FXML
	public void onclearclick() {
		notepadTextArea.setText("");
	}
	
	@javafx.fxml.FXML
	public void oncodesclick() {
		if (mainDesktopControllerObj != null) {
			codesWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Misc/codes-window.fxml", "Notepad Codes", false, 1, true, false, mainDesktopControllerObj.getTaskBarApps(),
			                                               new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png")));
		}
	}
	
	@javafx.fxml.FXML
	public void showcodeselection(ActionEvent actionEvent) {
		if (codeSelectionPane.isVisible()) {
			
			double toWidth = 0;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(codeSelectionPane.prefWidthProperty(), toWidth);
			KeyValue keyValueMaxHeight = new KeyValue(codeSelectionPane.maxWidthProperty(), toWidth);
			KeyValue keyValueMinHeight = new KeyValue(codeSelectionPane.minWidthProperty(), toWidth);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValuePrefHeight, keyValueMaxHeight, keyValueMinHeight);
			
			timeline.getKeyFrames().add(keyFrame);
			
			timeline.play();
			codeSelectionPane.setVisible(false);
		} else {
			
			double toWidth = 200;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(codeSelectionPane.prefWidthProperty(), toWidth);
			KeyValue keyValueMaxHeight = new KeyValue(codeSelectionPane.maxWidthProperty(), toWidth);
			KeyValue keyValueMinHeight = new KeyValue(codeSelectionPane.minWidthProperty(), toWidth);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.2), keyValuePrefHeight, keyValueMaxHeight, keyValueMinHeight);
			
			timeline.getKeyFrames().add(keyFrame);
			
			timeline.play();
			codeSelectionPane.setVisible(true);
		}
	}
	
	private TextArea getSelectedTextArea() {
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		
		Parent parentPane = tab.getContent().getParent();
		return (TextArea) parentPane.lookup("#notepadTextArea");
	}
	
	@javafx.fxml.FXML
	public void onName(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-name ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onNumber(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-number ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onStreet(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-street ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onAddress(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-address ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onNotes(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-notes ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onSearchItems(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-searchitems ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onAge(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-age ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onModel(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-model ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onPlate(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-platenum ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onDescription(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-description ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onGender(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-gender ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onArea(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-area ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void onCounty(ActionEvent actionEvent) {
		getSelectedTextArea().appendText("-county ");
		getSelectedTextArea().requestFocus();
	}
	
	@javafx.fxml.FXML
	public void addTabBtn(ActionEvent actionEvent) throws IOException {
		int num = 1;
		for (NoteTab tab : notesTabList) {
			num++;
		}
		notesTabList.add(new NoteTab("Tab " + num));
		createNoteTabs();
	}
}
