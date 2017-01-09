package ru.jeki.schedulenow.parsers;

import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.ScheduleDayType;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ReplacementsParser implements Parser {

    private final Document rawHtmlDocument;
    private List<ScheduleDay> scheduleDays = Lists.newArrayList();

    public ReplacementsParser(Document document) {
        this.rawHtmlDocument = document;
    }

    @Override
    public List<ScheduleDay> parse() {
        parseScheduleDays();
        parseLessonsForScheduleDays();

        return scheduleDays;
    }

    private void parseScheduleDays() {
        scheduleDays = getSelectedDayHeaders()
                .stream()
                .map(Element::text)
                .map(aHeader -> new ScheduleDay(parseScheduleDayType(aHeader), parseDayOfWeekName(aHeader)))
                .collect(Collectors.toList());
    }

    private Elements getSelectedDayHeaders() {
        return rawHtmlDocument.select("td[colspan=7]");
    }

    private String parseDayOfWeekName(String dayHeader) {
        return dayHeader.split("\\s+")[2];
    }

    private ScheduleDayType parseScheduleDayType(String dayHeader) {
        return ScheduleDayType.of(dayHeader.split("\\s+")[3]);
    }

    private void parseLessonsForScheduleDays() {
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

                processingScheduleDay.lessons().list().add(parseLessonInstanceFrom(filteredReplacementColumns));
            } else if (hadSpace) {
                continue;
            } else {
                hadSpace = true;
            }
        }
    }

    private Lesson parseLessonInstanceFrom(Elements filteredReplacementColumns) {
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

        return new Lesson(lessonNumber, group, subgroup, subject, cabinet, teacher);
    }

    private boolean isLessonReplacementRow(Elements filteredReplacementColumns) {
        return filteredReplacementColumns.size() == 7;
    }
}
