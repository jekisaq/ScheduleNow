package ru.jeki.schedulenow.models;

import com.google.common.collect.Lists;
import ru.jeki.schedulenow.processingStages.ScheduleProcess;
import ru.jeki.schedulenow.processingStages.SiteScheduleProcess;
import ru.jeki.schedulenow.processingStages.SpreadsheetScheduleProcess;
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
        ScheduleProcess siteProcess = new SiteScheduleProcess(siteReplacementScheduleLink);
        ScheduleProcess mainScheduleProvider = new SpreadsheetScheduleProcess(siteProcess, user.getLinkToDepartmentSchedule());
        scheduleDays = mainScheduleProvider.getSchedule(user);
    }

    public List<ScheduleDay> getScheduleDays() {
        return scheduleDays;
    }

}
