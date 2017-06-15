package ru.jeki.schedulenow.parser.spreadsheet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.*;
import ru.jeki.schedulenow.entity.Lessons;
import ru.jeki.schedulenow.entity.ScheduleDay;
import ru.jeki.schedulenow.entity.Weeks;
import ru.jeki.schedulenow.parser.ScheduleSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class SpreadsheetScheduleParser implements ScheduleSource {

    private static final int MAX_LESSONS_IN_DAY = 5;
    private static final int CELLS_FOR_A_LESSON = 4;
    private static final int GROUP_CELLS_OFFSET = 6;
    private static final int ROW_GROUPS = 5;

    private Sheet sheet;
    private Workbook workbook;
    private Map<String, Integer> groupToColumnNum = Maps.newHashMap();
    private Map<String, List<ScheduleDay>> groupToScheduleDays = Maps.newHashMap();

    public SpreadsheetScheduleParser() {}

    public void parse(Workbook workbook) {
        this.workbook = workbook;
        this.sheet = workbook.getSheetAt(0);

        parseAvailableGroups();
        buildScheduleDayPlaceholders();
        groupToScheduleDays.forEach(this::parseScheduleDays);
    }

    private void buildScheduleDayPlaceholders() {
        groupToScheduleDays = getGroups().stream()
                .collect(Collectors.toMap(Function.identity(), group -> getScheduleDaysPlaceholders()));
    }

    private ArrayList<ScheduleDay> getScheduleDaysPlaceholders() {
        ArrayList<ScheduleDay> scheduleDays = Lists.newArrayList();
        scheduleDays.add(new ScheduleDay(Weeks.NUMERATOR, "понедельник"));
        scheduleDays.add(new ScheduleDay(Weeks.NUMERATOR, "вторник"));
        scheduleDays.add(new ScheduleDay(Weeks.NUMERATOR, "среда"));
        scheduleDays.add(new ScheduleDay(Weeks.NUMERATOR, "четверг"));
        scheduleDays.add(new ScheduleDay(Weeks.NUMERATOR, "пятница"));
        scheduleDays.add(new ScheduleDay(Weeks.NUMERATOR, "суббота"));
        scheduleDays.add(new ScheduleDay(Weeks.DENOMINATOR, "понедельник"));
        scheduleDays.add(new ScheduleDay(Weeks.DENOMINATOR, "вторник"));
        scheduleDays.add(new ScheduleDay(Weeks.DENOMINATOR, "среда"));
        scheduleDays.add(new ScheduleDay(Weeks.DENOMINATOR, "четверг"));
        scheduleDays.add(new ScheduleDay(Weeks.DENOMINATOR, "пятница"));
        scheduleDays.add(new ScheduleDay(Weeks.DENOMINATOR, "суббота"));

        return scheduleDays;
    }

    @Override
    public Lessons getDayLessons(String group, int subgroup, LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));
        Weeks week = Weeks.of(date);

        List<ScheduleDay> groupScheduleDays = groupToScheduleDays.get(group);

        ScheduleDay day = groupScheduleDays.stream()
                .filter(scheduleDay -> scheduleDay.getDayOfWeekName().equalsIgnoreCase(dayOfWeek))
                .filter(scheduleDay -> scheduleDay.getWeek().equals(week))
        .findAny().orElseThrow(IllegalArgumentException::new);

        return day.lessons().filterBy(subgroup);
    }

    @Override
    public Set<LocalDate> getDayDates() {
        return null;
    }

    public Set<String> getGroups() {
        return groupToColumnNum.keySet();
    }

    private void parseScheduleDays(String group, List<ScheduleDay> scheduleDays) {
        for (ScheduleDay scheduleDay : scheduleDays) {
            parseScheduleDay(group, scheduleDay);
        }
    }

    private void parseScheduleDay(String group, ScheduleDay scheduleDay) {
        Lessons parsingLessons = scheduleDay.lessons();
        int dayLessonCount = getLessonCountFor(scheduleDay);

        for (int iLesson = 1; iLesson <= dayLessonCount; iLesson++) {
            LessonCells lessonCells = getSpreadsheetListedCellsForLesson(group, scheduleDay, iLesson);
            Lessons parsedLesson = lessonCells.toLessons(iLesson);
            parsedLesson.removeEmptyLessons();
            parsedLesson.setGroup(group);

            parsingLessons.addAll(parsedLesson);
        }
    }

    private int getLessonCountFor(ScheduleDay scheduleDay) {
        return (scheduleDay.getDayOfWeekName().equalsIgnoreCase("суббота")) ?
                MAX_LESSONS_IN_DAY - 1 : MAX_LESSONS_IN_DAY;
    }

    private LessonCells getSpreadsheetListedCellsForLesson(String group, ScheduleDay scheduleDay, int lesson) {
        int groupColumnNum = getGroupColumnNumInSheet(group);
        int beginDayRow = getBeginDayRowNumber(scheduleDay);
        int beginLessonRow = beginDayRow + (lesson - 1) * CELLS_FOR_A_LESSON;

        List<LocalCell> list = Lists.newArrayList();

        for (int row = 0; row < 4; row++) {
            Row sheetRow = sheet.getRow(beginLessonRow + row);
            Cell cell = sheetRow.getCell(groupColumnNum);

            if (cell.getCellTypeEnum() == CellType.BLANK) {
                cell.setCellValue("");
            }

            String trimmedCellValue = cell.getStringCellValue().trim();
            cell.setCellValue(trimmedCellValue);

            list.add(new LocalCell(workbook, cell));
        }

        return new LessonCells(list);
    }

    private int getBeginDayRowNumber(ScheduleDay scheduleDay) {
        String dayOfWeekName = scheduleDay.getDayOfWeekName();
        Map<String, Integer> dayOfWeekToNumber = Stream.of(DayOfWeek.values())
                .collect(Collectors.toMap(
                        dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("ru")),
                        DayOfWeek::getValue));

        return GROUP_CELLS_OFFSET + (dayOfWeekToNumber.get(dayOfWeekName) - 1)
                * MAX_LESSONS_IN_DAY * CELLS_FOR_A_LESSON;
    }


    private Integer getGroupColumnNumInSheet(String group) {
        if (!groupToColumnNum.containsKey(group)) {
            throw new IllegalStateException("Group column num cannot be found. ");
        }

        return groupToColumnNum.get(group);
    }

    private void parseAvailableGroups() {
        Row groupNamesRow = sheet.getRow(ROW_GROUPS);
        short lastCellNum = groupNamesRow.getLastCellNum();

        for (int i = 2; i < lastCellNum; i++) {
            Cell groupNameCell = groupNamesRow.getCell(i);

            if (groupNameCell.getCellTypeEnum() == CellType.STRING) {
                groupToColumnNum.put(groupNameCell.getStringCellValue(), i);
            }
        }
    }
}
