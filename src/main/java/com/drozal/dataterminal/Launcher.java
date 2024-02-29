package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {

        if(ConfigReader.doesConfigExist()){
            System.out.println("config exists, showing dataterminal");
            DataTerminalHomeApplication.main(args);
        } else {
            System.out.println("no config, showing login");
            newOfficerApplication.main(args);
        }
    }
}