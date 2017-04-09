package ru.jeki.schedulenow.parsers.spreadsheet.mapper;

import ru.jeki.schedulenow.parsers.spreadsheet.LessonCells;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.Lessons;
import ru.jeki.schedulenow.structures.Weeks;

import java.util.List;

public class LabWorkLessonCellMapper implements LessonCellsMapper {

    private NormalLectureLessonCellsMapper normalLectureLessonCellsMapper
            = new NormalLectureLessonCellsMapper();

//    protected String readDescription(List<Cell> lessonCells) {
//        return lessonCells.get(user.getSubgroup()).getStringCellValue();
//    }

    @Override
    public Lessons map(int lessonNumber, LessonCells lessonCells) {
        Lessons lessons = normalLectureLessonCellsMapper.map(lessonNumber, lessonCells);

        lessons.setSubgroup(1);

        Lesson lesson = new Lesson();
        lesson.setSubject(lessonCells.getCell(1).getContent());
        lesson.setSubgroup(2);
        lesson.setNumber(lessonNumber);

        List<String> splicedDescription = splitStringBySpace(lessonCells.getCell(3).getContent());
        lesson.setTeacher(splicedDescription.get(0));
        lesson.setCabinet(splicedDescription.get(1));
        lesson.setWeek(Weeks.ANY);

        lessons.add(lesson);


        return lessons;
    }
}
