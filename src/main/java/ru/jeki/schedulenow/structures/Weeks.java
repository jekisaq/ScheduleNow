package ru.jeki.schedulenow.structures;

import java.time.LocalDate;
import java.time.temporal.IsoFields;

public enum Weeks {
    ANY(""),
    NUMERATOR("числитель"),
    DENOMINATOR("знаменатель");

    private final String type;

    Weeks(String type) {

        this.type = type;
    }

    public static Weeks of(String type) {
        return (type.equalsIgnoreCase(NUMERATOR.type)) ? NUMERATOR :
                (type.equalsIgnoreCase(DENOMINATOR.type)) ? DENOMINATOR : ANY;
    }

    public static Weeks of(LocalDate date) {
        int weekBasedYearNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        return (weekBasedYearNumber % 2 == 1) ? NUMERATOR: DENOMINATOR;
    }


}