package ru.jeki.schedulenow.models;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.jeki.schedulenow.ExcelScheduleLoader;
import ru.jeki.schedulenow.parsers.ReplacementsParser;
import ru.jeki.schedulenow.parsers.spreadsheet.SpreadsheetParser;
import ru.jeki.schedulenow.processingStages.ScheduleProcessing;
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
    private ScheduleProcessing scheduleProvider;

    private List<ScheduleDay> scheduleDays = Lists.newArrayList();

    public ScheduleModel(User user) {
        this.user = user;
    }

    public void buildSchedule() throws IOException {
        parseReplacements();
        filterLessonsOnGroupAndSubgroup();
        completeScheduleFromXls();
    }

    public List<ScheduleDay> getScheduleDays() {
        return scheduleDays;
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

}
