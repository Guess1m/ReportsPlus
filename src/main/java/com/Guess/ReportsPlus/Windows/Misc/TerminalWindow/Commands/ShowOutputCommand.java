package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;

public class ShowOutputCommand implements Command {
	public static boolean TerminalLogging;
	
	@Override
	public String getName() {
		return "showoutput";
	}
	
	@Override
	public String getDescription() {
		return "Show application output in the terminal; accepts true / false";
	}
	
	@Override
	public void execute(String[] args, Output output) {
		if (args.length == 0) {
			output.println("~d~Usage:~g~ showoutput ~y~<true/false>");
			return;
		}
		
		boolean showOutput = Boolean.parseBoolean(args[0]);
		if (showOutput) {
			output.println("~g~Enabled~d~: Terminal logging activated");
		} else {
			output.println("~r~Disabled~d~: Terminal logging deactivated");
		}
		TerminalLogging = showOutput;
	}
}