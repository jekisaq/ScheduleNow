package ru.jeki.schedulenow.structures;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class Lessons {
    private List<Lesson> lessonList = Lists.newArrayList();

    public Lessons() {}

    public Lessons(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    public List<Lesson> list() {
        return lessonList;
    }

    public void addAll(Lessons lessons) {
        this.lessonList.addAll(lessons.lessonList);
    }

    public void add(Lesson lesson) {
        lessonList.add(lesson);
    }

    public boolean contains(Lesson requested) {
        return lessonList.stream().anyMatch(lesson -> lesson.equals(requested));
    }

    public Lessons filterBy(String group) {
        return new Lessons(lessonList.stream()
                .filter(lesson -> lesson.getGroupName().equalsIgnoreCase(group))
                .collect(Collectors.toList()));
    }

    public Lessons filterBy(int subgroup) {
        if (subgroup == 0) {
            return this;
        }

        return new Lessons(lessonList.stream()
                .filter(lesson -> lesson.getSubgroup() == 0 || lesson.getSubgroup() == subgroup)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "Weeks{" +
                "lessonList=" + lessonList +
                '}';
    }

    public void removeEmptyLessons() {
        lessonList = lessonList.stream().filter(this::isAnyParsedPropertyFilled).collect(Collectors.toList());
    }

    private boolean isAnyParsedPropertyFilled(Lesson parsedLesson) {

        return !parsedLesson.getSubject().isEmpty() || !parsedLesson.getTeacher().isEmpty();
    }

    public void setSubgroup(int subgroup) {
        lessonList.forEach(lesson -> lesson.setSubgroup(subgroup));
    }

    public void setWeek(Weeks week) {
        lessonList.forEach(lesson -> lesson.setWeek(week));
    }

    public void setGroup(String group) {
        lessonList.forEach(lesson -> lesson.setGroupName(group));
    }
}
