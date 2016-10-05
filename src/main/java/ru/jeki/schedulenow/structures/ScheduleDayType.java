package ru.jeki.schedulenow.structures;

public enum ScheduleDayType {
    Numerator("числитель"),
    Denominator("знаменатель");


    private final String type;

    ScheduleDayType(String type) {

        this.type = type;
    }

    public static ScheduleDayType of(String type) {
        return (type.equals(Numerator.type)) ? Numerator : Denominator;
    }
}
