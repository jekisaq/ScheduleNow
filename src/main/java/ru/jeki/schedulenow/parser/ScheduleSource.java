package ru.jeki.schedulenow.parser;

import ru.jeki.schedulenow.entity.Lessons;

import java.time.LocalDate;
import java.util.Set;

public interface ScheduleSource {

    Set<String> getGroups();
    Lessons getDayLessons(String group, int subgroup, LocalDate date);
    Set getDayDates();

}
