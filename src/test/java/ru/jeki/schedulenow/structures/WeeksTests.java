package ru.jeki.schedulenow.structures;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class WeeksTests {

    @Test
    public void testLocalDateMappingToNumeratorWeek() {
        LocalDate testDate = LocalDate.of(2017, 3, 27);
        Weeks actual = Weeks.of(testDate);

        Assert.assertEquals(Weeks.NUMERATOR, actual);
    }

    @Test
    public void testLocalDateMappingToDenominatorWeek() {
        LocalDate testDate = LocalDate.of(2017, 4, 8);
        Weeks actual = Weeks.of(testDate);

        Assert.assertEquals(Weeks.DENOMINATOR, actual);
    }

    @Test
    public void testLocalDateMappingToNumeratorWeek2() {
        LocalDate testDate = LocalDate.of(2017, 4, 10);
        Weeks actual = Weeks.of(testDate);

        Assert.assertEquals(Weeks.NUMERATOR, actual);
    }
}
