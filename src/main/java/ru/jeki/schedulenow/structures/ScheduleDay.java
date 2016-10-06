package ru.jeki.schedulenow.structures;

public class ScheduleDay {
    private ScheduleDayType dayType;
    private String dayOfWeekName;

    public ScheduleDay(ScheduleDayType dayType, String dayOfWeekName) {
        this.dayType = dayType;
        this.dayOfWeekName = dayOfWeekName;
    }

    public ScheduleDayType getDayType() {
        return dayType;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }
}
