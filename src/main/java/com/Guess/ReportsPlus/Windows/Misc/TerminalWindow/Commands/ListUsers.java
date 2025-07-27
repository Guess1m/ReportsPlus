package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

import java.io.IOException;

import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.UserProfiles.User;

import jakarta.xml.bind.JAXBException;

public class ListUsers implements Command {
	@Override
	public String getName() {
		return "listusers";
	}

	@Override
	public String getDescription() {
		return "List current user accounts";
	}

	@Override
	public void execute(String[] args, Output output) {
		String userName;
		try {
			userName = ConfigReader.configRead("userInfo", "Name");
		} catch (IOException e) {
			userName = "Unknown";
			logError("Error reading user info from ListUsers: ", e);
		}
		output.println("~y~Current user accounts:");
		try {
			for (User user : User.loadUserProfiles().getUserList()) {
				output.println("~c~" + user.getUsername() + (userName.equalsIgnoreCase(
						user.getName()) ? " (You)" : "") + "\n   Password: ~o~" + user.getPassword() + "\n   Name: ~y~"
						+ user.getName() + "\n   Rank: ~y~" + user.getRank() + "\n   Division: ~y~" + user.getDivision()
						+ "\n   Agency: ~y~" + user.getAgency() + "\n   Callsign: ~y~" + user.getCallsign()
						+ "\n   Number: ~y~" + user.getNumber());
			}
		} catch (JAXBException e) {
			logError("Failed to load user profiles from ListUsers: ", e);
		}
	}
}