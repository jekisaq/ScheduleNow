package ru.jeki.schedulenow.parsers;

import ru.jeki.schedulenow.structures.ScheduleDay;

import java.util.List;

interface Parser<T> {
    void parse();
    List<ScheduleDay> getParsedData();
}
