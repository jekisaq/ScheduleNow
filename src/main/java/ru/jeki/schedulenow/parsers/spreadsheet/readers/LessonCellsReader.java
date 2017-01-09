package ru.jeki.schedulenow.parsers.spreadsheet.readers;

import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public interface LessonCellsReader {
    void read(List<Cell> lessonCells);
    String getSubject();
    String getTeacher();
    String getCabinet();
}
