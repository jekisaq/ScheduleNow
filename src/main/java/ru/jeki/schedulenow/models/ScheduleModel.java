package ru.jeki.schedulenow.models;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.jeki.schedulenow.ExcelScheduleLoader;
import ru.jeki.schedulenow.parsers.ReplacementsParser;
import ru.jeki.schedulenow.parsers.spreadsheet.SpreadsheetParser;
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

    public void buildSchedule() throws IOException {
        System.out.println("ScheduleModel: Building schedule");

        parseReplacements();
        filterLessonsOnGroupAndSubgroup();
        completeScheduleFromXls();
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
            try {
                Workbook workbook = new HSSFWorkbook(inputStream);
                SpreadsheetParser spreadsheetParser = new SpreadsheetParser(workbook, user, scheduleDays);
                spreadsheetParser.parse();
            } catch (IOException e) {
                throw new IllegalStateException("The main schedule xls file must be invalid.");
            }
        });
    }

    private void filterLessonsOnGroupAndSubgroup() {
        List<Lesson> filteredLessons;
        for (ScheduleDay scheduleDay : scheduleDays) {
            filteredLessons = scheduleDay.lessons().list()
                    .stream()
                    .filter(lesson -> lesson.getGroupName().equalsIgnoreCase(user.getGroupName()))
                    .filter(lesson -> lesson.getSubgroup() == 0 || lesson.getSubgroup() == user.getSubgroup())
                    .collect(Collectors.toList());
            scheduleDay.lessons().list().clear();
            scheduleDay.lessons().list().addAll(filteredLessons);
        }
    }

    public List<Lesson> getScheduleLessons(String scheduleDayName) {
        return scheduleDays.stream()
                .filter(scheduleDay -> scheduleDay.getDayOfWeekName().equalsIgnoreCase(scheduleDayName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("There's no lessons by this day"))
                .lessons().list();
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
