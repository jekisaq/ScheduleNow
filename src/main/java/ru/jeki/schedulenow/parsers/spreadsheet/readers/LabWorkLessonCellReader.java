package ru.jeki.schedulenow.parsers.spreadsheet.readers;

import org.apache.poi.ss.usermodel.Cell;
import ru.jeki.schedulenow.structures.User;

import java.util.List;

public class LabWorkLessonCellReader extends NormalLectureLessonCellsReader {

    private final User user;

    public LabWorkLessonCellReader(User user) {
        this.user = user;
    }

    @Override
    protected String readDescription(List<Cell> lessonCells) {
        return lessonCells.get(user.getSubgroup()).getStringCellValue();
    }
}
