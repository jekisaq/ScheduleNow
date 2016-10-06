package ru.jeki.schedulenow.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.parsers.ReplacementsParser;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.util.List;
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
                .map(scheduleDay -> makeFirstSymbolInUpperCase(scheduleDay.getDayOfWeekName()))
                .collect(Collectors.toList());
    }

    private String makeFirstSymbolInUpperCase(String source) {
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }
}
