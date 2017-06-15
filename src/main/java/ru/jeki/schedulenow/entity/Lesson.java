package ru.jeki.schedulenow.entity;

import java.util.Objects;

public class Lesson {

    private int number;
    private String groupName;
    private int subgroup;
    private String subject;
    private String cabinet;
    private String teacher;
    private Weeks week;

    public Lesson() {}

    public Lesson(int number, String groupName, int subgroup, String subject, String cabinet, String teacher, Weeks week) {
        this.number = number;
        this.groupName = groupName;
        this.subgroup = subgroup;
        this.subject = subject;
        this.cabinet = cabinet;
        this.teacher = teacher;
        this.week = week;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setSubgroup(int subgroup) {
        this.subgroup = subgroup;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setWeek(Weeks week) {
        this.week = week;
    }

    public int getNumber() {
        return number;
    }

    public String getSubject() {
        return subject;
    }


    public String getTeacher() {
        return teacher;
    }

    // Don't delete getter. Idea raves...
    public String getCabinet() {
        return cabinet;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getSubgroup() {
        return subgroup;
    }

    public Weeks getWeek() {
        return week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return number == lesson.number &&
                (subgroup == 0 || lesson.subgroup == 0 || subgroup == lesson.subgroup) &&
                Objects.equals(groupName, lesson.groupName) &&
                Objects.equals(subject, lesson.subject) &&
                Objects.equals(cabinet, lesson.cabinet) &&
                Objects.equals(teacher, lesson.teacher) &&
                ( week == Weeks.ANY || lesson.week == Weeks.ANY || Objects.equals(week, lesson.week));
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, groupName, subgroup, subject, cabinet, teacher, week);
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
                ", week=" + week +
                '}';
    }
}
