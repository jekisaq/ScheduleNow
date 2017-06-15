package ru.jeki.schedulenow.entity;

import org.junit.Assert;
import org.junit.Test;

public class LessonTests {

    @Test
    public void testCommonLessonEquals() {
        Lesson expected = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.DENOMINATOR);
        Lesson actual = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.DENOMINATOR);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAnyWeekToNumeratorWeekLessonEquals() {
        Lesson expected = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.ANY);
        Lesson actual = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.NUMERATOR);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNumeratorWeekToAnyWeekLessonEquals() {
        Lesson expected = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.NUMERATOR);
        Lesson actual = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.ANY);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAnySubgroupToSubgroup1Equals() {
        Lesson expected = new Lesson(1,
                "454", 0, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.NUMERATOR);
        Lesson actual = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.NUMERATOR);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAnySubgroupToSubgroup2Equals() {
        Lesson expected = new Lesson(1,
                "454", 0, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.NUMERATOR);
        Lesson actual = new Lesson(1,
                "454", 2, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.NUMERATOR);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSubgroupEquals() {
        Lesson expected = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.NUMERATOR);
        Lesson actual = new Lesson(1,
                "454", 1, "Прикладное программирование",
                "416-1", "Р.А.Карелова", Weeks.NUMERATOR);

        Assert.assertEquals(expected, actual);
    }
}
