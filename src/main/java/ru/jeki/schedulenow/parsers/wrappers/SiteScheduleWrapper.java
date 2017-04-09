package ru.jeki.schedulenow.parsers.wrappers;

import com.google.common.collect.Lists;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.jeki.schedulenow.parsers.ReplacementsParser;
import ru.jeki.schedulenow.structures.Lesson;
import ru.jeki.schedulenow.structures.ScheduleDay;
import ru.jeki.schedulenow.structures.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SiteScheduleWrapper implements ScheduleWrapper {

    private final String siteReplacementScheduleLink;
    private final ReplacementsParser replacementsParser;
    private Document siteSchedule;
    private List<ScheduleDay> scheduleDays = Lists.newArrayList();

    public SiteScheduleWrapper(String siteReplacementScheduleLink) {
        this.siteReplacementScheduleLink = siteReplacementScheduleLink;

        loadRawSiteSchedule();

        replacementsParser = new ReplacementsParser(siteSchedule);
        replacementsParser.parse();
    }


    @Override
    public List<ScheduleDay> getSchedule(User user) {
        prepareRawReplacementSchedule();
        applyLessonFilter(groupEqualsFilter(user.getGroupName()));
        applyLessonFilter(subgroupSuitableFilter(user.getSubgroup()));

        return scheduleDays;
    }

    @Override
    public Set<String> getGroups() {
        return replacementsParser.getGroups();
    }

    private void loadRawSiteSchedule() {
        try {
            Connection connection = Jsoup.connect(siteReplacementScheduleLink);
            siteSchedule =  connection.get();
        } catch (IOException e) {
            throw new IllegalStateException("Расписание с сайта или сам сайт ntgmk.ru недоступен!", e);
        }
    }

    private void prepareRawReplacementSchedule() {
        scheduleDays = replacementsParser.getScheduleDays();
    }

    private void applyLessonFilter(Predicate<? super Lesson> predicate) {
        List<Lesson> filteredLessons;
        for (ScheduleDay scheduleDay : scheduleDays) {
            List<Lesson> lessons = scheduleDay.lessons().list();

            filteredLessons = lessons.stream().filter(predicate).collect(Collectors.toList());
            lessons.clear();
            lessons.addAll(filteredLessons);
        }
    }

    private static Predicate<Lesson> groupEqualsFilter(String filteringGroupName) {
        return lesson -> lesson.getGroupName().equalsIgnoreCase(filteringGroupName);
    }

    private static Predicate<Lesson> subgroupSuitableFilter(int filteringSubgroup) {
        return lesson -> lesson.getSubgroup() == 0 || lesson.getSubgroup() == filteringSubgroup;
    }
}
