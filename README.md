# Reports Plus

<h3 align="center">
    <a href="https://github.com/Guess1m/ReportsPlus/releases">Download Latest Release</a>
    |
    <a href="https://github.com/Guess1m/ReportsPlus/issues/new?assignees=zainrd123&labels=enhancement&projects=&template=feature_request.md&title=">Feature Request</a>
    |
    <a href="https://github.com/Guess1m/ReportsPlus/issues/new?assignees=zainrd123&labels=bug&projects=&template=bug_report.md&title=">Report Bug</a>
</h3>

## Overview

Reports Plus is designed for users who appreciate the realism aspect of LSPDFR, aiming to make stops, arrests, searches,
and other interactions more realistic and thorough.

### Key Features

- Function as an MDT for use from another computer.
- Perform ped and vehicle lookups.
- Display IDs and show callouts.
- Create in-depth reports for Citations, Callouts, Patrols, Traffic Stops, Impounds, Searches, Incidents, and Arrests.
- Fully functional log database that displays all the information for every log.
- Speed up report creation by transferring information into another report. For example, if you create an arrest report,
  you can quickly generate a search report by transferring details like the suspect's name and location of arrest,
  saving a lot of time.
- High-quality zoomable map.
- Notepad text area, useful for remembering location info and search items.
- Customizable UI. Plans to make the entire UI fully customizable.
- Fully modifiable arrest charges and citations through XML, compatible with plugins like Realistic Charges and
  Citations for Compulite.
- Numerous divisions and agencies to choose from, inspired by PaperWorkPlus.
- Many more features.

## Prerequisites

Ensure you have the following installed:

- [Java JDK 22](https://www.oracle.com/java/technologies/downloads/)
- [Java](https://www.java.com/en/download/)

## Installation

### **1. Installation `With` GTA Integration - For Using Another PC/Laptop (Recommended)**

- GTA V Integration **`REQUIRES`** `Stop The Ped` and `Callout Interface`!
- The computer `NOT` running GTA needs the Reports Plus application download.
- The computer `RUNNING` GTA needs the Server download.

### Server/Plugin Setup: (Instructions in download)

1. Download the `Server` from the <a href="https://github.com/Guess1m/ReportsPlus/releases">Releases</a> tab in Github.
2. Move the contents of the ReportsPlus Server folder to the base GTA directory on the PC running GTA. This will move a
   folder named ReportsPlus to the base GTA directory.
3. Open the ReportsPlusServer JAR. If there are issues, ensure the prerequisites are installed.
4. The server will display your PC's local IPV4 address, which must be entered into the ReportsPlus application
   connection window.
5. Choose a port for the connection. If 5000 is in use, try 5001, 5002, etc. You will know the server is running if the
   connection window says "server started."
6. Ensure the port and address match between the Server and ReportsPlus application.
7. **Use the help button on both applications for additional assistance.**

### Reports Plus Setup: (Instructions in download)

1. Download the `Application` from the <a href="https://github.com/Guess1m/ReportsPlus/releases">Releases</a> tab in
   Github.
2. Move the Reports Plus folder to any location on the computer NOT running GTA.
3. Run the JAR file inside the ReportsPlus folder. Ensure the prerequisites are installed if there are any issues.
4. Open the connection window by clicking the server connection status below the settings in the top right.
5. Ensure the server is open and started, then copy the displayed address into the appropriate field in ReportsPlus.
6. Choose a port that matches the one used on the server, and connect to the server.
7. **Use the help button on both applications for additional assistance.**

### **2. Installation `without` Server/GTA Integration**

1. Download only the Reports Plus Application.
2. Move the downloaded folder to any location (Desktop or Documents work fine).
3. Run the JAR file inside the ReportsPlus folder. Ensure the prerequisites are installed if there are any issues.

## Troubleshooting

- If on Mac, I have had issues with the application if it's in a folder that is backed up by iCloud, I believe this has
  to do with the Sync messing with the files, I recommend moving the application to a folder that is not backed up by
  iCloud. Putting the ReportsPlus folder in the `{user}\Applications` folder has worked fine for me, this directory
  shouldn't be getting backed up by iCloud.