package ru.jeki.schedulenow.parsers.spreadsheet.readers;

import org.apache.poi.ss.usermodel.Cell;
import ru.jeki.schedulenow.structures.ScheduleDayType;

import java.util.List;

public class DistributedLectureLessonCellReader extends NormalLectureLessonCellsReader {
    private final ScheduleDayType scheduleDayType;

    public DistributedLectureLessonCellReader(ScheduleDayType scheduleDayType) {
        this.scheduleDayType = scheduleDayType;
    }

    @Override
    protected String readSubject(List<Cell> lessonCells) {
        if (scheduleDayType == ScheduleDayType.Denominator) {
            return lessonCells.get(2).getStringCellValue();
        }

        return super.readSubject(lessonCells);
    }

    @Override
    protected String readDescription(List<Cell> lessonCells) {
        if (scheduleDayType == ScheduleDayType.Denominator) {
            return lessonCells.get(3).getStringCellValue();
        }

        return super.readDescription(lessonCells);
    }
}
