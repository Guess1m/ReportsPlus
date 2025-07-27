package com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.Commands;

import static com.Guess.ReportsPlus.util.History.Vehicle.VehicleHistoryUtils.addVehicle;

import com.Guess.ReportsPlus.Windows.Apps.VehLookupViewController;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Command;
import com.Guess.ReportsPlus.Windows.Misc.TerminalWindow.CommandUtils.Output;
import com.Guess.ReportsPlus.util.History.Vehicle;

import jakarta.xml.bind.JAXBException;

public class CreateVehicleCommand implements Command {
    @Override
    public String getName() {
        return "createveh";
    }

    @Override
    public String getDescription() {
        return "Create a history vehicle; accepts 'platenumber'";
    }

    @Override
    public void execute(String[] args, Output output) {
        if (args.length == 0 || args.length < 1) {
            output.println("~d~Usage:~g~ createveh ~y~platenumber");
            return;
        }
        String plateNumber = args[0];
        output.println("~g~Created Vehicle: [" + plateNumber + "]");
        Vehicle vehicle = new Vehicle();
        vehicle.setPlateNumber(plateNumber);
        try {
            addVehicle(vehicle);
        } catch (JAXBException e) {
            output.println("~r~Error creating Vehicle");
            return;
        }
        VehLookupViewController.performVehicleLookup(plateNumber);
    }
}