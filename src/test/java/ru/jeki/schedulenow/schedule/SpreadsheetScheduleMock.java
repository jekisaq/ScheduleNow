package ru.jeki.schedulenow.schedule;

import ru.jeki.schedulenow.entity.Lesson;
import ru.jeki.schedulenow.entity.Lessons;
import ru.jeki.schedulenow.entity.Weeks;
import ru.jeki.schedulenow.parser.ScheduleSource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SpreadsheetScheduleMock implements ScheduleSource {
    @Override
    public Set<String> getGroups() {
        HashSet<String> groups = new HashSet<>();

        groups.add("454");

        return groups;
    }

    @Override
    public Lessons getDayLessons(String group, int subgroup, LocalDate date) {
        Lessons lessons = new Lessons();

        lessons.add(new Lesson(1, group, subgroup,
                "англ.яз.", "314-2", "Полуянова", Weeks.NUMERATOR));

        lessons.add(new Lesson(2, group, subgroup,
                "Технология разраб", "217-2", "Карелова", Weeks.NUMERATOR));

        lessons.add(new Lesson(3, group, subgroup,
                "Прикл.програм.", "217-2", "Карелова", Weeks.NUMERATOR));

        lessons.add(new Lesson(4, group, subgroup,
                "англ.яз.", "414-2", "Зотова", Weeks.NUMERATOR));

        return lessons;
    }

    @Override
    public Set<LocalDate> getDayDates() {
        return Collections.emptySet();
    }
}
