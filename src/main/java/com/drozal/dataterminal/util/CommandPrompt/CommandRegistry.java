package com.drozal.dataterminal.util.CommandPrompt;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
	private static final Map<String, Command> commands = new HashMap<>();
	
	public static Map<String, Command> getCommands() {
		return commands;
	}
	
	public void registerCommand(String name, Command command) {
		commands.put(name.toLowerCase(), command);
	}
	
	public String executeCommand(String input) {
		String[] parts = input.split(" ");
		String commandName = parts[0];
		String[] args = new String[parts.length - 1];
		System.arraycopy(parts, 1, args, 0, args.length);
		
		Command command = commands.get(commandName.toLowerCase());
		if (command != null) {
			return command.execute(args);
		} else {
			return "Unknown command: " + commandName + "\n";
		}
	}
}
