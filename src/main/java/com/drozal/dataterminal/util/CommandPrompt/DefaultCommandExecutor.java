package com.drozal.dataterminal.util.CommandPrompt;

public class DefaultCommandExecutor {
	
	private final CommandRegistry commandRegistry;
	
	public DefaultCommandExecutor(CommandRegistry commandRegistry) {
		this.commandRegistry = commandRegistry;
	}
	
	public String executeCommand(String commandInput) {
		return commandRegistry.executeCommand(commandInput);
	}
	
}
