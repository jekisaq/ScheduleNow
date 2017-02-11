package ru.jeki.schedulenow.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.models.StartModel;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    private StartModel model = new StartModel();
    private Properties configuration;

    @FXML private TextField groupNameField;
    @FXML private TextField subgroupField;
    @FXML private ChoiceBox<String> departmentMenu;
    @FXML private ProgressIndicator scheduleLoadIndicator;
    @FXML private Text versionContainer;

    public StartController(Properties configuration) {
        this.configuration = configuration;
    }

    public void initialize(URL location, ResourceBundle resources) {
        addVersionToForm();

        model.loadDepartments();
        addDepartmentsToForm();

        wireFieldsWithCacheService();
        model.fillProperties();
    }

    private void addVersionToForm() {
        versionContainer.setText(configuration.getProperty("version"));
    }

    private void wireFieldsWithCacheService() {
        StringProperty departmentMenuProperty = getBuiltDepartmentMenuProperty();

        model.wireFieldsWithCacheService(
                        groupNameField.textProperty(),
                        subgroupField.textProperty(),
                        departmentMenuProperty);
    }

    private StringProperty getBuiltDepartmentMenuProperty() {
        StringProperty departmentMenuProperty = new SimpleStringProperty(
                departmentMenu, "text",
                String.valueOf(departmentMenu.getSelectionModel().getSelectedIndex()));

        departmentMenuProperty.addListener((observable, oldValue, newValue) -> {
            int index;

            try {
                index = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                index = 0;
            }

            departmentMenu.getSelectionModel().select(index);
        });

        departmentMenu.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> departmentMenuProperty.setValue(String.valueOf(newValue.intValue())));

        return departmentMenuProperty;
    }

    private void addDepartmentsToForm() {
        departmentMenu.getItems().addAll(
                model.getDepartmentNameSet()
        );

        departmentMenu.setValue(departmentMenu.getItems().get(0));
    }


    public void onShowScheduleRequest(ActionEvent event) {
        scheduleLoadIndicator.setVisible(true);
        departmentMenu.setDisable(false);
        groupNameField.setEditable(false);
        subgroupField.setEditable(false);

        openScheduleWindow();

        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void openScheduleWindow() {
        try {
            tryToOpenScheduleWindow();
        } catch (IOException e) {
            System.out.println("Scene schedule cannot be opened, showed and so on...");
            System.out.println();

            e.printStackTrace();
        } catch (IllegalStateException e) {
            AlertBox.display("Schedule now", "Произошла ошибка. \nВозможно отсутствует шапка в заменах.");
            System.out.println("There's no header in replacements");
            System.out.println();

            e.printStackTrace();
        }
    }

    private void tryToOpenScheduleWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(type -> new ScheduleController(getConstructedUser(), configuration));
        Parent root = loader.load(getClass().getResourceAsStream("/fxml/schedule.fxml"));
        Stage scheduleStage = new Stage();

        scheduleStage.setResizable(false);
        scheduleStage.setFullScreen(false);
        scheduleStage.setTitle(configuration.getProperty("form.title"));

        scheduleStage.setScene(new Scene(root));
        scheduleStage.show();
    }

    private User getConstructedUser() {
        return new User(groupNameField.getText(), Integer.valueOf(subgroupField.getText()), model.getDepartmentSchedule(departmentMenu.getValue()));
    }

}
