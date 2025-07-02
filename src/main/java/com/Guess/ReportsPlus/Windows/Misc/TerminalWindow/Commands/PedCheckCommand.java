package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import com.Guess.ReportsPlus.Windows.Apps.PedLookupViewControllerCopy;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;
import com.Guess.ReportsPlus.util.History.Ped;

public class PedCheckCommand implements Command {

	@Override
	public String getName() {
		return "pc";
	}

	@Override
	public String getDescription() {
		return "Perform ped check; accepts 'firstname lastname'";
	}

	@Override
	public void execute(String[] args, Output output) {
		if (args.length == 0 || args.length < 2) {
			output.println("~d~Usage:~g~ pc ~y~firstname lastname");
			return;
		}

		String firstName = args[0];
		String lastName = args[1];
		String fullName = firstName + " " + lastName;

		output.println("~g~Performing ped check for: [" + fullName + "]");
		Ped ped = PedLookupViewControllerCopy.performPedLookup(fullName);
		if (ped == null) {
			output.println("~r~Ped not found: [" + fullName + "]");
			return;
		}

		String[] pedDetails = ped.toString().split("\\| ");
		if (pedDetails.length == 0) {
			output.println("~r~No details found for ped: [" + fullName + "]");
			return;
		}
		for (String detail : pedDetails) {
			String[] detailParts = detail.split("=");
			if (detailParts.length < 2) {
				continue;
			}
			String key = detailParts[0].trim();
			String value = detailParts[1].trim();
			output.println("~y~" + key + ": ~d~" + value);
		}

	}
}