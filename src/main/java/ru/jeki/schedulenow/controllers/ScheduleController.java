package ru.jeki.schedulenow.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.models.ScheduleModel;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    private ScheduleModel model;

    @FXML private TableView<Lesson> scheduleTable;

    @FXML private TableColumn<Lesson, Integer> lessonNumber;
    @FXML private TableColumn<Lesson, String> subject;
    @FXML private TableColumn<Lesson, String> cabinet;
    @FXML private TableColumn<Lesson, String> teacher;

    @FXML private ListView<String> daysListView;

    ScheduleController(User user) {
        this.model = new ScheduleModel(user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            model.buildSchedule();
        } catch (IOException | IllegalStateException e) {
            AlertBox.display("Schedule Now - Ошибка", "Возникла ошибка: \n" + e.getLocalizedMessage());
            e.printStackTrace();
        }

        lessonNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        cabinet.setCellValueFactory(new PropertyValueFactory<>("cabinet"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));

        daysListView.getItems().addAll(model.getReplacementDayNames());
    }

    @FXML private void onDayOfWeekChosen(Event event) {
        String selectedDayName = daysListView.getSelectionModel().getSelectedItem();
        List<Lesson> scheduleLessons = model.getScheduleLessons(selectedDayName);
        scheduleLessons.sort(Comparator.comparingInt(Lesson::getNumber));
        scheduleTable.getItems().setAll(scheduleLessons);
    }
}
