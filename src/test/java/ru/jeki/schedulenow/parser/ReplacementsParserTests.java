package ru.jeki.schedulenow.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import ru.jeki.schedulenow.entity.Lesson;
import ru.jeki.schedulenow.entity.Weeks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReplacementsParserTests {

    private ScheduleSourceTester scheduleSourceTester;

    @Before
    public void setUp() throws Exception {

        Document document = Jsoup.parse(getClass().getResourceAsStream("view_zamen.html"),
                "UTF-8", "");

        ReplacementsParser replacementsParser = new ReplacementsParser(document);
        replacementsParser.parse();

        scheduleSourceTester = new ScheduleSourceTester(replacementsParser);
    }

    @Test
    public void testGroup454Lessons() {
        String group = "454";
        int subgroup = 0;

        LocalDate testDate = LocalDate.of(2017, 4, 10);

        List<Lesson> expectedLessons = new ArrayList<>();

        expectedLessons.add(new Lesson(2, "454", 1,
                "Инж.и компьют.графика", "26-1", "Семенчева В.А.", Weeks.NUMERATOR));

        expectedLessons.add(new Lesson(2, "454", 2,
                "Инстр.сред.разработки ПО", "217-2", "Карелова Р.А.", Weeks.NUMERATOR));

        expectedLessons.add(new Lesson(4, "454", 1,
                "Инстр.сред.разработки ПО", "217-2", "Карелова Р.А.", Weeks.NUMERATOR));

        scheduleSourceTester.testLessons(expectedLessons, group, subgroup, testDate);
    }
}
