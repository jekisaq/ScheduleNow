package ru.jeki.schedulenow.schedule;

import ru.jeki.schedulenow.parsers.ScheduleSource;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.Lessons;
import ru.jeki.schedulenow.structures.Weeks;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ReplacementScheduleMock implements ScheduleSource {
    @Override
    public Set<String> getGroups() {
        HashSet<String> groups = new HashSet<>();

        groups.add("454");

        return groups;
    }

    @Override
    public Lessons getDayLessons(String group, int subgroup, LocalDate date) {
        Lessons lessons = new Lessons();

        lessons.add(new Lesson(2, group, subgroup,
                "Инж.и компьют.графика", "26-1", "Семенчева В.А.", Weeks.NUMERATOR));

        lessons.add(new Lesson(4, group, subgroup,
                "Инстр.сред.разработки ПО", "217-2", "Карелова Р.А.", Weeks.NUMERATOR));

        return lessons;
}

    @Override
    public Set<LocalDate> getDayDates() {
        return null;
    }

}