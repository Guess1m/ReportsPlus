package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import com.Guess.ReportsPlus.Desktop.Utils.AppUtils.DesktopApp;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;

import java.io.File;
import java.nio.file.Files;

import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig.appConfig.appConfigRead;
import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig.appConfig.appConfigWrite;
import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppUtils.DesktopApps;
import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.windows;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;

public class MiscCommands {
	public static class CenterWindowsCommand implements Command {
		@Override
		public String getName() {
			return "centerwindows";
		}
		
		@Override
		public String getDescription() {
			return "Debug Command to center all windows";
		}
		
		@Override
		public void execute(String[] args, Output output) {
			int i = 0;
			for (CustomWindow window : windows.values()) {
				window.centerOnDesktop();
				logDebug("Centered window: " + window.title);
				i++;
			}
			output.println("Centered all windows [" + i + "]");
		}
	}
	
	public static class ResetAppPositionsCommand implements Command {
		@Override
		public String getName() {
			return "resetapppos";
		}
		
		@Override
		public String getDescription() {
			return "Debug Command to reset all app positions";
		}
		
		@Override
		public void execute(String[] args, Output output) {
			logDebug("Resetting App Positions..");
			String x1 = String.valueOf(45.0);
			appConfigWrite("Callouts", "x", x1);
			appConfigWrite("Callouts", "y", String.valueOf(20.0));
			appConfigWrite("CourtCase", "x", x1);
			appConfigWrite("CourtCase", "y", String.valueOf(320.0));
			appConfigWrite("Log Browser", "x", x1);
			appConfigWrite("Log Browser", "y", String.valueOf(220.0));
			appConfigWrite("Notes", "x", x1);
			appConfigWrite("Notes", "y", String.valueOf(120.0));
			appConfigWrite("Show IDs", "x", x1);
			appConfigWrite("Show IDs", "y", String.valueOf(420.0));
			
			String x2 = String.valueOf(175.0);
			appConfigWrite("New Report", "x", x2);
			appConfigWrite("New Report", "y", String.valueOf(220.0));
			appConfigWrite("Ped Lookup", "x", x2);
			appConfigWrite("Ped Lookup", "y", String.valueOf(20.0));
			appConfigWrite("Veh Lookup", "x", x2);
			appConfigWrite("Veh Lookup", "y", String.valueOf(120.0));
			appConfigWrite("ALPR", "x", x2);
			appConfigWrite("ALPR", "y", String.valueOf(320.0));
			
			String x3 = String.valueOf(305.0);
			appConfigWrite("Server", "x", x3);
			appConfigWrite("Server", "y", String.valueOf(20.0));
			appConfigWrite("Settings", "x", x3);
			appConfigWrite("Settings", "y", String.valueOf(220.0));
			appConfigWrite("Updates", "x", x3);
			appConfigWrite("Updates", "y", String.valueOf(120.0));
			
			String x4 = String.valueOf(420.0);
			appConfigWrite("Profile", "x", x4);
			appConfigWrite("Profile", "y", String.valueOf(20.0));
			appConfigWrite("Report Statistics", "x", x4);
			appConfigWrite("Report Statistics", "y", String.valueOf(120.0));
			for (DesktopApp desktopApp : DesktopApps) {
				double appX = appConfigRead(desktopApp.getName(), "x");
				double appY = appConfigRead(desktopApp.getName(), "y");
				
				desktopApp.getMainPane().setTranslateX(appX);
				desktopApp.getMainPane().setTranslateY(appY);
				logInfo("Reset App Position for: " + desktopApp.getName() + " X: [" + appX + "] Y: [" + appY + "]");
			}
			logDebug("Finished Resetting App Positions");
			output.println("Finished resetting app positions");
		}
	}
	
	public static class ClearLookupData implements Command {
		@Override
		public String getName() {
			return "clearlookup";
		}
		
		@Override
		public String getDescription() {
			return "Debug Command to clear all previous ped/vehicle data (free space)";
		}
		
		@Override
		public void execute(String[] args, Output output) {
			try {
				String dataFolderPath = getJarPath() + File.separator + "data";
				
				File pedHistoryFile = new File(dataFolderPath + File.separator + "pedHistory.xml");
				if (pedHistoryFile.exists() && pedHistoryFile.isFile()) {
					logInfo("pedHistory.xml exists. [2]");
					Files.deleteIfExists(pedHistoryFile.toPath());
					logInfo("pedHistory.xml deleted. [2]");
					output.println("pedHistory.xml ~g~cleared");
				} else {
					logWarn("pedHistory.xml does not exist. [2]");
				}
				
				File vehHistoryFile = new File(dataFolderPath + File.separator + "vehHistory.xml");
				if (vehHistoryFile.exists() && vehHistoryFile.isFile()) {
					logInfo("vehHistory.xml exists. [2]");
					Files.deleteIfExists(vehHistoryFile.toPath());
					logInfo("vehHistory.xml deleted. [2]");
					output.println("vehHistory.xml ~g~cleared");
				} else {
					logWarn("vehHistory.xml does not exist. [2]");
				}
				
				showNotificationInfo("Developer", "Successfully Cleared Lookup Data");
				output.println("Successfully cleared lookup data");
			} catch (Exception e) {
				logError("Clear lookup data error [2]: ", e);
			}
		}
	}
}