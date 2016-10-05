package ru.jeki.schedulenow.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.jeki.schedulenow.models.ScheduleModel;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    private ScheduleModel model;

    @FXML private TableView<Lesson> scheduleTable;

    @FXML private TableColumn lessonNumber;
    @FXML private TableColumn subject;
    @FXML private TableColumn cabinet;
    @FXML private TableColumn teacher;

    @FXML private ListView<String> daysListView;

    public ScheduleController(User user) throws IOException {
        this.model = new ScheduleModel(user);
        model.buildSchedule();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lessonNumber.setCellValueFactory(new PropertyValueFactory<Lesson, Integer>("number"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        cabinet.setCellValueFactory(new PropertyValueFactory<>("cabinet"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));

        scheduleTable.getItems().addAll(model.getScheduleLessons());
        daysListView.getItems().addAll(model.getReplacementDays());
    }

    @FXML private void onDayOfWeekChosen(Event event) {
        System.out.println("DayOfWeek has been chose");
    }
}
