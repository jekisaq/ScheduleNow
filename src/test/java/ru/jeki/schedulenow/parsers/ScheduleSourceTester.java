package ru.jeki.schedulenow.parsers;

import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.Lessons;

import java.time.LocalDate;
import java.util.List;

public class ScheduleSourceTester {


    private final ScheduleSource scheduleSource;

    public ScheduleSourceTester(ScheduleSource scheduleSource) {
        this.scheduleSource = scheduleSource;
    }

    public void testLessons(List<Lesson> expectedLessons, String group, int subgroup, LocalDate date) {
        Lessons parsedLessons = scheduleSource.getDayLessons(group, subgroup, date);

        for (Lesson expectedLesson : expectedLessons) {
            assert parsedLessons.contains(expectedLesson);
        }
    }
}
