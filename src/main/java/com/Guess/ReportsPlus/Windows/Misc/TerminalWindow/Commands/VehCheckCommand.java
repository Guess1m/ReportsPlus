package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import static com.Guess.ReportsPlus.Windows.Apps.VehLookupViewController.performVehicleLookup;

import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;

public class VehCheckCommand implements Command {

	@Override
	public String getName() {
		return "vc";
	}

	@Override
	public String getDescription() {
		return "Perform veh check; accepts 'platenumber'";
	}

	@Override
	public void execute(String[] args, Output output) {
		if (args.length == 0 || args.length != 1) {
			output.println("~d~Usage:~g~ vc ~y~platenumber");
			return;
		}

		String plate = args[0];

		output.println("~g~Performing vehicle check for: [" + plate + "]");
		com.Guess.ReportsPlus.util.History.Vehicle vehicle = performVehicleLookup(plate);
		if (vehicle == null) {
			output.println("~r~Vehicle not found: [" + plate + "]");
			return;
		}

		String[] vehDetails = vehicle.toString().split("\\| ");
		if (vehDetails.length == 0) {
			output.println("~r~No details found for vehicle: [" + plate + "]");
			return;
		}
		for (String detail : vehDetails) {
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