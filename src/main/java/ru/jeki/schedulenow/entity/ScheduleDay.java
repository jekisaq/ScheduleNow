package ru.jeki.schedulenow.entity;

public class ScheduleDay {
    private Weeks week;
    private String dayOfWeekName;
    private Lessons lessons = new Lessons();

    public ScheduleDay(Weeks week, String dayOfWeekName) {
        this.week = week;
        this.dayOfWeekName = dayOfWeekName;
    }

    public Lessons lessons() {
        return lessons;
    }

    public Weeks getWeek() {
        return week;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    @Override
    public String toString() {
        return "ScheduleDay{" +
                "week=" + week +
                ", dayOfWeekName='" + dayOfWeekName + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}
