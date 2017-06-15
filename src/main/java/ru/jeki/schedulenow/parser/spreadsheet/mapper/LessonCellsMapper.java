package ru.jeki.schedulenow.parser.spreadsheet.mapper;

import com.google.common.collect.Lists;
import ru.jeki.schedulenow.entity.Lessons;
import ru.jeki.schedulenow.parser.spreadsheet.LessonCells;

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
