package ru.jeki.schedulenow;

import junit.framework.TestCase;

import java.net.URL;

public class ExcelScheduleLoaderTest extends TestCase {

    private ExcelScheduleLoader excelScheduleLoader;
    private String fileName = "rasp4.xls";

    @Override
    protected void setUp() throws Exception {
        excelScheduleLoader = new ExcelScheduleLoader(new URL("http://ntgmk.ru/" + fileName));
    }

    public void testRetrievedFileNameFromScheduleURL() {
        String gotFileName = excelScheduleLoader.retrieveFileNameFromScheduleURL();
        assert gotFileName.equals(fileName);
    }
}
