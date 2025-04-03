package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow;

import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.CommandRegistry;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands.HelpCommand;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands.ListUsers;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands.MiscCommands;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands.ShowOutputCommand;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Guess.ReportsPlus.util.Strings.updateStrings.version;

public class TerminalController implements Initializable {
	public static TerminalController terminalController;
	
	private final CommandRegistry commandRegistry = new CommandRegistry();
	private final String prompt = "user@system~: ";
	
	private final List<String> commandHistory = new ArrayList<>();
	private int currentHistoryIndex = 0;
	
	@FXML
	private TextField inputField;
	@FXML
	private VBox outputContainer;
	@FXML
	private ScrollPane scrollPane;
	
	private void addCommandLine(String commandLine) {
		HBox line = new HBox();
		Text promptText = new Text(prompt);
		promptText.getStyleClass().add("prompt-text");
		Text commandText = new Text(commandLine);
		commandText.getStyleClass().add("command-text");
		line.getChildren().addAll(promptText, commandText);
		outputContainer.getChildren().add(line);
	}
	
	public void printToOutput(String text) {
		Platform.runLater(() -> {
			Pattern errorPattern = Pattern.compile("\\*\\*\\*.*?\\*\\*\\*", Pattern.DOTALL);
			Matcher matcher = errorPattern.matcher(text);
			int lastIndex = 0;
			
			while (matcher.find()) {
				addStyledSegment(text.substring(lastIndex, matcher.start()), false);
				addStyledSegment(matcher.group(), true);
				lastIndex = matcher.end();
			}
			addStyledSegment(text.substring(lastIndex), false);
			
			outputContainer.getChildren().add(new Text(""));
			scrollToBottom();
		});
	}
	
	private void addStyledSegment(String text, boolean isErrorBlock) {
		String[] lines = text.split("\n");
		for (String line : lines) {
			if (line.isEmpty()) {
				continue;
			}
			
			if (isErrorBlock) {
				HBox hbox = new HBox(new Text(line));
				hbox.getChildren().forEach(n -> ((Text) n).getStyleClass().add("error-text"));
				outputContainer.getChildren().add(hbox);
			} else {
				outputContainer.getChildren().add(processColorCodesAndLogStyles(line));
			}
		}
	}
	
	private void applyLogTypeStyling(Text textNode, String line) {
		if (line.contains("[INFO]")) {
			textNode.getStyleClass().add("info-text");
		} else if (line.contains("[WARN]")) {
			textNode.getStyleClass().add("warn-text");
		} else if (line.contains("[ERROR]")) {
			textNode.getStyleClass().add("error-text");
		} else if (line.contains("[DEBUG]")) {
			textNode.getStyleClass().add("debug-text");
		} else {
			textNode.getStyleClass().add("output-text");
		}
	}
	
	private HBox processColorCodesAndLogStyles(String line) {
		HBox container = new HBox();
		container.setSpacing(0);
		
		Pattern colorPattern = Pattern.compile("~(\\w)~|([^~]+)");
		Matcher matcher = colorPattern.matcher(line);
		String currentColor = null;
		
		while (matcher.find()) {
			if (matcher.group(1) != null) {
				String code = matcher.group(1).toLowerCase();
				currentColor = code.equals("d") ? null : code;
			} else {
				String segment = matcher.group(2);
				if (!segment.isEmpty()) {
					Text textNode = new Text(segment);
					
					if (currentColor != null) {
						applyColorStyle(textNode, currentColor);
					} else {
						applyLogTypeStyling(textNode, segment);
					}
					
					container.getChildren().add(textNode);
				}
			}
		}
		
		return container;
	}
	
	private void applyColorStyle(Text textNode, String colorCode) {
		switch (colorCode) {
			case "o":
				textNode.getStyleClass().add("color-orange");
				break;
			case "r":
				textNode.getStyleClass().add("color-red");
				break;
			case "g":
				textNode.getStyleClass().add("color-green");
				break;
			case "b":
				textNode.getStyleClass().add("color-blue");
				break;
			case "y":
				textNode.getStyleClass().add("color-yellow");
				break;
			case "c":
				textNode.getStyleClass().add("color-cyan");
				break;
			default:
		}
	}
	
	private void scrollToBottom() {
		Platform.runLater(() -> scrollPane.setVvalue(1.0));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Text header = new Text("ReportsPlus [" + version + "]");
		header.getStyleClass().add("system-text");
		outputContainer.getChildren().add(header);
		
		Text helpMsg = new Text("Type 'help' for available commands");
		helpMsg.getStyleClass().add("system-text");
		outputContainer.getChildren().addAll(helpMsg, new Text(""));
		
		commandRegistry.registerCommand(new HelpCommand(commandRegistry));
		commandRegistry.registerCommand(new ShowOutputCommand());
		commandRegistry.registerCommand(new ListUsers());
		commandRegistry.registerCommand(new MiscCommands.CenterWindowsCommand());
		commandRegistry.registerCommand(new MiscCommands.ResetAppPositionsCommand());
		commandRegistry.registerCommand(new MiscCommands.ClearLookupData());
	}
	
	@FXML
	private void handleInput(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			String commandLine = inputField.getText().trim();
			inputField.clear();
			
			if (commandLine.isEmpty()) {
				return;
			}
			
			commandHistory.add(commandLine);
			if (commandHistory.size() > 5) {
				commandHistory.remove(0);
			}
			currentHistoryIndex = commandHistory.size();
			
			addCommandLine(commandLine);
			processCommand(commandLine);
			scrollToBottom();
		} else if (event.getCode() == KeyCode.UP) {
			if (!commandHistory.isEmpty() && currentHistoryIndex > 0) {
				currentHistoryIndex--;
				inputField.setText(commandHistory.get(currentHistoryIndex));
				inputField.positionCaret(inputField.getText().length());
			}
			event.consume();
		} else if (event.getCode() == KeyCode.DOWN) {
			if (!commandHistory.isEmpty() && currentHistoryIndex < commandHistory.size()) {
				currentHistoryIndex++;
				if (currentHistoryIndex < commandHistory.size()) {
					inputField.setText(commandHistory.get(currentHistoryIndex));
					inputField.positionCaret(inputField.getText().length());
				} else {
					inputField.setText("");
				}
			}
			event.consume();
		}
	}
	
	private void processCommand(String commandLine) {
		if (!commandLine.isEmpty()) {
			String[] parts = commandLine.split(" ", 2);
			String commandName = parts[0];
			String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];
			
			Command command = commandRegistry.getCommand(commandName);
			if (command != null) {
				command.execute(args, this::printToOutput);
			} else {
				printToOutput("~r~Command not found: ~y~" + commandName);
			}
		}
	}
}