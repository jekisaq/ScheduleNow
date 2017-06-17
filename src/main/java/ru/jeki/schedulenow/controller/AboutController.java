package ru.jeki.schedulenow.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

    @FXML private Button okButton;

    @FXML private void closeStage() {
        Stage aboutStage = (Stage) okButton.getScene().getWindow();
        aboutStage.close();
    }
}
