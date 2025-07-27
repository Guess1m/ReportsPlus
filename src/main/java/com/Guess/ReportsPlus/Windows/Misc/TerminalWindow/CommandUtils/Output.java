package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils;

public interface Output {
	void print(String text);

	default void println(String text) {
		print(text + "\n\n");
	}
}
