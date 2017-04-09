package ru.jeki.schedulenow.parsers.spreadsheet.mapper;

import ru.jeki.schedulenow.parsers.spreadsheet.LessonCells;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.Lessons;
import ru.jeki.schedulenow.structures.Weeks;

import java.util.List;

public class DistributedLectureLessonCellMapper implements LessonCellsMapper {

    private NormalLectureLessonCellsMapper normalLectureLessonCellsMapper
            = new NormalLectureLessonCellsMapper();

    @Override
    public Lessons map(int lessonNumber, LessonCells lessonCells) {
        Lessons lessons = normalLectureLessonCellsMapper.map(lessonNumber, lessonCells);

        lessons.setWeek(Weeks.NUMERATOR);

        Lesson lesson = new Lesson();

        lesson.setSubject(lessonCells.getCell(3).getContent());

        List<String> splicedDescription = splitStringBySpace(lessonCells.getCell(4).getContent());

        lesson.setTeacher(splicedDescription.get(0));
        lesson.setCabinet(splicedDescription.get(1));
        lesson.setNumber(lessonNumber);
        lesson.setSubgroup(0);
        lesson.setWeek(Weeks.DENOMINATOR);

        lessons.add(lesson);

        return lessons;
    }
}
