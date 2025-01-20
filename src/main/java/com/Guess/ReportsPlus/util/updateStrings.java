package com.Guess.ReportsPlus.util;

public class updateStrings {
	/*
	UPDATE:
	 version.txt GOOD
	 version GOOD
	 pom.xml GOOD
	 Server Updates GOOD
	 Plugin Updates GOOD
	 Updater Updates GOOD
	 Sound List If Changed
	 UserGuide Version / Changes
	 Copy Over Updater To Resources
	 New locale
	 Completed locale file
	 updatesList
	 */
	
	public static final String version = "v1.4.0-alpha";
	public static final String[] updatesList = {"Completely Reworked Ped/Veh Lookup Implementation", "New ReportStatistics App To View Some Statistics", "Support For 'can_be_warrant' Tag In Charges.xml", "Added Support For Policing Redefined", "Locale For 'New Report' App", "Get Time Directly From Game When Connected", "Option To Use GTA Game Time When Connected", "Copy Street/Area/County Individually", "Added LSPDFR Default Events For ID Lookups", "Fixed DesktopApps Not Bringing Window To Front", "Input Lock Button", "Better Looking TaskbarApps With Minimize/Close", "Support For Multiple Profiles", "Redesigned Profile App", "Snap DesktopApps / New Default Positions", "Fixed Notes Tabs Styling", "Fix ReportWindow Localization For Notes", "Updated Default App Positions", "Updated Default Charges.xml", "Show Previous Version(s) Locale In Locale Updates"};
	public static final String[] soundList = {"alert-callout.wav", "alert-success.wav", "alert-delete.wav", "alert-wanted.wav"};
	public static final String localeChanges = """
			Version: v1.4.0-alpha
			NEW:
				UserManager.editProfileLabel=Edit Current Profile:
				UserManager.editProfileSubLabel=Switch to or edit the selected profile
				UserManager.userProfileLabel=User Profiles:
				UserManager.userProfileSubLabel=These are the found user profiles
				Settings.inputLockKeybindLabel=Input Lock Keybind
				Settings.inputLockKeybindTT=Keybind to use for activating desktop input lock
				VehicleLookup.FieldVIN=Registered VIN:
				ReportStatistics.reportsByLabel=Reports By:
				Settings.useGameTimeLabel=Use Game Time
				Settings.useGameTimeTT=Toggle whether game time is used when connected
				NewReportApp.patrol=Patrol Report
				NewReportApp.search=Search Report
				NewReportApp.callout=Callout Report
				NewReportApp.trafficstop=TrafficStop Report
				NewReportApp.arrest=Arrest Report
				NewReportApp.death=Death Report
				NewReportApp.impound=Impound Report
				NewReportApp.citation=Citation Report
				NewReportApp.incident=Incident Report
				NewReportApp.accident=Accident Report
			
			REMOVED:
				UserManager.MainHeader=Edit Officer Data:
				UserManager.NameFieldPrompt=Name
				UserManager.NumberFieldPrompt=Number
			
			Version: v1.3-alpha
			NEW:
				Settings.mainSettingsSubheader=MAIN SETTINGS
				Settings.colorSettingsSubheader=COLOR SETTINGS
				Settings.MiscSettingsSubheader=MISC / SERVER SETTINGS
				Settings.developerSubheader=DEVELOPER / RESET DATA
				Settings.desktopTaskBarClrLabel=Taskbar Color
				Settings.desktopTaskBarClrTT=Set the color of the bottom task bar
				Settings.desktopTaskBarTextClrLabel=Taskbar Text Color
				Settings.desktopTaskBarTextClrTT=Set the color of the text on the bottom taskbar
				Settings.resetAppPosBtn=RESET APP POSITIONS
				Settings.resetAppPosLabel=Reset App Positions
				Settings.resetAppPosTT=Reset apps to their default positions
				UpdatesWindow.notStartedLabel=Not Started
				UpdatesWindow.armChipCheckbox=Use Windows / ARM MacOS Download
				UpdatesWindow.startAutoUpdateBtn=Start AutoUpdate
				UpdatesWindow.autoUpdateUtilityHeader=AutoUpdate Utility
				UpdatesWindow.validAutoUpdateCheck=OK
				UpdatesWindow.invalidAutoUpdateCheck=Invalid
				UpdatesWindow.successfulAutoUpdateCheck=Successful Check!
				UpdatesWindow.failedAutoUpdateCheck=Issues Found
				UpdatesWindow.checksDidntPassLabel=Can't Update:
				UpdatesWindow.LocaleChangesButton=Locale Changes
				UpdatesWindow.updateServerCheckbox=Update Server (Requires you to be connected)
				NewReportApp.selectReportTypeLabel=Select a Report Type:
				UpdatesWindow.intelChipCheckbox=Use Intel Chip MacOS Download (Only select if using a Intel chip mac)
				UpdatesWindow.launchAutoUpdateBtn=Launch Update Utility (BETA)
				LogBrowser.SubHeading=Log Browser
				LogBrowser.reportDatabaseLabel=Report Database
			
			REMOVED:
				Desktop.CreateReportButton=Create Report
				UpdatesWindow.IntelChipCheckbox=Intel Chip (ONLY For Intel MacOS!)
				UpdatesWindow.UpdateButton=AutoUpdate
				LogBrowser.SubHeading=Log Browser
			
			Version: v1.2-alpha
			NEW:
				Desktop.AvailableLabel=Available!
				Desktop.NewVersionAvailable=New Version Available!
				Settings.desktopAppTextClrLabel=App Text Color
				Settings.ClearLookupDataBtn=CLEAR LOOKUP DATA
				Settings.desktopAppTextClrTT=Set the color of the app name text
				UpdatesWindow.UpdateButton=AutoUpdate (BETA)
				UpdatesWindow.CantUpdateLabel=No Update Available!
				UpdatesWindow.IntelChipCheckbox=Intel Chip (ONLY For Intel MacOS!)
				UpdatesWindow.MissingUpdater=Missing UpdateUtility!
				Settings.installSoundsBtn=INSTALL/UPDATE
				Settings.soundPackNotDetectedLbl=Sound Pack Not Detected
				Settings.imagesNotDetectedLbl=Ped/Veh Images Not Detected
				Settings.enablePedVehImages=Enable Ped/Veh Images
				Settings.enablePedVehImagesTT=Toggle whether ped/veh images will be shown in lookup
				Settings.clearLookupDataLabel=Clear Old Ped / Veh Data
				Settings.clearLookupDataLabelTT=ONLY delete saved ped / veh history data from previous lookups to free space
				PedLookup.PedImageFoundlbl=Image Found in File:
			
			REMOVED:
				UpdatesWindow.CheckUpdatesButton=Check For Updates
			
			CHANGED:
				Settings.AudioSettingsHeader=AUDIO SETTINGS  ->  Settings.AudioSettingsHeader=AUDIO/OPTIONAL SETTINGS
				Settings.AudioBtn=Audio  ->  Settings.AudioBtn=Audio/Optionals
			""";
	
	public static final String name = "Reports Plus";
	
}
