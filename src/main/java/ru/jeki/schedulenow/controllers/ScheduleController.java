package ru.jeki.schedulenow.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.models.ScheduleModel;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ScheduleController implements Controller, Initializable {

    private final Supplier<User> userSupplier;
    private ScheduleModel model;
    private List<ScheduleDay> scheduleDays;

    @FXML private TableView<Lesson> scheduleTable;

    @FXML private TableColumn<Lesson, Integer> lessonNumber;
    @FXML private TableColumn<Lesson, String> subject;
    @FXML private TableColumn<Lesson, String> cabinet;
    @FXML private TableColumn<Lesson, String> teacher;

    @FXML private ListView<String> daysListView;

    public ScheduleController(Supplier<User> userSupplier, Properties configuration) {
        this.userSupplier = userSupplier;
        this.model = new ScheduleModel(configuration);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lessonNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        cabinet.setCellValueFactory(new PropertyValueFactory<>("cabinet"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
    }

    @Override
    public void onSceneApply() {
        try {
            model.buildSchedule(userSupplier.get());
        } catch (IOException | IllegalStateException e) {
            AlertBox.display("ScheduleSource Now - Ошибка", "Возникла ошибка: \n" + e.getLocalizedMessage());
            e.printStackTrace();
        }

        scheduleDays = model.getScheduleDays();
        daysListView.getItems().addAll(getReplacementDayNames());
    }

    private List<String> getReplacementDayNames() {
        return scheduleDays
                .stream()
                .map(ScheduleDay::getDayOfWeekName)
                .map(this::makeFirstSymbolInUpperCase)
                .collect(Collectors.toList());
    }

    private String makeFirstSymbolInUpperCase(String source) {
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }

    @FXML private void onDayOfWeekChosen() {
        String selectedDayName = daysListView.getSelectionModel().getSelectedItem();
        List<Lesson> scheduleLessons = getScheduleDay(selectedDayName);
        scheduleLessons.sort(Comparator.comparingInt(Lesson::getNumber));
        scheduleTable.getItems().setAll(scheduleLessons);
    }

    private List<Lesson> getScheduleDay(String scheduleDayName) {
        return scheduleDays.stream()
                .filter(scheduleDay -> scheduleDay.getDayOfWeekName().equalsIgnoreCase(scheduleDayName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("There's no lessons by this day"))
                .lessons().list();
    }
}
