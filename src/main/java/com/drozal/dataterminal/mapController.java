package com.drozal.dataterminal;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import static org.controlsfx.tools.Utils.clamp;

public class mapController {
    public ImageView losSantosMap;
    private double lastX;
    private double lastY;

    public void onLosSantosMapScroll(ScrollEvent scrollEvent) {
        double deltaYScroll = scrollEvent.getDeltaY(); // Get the scroll delta
        double MIN_SCALE = 1.0;
        double MAX_SCALE = 10.0;
        double SCROLL_FACTOR = 0.015; // Adjust this value to control the scrolling speed

        // Invert the scroll controls
        deltaYScroll = -deltaYScroll;

        // Get the current mouse position
        double mouseX = scrollEvent.getX();
        double mouseY = scrollEvent.getY();

        // Get the current scale
        double currentScaleX = losSantosMap.getScaleX();
        double currentScaleY = losSantosMap.getScaleY();

        // Zoom in or out based on scroll direction and scrolling speed
        double newScale = currentScaleX + deltaYScroll * SCROLL_FACTOR;
        newScale = clamp(newScale, MIN_SCALE, MAX_SCALE);

        // Adjust the pivot point to keep the zoom centered around the mouse cursor
        double pivotX = mouseX - losSantosMap.getBoundsInParent().getMinX();
        double pivotY = mouseY - losSantosMap.getBoundsInParent().getMinY();
        double pivotDeltaX = (pivotX * (newScale - currentScaleX));
        double pivotDeltaY = (pivotY * (newScale - currentScaleY));

        // Calculate the translation adjustment
        double deltaX = pivotDeltaX - pivotX * (newScale - currentScaleX);
        double deltaY = pivotDeltaY - pivotY * (newScale - currentScaleY);

        // Update the translation
        losSantosMap.setTranslateX(losSantosMap.getTranslateX() + deltaX);
        losSantosMap.setTranslateY(losSantosMap.getTranslateY() + deltaY);

        // Set the new scale
        losSantosMap.setScaleX(newScale);
        losSantosMap.setScaleY(newScale);

        scrollEvent.consume();
    }


    public void mapMouseDrag(MouseEvent mouseEvent) {
        double deltaX = mouseEvent.getSceneX() - lastX;
        double deltaY = mouseEvent.getSceneY() - lastY;

        losSantosMap.setTranslateX(losSantosMap.getTranslateX() + deltaX);
        losSantosMap.setTranslateY(losSantosMap.getTranslateY() + deltaY);

        lastX = mouseEvent.getSceneX();
        lastY = mouseEvent.getSceneY();
    }


    public void mapMousePress(MouseEvent mouseEvent) {
        lastX = mouseEvent.getSceneX();
        lastY = mouseEvent.getSceneY();
    }

    public void onZoomInPressed(MouseEvent mouseEvent) {
        zoom(losSantosMap, 1.5);
    }

    public void onZoomOutPressed(MouseEvent mouseEvent) {
        zoom(losSantosMap, 0.5);
    }

    private void zoom(ImageView imageView, double factor) {
        double currentScaleX = imageView.getScaleX();
        double currentScaleY = imageView.getScaleY();

        double newScaleX = currentScaleX * factor;
        double newScaleY = currentScaleY * factor;

        // Clamp the scale to avoid zooming out too much or zooming in too much
        double MIN_SCALE = 1.0;
        double MAX_SCALE = 10.0;
        newScaleX = clamp(newScaleX, MIN_SCALE, MAX_SCALE);
        newScaleY = clamp(newScaleY, MIN_SCALE, MAX_SCALE);

        imageView.setScaleX(newScaleX);
        imageView.setScaleY(newScaleY);
    }
}