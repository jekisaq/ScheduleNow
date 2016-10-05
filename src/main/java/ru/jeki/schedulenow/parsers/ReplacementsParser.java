package ru.jeki.schedulenow.parsers;

import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.ScheduleDayType;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ReplacementsParser implements Parser<String> {

    private final Document rawReplacements;
    private final User user;
    private List<ScheduleDay> dayOfWeekAvailableList = Lists.newArrayList();

    public ReplacementsParser(User user) throws IOException {
        this.rawReplacements = Jsoup.connect("http://ntgmk.ru/view_zamen.php").get();
        this.user = user;
    }

    @Override
    public void parse() {
        dayOfWeekAvailableList.addAll(rawReplacements.select("td[colspan=7]").stream()
                .map(Element::text)
                .map(element -> new ScheduleDay(parseScheduleDayType(element), parseDayOfWeekName(element)))
                .collect(Collectors.toList()));
    }

    private String parseDayOfWeekName(String dayHeader) {
        return dayHeader.split("\\s+")[2];
    }

    private ScheduleDayType parseScheduleDayType(String dayHeader) {
        return ScheduleDayType.of(dayHeader.split("\\s+")[3]);
    }

    @Override
    public List<ScheduleDay> getParsedData() {
        return dayOfWeekAvailableList;
    }
}
