package com.drozal.dataterminal.Windows.Other;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.util.Misc.LogUtils;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static org.controlsfx.tools.Utils.clamp;

public class mapController {
    @javafx.fxml.FXML
    public ImageView losSantosMap;
    private double lastX;
    private double lastY;
    private double defaultTX;
    private double defaultTY;
    private double defaultXS;
    private double defaultYS;
    private double defaulutXPos;
    private double defaulutYPos;
    private String currentImagePath;
    @javafx.fxml.FXML
    private VBox root;

    public void initialize() {
        currentImagePath = "/com/drozal/dataterminal/imgs/losSantos.jpg";
        Image initialImage = new Image(Launcher.class.getResourceAsStream(currentImagePath));
        losSantosMap.setImage(initialImage);
        root.setStyle("-fx-background-color:  #143d6b;");
        defaultTX = losSantosMap.getTranslateX();
        defaultTY = losSantosMap.getTranslateY();
        defaultXS = losSantosMap.getScaleX();
        defaultYS = losSantosMap.getScaleY();
        defaulutXPos = losSantosMap.getX();
        defaulutYPos = losSantosMap.getY();
    }

    @javafx.fxml.FXML
    public void onLosSantosMapScroll(ScrollEvent scrollEvent) {
        double deltaYScroll = scrollEvent.getDeltaY();
        double MIN_SCALE = 1.0;
        double MAX_SCALE = 10.0;
        double SCROLL_FACTOR = 0.015;

        deltaYScroll = -deltaYScroll;

        double mouseX = scrollEvent.getX();
        double mouseY = scrollEvent.getY();

        double currentScaleX = losSantosMap.getScaleX();
        double currentScaleY = losSantosMap.getScaleY();

        double newScale = currentScaleX + deltaYScroll * SCROLL_FACTOR;
        newScale = clamp(newScale, MIN_SCALE, MAX_SCALE);

        double f = newScale / currentScaleX - 1;

        double dx = mouseX - (losSantosMap.getBoundsInParent().getWidth() / 2 + losSantosMap.getBoundsInParent().getMinX());
        double dy = mouseY - (losSantosMap.getBoundsInParent().getHeight() / 2 + losSantosMap.getBoundsInParent().getMinY());

        losSantosMap.setScaleX(newScale);
        losSantosMap.setScaleY(newScale);

        losSantosMap.setTranslateX(losSantosMap.getTranslateX() - f * dx);
        losSantosMap.setTranslateY(losSantosMap.getTranslateY() - f * dy);

        scrollEvent.consume();
    }

    @javafx.fxml.FXML
    public void mapMouseDrag(MouseEvent mouseEvent) {
        double deltaX = mouseEvent.getSceneX() - lastX;
        double deltaY = mouseEvent.getSceneY() - lastY;

        losSantosMap.setTranslateX(losSantosMap.getTranslateX() + deltaX);
        losSantosMap.setTranslateY(losSantosMap.getTranslateY() + deltaY);

        lastX = mouseEvent.getSceneX();
        lastY = mouseEvent.getSceneY();
    }

    @javafx.fxml.FXML
    public void mapMousePress(MouseEvent mouseEvent) {
        lastX = mouseEvent.getSceneX();
        lastY = mouseEvent.getSceneY();
    }

    @javafx.fxml.FXML
    public void onZoomInPressed() {
        zoom(losSantosMap, 1.5);
    }

    @javafx.fxml.FXML
    public void onZoomOutPressed() {
        zoom(losSantosMap, 0.5);
    }

    private void zoom(ImageView imageView, double factor) {
        double currentScaleX = imageView.getScaleX();
        double currentScaleY = imageView.getScaleY();

        double newScaleX = currentScaleX * factor;
        double newScaleY = currentScaleY * factor;

        double MIN_SCALE = 1.0;
        double MAX_SCALE = 10.0;
        newScaleX = clamp(newScaleX, MIN_SCALE, MAX_SCALE);
        newScaleY = clamp(newScaleY, MIN_SCALE, MAX_SCALE);

        imageView.setScaleX(newScaleX);
        imageView.setScaleY(newScaleY);
    }

    @javafx.fxml.FXML
    public void onRefreshPress(Event event) {
        losSantosMap.setTranslateX(defaultTX);
        losSantosMap.setTranslateY(defaultTY);
        losSantosMap.setScaleX(defaultXS);
        losSantosMap.setScaleY(defaultYS);
        losSantosMap.setX(defaulutXPos);
        losSantosMap.setY(defaulutYPos);
    }

    @javafx.fxml.FXML
    public void onOverlayPress(Event event) {
        double defaultTX = losSantosMap.getTranslateX();
        double defaultTY = losSantosMap.getTranslateY();
        double defaultXS = losSantosMap.getScaleX();
        double defaultYS = losSantosMap.getScaleY();
        double defaulutXPos = losSantosMap.getX();
        double defaulutYPos = losSantosMap.getY();
        swapImage();

        losSantosMap.setTranslateX(defaultTX);
        losSantosMap.setTranslateY(defaultTY);
        losSantosMap.setScaleX(defaultXS);
        losSantosMap.setScaleY(defaultYS);
        losSantosMap.setX(defaulutXPos);
        losSantosMap.setY(defaulutYPos);

    }

    private void swapImage() {
        switch (currentImagePath) {
            case "/com/drozal/dataterminal/imgs/areaMap.jpg":
                currentImagePath = "/com/drozal/dataterminal/imgs/losSantos.jpg";
                log("setting lossantos", LogUtils.Severity.DEBUG);
                root.setStyle("-fx-background-color: #143d6b;");
                break;
            case "/com/drozal/dataterminal/imgs/losSantos.jpg":
                currentImagePath = "/com/drozal/dataterminal/imgs/AtlasMap.jpg";
                log("setting atlasmap", LogUtils.Severity.DEBUG);
                root.setStyle("-fx-background-color: #00abd7;");
                break;
            case "/com/drozal/dataterminal/imgs/AtlasMap.jpg":
                currentImagePath = "/com/drozal/dataterminal/imgs/RoadMap.jpg";
                log("setting roadmap", LogUtils.Severity.DEBUG);
                root.setStyle("-fx-background-color: #0064b3;");
                break;
            case "/com/drozal/dataterminal/imgs/RoadMap.jpg":
                currentImagePath = "/com/drozal/dataterminal/imgs/areaMap.jpg";
                log("setting areaMap", LogUtils.Severity.DEBUG);
                root.setStyle("-fx-background-color: #00c1e1;");
                break;
            default:
                currentImagePath = "/com/drozal/dataterminal/imgs/losSantos.jpg";
                break;
        }

        Image newImage = new Image(Launcher.class.getResourceAsStream(currentImagePath));
        losSantosMap.setImage(newImage);
    }
}
