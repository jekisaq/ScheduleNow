package ru.jeki.schedulenow.parsers.spreadsheet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import ru.jeki.schedulenow.parsers.spreadsheet.readers.DistributedLectureLessonCellReader;
import ru.jeki.schedulenow.parsers.spreadsheet.readers.LabWorkLessonCellReader;
import ru.jeki.schedulenow.parsers.spreadsheet.readers.LessonCellsReader;
import ru.jeki.schedulenow.parsers.spreadsheet.readers.NormalLectureLessonCellsReader;
import ru.jeki.schedulenow.structures.*;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpreadsheetParser {

    private static final int MAX_LESSONS_IN_DAY = 5;
    private static final int CELLS_FOR_A_LESSON = 4;
    private static final int GROUP_CELLS_OFFSET = 7;

    private Logger logger = Logger.getLogger(getClass());

    private Sheet sheet;
    private Workbook workbook;
    private Map<String, Integer> groupNameToColumnNum = Maps.newHashMap();
    private List<ScheduleDay> availableScheduleDays;
    private User user;

    public SpreadsheetParser(Workbook workbook, User user, List<ScheduleDay> availableScheduleDays) {
        this.user = user;
        this.workbook = workbook;
        this.sheet = workbook.getSheetAt(0);
        this.availableScheduleDays = availableScheduleDays;
    }

    public void parse() {
        parseAvailableGroups();
        parseLessonsForAvailableScheduleDays();
    }

    private void parseLessonsForAvailableScheduleDays() {
        int groupColumnNum = getGroupColumnNumInSheet();

        for (ScheduleDay scheduleDay : availableScheduleDays) {
            parseScheduleDay(scheduleDay, groupColumnNum);
        }
    }

    private void parseScheduleDay(ScheduleDay scheduleDay, int groupColumnNum) {
        int beginDayRow = getBeginDayRowNumber(scheduleDay);
        Lessons replacementsDaysLessons = scheduleDay.lessons();
        int dayLessonCount = getLessonCountFor(scheduleDay);

        for (int currentLessonNum = 1; currentLessonNum <= dayLessonCount; currentLessonNum++) {
            if (!replacementsDaysLessons.containsWith(currentLessonNum)) {
                int beginLessonRow = beginDayRow + (currentLessonNum - 1) * CELLS_FOR_A_LESSON;
                List<Cell> lessonCells = getSpreadsheetListedCellsForLesson(groupColumnNum, beginLessonRow);
                Lesson parsedLesson = parseLesson(currentLessonNum, lessonCells, scheduleDay.getDayType());

                if (isAnyParsedPropertyFilled(parsedLesson)) {
                    replacementsDaysLessons.list().add(parsedLesson);
                }
            }
        }
    }

    private boolean isAnyParsedPropertyFilled(Lesson parsedLesson) {

        return !parsedLesson.getSubject().isEmpty() || !parsedLesson.getTeacher().isEmpty();
    }

    private int getLessonCountFor(ScheduleDay scheduleDay) {
        return (scheduleDay.getDayOfWeekName().equalsIgnoreCase("суббота")) ?
                MAX_LESSONS_IN_DAY - 1 : MAX_LESSONS_IN_DAY;
    }

    private List<Cell> getSpreadsheetListedCellsForLesson(int groupColumnNum, int beginLessonRow) {
        List<Cell> list = Lists.newArrayList();


        for (int row = 0; row < 4; row++) {
            Row sheetRow = sheet.getRow(beginLessonRow + row);
            Cell cell = sheetRow.getCell(groupColumnNum);

            if (cell.getCellTypeEnum() == CellType.BLANK) {
                cell.setCellValue("");
            }

            String trimedCellValue = cell.getStringCellValue().trim();
            cell.setCellValue(trimedCellValue);

            list.add(cell);
        }

        return list;
    }

    private Lesson parseLesson(int currentLessonNum, List<Cell> lessonCells, ScheduleDayType scheduleDayType) {
        LessonCellsReader lessonCellsReader = new NormalLectureLessonCellsReader();

        if (isLessonDistributedLecture(lessonCells)) {
            logger.debug("Reading lesson selected as distributed lecture");
            lessonCellsReader = new DistributedLectureLessonCellReader(scheduleDayType);
        } else if (isLessonLabWork(lessonCells)) {
            logger.debug("Reading lesson selected as lab work");
            lessonCellsReader = new LabWorkLessonCellReader(user);
        } else {
            logger.debug("Reading lesson selected as normal lecture");
        }

        logger.debug(lessonCells);

        lessonCellsReader.read(lessonCells);

        return new Lesson(currentLessonNum, user.getGroupName(), user.getSubgroup(),
                lessonCellsReader.getSubject(), lessonCellsReader.getCabinet(), lessonCellsReader.getTeacher());
    }

    private boolean isLessonDistributedLecture(List<Cell> lessonCells) {
        Cell firstCell = lessonCells.get(0);
        Cell thirdCell = lessonCells.get(2);

        return thirdCell.getCellStyle().getBorderTopEnum() == BorderStyle.THIN || hasCellLessonName(firstCell) && hasCellLessonName(thirdCell);
    }

    private boolean hasCellLessonName(Cell cell) {
        return isCellTextBold(cell) && !cell.getStringCellValue().isEmpty();
    }

    private boolean isCellTextBold(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        short fontIndex = cellStyle.getFontIndex();
        Font font = workbook.getFontAt(fontIndex);
        return font.getBold();
    }

    private boolean isLessonLabWork(List<Cell> lessonCells) {
        Cell secondCell = lessonCells.get(1);
        Cell thirdCell = lessonCells.get(2);

        return isCellTeacherDescription(secondCell) && isCellTeacherDescription(thirdCell);
    }

    private boolean isCellTeacherDescription(Cell cell) {
        String lectureDescription = cell.getStringCellValue();
        Pattern descriptionPattern = Pattern.compile("^\\p{IsCyrillic}+(\\s\\d{1,3}-\\d?)?$");
        Matcher matcher = descriptionPattern.matcher(lectureDescription);
        return matcher.find() && !isCellTextBold(cell);
    }



    private int getBeginDayRowNumber(ScheduleDay scheduleDay) {
        String dayOfWeekName = scheduleDay.getDayOfWeekName();
        Map<String, Integer> dayOfWeekToItsNumber = Stream.of(DayOfWeek.values())
                .collect(Collectors.toMap(
                        dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("ru")),
                        DayOfWeek::getValue));

        return (GROUP_CELLS_OFFSET - 1) + (dayOfWeekToItsNumber.get(dayOfWeekName) - 1)
                * MAX_LESSONS_IN_DAY * CELLS_FOR_A_LESSON;
    }


    private Integer getGroupColumnNumInSheet() {
        Integer groupColumnNum = groupNameToColumnNum.get(user.getGroupName());

        if (groupColumnNum == null) {
            throw new IllegalStateException("Group column num cannot be found. ");
        }

        return groupColumnNum;
    }

    private void parseAvailableGroups() {
        Row groupNamesRow = sheet.getRow(5);
        short lastCellNum = groupNamesRow.getLastCellNum();

        for (int i = 2; i < lastCellNum; i++) {
            Cell groupNameCell = groupNamesRow.getCell(i);

            if (groupNameCell.getCellTypeEnum() == CellType.STRING) {
                groupNameToColumnNum.put(groupNameCell.getStringCellValue(), i);
            }
        }
    }


}
