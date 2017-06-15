package ru.jeki.schedulenow.parser.spreadsheet.mapper;

import ru.jeki.schedulenow.entity.Lesson;
import ru.jeki.schedulenow.entity.Lessons;
import ru.jeki.schedulenow.entity.Weeks;
import ru.jeki.schedulenow.parser.spreadsheet.LessonCells;

import java.util.List;

public class NormalLectureLessonCellsMapper implements LessonCellsMapper {

    @Override
    public Lessons map(int lessonNumber, LessonCells lessonCells) {
        Lessons lessons = new Lessons();

        Lesson lesson = new Lesson();
        lesson.setSubject(lessonCells.getCell(1).getContent());

        List<String> splicedDescription = splitStringBySpace(lessonCells.getCell(2).getContent());
        lesson.setTeacher(splicedDescription.get(0));
        lesson.setCabinet(splicedDescription.get(1));
        lesson.setNumber(lessonNumber);
        lesson.setSubgroup(0);
        lesson.setWeek(Weeks.ANY);

        lessons.add(lesson);

        return lessons;
    }
}
