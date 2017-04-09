package ru.jeki.schedulenow.parsers.spreadsheet;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import ru.jeki.schedulenow.parsers.ScheduleSourceTester;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.Weeks;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

public class SpreadsheetParserTests {

    private ScheduleSourceTester scheduleSourceTester;

    @Before
    public void setUp() throws IOException {
        InputStream workbookInputStream = getClass().getResourceAsStream("testWorkbook.xls");
        Workbook workbook = new HSSFWorkbook(workbookInputStream);

        SpreadsheetScheduleParser spreadsheetSchedule = new SpreadsheetScheduleParser();
        spreadsheetSchedule.parse(workbook);

        scheduleSourceTester = new ScheduleSourceTester(spreadsheetSchedule);
    }

    @Test
    public void mondaySpecifiedLessonsFor681Group() {
        String group = "681";
        int subgroup = 1;

        List<Lesson> expectedLessons = Lists.newArrayList();
        expectedLessons.add(new Lesson(1, group, subgroup,
                "Рус.язык", "309-2", "Кайгородова", Weeks.NUMERATOR));
        expectedLessons.add(new Lesson(2, group, subgroup,
                "ОБЖ", "9-1", "Селяхина", Weeks.NUMERATOR));
        expectedLessons.add(new Lesson(3, group, subgroup,
                "математика", "318-2", "Захарова", Weeks.NUMERATOR));
        expectedLessons.add(new Lesson(5, group, subgroup,
                "Математика: алг,", "", "", Weeks.NUMERATOR));

        LocalDate numeratorMonday = LocalDate.of(2017, 4, 10);
        scheduleSourceTester.testLessons(expectedLessons, group, subgroup, numeratorMonday);
    }

    @Test
    public void tuesdaySpecifiedLessonsFor571Group() {
        String group = "571";
        int subgroup = 1;

        List<Lesson> expectedLessons = Lists.newArrayList();
        expectedLessons.add(new Lesson(1, group, subgroup,
                "Материалов.", "18-", "Скоробогатова", Weeks.NUMERATOR));
        expectedLessons.add(new Lesson(2, group, subgroup,
                "БЖ", "21-1", "Смирнова", Weeks.NUMERATOR));
        expectedLessons.add(new Lesson(3, group, subgroup,
                "Осн.экономики орг", "402-2", "Емакаева", Weeks.NUMERATOR));

        LocalDate numeratorTuesday = LocalDate.of(2017, 4, 11);
        scheduleSourceTester.testLessons(expectedLessons, group, subgroup, numeratorTuesday);
    }

    @Test
    public void labWorkReadingTest() {
        String group = "521";
        int subgroup = 2;

        List<Lesson> expectedLessons = Lists.newArrayList();
        expectedLessons.add(new Lesson(1, group, subgroup,
                "информатика", "305-4", "Прокопьева", Weeks.NUMERATOR));
        expectedLessons.add(new Lesson(2, group, subgroup,
                "Устройство автомо", "204-4", "Поминова", Weeks.NUMERATOR));
        expectedLessons.add(new Lesson(3, group, subgroup,
                "Физ-ра", "", "Цыганенко", Weeks.NUMERATOR));

        LocalDate numeratorTuesday = LocalDate.of(2017, 4, 11);
        scheduleSourceTester.testLessons(expectedLessons, group, subgroup, numeratorTuesday);
    }
}
