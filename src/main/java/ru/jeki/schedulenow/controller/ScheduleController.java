package ru.jeki.schedulenow.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.converter.LocalDateStringConverter;
import ru.jeki.schedulenow.entity.Lesson;
import ru.jeki.schedulenow.model.ScheduleModel;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ScheduleController implements Controller, Initializable {

    private ScheduleModel model;

    @FXML private TableView<Lesson> scheduleTable;

    @FXML private TableColumn<Lesson, Integer> lessonNumber;
    @FXML private TableColumn<Lesson, String> subject;
    @FXML private TableColumn<Lesson, String> cabinet;
    @FXML private TableColumn<Lesson, String> teacher;

    @FXML private ListView<LocalDate> daysListView;

    public void setModel(ScheduleModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model was already set");
        }

        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lessonNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        cabinet.setCellValueFactory(new PropertyValueFactory<>("cabinet"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));

        daysListView.setCellFactory(TextFieldListCell.forListView(new LocalDateStringConverter(FormatStyle.MEDIUM)));

        model.chosenDateProperty().bind(daysListView.getSelectionModel().selectedItemProperty());
        scheduleTable.itemsProperty().bind(model.lessonListProperty());
    }

    @Override
    public void onSceneApply() {
        daysListView.getItems().addAll(model.scheduleDays().sorted(Comparator.reverseOrder()));
    }

}
