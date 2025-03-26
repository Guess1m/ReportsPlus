package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils;

public interface Command {
	String getName();
	
	String getDescription();
	
	void execute(String[] args, Output output);
}