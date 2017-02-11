package ru.jeki.schedulenow.processingStages;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import ru.jeki.schedulenow.SpreadsheetScheduleLoader;
import ru.jeki.schedulenow.parsers.spreadsheet.SpreadsheetParser;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class SpreadsheetScheduleProcess implements ScheduleProcess {

    private final URL siteSpreadsheetScheduleLink;
    private ScheduleProcess previousStage;

    public SpreadsheetScheduleProcess(ScheduleProcess previousStage, URL siteSpreadsheetScheduleLink) {
        this.previousStage = previousStage;
        this.siteSpreadsheetScheduleLink = siteSpreadsheetScheduleLink;
    }

    @Override
    public List<ScheduleDay> getSchedule(User user) {
        List<ScheduleDay> previousStageSchedule = previousStage.getSchedule(user);

        InputStream spreadsheetInputStream = getScheduleInputStream();
        readSpreadsheetScheduleDays(previousStageSchedule, user, spreadsheetInputStream);

        return previousStageSchedule;
    }

    private void readSpreadsheetScheduleDays(List<ScheduleDay> previousStageSchedule, User user, InputStream spreadsheetInputStream) {
        try {
            Workbook workbook = new HSSFWorkbook(spreadsheetInputStream);
            SpreadsheetParser spreadsheetParser = new SpreadsheetParser(workbook, user, previousStageSchedule);
            spreadsheetParser.parse();
        } catch (IOException e) {
            throw new IllegalStateException("The main schedule xls file might be invalid.");
        }
    }

    private InputStream getScheduleInputStream() {
        SpreadsheetScheduleLoader spreadsheetScheduleLoader = new SpreadsheetScheduleLoader(siteSpreadsheetScheduleLink);
        return spreadsheetScheduleLoader.getInputStream();
    }
}
