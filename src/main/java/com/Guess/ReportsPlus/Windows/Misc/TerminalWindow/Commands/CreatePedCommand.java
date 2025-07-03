package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import static com.Guess.ReportsPlus.util.History.Ped.PedHistoryUtils.addPed;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateLicenseNumber;

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
		return "Create a history ped. Optional flags: -wanted <true/false>, -license <status>";
	}

	@Override
	public void execute(String[] args, Output output) {
		if (args.length < 2) {
			output.println("~d~Usage:~g~ createped ~y~firstname lastname [-wanted true/false] [-license <status>]");
			return;
		}

		String firstName = args[0];
		String lastName = args[1];
		String fullName = firstName + " " + lastName;

		String isWanted = null;
		String licenseStatus = null;

		for (int i = 2; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-wanted") && i + 1 < args.length) {
				isWanted = args[i + 1];
				i++;
			} else if (args[i].equalsIgnoreCase("-license") && i + 1 < args.length) {
				licenseStatus = args[i + 1];
				i++;
			}
		}

		output.println("~g~Creating Ped: [" + fullName + "]");
		Ped ped = new Ped();
		ped.setName(fullName);
		ped.setLicenseNumber(generateLicenseNumber());

		if (isWanted != null) {
			ped.setWantedStatus(isWanted);
			output.println("~g~Setting wanted status to: ~y~" + isWanted);
		}

		if (licenseStatus != null) {
			ped.setLicenseStatus(licenseStatus);
			output.println("~g~Setting license status to: ~y~" + licenseStatus);
		}

		try {
			addPed(ped);
			output.println("~g~Successfully created Ped with License #: ~y~" + ped.getLicenseNumber());
		} catch (JAXBException e) {
			output.println("~r~Error creating ped.");
		}
	}
}