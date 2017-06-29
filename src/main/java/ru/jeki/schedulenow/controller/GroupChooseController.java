package ru.jeki.schedulenow.controller;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.ScheduleNow;
import ru.jeki.schedulenow.model.ScheduleModel;
import ru.jeki.schedulenow.service.SceneNavigationService;
import ru.jeki.schedulenow.service.Services;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class GroupChooseController implements Controller {

    private ScheduleModel model;
    private Properties configuration;

    private Logger logger = Logger.getLogger(getClass());

    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    @FXML private TextField group;
    @FXML private ChoiceBox<String> subgroup;
    @FXML private Text message;
    @FXML private Text versionContainer;

    public GroupChooseController(Properties configuration) {
        this.configuration = configuration;
    }

    public void initialize(URL location, ResourceBundle resources) {
        releaseVersion();
        releaseSubgroups();
        releaseGroup();
    }

    public void setModel(ScheduleModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model was already set");
        }

        this.model = model;
    }

    private void releaseGroup() {
        group.focusedProperty().addListener((focus, oldValue, fieldFocused) -> {
            if (!fieldFocused) {
                if (!IsGroupFieldValidated()) {
                    group.pseudoClassStateChanged(errorClass, true);
                    message.setText("Указанной группы не существует!");
                } else {
                    group.pseudoClassStateChanged(errorClass, false);
                    message.setText("");
                }
            }
        });

        group.textProperty().bindBidirectional(model.chosenGroupProperty());
    }

    private boolean IsGroupFieldValidated() {
        return model.isGroupExist(group.getText());
    }

    private void releaseSubgroups() {
        subgroup.getItems().addAll("1", "2");
        subgroup.getSelectionModel().selectFirst();

        subgroup.valueProperty().addListener((observable, oldValue, newValue) ->
                model.chosenSubgroupProperty().set(Integer.parseInt(newValue)));
        model.chosenSubgroupProperty().addListener((observable, oldValue, newValue) ->
                subgroup.valueProperty().set(String.valueOf(newValue)));
    }

    @Override
    public void onSceneApply() {}

    public void onShowScheduleRequest() {
        openScheduleWindow();
    }

    private void releaseVersion() {
        versionContainer.setText(configuration.getProperty("version"));
    }


    private void openScheduleWindow() {
        try {
            Services.getService(SceneNavigationService.class).apply("schedule");
        } catch (IllegalStateException e) {
            AlertBox.display("ScheduleSource now", "Произошла ошибка. \nВозможно отсутствует шапка в заменах.");
            logger.error("There's no header in replacements", e);
        }
    }

    @FXML private void openAbout() {
        ScheduleNow.openAbout();
    }

}
