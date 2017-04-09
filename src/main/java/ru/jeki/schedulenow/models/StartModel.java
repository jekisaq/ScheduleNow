package ru.jeki.schedulenow.models;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.jeki.schedulenow.AlertBox;
import ru.jeki.schedulenow.services.ApplicationCacheService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StartModel {
    private static final String SITE = "http://ntgmk.ru/";

    private Map<String, String> departmentToSpreadsheetScheduleFileName = new LinkedHashMap<>();
    private ApplicationCacheService cacheService = ApplicationCacheService.getInstance();

    public void loadDepartments() {
        try {
            Document doc = Jsoup.connect(SITE + "okno.php?8").get();

            departmentToSpreadsheetScheduleFileName = doc.select("p[class='P1'] a")
                    .stream()
                    .filter(element -> !element.attr("href").contains("view_zamen.php"))
                    .collect(Collectors.toMap(Element::text, element-> element.attr("href")));
        } catch (IOException e) {
            AlertBox.display("ScheduleSource Now", "Ошибка: невозможно загрузить список отделений. Наверно сайт ntgmk.ru упал!");
            e.printStackTrace();
        }
    }

    public void wireFieldsWithCacheService(StringProperty... fields) {
        Stream.of(fields).forEachOrdered(
                stringProperty -> cacheService.wire(((Control)stringProperty.getBean()).getId(), stringProperty));
    }

    public String getSpreadsheetScheduleFileName(String department) {
        if (!departmentToSpreadsheetScheduleFileName.containsKey(department)) {
            throw new IllegalArgumentException("There's no specified department in the department map list from ntgmk.ru");
        }

        return departmentToSpreadsheetScheduleFileName.get(department);
    }

    public void fillProperties() {
        cacheService.loadPropertiesValues();
    }

    public Set<String> getDepartmentNameSet() {
        return departmentToSpreadsheetScheduleFileName.keySet();
    }
}
