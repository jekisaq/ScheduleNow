package ru.jeki.schedulenow.parsers.spreadsheet;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.ScheduleDayType;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SpreadsheetParserTests {

    private Workbook workbook;
    private User user = new User("681", 1, null);

    @Before
    public void setUp() throws IOException {
        InputStream workbookInputStream = getClass().getResourceAsStream("testWorkbook.xls");
        this.workbook = new HSSFWorkbook(workbookInputStream);
    }

    @Test
    public void mondaySpecifiedLessons() {
        List<Lesson> expectedLessons = Lists.newArrayList();
        expectedLessons.add(new Lesson(1, user.getGroupName(), user.getSubgroup(),
                "Рус.язык", "309-2", "Кайгородова"));
        expectedLessons.add(new Lesson(2, user.getGroupName(), user.getSubgroup(),
                "ОБЖ", "9-1", "Селяхина"));
        expectedLessons.add(new Lesson(3, user.getGroupName(), user.getSubgroup(),
                "математика", "318-2", "Захарова"));
        expectedLessons.add(new Lesson(5, user.getGroupName(), user.getSubgroup(),
                "Математика: алг, ", "", ""));

        ScheduleDay scheduleDay = new ScheduleDay(ScheduleDayType.Numerator, "понедельник");
        List<ScheduleDay> scheduleDays = Lists.newArrayList(
                scheduleDay);

        SpreadsheetParser spreadsheetParser = new SpreadsheetParser(workbook, user, scheduleDays);
        spreadsheetParser.parse();

        List<Lesson> parsedLessons = scheduleDay.lessons().list();

        for (int i = 0; i <= 3; i++) {
            Lesson expectedLesson = expectedLessons.get(i);
            Lesson parsedLesson = parsedLessons.get(i);
            assertEquals(expectedLesson, parsedLesson);
        }
    }
}
