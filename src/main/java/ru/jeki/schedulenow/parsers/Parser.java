package ru.jeki.schedulenow.parsers;

import ru.jeki.schedulenow.structures.ScheduleDay;

import java.util.List;

interface Parser {
    List<ScheduleDay> parse();
}
