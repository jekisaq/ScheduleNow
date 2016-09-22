package ru.jeki.schedulenow.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import ru.jeki.schedulenow.models.StartModel;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    private StartModel model = new StartModel();

    @FXML private TextField groupNameField;
    @FXML private TextField subgroupField;
    @FXML private ChoiceBox<String> departmentMenu;
    @FXML private ProgressIndicator scheduleLoadIndicator;

    public void initialize(URL location, ResourceBundle resources) {
        model.loadDepartments();
        addDepartmentsToForm();

        model.wiringFieldWithCacheService(groupNameField.textProperty(), subgroupField.textProperty());
        model.fillProperties();
    }



    private void addDepartmentsToForm() {
        departmentMenu.getItems().addAll(
                model.getDepartmentNameSet()
        );

        departmentMenu.setValue(departmentMenu.getItems().get(0));
    }


    public void onShowScheduleRequest(ActionEvent e) {


    }
}
