package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

import java.io.IOException;
import java.util.Objects;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;

import javafx.scene.image.Image;

public class TestCommand implements Command {

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public String getDescription() {
		return "command for testing purposes";
	}

	@Override
	public void execute(String[] args, Output output) {
		WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
				"Windows/Apps/lookup-ped-view copy.fxml", "Pedestrian Lookup COPY", true, 1, true, false,
				mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/ped-search.png"))));
		try {
			settingsController.loadTheme();
		} catch (IOException e) {
			logError("Error loading theme from lookupApp", e);
		}
	}
}