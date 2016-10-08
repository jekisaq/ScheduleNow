package ru.jeki.schedulenow.parsers;

import com.google.common.collect.Maps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.ScheduleDayType;

import java.io.IOException;
import java.util.Map;

public class ReplacementsParser implements Parser {

    private final Document rawReplacements;
    private Map<ScheduleDay, ObservableList<Lesson>> lessonsPerScheduleDay = Maps.newLinkedHashMap();

    public ReplacementsParser() throws IOException {
        this.rawReplacements = Jsoup.connect("http://ntgmk.ru/view_zamen.php").get();
    }

    @Override
    public void parse() {
        ScheduleDay currentProcessingScheduleDay = null;
        Elements replacementsRows = rawReplacements.select("table tbody tr");

        for (Element replacementsRow : replacementsRows) {
            Elements dayHeaderTd = replacementsRow.select("td[colspan=7]");
            String dayHeader = dayHeaderTd.text();
            if (isReplacementsRowHeader(dayHeaderTd) &&
                    !isCurrentScheduleDayTheSameInRow(dayHeader, currentProcessingScheduleDay)) {
                currentProcessingScheduleDay = new ScheduleDay(
                        parseScheduleDayType(dayHeader),
                        parseDayOfWeekName(dayHeader)
                );

                lessonsPerScheduleDay.put(currentProcessingScheduleDay, FXCollections.observableArrayList());
                continue;
            }

            Elements filteredReplacementRows = replacementsRow.select("td[height=12]");
            if (isLessonReplacementRow(filteredReplacementRows)) {
                String group = filteredReplacementRows.get(0).text().trim();
                int lessonNumber = Integer.valueOf(filteredReplacementRows.get(1).text());
                int subgroup = Integer.getInteger(filteredReplacementRows.get(2).text(), 0);
                String subject = filteredReplacementRows.get(4).text().trim();
                String teacher = filteredReplacementRows.get(5).text().trim();
                String cabinet  = filteredReplacementRows.get(6).text().trim();

                Lesson lesson = new Lesson(lessonNumber, group, subgroup, subject, cabinet, teacher,
                        currentProcessingScheduleDay);
                if (lessonsPerScheduleDay.containsKey(currentProcessingScheduleDay)) {
                    lessonsPerScheduleDay.get(currentProcessingScheduleDay).add(lesson);
                } else {
                    throw new IllegalStateException("There's no current parsing schedule day in raw document.");
                }
            }
        }



    }

    private boolean isLessonReplacementRow(Elements filteredReplacementRows) {
        return filteredReplacementRows.size() == 7;
    }

    private boolean isCurrentScheduleDayTheSameInRow(String headerRow, ScheduleDay scheduleDay) {

        return scheduleDay != null &&
                        scheduleDay.getDayOfWeekName().equalsIgnoreCase(parseDayOfWeekName(headerRow));
    }

    private boolean isReplacementsRowHeader(Elements dayHeaderTd) {
        return dayHeaderTd.size() > 0;
    }

    private String parseDayOfWeekName(String dayHeader) {
        return dayHeader.split("\\s+")[2];
    }

    private ScheduleDayType parseScheduleDayType(String dayHeader) {
        return ScheduleDayType.of(dayHeader.split("\\s+")[3]);
    }

    public Map<ScheduleDay,ObservableList<Lesson>> getLessonsPerScheduleDay() {
        return lessonsPerScheduleDay;
    }
}
