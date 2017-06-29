package ru.jeki.schedulenow.entity;

import javafx.scene.image.Image;

public enum Source {
    MAIN, REPLACE("icon-replace.png");

    private Image image;

    Source(String imageFileName) {
        image = new Image("ico/" + imageFileName);
    }

    Source () {
        image = null;
    }

    public Image getImage() {
        return image;
    }
}
