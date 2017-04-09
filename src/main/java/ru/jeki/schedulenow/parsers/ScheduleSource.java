package ru.jeki.schedulenow.parsers;

import ru.jeki.schedulenow.structures.Lessons;

import java.time.LocalDate;
import java.util.Set;

public interface ScheduleSource {

    Set<String> getGroups();
    Lessons getDayLessons(String group, int subgroup, LocalDate date);

}
