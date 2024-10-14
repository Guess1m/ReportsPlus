package com.drozal.dataterminal.util.CommandPrompt.Commands;

import com.drozal.dataterminal.util.CommandPrompt.Command;
import com.drozal.dataterminal.util.CommandPrompt.CommandRegistry;

import java.util.Map;

public class HelpCommand implements Command {
	
	@Override
	public String execute(String[] args) {
		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, Command> entry : CommandRegistry.getCommands().entrySet()) {
			String commandName = entry.getKey();
			Command command = entry.getValue();
			result.append(commandName).append(": ").append(command.desc(null)).append("\n");
		}
		return result.toString();
	}
	
	@Override
	public String desc(String[] args) {
		return "Show The Available Commands";
	}
}
