package ru.jeki.schedulenow.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.parsers.ReplacementsParser;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ScheduleModel {
    private final User user;
    private ObservableList<Lesson> scheduleLessons = FXCollections.observableArrayList();
    private ReplacementsParser replacementsParser;

    public ScheduleModel(User user) {
        this.user = user;
    }

    public void buildSchedule() throws IOException {
        // TODO make building schedule
        System.out.println("ScheduleModel: Building schedule");
        try {
            replacementsParser = new ReplacementsParser(user);
            replacementsParser.parse();
        } catch (IOException e) {
            AlertBox.display("Schedule now", "Ошибка загрузки расписания");
            throw new IOException(e);
        }

    }

    public ObservableList<Lesson> getScheduleLessons() {
        return scheduleLessons;
    }


    public List<String> getReplacementDays() {
        return replacementsParser.getParsedData()
                .stream()
                .map(scheduleDay -> scheduleDay.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")))
                .collect(Collectors.toList());
    }
}
