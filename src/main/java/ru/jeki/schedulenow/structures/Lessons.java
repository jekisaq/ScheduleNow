package ru.jeki.schedulenow.structures;

import com.google.common.collect.Lists;

import java.util.List;

public class Lessons {
    private List<Lesson> lessons = Lists.newArrayList();

    public List<Lesson> list() {
        return lessons;
    }

    public boolean containsWith(int currentLessonNum) {
        return lessons.stream().anyMatch(lesson -> lesson.getNumber() == currentLessonNum);
    }

    @Override
    public String toString() {
        return "Lessons{" +
                "lessons=" + lessons +
                '}';
    }
}
