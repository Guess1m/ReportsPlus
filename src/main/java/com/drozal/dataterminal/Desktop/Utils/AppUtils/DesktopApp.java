package com.drozal.dataterminal.Desktop.Utils.AppUtils;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils.DesktopApps;

public class DesktopApp {

    private final String name;
    private final Image image;
    private VBox mainPane;

    public DesktopApp(String name, Image image) {
        this.name = name;
        this.image = image;
    }

    public VBox createDesktopApp(EventHandler<MouseEvent> mouseClickHandler) {
        if (name == null || image == null) {
            throw new IllegalArgumentException("Name and image cannot be null.");
        }

        VBox mainVbox = new VBox();
        mainVbox.setAlignment(Pos.TOP_CENTER);
        mainVbox.setSpacing(3);
        mainVbox.setPadding(new Insets(0, -5, 7, -5));

        Button appButton = createImageButton(image);

        BorderPane borderPaneLabel = new BorderPane();

        Label appLabel = new Label(name);
        appLabel.setStyle("-fx-font-family: 'Segoe UI Semibold'; -fx-text-fill:  white; -fx-font-size: 15px;");
        appLabel.setMouseTransparent(true);
        appLabel.setAlignment(Pos.BOTTOM_CENTER);
        appLabel.setTextAlignment(TextAlignment.CENTER);
        appLabel.setWrapText(true);
        appLabel.setMaxWidth(100);
        appLabel.setMinWidth(100);
        borderPaneLabel.setCenter(appLabel);

        mainVbox.getChildren().add(appButton);
        mainVbox.getChildren().add(borderPaneLabel);

        setUpDragEvents(appButton, mainVbox);

        appButton.setOnMouseClicked(mouseClickHandler);

        DesktopApps.add(this);
        mainPane = mainVbox;
        return mainVbox;
    }

    private Button createImageButton(Image image) {
        Button button = new Button();
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(48);
        imageView.setFitWidth(48);
        imageView.setPreserveRatio(true);
        imageView.setMouseTransparent(true);

        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");

        return button;
    }

    private void setUpDragEvents(Button button, VBox app) {
        AppUtils.setUpDragEvents(button, app);
    }

    public String getName() {
        return name;
    }

    public VBox getMainPane() {
        return mainPane;
    }

    public double getX() {
        return mainPane.getTranslateX();
    }

    public double getY() {
        return mainPane.getTranslateY();
    }

    public Image getImage() {
        return image;
    }
}
