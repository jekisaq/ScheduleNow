package ru.jeki.schedulenow.models;

import com.google.common.collect.Lists;
import ru.jeki.schedulenow.parsers.wrappers.ScheduleWrapper;
import ru.jeki.schedulenow.parsers.wrappers.SiteScheduleWrapper;
import ru.jeki.schedulenow.parsers.wrappers.SpreadsheetScheduleWrapper;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ScheduleModel {
    private String siteReplacementScheduleLink;

    private List<ScheduleDay> scheduleDays = Lists.newArrayList();

    public ScheduleModel(Properties configuration) {

        siteReplacementScheduleLink = configuration.getProperty("site.link")
                + configuration.getProperty("schedule.site.filename");
    }

    public void buildSchedule(User user) throws IOException {
        ScheduleWrapper siteProcess = new SiteScheduleWrapper(siteReplacementScheduleLink);
        ScheduleWrapper mainScheduleProvider = new SpreadsheetScheduleWrapper(siteProcess, user.getSpreadsheetScheduleFileName());
        scheduleDays = mainScheduleProvider.getSchedule(user);
    }

    public List<ScheduleDay> getScheduleDays() {
        return scheduleDays;
    }

}
