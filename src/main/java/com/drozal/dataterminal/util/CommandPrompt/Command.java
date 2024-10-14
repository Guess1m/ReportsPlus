package com.drozal.dataterminal.util.CommandPrompt;

public interface Command {
	
	String execute(String[] args);
	
	String desc(String[] args);
	
}
