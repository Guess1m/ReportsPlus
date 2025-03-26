package com.Guess.ReportsPlus.util.Strings;

public class updateStrings {
	/*
	UPDATE:
	 version.txt
	 version
	 pom.xml
	 Server Updates
	 Plugin Updates
	 Updater Updates
	 Sound List If Changed
	 UserGuide Version / Changes
	 Copy Over Updater To Resources
	 New locale
	 Completed locale file
	 updatesList
	 */
	
	public static final String version = "v1.5.1-alpha";
	public static final String[] updatesList = {"Fully Custom Configurable ALPR System", "ALPR In-Game Plate Display", "Added Login / Registration System", "Fixed Bug With Info Box Background Color", "Added Menu To Plugin", "Ability To Give/Discard Citations In Menu", "Added Extended Expiration Data To License Information"};
	public static final String[] soundList = {"alert-callout.wav", "alert-success.wav", "alert-delete.wav", "alert-wanted.wav"};
	public static final String localeChanges = """
			Version: v1.5.0-alpha
			NEW:
			    ALPR.flagsSubLabel=Vehicle Flags:
			    ALPR.scannedPlatesSubLabel=Scanned Plates:
			    ALPR.distanceSubLabel=Distance from Scanner:
			    ALPR.scannerInfoSubLabel=ALPR Information:
			    ALPR.scannerUsedSubLabel=Scanner Used:
			    ALPR.clearButton=Clear Old
			    ALPR.timestampSubLabel=Timestamp:
			    ALPR.scannedPlateSubLabel=Plate Type:
			    ALPR.speedSubLabel=Speed:
			    Desktop.officerloginLabel=Officer Login:
			    Desktop.officerregistrationLabel=Officer Registration:
			    Desktop.loginButton=Login
			    Desktop.passLabel=Password:
			    Desktop.usernameLabel=Username:
			    Desktop.registerButton=Register
			
			REMOVED:
			    Login_Window.LoginButton=Login
			    Login_Window.NumberPromptText=Number
			    Login_Window.NamePromptText=Name
			    Login_Window.MainHeaderLabel=ReportsPlus Officer Login
			    Login_Window.IncompleteFormLabel=Please Fill Out the Form Completely.
			
			
			Version: v1.4.1-alpha
			NEW:
				DropdownCreator.selectPrompt=Select Dropdown
			    DropdownCreator.titleLabel=Dropdown Title:
			    DropdownCreator.itemsLabel=Dropdown Items:
			    DropdownCreator.windowTitle=Dropdown Manager
				ReportWindows.StopNumber=stop number
				ReportWindows.SectionButton=Section
				ReportWindows.RowButton=Row
				ReportWindows.FieldButton=Field
				ReportWindows.WidthLabel=Width:
				ReportWindows.TypeLabel=Type:
				NewReportApp.createNewReport=Create New Report (BETA)
			    NewReportApp.customReport=Custom Reports:
			    ImportExport.exportToFile=Export Current Layout to File
			    ImportExport.description=Import or export layout configuration from/to JSON format
			    ImportExport.openFileTitle=Open JSON File
			    ImportExport.title=JSON Import/Export
			    ImportExport.windowTitle=Layout Import/Export
			    ImportExport.applyLayout=Apply Layout
			    ImportExport.importFromFile=Import from File
			    LogBrowser.TotalReports=Total Reports:
			    LogBrowser.TableSettings=Table Settings
			    LogBrowser.ReportStatus=Status
			    LogBrowser.Refresh=Refresh
			    LogBrowser.HiddenColumns=Hidden Columns
			    LogBrowser.VisibleColumns=Visible Columns
			    LogBrowser.reportsInProgressHeader=Reports In Progress:
			    LogBrowser.closedReportsHeader=Reports Closed:
			    Settings.KeybindSettingsBtn=Keybind Settings
			    Settings.KeybindSettingsHeader=KEYBIND SETTINGS
			    Settings.NotiDeleteButton=Delete
			    LayoutBuilder.ImportExportButton=Import / Export
			    LayoutBuilder.Heading=Custom Layout Designer
			    LayoutBuilder.CustomDropdownButton=Custom Dropdowns
			    LayoutBuilder.BuildLayoutButton=View Report Layout
			
			REMOVED:
				LogBrowser.SubHeading=Log Browser
			
			
			Version: v1.4.0-alpha
			NEW:
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
				UserManager.editProfileLabel=Edit Profile:
				UserManager.userProfileLabel=Active User Profiles:
				UserManager.CallsignLabel=Callsign:
				UserManager.RankLabel=Rank:
				UserManager.DivisionLabel=Division:
				UserManager.Agencylabel=Agency:
				UserManager.newProfileButton=Create New Profile
				ReportWindows.CitationTypePrompt=Citation Type
				ReportWindows.CitationTypePrinted=Printed Citation
				ReportWindows.CitationTypeNonPrinted=Non-Printed
				ReportWindows.CitationTypeParking=Parking Citation
				Settings.inputLockKeybindLabel=Input Lock Keybind
				Settings.inputLockKeybindTT=Keybind to use for activating desktop input lock
				Settings.useGameTimeLabel=Use Game Time
				Settings.useGameTimeTT=Toggle whether game time is used when connected
				VehicleLookup.FieldVIN=Registered VIN:
				ReportStatistics.reportsByLabel=Reports By:
			
			REMOVED:
				UserManager.MainHeader
				UserManager.NameFieldPrompt
				UserManager.NumberFieldPrompt
				Callout_Manager.CalloutNum
				CalloutPopup.NumberLabel
				ServerConnectionWindow.ClientHeading
			
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
				Desktop.CheckedLabel=Checked:
			
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
