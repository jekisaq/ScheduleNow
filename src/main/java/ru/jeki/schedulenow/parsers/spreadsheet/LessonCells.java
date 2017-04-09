package ru.jeki.schedulenow.parsers.spreadsheet;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import ru.jeki.schedulenow.parsers.spreadsheet.mapper.DistributedLectureLessonCellMapper;
import ru.jeki.schedulenow.parsers.spreadsheet.mapper.LabWorkLessonCellMapper;
import ru.jeki.schedulenow.parsers.spreadsheet.mapper.LessonCellsMapper;
import ru.jeki.schedulenow.parsers.spreadsheet.mapper.NormalLectureLessonCellsMapper;
import ru.jeki.schedulenow.structures.Lessons;

import java.util.List;

public class LessonCells {

    private Logger logger = Logger.getLogger(getClass());

    private List<LocalCell> lessonCells;


    LessonCells(List<LocalCell> lessonCells) {
        this.lessonCells = lessonCells;
    }

    Lessons toLessons(int lesson) {
        LessonCellsMapper lessonCellsMapper = new NormalLectureLessonCellsMapper();

        if (isLessonDistributedLecture()) {
            logger.debug("Reading lesson selected as distributed lecture");
            lessonCellsMapper = new DistributedLectureLessonCellMapper();
        } else if (isLessonLabWork()) {
            logger.debug("Reading lesson selected as lab work");
            lessonCellsMapper = new LabWorkLessonCellMapper();
        } else {
            logger.debug("Reading lesson selected as normal lecture");
        }

        logger.debug(lessonCells);

        return lessonCellsMapper.map(lesson, this);
    }

    public LocalCell getCell(int cell) {
        return lessonCells.get(cell - 1);
    }

    private boolean isLessonDistributedLecture() {
        LocalCell firstCell = getCell(1);
        LocalCell thirdCell = getCell(3);

        return thirdCell.isBorderTopStyle(BorderStyle.THIN) ||
                firstCell.hasLessonName() && thirdCell.hasLessonName();
    }

    private boolean isLessonLabWork() {
        LocalCell secondCell = getCell(2);
        LocalCell thirdCell = getCell(3);

        return secondCell.isTeacherDescription() && thirdCell.isTeacherDescription();
    }
}
