package ru.jeki.schedulenow.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.jeki.schedulenow.models.StartModel;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
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
        scheduleLoadIndicator.setVisible(true);
        departmentMenu.setDisable(false);
        groupNameField.setEditable(false);
        subgroupField.setEditable(false);

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(type -> {
            try {
                return new ScheduleController(getConstructedUser());
            } catch (IOException e1) {
                System.out.println("Controller of schedule has troubles...");
                System.out.println();

                e1.printStackTrace();
            }
            return null;
        });

        try {
            Parent root = loader.load(getClass().getResourceAsStream("/fxml/schedule.fxml"));
            Stage scheduleStage = new Stage();
            scheduleStage.setScene(new Scene(root));
            scheduleStage.show();
        } catch (IOException e1) {
            System.out.println("Scene schedule cannot be opened, showed and so on...");
            System.out.println();

            e1.printStackTrace();
        }

        Stage currentStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private User getConstructedUser() {
        return new User(groupNameField.getText(), subgroupField.getText(), model.getDepartmentSchedule(departmentMenu.getValue()));
    }

}
