package ru.jeki.schedulenow.parsers.wrappers;

import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.util.List;
import java.util.Set;

public interface ScheduleWrapper {
    List<ScheduleDay> getSchedule(User user);
    Set<String> getGroups();
}
