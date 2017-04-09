package ru.jeki.schedulenow.parsers.wrappers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import ru.jeki.schedulenow.SpreadsheetScheduleLoader;
import ru.jeki.schedulenow.parsers.spreadsheet.SpreadsheetScheduleParser;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class SpreadsheetScheduleWrapper implements ScheduleWrapper {

    private final String spreadsheetScheduleFileName;
    private ScheduleWrapper previousStage;

    public SpreadsheetScheduleWrapper(ScheduleWrapper previousStage, String spreadsheetScheduleFileName) {
        this.previousStage = previousStage;
        this.spreadsheetScheduleFileName = spreadsheetScheduleFileName;
    }

    @Override
    public List<ScheduleDay> getSchedule(User user) {
        List<ScheduleDay> previousStageSchedule = previousStage.getSchedule(user);

        InputStream spreadsheetInputStream = getScheduleInputStream();
        readSpreadsheetScheduleDays(previousStageSchedule, user, spreadsheetInputStream);

        return previousStageSchedule;
    }

    @Override
    public Set<String> getGroups() {
        return null;
    }

    private void readSpreadsheetScheduleDays(List<ScheduleDay> previousStageSchedule, User user, InputStream spreadsheetInputStream) {
        try {
            Workbook workbook = new HSSFWorkbook(spreadsheetInputStream);
            SpreadsheetScheduleParser spreadsheetSchedule = new SpreadsheetScheduleParser();
            spreadsheetSchedule.parse(workbook);
        } catch (IOException e) {
            throw new IllegalStateException("The main schedule xls file might be invalid.", e);
        }
    }

    private InputStream getScheduleInputStream() {
        SpreadsheetScheduleLoader spreadsheetScheduleLoader = new SpreadsheetScheduleLoader(spreadsheetScheduleFileName);
        return spreadsheetScheduleLoader.getInputStream();
    }
}
