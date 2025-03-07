package com.Guess.ReportsPlus.Desktop.Utils.WindowUtils;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;

public class WindowManager {
    public static Map<String, CustomWindow> windows = new HashMap<>();
    public static Map<String, CustomWindow> minimizedWindows = new HashMap<>();
    public static Map<String, double[]> windowPositions = new HashMap<>();
    public static Map<String, double[]> windowSizes = new HashMap<>();
    public static CustomWindow activeWindow = null;

    public static CustomWindow createCustomWindow(AnchorPane root, String fileName, String title, boolean resizable, int priority, boolean centerOnDesktop, boolean reopen, HBox taskBarApps, Image image) {
        log("Creating FXML Window: " + title, LogUtils.Severity.INFO);
        if (!windows.containsKey(title)) {
            URL fxmlUrl = Launcher.class.getResource(fileName);
            if (fxmlUrl == null) {
                throw new RuntimeException("FXML file not found: " + fileName);
            }

            CustomWindow customWindow = new CustomWindow(fileName, title, resizable, priority, taskBarApps, root, image);
            if (customWindow.getWindowPane() != null) {
                try {
                    if (root != null) {
                        Platform.runLater(() -> {
                            windows.put(title, customWindow);

                            double screenWidth = root.getWidth();
                            double screenHeight = root.getHeight();

                            double fxmlWidth = customWindow.getWindowPane().prefWidth(-1);
                            double fxmlHeight = customWindow.getWindowPane().prefHeight(-1);

                            if (fxmlWidth <= 0 || fxmlHeight <= 0) {
                                fxmlWidth = customWindow.getWindowPane().getLayoutBounds().getWidth();
                                fxmlHeight = customWindow.getWindowPane().getLayoutBounds().getHeight();
                            }

                            if (fxmlWidth <= 0) {
                                fxmlWidth = 800;
                            }
                            if (fxmlHeight <= 0) {
                                fxmlHeight = 600;
                            }

                            double scaleByWidth = (screenWidth * 0.8) / fxmlWidth;
                            double scaleByHeight = (screenHeight * 0.75) / fxmlHeight;
                            double scale = Math.min(scaleByWidth, scaleByHeight);

                            if (scale > 1) {
                                scale = 1;
                            }

                            double windowWidth = fxmlWidth * scale;
                            double windowHeight = fxmlHeight * scale;

                            customWindow.setSize(windowWidth, windowHeight);

                            double[] savedPosition = windowPositions.get(title);
                            try {
                                if (savedPosition != null && ConfigReader.configRead("desktopSettings", "saveWindowPosition").equalsIgnoreCase("true")) {
                                    customWindow.setPosition(savedPosition[0], savedPosition[1]);
                                } else if (centerOnDesktop) {
                                    customWindow.centerOnDesktop();
                                }
                            } catch (IOException e) {
                                logError("Error getting config value desktopSettings.saveWindowPosition for: " + title, e);
                            }

                            double[] savedSize = windowSizes.get(title);
                            try {
                                if (savedSize != null && ConfigReader.configRead("desktopSettings", "saveWindowSize").equalsIgnoreCase("true")) {
                                    customWindow.setSize(savedSize[0], savedSize[1]);
                                }
                            } catch (IOException e) {
                                logError("Error getting config value desktopSettings.saveWindowSize for: " + title, e);
                            }

                            customWindow.bringToFront();

                            Platform.runLater(() -> {
                                root.getChildren().add(customWindow.getWindowPane());
                            });
                        });
                    }
                } catch (NullPointerException e) {
                    logError("Error creating window: " + title, e);
                }
            } else {
                log("WindowPane was null after creation, Window likely had an error: " + title, LogUtils.Severity.ERROR);
            }
            return customWindow;
        } else {
            CustomWindow customWindow = windows.get(title);
            customWindow.bringToFront();
            if (reopen) {
                double x = customWindow.getWindowPane().getLayoutX();
                double y = customWindow.getWindowPane().getLayoutY();
                windowPositions.put(title, new double[]{x, y});
                windowSizes.put(title, new double[]{customWindow.getWindowPane().getWidth(), customWindow.getWindowPane().getHeight()});

                LogUtils.log(customWindow.title + " is already created, closing", LogUtils.Severity.WARN);
                customWindow.closeWindow();
                windows.remove(title);

                return createCustomWindow(root, fileName, title, resizable, priority, centerOnDesktop, false, taskBarApps, image);
            }
        }
        return null;
    }

