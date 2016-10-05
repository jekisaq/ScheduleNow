package ru.jeki.schedulenow.structures;

import com.google.common.collect.Lists;

import java.time.DayOfWeek;
import java.util.List;

public class ScheduleDay {
    private ScheduleDayType dayType;
    private DayOfWeek dayOfWeek;

    public ScheduleDay(ScheduleDayType dayType, String nameDayOfWeek) {
        this.dayType = dayType;
        this.dayOfWeek = getDayOfWeekNameInRussian(nameDayOfWeek);
    }

    private DayOfWeek getDayOfWeekNameInRussian(String nameDayOfWeek) {
        final List<String> daysOfWeekInRus = Lists.newArrayList(
                "понедельник",
                "вторник",
                "среда",
                "четверг",
                "пятница",
                "суббота",
                "воскресенье");

        return DayOfWeek.of(daysOfWeekInRus.indexOf(nameDayOfWeek) + 1);
    }

    public ScheduleDayType getDayType() {
        return dayType;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
