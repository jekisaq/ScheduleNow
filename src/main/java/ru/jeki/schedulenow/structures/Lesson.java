package ru.jeki.schedulenow.structures;

import java.util.Objects;

public class Lesson {
    private int number;
    private String groupName;
    private int subgroup;
    private String subject;
    private String cabinet;
    private String teacher;

    public Lesson(int number, String groupName, int subgroup, String subject, String cabinet, String teacher) {
        this.number = number;
        this.groupName = groupName;
        this.subgroup = subgroup;
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


    public String getGroupName() {
        return groupName;
    }

    public int getSubgroup() {
        return subgroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return number == lesson.number &&
                subgroup == lesson.subgroup &&
                Objects.equals(groupName, lesson.groupName) &&
                Objects.equals(subject, lesson.subject) &&
                Objects.equals(cabinet, lesson.cabinet) &&
                Objects.equals(teacher, lesson.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, groupName, subgroup, subject, cabinet, teacher);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "number=" + number +
                ", groupName='" + groupName + '\'' +
                ", subgroup=" + subgroup +
                ", subject='" + subject + '\'' +
                ", cabinet='" + cabinet + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}
