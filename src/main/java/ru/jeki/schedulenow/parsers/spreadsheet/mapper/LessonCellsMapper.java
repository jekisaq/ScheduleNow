package ru.jeki.schedulenow.parsers.spreadsheet.mapper;

import com.google.common.collect.Lists;
import ru.jeki.schedulenow.parsers.spreadsheet.LessonCells;
import ru.jeki.schedulenow.structures.Lessons;

import java.util.List;

public interface LessonCellsMapper {
    Lessons map(int lessonNumber, LessonCells lessonCells);

    default List<String> splitStringBySpace(String s) {
        // \\s - space symbol
        List<String> split = Lists.newArrayList(s.split("\\s"));
        while (split.size() < 2) {
            split.add("");
        }

        return split;
    }
}
