package ru.jeki.schedulenow.models;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.ExcelScheduleLoader;
import ru.jeki.schedulenow.parsers.ExcelScheduleParser;
import ru.jeki.schedulenow.parsers.ReplacementsParser;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScheduleModel {
    private final User user;
    private List<ScheduleDay> scheduleDays;

    public ScheduleModel(User user) {
        this.user = user;
    }

    public void buildSchedule() {
        System.out.println("ScheduleModel: Building schedule");
        try {
            parseReplacements();
            completeScheduleFromXls();

            filterLessonsOnGroupAndSubgroup();
        } catch (IOException e) {
            AlertBox.display("Schedule now", "Ошибка загрузки расписания");
            e.printStackTrace();
        }
    }

    private void parseReplacements() throws IOException {
        ReplacementsParser replacementsParser = new ReplacementsParser(getLoadedDocument());
        scheduleDays = replacementsParser.parse();
    }

    private Document getLoadedDocument() throws IOException {
        Connection connection = Jsoup.connect("http://ntgmk.ru/view_zamen.php");
        return connection.get();
    }

    private void completeScheduleFromXls() {
        ExcelScheduleLoader excelScheduleLoader = new ExcelScheduleLoader(user.getLinkToDepartmentSchedule());
        Optional<InputStream> scheduleInputStreamOptional = excelScheduleLoader.loadInputStream();
        scheduleInputStreamOptional.ifPresent(inputStream -> {
            ExcelScheduleParser excelScheduleParser = new ExcelScheduleParser(inputStream, scheduleDays);
            excelScheduleParser.parse();
        });
    }

    private void filterLessonsOnGroupAndSubgroup() {
        List<Lesson> filteredLessons;
        for (ScheduleDay scheduleDay : scheduleDays) {
            filteredLessons = scheduleDay.lessons()
                    .stream()
                    .filter(lesson -> lesson.getGroupName().equalsIgnoreCase(user.getGroupName()))
                    .filter(lesson -> lesson.getSubgroup() == 0 || lesson.getSubgroup() == user.getSubgroup())
                    .collect(Collectors.toList());
            scheduleDay.lessons().clear();
            scheduleDay.lessons().addAll(filteredLessons);
        }
    }

    public List<Lesson> getScheduleLessons(String scheduleDayName) {
        return scheduleDays.stream()
                .filter(scheduleDay -> scheduleDay.getDayOfWeekName().equalsIgnoreCase(scheduleDayName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("There's no lessons by this day"))
                .lessons();
    }

    public List<String> getReplacementDayNames() {
        return scheduleDays
                .stream()
                .map(scheduleDay -> makeFirstSymbolInUpperCase(scheduleDay.getDayOfWeekName()))
                .collect(Collectors.toList());
    }

    private String makeFirstSymbolInUpperCase(String source) {
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }

}
