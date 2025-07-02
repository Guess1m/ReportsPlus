package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import static com.Guess.ReportsPlus.util.History.Ped.PedHistoryUtils.addPed;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateLicenseNumber;

import com.Guess.ReportsPlus.Windows.Apps.PedLookupViewControllerCopy;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;
import com.Guess.ReportsPlus.util.History.Ped;

import jakarta.xml.bind.JAXBException;

public class CreatePedCommand implements Command {

	@Override
	public String getName() {
		return "createped";
	}

	@Override
	public String getDescription() {
		return "Create a history ped; accepts 'firstname lastname'";
	}

	@Override
	public void execute(String[] args, Output output) {
		if (args.length == 0 || args.length < 2) {
			output.println("~d~Usage:~g~ createped ~y~firstname lastname");
			return;
		}

		String firstName = args[0];
		String lastName = args[1];
		String fullName = firstName + " " + lastName;

		output.println("~g~Created Ped: [" + fullName + "]");
		Ped ped = new Ped();
		ped.setName(fullName);
		ped.setLicenseNumber(generateLicenseNumber());
		try {
			addPed(ped);
		} catch (JAXBException e) {
			output.println("~r~Error creating ped");
			return;
		}

		PedLookupViewControllerCopy.performPedLookup(fullName);

	}
}