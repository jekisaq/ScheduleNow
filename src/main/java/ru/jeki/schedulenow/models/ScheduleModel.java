package ru.jeki.schedulenow.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.parsers.ReplacementsParser;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScheduleModel {
    private final User user;
    private Map<ScheduleDay, ObservableList<Lesson>> lessonsPerScheduleDay;

    public ScheduleModel(User user) {
        this.user = user;
    }

    public void buildSchedule() {
        System.out.println("ScheduleModel: Building schedule");
        try {
            parseReplacements();

            filterReplacementsOnGroupAndSubgroup();
        } catch (IOException e) {
            AlertBox.display("Schedule now", "Ошибка загрузки расписания");
            e.printStackTrace();
        }
    }

    private void filterReplacementsOnGroupAndSubgroup() {
        for (Map.Entry<ScheduleDay, ObservableList<Lesson>> lessonsOfDayEntry : lessonsPerScheduleDay.entrySet()) {
            ObservableList<Lesson> filteredLessons = lessonsOfDayEntry.getValue()
                    .stream()
                    .filter(lesson -> lesson.getGroupName().equalsIgnoreCase(user.getGroupName()))
                    .filter(lesson -> lesson.getSubgroup() == 0 || lesson.getSubgroup() == user.getSubgroup())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            lessonsOfDayEntry.setValue(filteredLessons);
        }
    }

    private void parseReplacements() throws IOException {
        ReplacementsParser replacementsParser = new ReplacementsParser(getLoadedDocument());
        replacementsParser.parse();
        lessonsPerScheduleDay = replacementsParser.getLessonsPerScheduleDay();
    }

    private Document getLoadedDocument() throws IOException {
        Connection connection = Jsoup.connect("http://ntgmk.ru/view_zamen.php");
        return connection.get();
    }

    public ObservableList<Lesson> getScheduleLessons(String scheduleDayName) {
        Optional<ScheduleDay> key = getKeyByScheduleDayName(scheduleDayName);

        return lessonsPerScheduleDay.get(
                key.orElseThrow(() -> new IllegalArgumentException("There's no lessons by this day")));
    }

    private Optional<ScheduleDay> getKeyByScheduleDayName(String scheduleDayName) {
        return lessonsPerScheduleDay.keySet()
                .stream()
                .filter(scheduleDay -> scheduleDay.getDayOfWeekName().equalsIgnoreCase(scheduleDayName))
                .findAny();
    }


    public List<String> getReplacementDays() {
        return lessonsPerScheduleDay.keySet()
                .stream()
                .map(scheduleDay -> makeFirstSymbolInUpperCase(scheduleDay.getDayOfWeekName()))
                .collect(Collectors.toList());
    }

    private String makeFirstSymbolInUpperCase(String source) {
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }
}
