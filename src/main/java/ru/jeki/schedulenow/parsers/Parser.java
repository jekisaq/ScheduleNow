package ru.jeki.schedulenow.parsers;

import ru.jeki.schedulenow.structures.ScheduleDay;

import java.util.Set;

interface Parser {
    void parse();
    Set<ScheduleDay> getParsedData();
}
