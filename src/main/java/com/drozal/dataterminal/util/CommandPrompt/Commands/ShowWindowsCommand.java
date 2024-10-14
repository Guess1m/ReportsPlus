package com.drozal.dataterminal.util.CommandPrompt.Commands;

import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.util.CommandPrompt.Command;

import java.util.Map;

import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.windows;

public class ShowWindowsCommand implements Command {
	@Override
	public String desc(String[] args) {
		return "Centers and shows all windows";
	}
	
	@Override
	public String execute(String[] args) {
		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, CustomWindow> entry : windows.entrySet()) {
			CustomWindow window = entry.getValue();
			window.centerOnDesktop();
			window.setHeight(100);
			window.setWidth(100);
			result.append(window.getTitle());
			result.append(", ");
		}
		
		return result.toString();
	}
}