    public static CustomWindow createCustomWindow(AnchorPane root, BorderPane window, String title, boolean resizable, int priority, boolean centerOnDesktop, boolean reopen, HBox taskBarApps, Image image) {
        log("Creating Window: " + title, LogUtils.Severity.INFO);
        if (!windows.containsKey(title)) {
            CustomWindow customWindow = new CustomWindow(window, title, resizable, priority, taskBarApps, root, image);

            if (root != null) {
                Platform.runLater(() -> {
                    windows.put(title, customWindow);

                    double screenWidth = root.getWidth();
                    double screenHeight = root.getHeight();

                    double fxmlWidth = customWindow.getWindowPane().prefWidth(-1);
                    double fxmlHeight = customWindow.getWindowPane().prefHeight(-1);

                    if (fxmlWidth <= 0 || fxmlHeight <= 0) {
                        fxmlWidth = customWindow.getWindowPane().getLayoutBounds().getWidth();
                        fxmlHeight = customWindow.getWindowPane().getLayoutBounds().getHeight();
                    }

                    if (fxmlWidth <= 0) {
                        fxmlWidth = 800;
                    }
                    if (fxmlHeight <= 0) {
                        fxmlHeight = 600;
                    }

                    double scaleByWidth = (screenWidth * 0.8) / fxmlWidth;
                    double scaleByHeight = (screenHeight * .75) / fxmlHeight;
                    double scale = Math.min(scaleByWidth, scaleByHeight);

                    if (scale > 1) {
                        scale = 1;
                    }

                    double windowWidth = fxmlWidth * scale;
                    double windowHeight = fxmlHeight * scale;

                    customWindow.setSize(windowWidth, windowHeight);

                    double[] savedPosition = windowPositions.get(title);
                    try {
                        if (savedPosition != null && ConfigReader.configRead("desktopSettings", "saveWindowPosition").equalsIgnoreCase("true")) {
                            customWindow.setPosition(savedPosition[0], savedPosition[1]);
                        } else if (centerOnDesktop) {
                            customWindow.centerOnDesktop();
                        }
                    } catch (IOException e) {
                        logError("Error getting config value desktopSettings.saveWindowPosition for: " + title, e);
                    }

                    double[] savedSize = windowSizes.get(title);
                    try {
                        if (savedSize != null && ConfigReader.configRead("desktopSettings", "saveWindowSize").equalsIgnoreCase("true")) {
                            customWindow.setSize(savedSize[0], savedSize[1]);
                        }
                    } catch (IOException e) {
                        logError("Error getting config value desktopSettings.saveWindowSize for: " + title, e);
                    }

                    customWindow.bringToFront();
                    root.getChildren().add(customWindow.getWindowPane());

                });
            }
            return customWindow;
        } else {
            CustomWindow customWindow = windows.get(title);
            customWindow.bringToFront();
            if (reopen) {
                double x = customWindow.getWindowPane().getLayoutX();
                double y = customWindow.getWindowPane().getLayoutY();
                windowPositions.put(title, new double[]{x, y});
                windowSizes.put(title, new double[]{customWindow.getWindowPane().getWidth(), customWindow.getWindowPane().getHeight()});

                LogUtils.log(customWindow.title + " is already created, closing", LogUtils.Severity.WARN);
                customWindow.closeWindow();
                windows.remove(title);

                return createCustomWindow(root, window, title, resizable, priority, centerOnDesktop, false, taskBarApps, image);
            } else {
                customWindow.bringToFront();
            }
        }
        return null;
    }

    public static CustomWindow getWindow(String title) {
        return windows.get(title);
    }
}
