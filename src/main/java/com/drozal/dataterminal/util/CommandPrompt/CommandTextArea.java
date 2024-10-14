package com.drozal.dataterminal.util.CommandPrompt;

import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CommandTextArea {
	private final TextArea textArea;
	private final DefaultCommandExecutor executor;
	private String currentInput = "";
	private boolean isFirstLine = true;
	
	public CommandTextArea(DefaultCommandExecutor executor) {
		this.executor = executor;
		
		textArea = new TextArea();
		textArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14;");
		textArea.setWrapText(true);
		textArea.setEditable(false);
		
		textArea.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
		textArea.addEventFilter(KeyEvent.KEY_PRESSED, this::handleCopyPaste);
		
		textArea.positionCaret(textArea.getText().length());
		
		printPrompt();
		
	}
	
	public TextArea getTextArea() {
		return textArea;
	}
	
	private void handleKeyPress(KeyEvent event) {
		if (event.isControlDown()) {
			return;
		}
		
		if (event.getCode() == KeyCode.ENTER) {
			handleInput();
			event.consume();
		} else if (event.getCode() == KeyCode.BACK_SPACE) {
			if (!currentInput.isEmpty()) {
				currentInput = currentInput.substring(0, currentInput.length() - 1);
				updateTextArea();
			}
			event.consume();
		} else if (event.getCode().isLetterKey() || event.getCode().isDigitKey() || event.getCode() == KeyCode.SPACE) {
			currentInput += event.getText();
			updateTextArea();
			event.consume();
		}
	}
	
	private void handleCopyPaste(KeyEvent event) {
		if (event.isControlDown()) {
			if (event.getCode() == KeyCode.C) {
				textArea.copy();
				event.consume();
			} else if (event.getCode() == KeyCode.V) {
				textArea.paste();
				event.consume();
			} else if (event.getCode() == KeyCode.A) {
				textArea.selectAll();
				event.consume();
			}
		}
	}
	
	private void handleInput() {
		String command = currentInput.trim();
		if (!command.isEmpty()) {
			String result = executor.executeCommand(command);
			if (isFirstLine) {
				textArea.appendText("\n" + result);
				isFirstLine = false;
			} else {
				textArea.appendText("\n" + result);
			}
		}
		currentInput = "";
		printPrompt();
	}
	
	private void printPrompt() {
		if (!isFirstLine) {
			textArea.appendText("\n");
		}
		textArea.appendText("> ");
	}
	
	private void updateTextArea() {
		StringBuilder currentText = new StringBuilder(textArea.getText());
		int lastPromptIndex = currentText.lastIndexOf("\n> ");
		if (lastPromptIndex >= 0) {
			currentText.delete(lastPromptIndex + 3, currentText.length());
		} else {
			int firstPromptIndex = currentText.indexOf("> ");
			if (firstPromptIndex >= 0) {
				currentText.delete(firstPromptIndex + 2, currentText.length());
			}
		}
		currentText.append(currentInput);
		textArea.setText(currentText.toString());
		textArea.positionCaret(textArea.getText().length());
	}
}