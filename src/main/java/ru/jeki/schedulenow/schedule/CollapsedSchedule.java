package ru.jeki.schedulenow.schedule;

import com.google.common.collect.Lists;
import ru.jeki.schedulenow.entity.Lessons;
import ru.jeki.schedulenow.parser.ScheduleSource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CollapsedSchedule implements ScheduleSource {

    private List<ScheduleSource> scheduleSourceList = Lists.newArrayList();

    public void add(ScheduleSource scheduleSource) {
        scheduleSourceList.add(scheduleSource);
    }

    @Override
    public Set<String> getGroups() {
        return Collections.emptySet();
    }

    @Override
    public Lessons getDayLessons(String group, int subgroup, LocalDate date) {
        Lessons lessons = new Lessons();

        for (ScheduleSource scheduleSource : scheduleSourceList) {
            Lessons dayLessons = scheduleSource.getDayLessons(group, subgroup, date);
            lessons.replaceAll(dayLessons);
        }

        return lessons;
    }

    @Override
    public Set<LocalDate> getDayDates() {
        return Collections.emptySet();
    }
}
