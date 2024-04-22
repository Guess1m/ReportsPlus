package com.drozal.dataterminal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class CommandPrompt extends Application {

    private final TextArea outputArea = new TextArea();
    private final TextField inputField = new TextField();
    private final HashMap<String, Consumer<String[]>> commands = new HashMap<>();
    private final Map<String, String> environment = new HashMap<>();
    private final List<String> history = new ArrayList<>();
    private File currentDirectory = new File(System.getProperty("user.dir"));
    private int historyPointer = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        outputArea.setEditable(false);
        outputArea.appendText("----==== Advanced Command Terminal ====----\n\n");

        inputField.setOnAction(event -> {
            String input = inputField.getText();
            history.add(input);
            historyPointer = history.size();
            processCommand(input);
            inputField.clear();
        });

        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP && historyPointer > 0) {
                historyPointer--;
                inputField.setText(history.get(historyPointer));
                inputField.end();
            } else if (event.getCode() == KeyCode.DOWN && historyPointer < history.size() - 1) {
                historyPointer++;
                inputField.setText(history.get(historyPointer));
                inputField.end();
            }
        });

        VBox layout = new VBox(10, outputArea, inputField);
        Scene scene = new Scene(layout, 800, 500);
        registerCommands();

        primaryStage.setTitle("JavaFX Advanced Command Prompt");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processCommand(String input) {
        String[] parts = parseArguments(input);
        if (parts.length == 0) return;
        String command = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        Consumer<String[]> action = commands.getOrDefault(command, this::unknownCommand);
        action.accept(args);
    }

    private String[] parseArguments(String input) {
        return input.split(" ");
    }

    private void unknownCommand(String[] args) {
        outputArea.appendText("Unknown command: " + String.join(" ", args) + "\n");
    }

    private void registerCommands() {
        commands.put("-help", this::helpCommand);
        commands.put("-cmds", args -> listCommands());
        commands.put("-clear", args -> outputArea.clear());
        commands.put("-echo", args -> outputArea.appendText(String.join(" ", args) + "\n"));
        commands.put("-exit", args -> System.exit(0));
        commands.put("-cd", this::changeDirectory);
        commands.put("-set", this::setEnv);
        commands.put("-get", this::getEnv);
        commands.put("-start", this::startBackgroundTask);
    }

    private void helpCommand(String[] args) {
        if (args.length == 0) {
            outputArea.appendText("Available commands:\n" + String.join("\n", commands.keySet()) + "\n");
        } else {
            String helpText = switch (args[0]) {
                case "-cd" -> "Change the current working directory. Usage: -cd [directory]";
                case "-set" -> "Set an environment variable. Usage: -set [key] [value]";
                case "-get" -> "Get an environment variable. Usage: -get [key]";
                default -> "No help available for " + args[0];
            };
            outputArea.appendText(helpText + "\n");
        }
    }

    private void listCommands() {
        StringBuilder cmds = new StringBuilder("Available Commands:\n");
        commands.keySet().forEach(cmd -> cmds.append(cmd).append("\n"));
        outputArea.appendText(cmds.toString());
    }

    private void changeDirectory(String[] args) {
        if (args.length > 0) {
            File newDir = new File(currentDirectory, args[0]);
            if (newDir.exists() && newDir.isDirectory()) {
                currentDirectory = newDir;
                outputArea.appendText("Changed directory to " + newDir.getPath() + "\n");
            } else {
                outputArea.appendText("Directory not found: " + newDir.getPath() + "\n");
            }
        }
    }

    private void setEnv(String[] args) {
        if (args.length >= 2) {
            environment.put(args[0], args[1]);
            outputArea.appendText("Set " + args[0] + " to " + args[1] + "\n");
        } else {
            outputArea.appendText("Usage: -set [key] [value]\n");
        }
    }

    private void getEnv(String[] args) {
        if (args.length > 0) {
            String value = environment.getOrDefault(args[0], "Not set");
            outputArea.appendText(args[0] + " = " + value + "\n");
        } else {
            outputArea.appendText("Usage: -get [key]\n");
        }
    }

    private void startBackgroundTask(String[] args) {
        if (args.length > 0) {
            // Simulate a background task
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    outputArea.appendText("Completed task: " + String.join(" ", args) + "\n");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
            outputArea.appendText("Started background task: " + String.join(" ", args) + "\n");
        } else {
            outputArea.appendText("Usage: -start [taskName]\n");
        }
    }
}
