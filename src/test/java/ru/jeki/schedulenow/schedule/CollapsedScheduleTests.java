package ru.jeki.schedulenow.schedule;

import org.junit.Before;
import org.junit.Test;
import ru.jeki.schedulenow.entity.Lesson;
import ru.jeki.schedulenow.entity.Weeks;
import ru.jeki.schedulenow.parser.ScheduleSourceTester;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class CollapsedScheduleTests {

    private ScheduleSourceTester scheduleSourceTester;

    @Before
    public void setUp() throws Exception {
        CollapsedSchedule collapsedSchedule = new CollapsedSchedule();

        collapsedSchedule.add(new SpreadsheetScheduleMock());
        collapsedSchedule.add(new ReplacementScheduleMock());

        scheduleSourceTester = new ScheduleSourceTester(collapsedSchedule);
    }

    @Test
    public void testMondayGroup454Lessons() {
        String group = "454";
        int subgroup = 1;

        LocalDate testDate = LocalDate.of(2017, 4, 10);

        List<Lesson> expectedLessons = new ArrayList<>();

        expectedLessons.add(new Lesson(1, group, subgroup,
                "англ.яз.", "314-2", "Полуянова", Weeks.NUMERATOR));

        expectedLessons.add(new Lesson(2, group, subgroup,
                "Инж.и компьют.графика", "26-1", "Семенчева В.А.", Weeks.NUMERATOR));

        expectedLessons.add(new Lesson(3, group, subgroup,
                "Прикл.програм.", "217-2", "Карелова", Weeks.NUMERATOR));

        expectedLessons.add(new Lesson(4, group, subgroup,
                "Инстр.сред.разработки ПО", "217-2", "Карелова Р.А.", Weeks.NUMERATOR));

        scheduleSourceTester.testLessons(expectedLessons, group, subgroup, testDate);
    }

}
