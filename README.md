# Reports Plus

<h3 align="center">
    <a href="https://github.com/Guess1m/ReportsPlus/releases">Download Latest Release</a>
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

- [Java JDK 22/23](https://www.oracle.com/java/technologies/downloads/)
- [Java](https://www.java.com/en/download/)

## Installation

### `Please view the page on lcpdfr.com for installation`

## Troubleshooting

- If on Mac, I have had issues with the application if it's in a folder that is backed up by iCloud, I believe this has
  to do with the Sync messing with the files, I recommend moving the application to a folder that is not backed up by
  iCloud. Putting the ReportsPlus folder in the `{user}\Applications` folder has worked fine for me, this directory
  shouldn't be getting backed up by iCloud.
- If you are getting blank ID or Callout windows, try closing both the application and server and relaunching both then
  reconnecting to the server. This is likely caused by the messages from the server getting out of sync, im currently
  working on a fix.
