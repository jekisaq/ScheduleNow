package ru.jeki.schedulenow.parsers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import ru.jeki.schedulenow.structures.ScheduleDay;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExcelScheduleParser {

    private HSSFWorkbook workbook;
    private List<ScheduleDay> parsingDays;

    public ExcelScheduleParser(InputStream workbookFileStream, List<ScheduleDay> parsingDays) {
        try {
            this.workbook = new HSSFWorkbook(workbookFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.parsingDays = parsingDays;
    }

    public void parse() {

    }


}
