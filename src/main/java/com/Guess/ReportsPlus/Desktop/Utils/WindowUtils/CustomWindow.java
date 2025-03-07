package com.Guess.ReportsPlus.Desktop.Utils.WindowUtils;

import com.Guess.ReportsPlus.Desktop.Utils.AppUtils.DesktopApp;
import com.Guess.ReportsPlus.Desktop.Utils.AppUtils.TaskbarApp;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;

import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppUtils.DesktopApps;
import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.*;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.currentNotifications;

public class CustomWindow {
    private static double xOffset;
    private static double yOffset;
    public final String title;
    private final int priority;
    private final Pane windowPane;
    public boolean isMinimized = false;
    public Object controller;
    private TaskbarApp taskbarApp;
    private AnchorPane root;
    private Image image;
    private double originalWidth;
    private double originalHeight;
    private double originalX;
    private double originalY;
    private boolean isMaximized = false;

    // From Code
    public CustomWindow(BorderPane window, String title, boolean resizable, int priority, HBox taskBarApps, AnchorPane root, Image image) {
        this.windowPane = window;
        this.title = title;
        this.priority = priority;
        this.root = root;
        this.image = image;

        initializeWindow(resizable);
        addMainStageResizeListener();

        this.taskbarApp = new TaskbarApp(title, title, taskBarApps, this, image);
    }

    // From FXML
    public CustomWindow(String fileName, String title, boolean resizable, int priority, HBox taskBarApps, AnchorPane root, Image image) {
        URL fxmlUrl = Launcher.class.getResource(fileName);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root1 = null;
        try {
            root1 = loader.load();
        } catch (IOException e) {
            logError("Could not load window: " + fileName, e);
        }
        this.windowPane = (BorderPane) root1;
        this.title = title;
        this.priority = priority;
        controller = loader.getController();
        this.root = root;
        this.image = image;

        if (windowPane == null) {
            log("Window returned null, window likely didnt load: " + title, LogUtils.Severity.ERROR);
            return;
        }

        initializeWindow(resizable);
        addMainStageResizeListener();

        this.taskbarApp = new TaskbarApp(title, title, taskBarApps, this, image);
    }

    public void setSize(double width, double height) {
        Platform.runLater(() -> {
            windowPane.setPrefWidth(width);
            windowPane.setPrefHeight(height);
            keepWithinBounds();
        });
    }

    public double getWidth() {
        return windowPane.getPrefWidth();
    }

    public double getHeight() {
        return windowPane.getPrefHeight();
    }

