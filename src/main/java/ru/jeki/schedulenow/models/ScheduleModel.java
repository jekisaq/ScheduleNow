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
    private final User user;
    private ScheduleProcess mainScheduleProvider;

    private List<ScheduleDay> scheduleDays = Lists.newArrayList();

    public ScheduleModel(User user, Properties configuration) {
        this.user = user;

        String siteReplacementScheduleLink = configuration.getProperty("site.link")
                + configuration.getProperty("schedule.site.filename");

        ScheduleProcess siteProcess = new SiteScheduleProcess(siteReplacementScheduleLink);
        this.mainScheduleProvider = new SpreadsheetScheduleProcess(siteProcess, user.getLinkToDepartmentSchedule());
    }

    public void buildSchedule() throws IOException {
        scheduleDays = mainScheduleProvider.getSchedule(user);
    }

    public List<ScheduleDay> getScheduleDays() {
        return scheduleDays;
    }

}
