package ru.jeki.schedulenow.structures;

public class ScheduleDay {
    private ScheduleDayType dayType;
    private String dayOfWeekName;
    private Lessons lessons = new Lessons();

    public ScheduleDay(ScheduleDayType dayType, String dayOfWeekName) {
        this.dayType = dayType;
        this.dayOfWeekName = dayOfWeekName;
    }

    public Lessons lessons() {
        return lessons;
    }

    public ScheduleDayType getDayType() {
        return dayType;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    @Override
    public String toString() {
        return "ScheduleDay{" +
                "dayType=" + dayType +
                ", dayOfWeekName='" + dayOfWeekName + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}
