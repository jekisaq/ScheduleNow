package ru.jeki.schedulenow.processingStages;

import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.util.List;

public interface ScheduleProcess {
    List<ScheduleDay> getSchedule(User user);
}
