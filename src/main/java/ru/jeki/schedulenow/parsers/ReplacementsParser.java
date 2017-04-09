package ru.jeki.schedulenow.parsers;

import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.Lessons;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.Weeks;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class ReplacementsParser implements ScheduleSource {

    private final Document rawHtmlDocument;
    private List<ScheduleDay> scheduleDays = Lists.newArrayList();

    public ReplacementsParser(Document document) {
        this.rawHtmlDocument = document;
    }

    public List<ScheduleDay> getScheduleDays() {
        return scheduleDays;
    }

    public void parse() {
        parseScheduleDays();
        parseLessons();
    }

    private void parseScheduleDays() {
        Elements dayHeaders = rawHtmlDocument.select("td[colspan=7]");

        scheduleDays = dayHeaders.stream()
                .map(Element::text)
                .map(DayHeader::new)
                .map(header -> new ScheduleDay(header.week, header.dayOfWeek))
                .collect(Collectors.toList());
    }

    private void parseLessons() {
        if (scheduleDays.iterator().hasNext()) {
            parseLessonsOnAnyScheduleDayExists();
        }
    }

    private void parseLessonsOnAnyScheduleDayExists() {
        Elements replacementsRows = rawHtmlDocument.select("table tbody tr");
        Iterator<ScheduleDay> scheduleDayIterator = scheduleDays.iterator();
        ScheduleDay processingScheduleDay = null;
        boolean hadSpace = false;

        for (Element replacementsRow : replacementsRows) {
            Elements filteredReplacementColumns = replacementsRow.select("td[height=12]");

            if (isLessonReplacementRow(filteredReplacementColumns)) {
                if (hadSpace) {
                    processingScheduleDay = scheduleDayIterator.next();
                    hadSpace = false;
                }

                Lesson lesson = parseLessonInstanceFrom(filteredReplacementColumns, processingScheduleDay.getWeek());
                processingScheduleDay.lessons().add(lesson);
            } else if (hadSpace) {
                continue;
            } else {
                hadSpace = true;
            }
        }
    }

    private Lesson parseLessonInstanceFrom(Elements filteredReplacementColumns, Weeks week) {
        String group = filteredReplacementColumns.get(0).text().trim();
        int lessonNumber = Integer.valueOf(filteredReplacementColumns.get(1).text());

        int subgroup;

        try {
            subgroup = Integer.parseInt(filteredReplacementColumns.get(2).text());
        } catch (NumberFormatException e) {
            subgroup = 0;
        }

        String subject = filteredReplacementColumns.get(4).text().trim();
        String teacher = filteredReplacementColumns.get(5).text().trim();
        String cabinet  = filteredReplacementColumns.get(6).text().trim();

        return new Lesson(lessonNumber, group, subgroup, subject, cabinet, teacher, week);
    }

    private boolean isLessonReplacementRow(Elements filteredReplacementColumns) {
        return filteredReplacementColumns.size() == 7;
    }

    public Set<String> getGroups() {

        // Mapping schedule days to a set of groups

        return scheduleDays.stream()
                .map(scheduleDay -> scheduleDay.lessons().list())
                .flatMap(List::stream)
                .map(Lesson::getGroupName)
                .collect(Collectors.toSet());
    }

    @Override
    public Lessons getDayLessons(String group, int subgroup, LocalDate date) {
        Weeks week = Weeks.of(date);
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));

        ScheduleDay day = scheduleDays.stream()
                .filter(scheduleDay -> scheduleDay.getWeek().equals(week))
                .filter(scheduleDay -> scheduleDay.getDayOfWeekName().equalsIgnoreCase(dayOfWeek))
                .findAny().orElseThrow(IllegalArgumentException::new);


        Lessons lessons1 = day.lessons().filterBy(group);
        return lessons1.filterBy(subgroup);
    }

    private class DayHeader {
        private final String dayOfWeek;
        private final Weeks week;

        private DayHeader(String rawDayHeader) {
            String[] dayHeaderWords = rawDayHeader.split("\\s+");

            this.dayOfWeek = dayHeaderWords[2];
            this.week = Weeks.of(dayHeaderWords[3]);
        }
    }
}
