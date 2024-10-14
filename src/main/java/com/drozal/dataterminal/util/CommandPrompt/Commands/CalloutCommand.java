package com.drozal.dataterminal.util.CommandPrompt.Commands;

import com.drozal.dataterminal.util.CommandPrompt.Command;
import com.drozal.dataterminal.util.Misc.CalloutManager;

import java.util.Random;

import static com.drozal.dataterminal.Windows.Apps.CalloutViewController.calloutViewController;
import static com.drozal.dataterminal.util.Misc.CalloutManager.addCallout;
import static com.drozal.dataterminal.util.Misc.stringUtil.calloutDataURL;

public class CalloutCommand implements Command {
	
	@Override
	public String execute(String[] args) {
		int randNum = 0;
		Random randomNuber = new Random();
		randomNuber.setSeed(System.currentTimeMillis());
		randNum = randomNuber.nextInt(10000, 99999);
		String arguments = String.join(" ", args);
		String stat = "Not Responded";
		if (arguments.equalsIgnoreCase("1")) {
			stat = "Responded";
		}
		addCallout(calloutDataURL, String.valueOf(randNum), "House Fire", "Witness Report a house fire",
		           "Sample message", "Code 3", "126 Forum Drive", "Sandy Shores", "Blaine", "11:00:00 AM", "04/14/24",
		           String.join(" ", stat));
		if (calloutViewController != null) {
			CalloutManager.loadActiveCallouts(calloutViewController.getCalActiveList());
			CalloutManager.loadHistoryCallouts(calloutViewController.getCalHistoryList());
		}
		return "Created sample history callout #" + randNum + "\n";
	}
	
	@Override
	public String desc(String[] args) {
		return "Create History Callout: 1 = 'Responded', else 'Not Responded'";
	}
}
