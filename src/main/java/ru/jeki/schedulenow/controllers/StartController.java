package ru.jeki.schedulenow.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.models.StartModel;
import ru.jeki.schedulenow.services.SceneNavigationService;
import ru.jeki.schedulenow.services.Services;
import ru.jeki.schedulenow.structures.User;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class StartController implements Controller {

    private StartModel model = new StartModel();
    private Properties configuration;

    private Logger logger = Logger.getLogger(getClass());

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

    @Override
    public void onSceneApply() {}

    public User getConstructedUser() {
        return new User(groupNameField.getText(), Integer.valueOf(subgroupField.getText()), model.getDepartmentSchedule(departmentMenu.getValue()));
    }

    public void onShowScheduleRequest() {
        scheduleLoadIndicator.setVisible(true);
        departmentMenu.setDisable(false);
        groupNameField.setEditable(false);
        subgroupField.setEditable(false);

        openScheduleWindow();
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

    private void openScheduleWindow() {
        try {
            Services.getService(SceneNavigationService.class).apply("schedule");
        } catch (IllegalStateException e) {
            AlertBox.display("Schedule now", "Произошла ошибка. \nВозможно отсутствует шапка в заменах.");
            logger.error("There's no header in replacements", e);
        }
    }
}
