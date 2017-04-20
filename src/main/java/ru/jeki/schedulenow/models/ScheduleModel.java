package ru.jeki.schedulenow.models;

import com.google.common.collect.Lists;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.jeki.schedulenow.parsers.ScheduleSource;
import ru.jeki.schedulenow.services.ApplicationPropertyCache;
import ru.jeki.schedulenow.structures.ScheduleDay;

import java.util.List;

public class ScheduleModel {

    private ScheduleSource spreadsheetSchedule;
    private List<ScheduleDay> scheduleDays = Lists.newArrayList();
    private ApplicationPropertyCache formCache;

    private StringProperty chosenGroup = new SimpleStringProperty();
    private IntegerProperty chosenSubgroup = new SimpleIntegerProperty();

    public ScheduleModel(ApplicationPropertyCache formCache) {
        this.formCache = formCache;

        releaseChosenGroupProperty();
        releaseChosenSubgroupProperty();
    }

    public String getChosenGroup() {
        return chosenGroup.get();
    }

    public StringProperty chosenGroupProperty() {
        return chosenGroup;
    }

    public int getChosenSubgroup() {
        return chosenSubgroup.get();
    }

    public IntegerProperty chosenSubgroupProperty() {
        return chosenSubgroup;
    }

    private void releaseChosenSubgroupProperty() {
        chosenSubgroup.addListener((observable, oldValue, newValue) ->
                formCache.setProperty("subgroup", String.valueOf(newValue)));
        chosenSubgroup.set(Integer.parseInt(formCache.getProperty("subgroup", "1")));
    }

    private void releaseChosenGroupProperty() {
        chosenGroup.addListener((observable, oldValue, newValue) -> formCache.setProperty("group", newValue));
        chosenGroup.set(formCache.getProperty("group", ""));
    }

    public void setSpreadsheetSchedule(ScheduleSource spreadsheetSchedule) {
        this.spreadsheetSchedule = spreadsheetSchedule;
    }

    public List<ScheduleDay> getScheduleDays() {
        return scheduleDays;
    }

    public boolean isGroupExist(String group) {
        return spreadsheetSchedule.getGroups().contains(group);
    }
}
