package ru.jeki.schedulenow.parsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.Lessons;
import ru.jeki.schedulenow.structures.Weeks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReplacementsParser implements ScheduleSource {

    private static final String TABLE_ENTRY_SELECTOR = "table tbody tr";

    private final Document rawHtmlDocument;
    private Map<LocalDate, Lessons> dateToLessonsMap = new HashMap<>();

    public ReplacementsParser(Document document) {
        this.rawHtmlDocument = document;
    }

    public void parse() {
        parseLessons();
    }

    private void parseLessons() {
        Elements tableEntries = rawHtmlDocument.select(TABLE_ENTRY_SELECTOR);

        LocalDate parsingDate = null;

        for (Element entry : tableEntries) {
            if (isDayHeader(entry)) {
                parsingDate = parseDayHeaderDate(entry);
                dateToLessonsMap.put(parsingDate, new Lessons());

            } else if (isLesson(entry) && parsingDate != null) {
                Lesson lesson = parseLesson(entry.children(), Weeks.of(parsingDate));
                dateToLessonsMap.get(parsingDate).add(lesson);
            }

        }
    }

    private LocalDate parseDayHeaderDate(Element entry) {
        String[] dayHeaderWords = entry.child(0).text().split("\\s+");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", new Locale("ru"));

        return LocalDate.parse(dayHeaderWords[1], dateFormatter);
    }

    private boolean isDayHeader(Element entry) {
        return entry.children().size() == 1;
    }

    private Lesson parseLesson(Elements columns, Weeks week) {
        String group = columns.get(0).text().trim();
        int lessonNumber = Integer.valueOf(columns.get(1).text());

        int subgroup;

        try {
            subgroup = Integer.parseInt(columns.get(2).text());
        } catch (NumberFormatException e) {
            subgroup = 0;
        }

        String subject = columns.get(4).text().trim();
        String teacher = columns.get(5).text().trim();
        String cabinet  = columns.get(6).text().trim();

        return new Lesson(lessonNumber, group, subgroup, subject, cabinet, teacher, week);
    }

    private boolean isLesson(Element entry) {
        return entry.children().size() == 7 && !isLessonLegend(entry);
    }

    private boolean isLessonLegend(Element entry) {
        return entry.child(0).text().equalsIgnoreCase("группа");
    }

    public Set<String> getGroups() {
        return dateToLessonsMap.values().stream()
                .flatMap(Lessons::stream)
                .map(Lesson::getGroupName)
                .collect(Collectors.toSet());
    }

    @Override
    public Lessons getDayLessons(String group, int subgroup, LocalDate date) {
        if (!dateToLessonsMap.containsKey(date)) {
            return new Lessons();
        }

        Lessons lessonsOfGroup = dateToLessonsMap.get(date).filterBy(group);

        return lessonsOfGroup.filterBy(subgroup);
    }

    @Override
    public Set<LocalDate> getDayDates() {
        return dateToLessonsMap.keySet();
    }
}
