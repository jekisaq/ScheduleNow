package ru.jeki.schedulenow.structures;

public class Lesson {
    private int number;
    private String subject;
    private String cabinet;
    private String teacher;
    private ScheduleDay day;

    public Lesson(int number, String subject, String cabinet, String teacher, ScheduleDay day) {
        this.number = number;
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
}
