package ru.jeki.schedulenow.spreadsheet;

import com.google.common.collect.Lists;
import ru.jeki.schedulenow.parsers.ScheduleSource;
import ru.jeki.schedulenow.structures.Lessons;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpreadsheetScheduleSource implements ScheduleSource {

    private List<ScheduleSource> scheduleSourceList = Lists.newArrayList();

    public SpreadsheetScheduleSource(List<ScheduleSource> scheduleSourceList) {
        this.scheduleSourceList = scheduleSourceList;
    }

    @Override
    public Set<String> getGroups() {
        Set<String> mergedGroups = new HashSet<>();
        scheduleSourceList.forEach(scheduleSource -> mergedGroups.addAll(scheduleSource.getGroups()));

        return mergedGroups;
    }

    @Override
    public Lessons getDayLessons(String group, int subgroup, LocalDate date) {
        Lessons lessons = new Lessons();

        scheduleSourceList.forEach(scheduleSource -> lessons.addAll(scheduleSource.getDayLessons(group, subgroup, date)));

        return lessons;
    }

    @Override
    public Set<LocalDate> getDayDates() {
        return null;
    }
}
