package ru.jeki.schedulenow.structures;

public class User {

    private String groupName;
    private int subgroup;
    private String spreadsheetScheduleFileName;

    public User(String groupName, int subgroup, String spreadsheetScheduleFileName) {
        this.groupName = groupName;
        this.subgroup = subgroup;
        this.spreadsheetScheduleFileName = spreadsheetScheduleFileName;
    }

    public String getGroupName() {
        return groupName;
    }


    public int getSubgroup() {
        return subgroup;
    }

    public String getSpreadsheetScheduleFileName() {
        return spreadsheetScheduleFileName;
    }
}
