package ru.jeki.schedulenow.structures;

public class Lesson {
    private int number;
    private String groupName;
    private int subgroup;
    private String subject;
    private String cabinet;
    private String teacher;
    private ScheduleDay day;

    public Lesson(int number, String groupName, int subgroup, String subject, String cabinet, String teacher, ScheduleDay day) {
        this.number = number;
        this.groupName = groupName;
        this.subgroup = subgroup;
        this.subject = subject;
        this.cabinet = cabinet;
        this.teacher = teacher;
        this.day = day;
    }

    public int getNumber() {
        return number;
    }

    public String getSubject() {
        return subject;
    }

    public String getCabinet() {
        return cabinet;
    }

    public String getTeacher() {
        return teacher;
    }

    public ScheduleDay getDay() {
        return day;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getSubgroup() {
        return subgroup;
    }
}
