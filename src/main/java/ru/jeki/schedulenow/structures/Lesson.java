package ru.jeki.schedulenow.structures;

public class Lesson {
    private int number;
    private String subject;
    private String cabinet;
    private String teacher;

    public Lesson(int number, String subject, String cabinet, String teacher) {
        this.number = number;
        this.subject = subject;
        this.cabinet = cabinet;
        this.teacher = teacher;
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
}
