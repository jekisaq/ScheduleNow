package ru.jeki.schedulenow.model;

import com.google.common.collect.Lists;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.jeki.schedulenow.entity.Lesson;
import ru.jeki.schedulenow.entity.Lessons;
import ru.jeki.schedulenow.parser.ScheduleSource;
import ru.jeki.schedulenow.service.ApplicationPropertyCache;

import java.time.LocalDate;

public class ScheduleModel {

    private ScheduleSource spreadsheetSchedule, replacementsSchedule, collapsedSchedule;

    private ObservableList<LocalDate> dateObservableList;
    private ObjectProperty<LocalDate> chosenDateProperty = new SimpleObjectProperty<>();
    private ObjectProperty<ObservableList<Lesson>> lessonsProperty = new SimpleObjectProperty<>();
    private ApplicationPropertyCache formCache;

    private StringProperty chosenGroup = new SimpleStringProperty();
    private IntegerProperty chosenSubgroup = new SimpleIntegerProperty();

    public ScheduleModel(ApplicationPropertyCache formCache) {
        this.formCache = formCache;
    }

    public void init() {
        releaseChosenGroupProperty();
        releaseChosenSubgroupProperty();

        chosenDateProperty.addListener((observable, oldValue, newValue) -> {
            Lessons dayLessons = collapsedSchedule.getDayLessons(getChosenGroup(), getChosenSubgroup(), newValue);
            lessonsProperty.setValue(FXCollections.observableArrayList(dayLessons.list()));
        });


        dateObservableList = FXCollections.observableList(Lists.newArrayList(replacementsSchedule.getDayDates()));
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

    public boolean isGroupExist(String group) {
        return spreadsheetSchedule.getGroups().contains(group);
    }

    public void setReplacementsSchedule(ScheduleSource replacementsSchedule) {
        this.replacementsSchedule = replacementsSchedule;
    }

    public void setCollapsedSchedule(ScheduleSource collapsedSchedule) {
        this.collapsedSchedule = collapsedSchedule;

    }

    public ObservableList<LocalDate> scheduleDays() {
        return dateObservableList;
    }

    public ObjectProperty<ObservableList<Lesson>> lessonListProperty() {
        return lessonsProperty;
    }

    public ObjectProperty<LocalDate> chosenDateProperty() {
        return chosenDateProperty;
    }
}
