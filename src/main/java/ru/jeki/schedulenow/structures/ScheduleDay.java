package ru.jeki.schedulenow.structures;

import com.google.common.collect.Lists;

import java.util.List;

public class ScheduleDay {
    private ScheduleDayType dayType;
    private String dayOfWeekName;
    private List<Lesson> lessons = Lists.newArrayList();

    public ScheduleDay(ScheduleDayType dayType, String dayOfWeekName) {
        this.dayType = dayType;
        this.dayOfWeekName = dayOfWeekName;
    }

    public List<Lesson> lessons() {
        return lessons;
    }

    public ScheduleDayType getDayType() {
        return dayType;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }
}
