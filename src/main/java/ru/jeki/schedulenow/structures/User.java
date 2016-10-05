package ru.jeki.schedulenow.structures;

import java.net.URL;

public class User {

    private String groupName;
    private String subgroup;
    private URL linkToDepartmentSchedule;

    public User(String groupName, String subgroup, URL linkToDepartmentSchedule) {
        this.groupName = groupName;
        this.subgroup = subgroup;
        this.linkToDepartmentSchedule = linkToDepartmentSchedule;
    }

    public String getGroupName() {
        return groupName;
    }


    public String getSubgroup() {
        return subgroup;
    }

    public URL getLinkToDepartmentSchedule() {
        return linkToDepartmentSchedule;
    }
}
