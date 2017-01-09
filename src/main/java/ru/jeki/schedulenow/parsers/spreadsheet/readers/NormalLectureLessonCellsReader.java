package ru.jeki.schedulenow.parsers.spreadsheet.readers;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class NormalLectureLessonCellsReader implements LessonCellsReader {
    private String subject;
    private String teacher;
    private String cabinet;

    @Override
    public void read(List<Cell> lessonCells) {
        subject = readSubject(lessonCells);

        String description = readDescription(lessonCells);

        List<String> splitDescription = splitStringBySpace(description);
        teacher = splitDescription.get(0);
        cabinet = splitDescription.get(1);
    }

    protected String readSubject(List<Cell> lessonCells) {
        return lessonCells.get(0).getStringCellValue();
    }

    protected String readDescription(List<Cell> lessonCells) {
        return lessonCells.get(1).getStringCellValue();
    }

    private List<String> splitStringBySpace(String s) {
        // \\s - space symbol
        List<String> split = Lists.newArrayList(s.split("\\s"));
        while (split.size() < 2) {
            split.add("");
        }

        return split;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getTeacher() {
        return teacher;
    }

    @Override
    public String getCabinet() {
        return cabinet;
    }
}
