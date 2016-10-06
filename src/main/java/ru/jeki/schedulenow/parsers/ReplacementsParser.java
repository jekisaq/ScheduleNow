package ru.jeki.schedulenow.parsers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.ScheduleDayType;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReplacementsParser implements Parser {

    private final Document rawReplacements;
    private final User user;
    private Map<ScheduleDay, List<Lesson>> lessonsPerScheduleDay = Maps.newLinkedHashMap();

    public ReplacementsParser(User user) throws IOException {
        this.rawReplacements = Jsoup.connect("http://ntgmk.ru/view_zamen.php").get();
        this.user = user;
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

                lessonsPerScheduleDay.put(currentProcessingScheduleDay, Lists.newArrayList());
                continue;
            }


        }

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

    @Override
    public Set<ScheduleDay> getParsedData() {
        return lessonsPerScheduleDay.keySet();
    }
}
