package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;

public class TestCommand implements Command {
	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String getDescription() {
		return "command for testing purposes";
	}

	@Override
	public void execute(String[] args, Output output) {
		output.println("~g~Executing test command");
	}
}