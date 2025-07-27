package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.CommandRegistry;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;

public class HelpCommand implements Command {
	private final CommandRegistry commandRegistry;

	public HelpCommand(CommandRegistry commandRegistry) {
		this.commandRegistry = commandRegistry;
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Show available commands";
	}

	@Override
	public void execute(String[] args, Output output) {
		output.println("~y~Available commands:");
		commandRegistry.getCommands().values().forEach(
				cmd -> output.println(String.format("  %-15s %s", cmd.getName(), "~c~" + cmd.getDescription())));
	}
}