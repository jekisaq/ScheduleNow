package ru.jeki.schedulenow.models;

import javafx.beans.property.StringProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.cache.ApplicationCacheService;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartModel {
    private static final String SITE = "http://ntgmk.ru/";

    private Map<String, URL> scheduleLinkToItsType = new LinkedHashMap<>();
    private ApplicationCacheService cacheService = ApplicationCacheService.getInstance();

    public void loadDepartments() {
        try {
            Document doc = Jsoup.connect(SITE + "okno.php?8").get();
            Pattern pattern = Pattern.compile("\\s\\(([^)]+)\\)\\s");

            for (Element a : doc.select("p[align='center'] a")) {
                Element bTagInATag = a.child(0);
                Matcher matcher = pattern.matcher(bTagInATag.text());

                if (matcher.find()) {
                    scheduleLinkToItsType.put(matcher.group(1), new URL(SITE + a.attr("href")));
                }
            }
        } catch (IOException e) {
            AlertBox.display("Schedule Now", "Ошибка: невозможно загрузить список отделений. Наверно сайт ntgmk.ru упал!");
            e.printStackTrace();
        }
    }

    public void wiringFieldWithCacheService(StringProperty groupName, StringProperty subgroup) {
        cacheService.wire("group_name", groupName);
        cacheService.wire("subgroup", subgroup);
    }

    public URL getDepartmentSchedule(String department) {
        return scheduleLinkToItsType.get(department);
    }

    public void fillProperties() {
        cacheService.loadPropertiesValues();
    }

    public Set<String> getDepartmentNameSet() {
        return scheduleLinkToItsType.keySet();
    }
}