    private void addMainStageResizeListener() {
        root.widthProperty().addListener((obs, oldVal, newVal) -> keepWithinBounds());
        root.heightProperty().addListener((obs, oldVal, newVal) -> keepWithinBounds());

        windowPane.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> keepWithinBounds());
    }

    private void keepWithinBounds() {
        if (root != null && windowPane != null) {
            double mainStageWidth = root.getWidth();
            double mainStageHeight = root.getHeight();

            double windowX = windowPane.getLayoutX();
            double windowY = windowPane.getLayoutY();

            double windowWidth = windowPane.getLayoutBounds().getWidth();
            double windowHeight = windowPane.getLayoutBounds().getHeight();

            if (windowX + windowWidth > mainStageWidth) {
                windowPane.setLayoutX(Math.round(mainStageWidth - windowWidth));
            }
            if (windowY + windowHeight > mainStageHeight) {
                windowPane.setLayoutY(Math.round(mainStageHeight - windowHeight));
            }

            if (windowX < 0) {
                windowPane.setLayoutX(0);
            }
            if (windowY < 0) {
                windowPane.setLayoutY(0);
            }
        }
    }

    private void enableResize(BorderPane pane) {
        final double resizeMargin = 20;

        pane.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            double paneWidth = pane.getWidth();
            double paneHeight = pane.getHeight();

            boolean right = x > paneWidth - resizeMargin;
            boolean bottom = y > paneHeight - resizeMargin;
            boolean left = x < resizeMargin;

            if (right && bottom) {
                pane.setCursor(javafx.scene.Cursor.SE_RESIZE);
            } else if (left && bottom) {
                pane.setCursor(javafx.scene.Cursor.SW_RESIZE);
            } else if (right) {
                pane.setCursor(javafx.scene.Cursor.E_RESIZE);
            } else if (left) {
                pane.setCursor(javafx.scene.Cursor.W_RESIZE);
            } else if (bottom) {
                pane.setCursor(javafx.scene.Cursor.S_RESIZE);
            } else {
                pane.setCursor(javafx.scene.Cursor.DEFAULT);
            }
        });

        pane.setOnMouseDragged(event -> {
            double x = event.getX();
            double y = event.getY();
            double paneX = pane.getLayoutX();
            double maxWidth = root.getWidth() - paneX;
            double maxHeight = root.getHeight() - pane.getLayoutY();

            if (pane.getCursor() == javafx.scene.Cursor.SE_RESIZE) {
                double newWidth = Math.min(Math.max(x, 400), maxWidth);
                double newHeight = Math.min(Math.max(y, 50), maxHeight);
                pane.setPrefSize(Math.round(newWidth), Math.round(newHeight));
            } else if (pane.getCursor() == javafx.scene.Cursor.E_RESIZE) {
                double newWidth = Math.min(Math.max(x, 400), maxWidth);
                pane.setPrefWidth(newWidth);
            } else if (pane.getCursor() == javafx.scene.Cursor.S_RESIZE) {
                double newHeight = Math.min(Math.max(y, 50), maxHeight);
                pane.setPrefHeight(newHeight);
            } else if (pane.getCursor() == javafx.scene.Cursor.W_RESIZE) {
                double newWidth = Math.min(Math.max(pane.getPrefWidth() - x, 400), pane.getLayoutX() + pane.getPrefWidth());
                double newX = pane.getLayoutX() + (pane.getPrefWidth() - newWidth);
                pane.setPrefWidth(newWidth);
                pane.setLayoutX(Math.round(newX));
            } else if (pane.getCursor() == javafx.scene.Cursor.SW_RESIZE) {
                double newWidth = Math.min(Math.max(pane.getPrefWidth() - x, 400), paneX + pane.getPrefWidth());
                double newHeight = Math.min(Math.max(y, 50), maxHeight);
                double newX = paneX + (pane.getWidth() - newWidth);
                pane.setPrefSize(newWidth, newHeight);
                pane.setLayoutX(Math.round(newX));
            }

            keepWithinBounds();
        });
    }

    private void initializeWindow(boolean resizable) {
        AnchorPane titleBar;
        if (resizable) {
            titleBar = createTitleBar(title, true);
        } else {
            titleBar = createTitleBar(title, false);
        }
        ((BorderPane) windowPane).setTop(titleBar);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(new Color(0, 0, 0, 0.3));
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setRadius(15);
        dropShadow.setSpread(.3);
        windowPane.setEffect(dropShadow);
        windowPane.setStyle("-fx-border-color: black; -fx-background-color: white; -fx-border-width: 1; -fx-background-radius: 0;");

        if (resizable) {
            enableResize((BorderPane) windowPane);
        }

        windowPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> bringToFront());
    }

    public void bringToFront() {
        Platform.runLater(() -> {
            int currentPriority = this.getPriority();

            this.getWindowPane().toFront();

            windows.values().stream().filter(window -> window.getPriority() > currentPriority).forEach(window -> window.getWindowPane().toFront());

            windows.values().stream().filter(window -> window.getPriority() < currentPriority).forEach(window -> window.getWindowPane().toBack());

            activeWindow = this;

            if (mainDesktopControllerObj != null) {
                for (DesktopApp app : DesktopApps) {
                    app.getMainPane().toBack();
                }
                for (AnchorPane noti : currentNotifications) {
                    noti.toFront();
                }
                mainDesktopControllerObj.getInfoHBox().toBack();
            }
        });
    }

    public void minimizeWindow() {
        CustomWindow customWindow = windows.get(title);
        if (customWindow != null) {
            customWindow.getWindowPane().setVisible(false);
            minimizedWindows.put(title, customWindow);
            isMinimized = true;
        }
    }

    public void restoreWindow(String title) {
        CustomWindow customWindow = minimizedWindows.get(title);
        if (customWindow != null) {
            customWindow.getWindowPane().setVisible(true);
            AnchorPane parent = (AnchorPane) customWindow.getWindowPane().getParent();
            if (parent != null && !parent.getChildren().contains(customWindow.getWindowPane())) {
                parent.getChildren().add(customWindow.getWindowPane());
            }
            minimizedWindows.remove(title);
            isMinimized = false;
        }
    }

    public void toggleMaximize() {
        if (isMaximized) {
            restoreWindowSize();
        } else {
            maximizeWindow();
        }
    }

    public void maximizeWindow() {
        double stageWidth = root.getWidth();
        double stageHeight = root.getHeight();

        originalWidth = windowPane.getPrefWidth();
        originalHeight = windowPane.getPrefHeight();
        originalX = windowPane.getLayoutX();
        originalY = windowPane.getLayoutY();

        windowPane.setPrefWidth(stageWidth);
        windowPane.setPrefHeight(stageHeight);
        windowPane.setLayoutX(0);
        windowPane.setLayoutY(0);
        isMaximized = true;
    }

    public void restoreWindowSize() {
        windowPane.setPrefWidth(originalWidth);
        windowPane.setPrefHeight(originalHeight);
        windowPane.setLayoutX(Math.round(originalX));
        windowPane.setLayoutY(Math.round(originalY));
        isMaximized = false;
    }

    public void centerOnDesktop() {
        if (windowPane != null) {
            Platform.runLater(() -> {
                windowPane.applyCss();
                windowPane.layout();
                double stageWidth = root.getWidth();
                double stageHeight = root.getHeight();
                double windowWidth = windowPane.getPrefWidth();
                double windowHeight = windowPane.getPrefHeight();
                double x = (stageWidth - windowWidth) / 2;
                double y = (stageHeight - windowHeight) / 2;
                windowPane.setLayoutX(Math.round(x));
                windowPane.setLayoutY(Math.round(y));
            });
        }
    }

    public void closeWindow() {
        Platform.runLater(() -> {
            double[] position = {windowPane.getLayoutX(), windowPane.getLayoutY()};
            windowPositions.put(title, position);

            double[] size = {windowPane.getWidth(), windowPane.getHeight()};
            windowSizes.put(title, size);

            windows.remove(title);

            if (taskbarApp != null) {
                taskbarApp.removeApp();
            }

            windowPane.setOnMouseMoved(null);
            windowPane.setOnMouseDragged(null);
            windowPane.removeEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> bringToFront());

            Pane parent = (Pane) windowPane.getParent();
            if (parent != null) {
                parent.getChildren().remove(windowPane);
            }

            controller = null;
            windowPane.setEffect(null);
            windowPane.setStyle(null);

            if (root != null) {
                root.widthProperty().removeListener((obs, oldVal, newVal) -> keepWithinBounds());
                root.heightProperty().removeListener((obs, oldVal, newVal) -> keepWithinBounds());
            }

            taskbarApp = null;
            image = null;
            root = null;

            System.gc();
        });
    }

    public AnchorPane createTitleBar(String titleText, boolean enableMaximize) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1.0);
        colorAdjust.setBrightness(-0.45);

        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleLabel.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(titleLabel, 0.0);
        AnchorPane.setRightAnchor(titleLabel, 0.0);
        AnchorPane.setTopAnchor(titleLabel, 0.0);
        AnchorPane.setBottomAnchor(titleLabel, 0.0);
        titleLabel.setEffect(colorAdjust);
        titleLabel.setMouseTransparent(true);

        AnchorPane titleBar = new AnchorPane(titleLabel);
        titleBar.setMinHeight(30);
        titleBar.setStyle("-fx-background-color: #383838;");

        Image placeholderImage = new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Logo.png"));
        ImageView placeholderImageView = new ImageView(placeholderImage);
        placeholderImageView.setFitWidth(49);
        placeholderImageView.setFitHeight(49);
        AnchorPane.setLeftAnchor(placeholderImageView, 0.0);
        AnchorPane.setTopAnchor(placeholderImageView, -10.0);
        AnchorPane.setBottomAnchor(placeholderImageView, -10.0);

        ColorAdjust colorAdjust1 = new ColorAdjust();
        colorAdjust1.setBrightness(0.5);
        placeholderImageView.setEffect(colorAdjust1);

        Image closeImage = new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/cross.png"));
        ImageView closeImageView = new ImageView(closeImage);
        closeImageView.setFitWidth(15);
        closeImageView.setFitHeight(15);
        AnchorPane.setRightAnchor(closeImageView, 15.0);
        AnchorPane.setTopAnchor(closeImageView, 7.0);
        closeImageView.setEffect(colorAdjust);

        Image minimizeImage = new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/minimize.png"));
        ImageView minimizeImageView = new ImageView(minimizeImage);
        minimizeImageView.setFitWidth(15);
        minimizeImageView.setFitHeight(15);
        AnchorPane.setRightAnchor(minimizeImageView, 70.0);
        AnchorPane.setTopAnchor(minimizeImageView, 7.0);
        minimizeImageView.setEffect(colorAdjust);

        Rectangle closeRect = new Rectangle(20, 20);
        closeRect.setFill(Color.TRANSPARENT);
        closeRect.setOnMouseClicked(event -> closeWindow());
        AnchorPane.setRightAnchor(closeRect, 12.5);
        AnchorPane.setTopAnchor(closeRect, 6.3);

        Rectangle minimizeRect = new Rectangle(20, 20);
        minimizeRect.setFill(Color.TRANSPARENT);
        minimizeRect.setOnMouseClicked(event -> minimizeWindow());
        AnchorPane.setTopAnchor(minimizeRect, 6.3);

        if (enableMaximize) {
            Image maximizeImage = new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/maximize.png"));
            ImageView maximizeImageView = new ImageView(maximizeImage);
            maximizeImageView.setFitWidth(15);
            maximizeImageView.setFitHeight(15);
            maximizeImageView.setEffect(colorAdjust);
            AnchorPane.setRightAnchor(maximizeImageView, 42.5);
            AnchorPane.setTopAnchor(maximizeImageView, 7.0);
            titleBar.getChildren().add(maximizeImageView);

            Rectangle maximizeRect = new Rectangle(20, 20);
            maximizeRect.setFill(Color.TRANSPARENT);
            maximizeRect.setOnMouseClicked(event -> toggleMaximize());
            AnchorPane.setRightAnchor(maximizeRect, 42.5);
            AnchorPane.setTopAnchor(maximizeRect, 6.3);
            titleBar.getChildren().add(maximizeRect);

            AnchorPane.setRightAnchor(minimizeImageView, 70.0);
            AnchorPane.setRightAnchor(minimizeRect, 70.0);
        } else {
            AnchorPane.setRightAnchor(minimizeImageView, 42.5);
            AnchorPane.setRightAnchor(minimizeRect, 42.5);
        }

        titleBar.getChildren().addAll(placeholderImageView, closeImageView, minimizeImageView, closeRect, minimizeRect);

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - xOffset;
            double deltaY = event.getSceneY() - yOffset;

            double newX = windowPane.getLayoutX() + deltaX;
            double newY = windowPane.getLayoutY() + deltaY;

            double maxWidth = root.getWidth() - windowPane.getWidth();
            double maxHeight = root.getHeight() - windowPane.getHeight();

            if (newX < 0) {
                newX = 0;
            }
            if (newY < 0) {
                newY = 0;
            }
            if (newX > maxWidth) {
                newX = maxWidth;
            }
            if (newY > maxHeight) {
                newY = maxHeight;
            }

            windowPane.setLayoutX(Math.round(newX));
            windowPane.setLayoutY(Math.round(newY));
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        return titleBar;
    }

    public int getPriority() {
        return priority;
    }

    public Pane getWindowPane() {
        return windowPane;
    }

    public void setPosition(double x, double y) {
        if (windowPane != null) {
            windowPane.setLayoutX(Math.round(x));
            windowPane.setLayoutY(Math.round(y));
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}