package ru.jeki.schedulenow;

import junit.framework.TestCase;

import java.net.URL;

public class SpreadsheetScheduleLoaderTest extends TestCase {

    private SpreadsheetScheduleLoader spreadsheetScheduleLoader;
    private String fileName = "rasp4.xls";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        spreadsheetScheduleLoader = new SpreadsheetScheduleLoader(new URL("http://ntgmk.ru/" + fileName));
    }

    public void testRetrievedFileNameFromScheduleURL() {
        String gotFileName = spreadsheetScheduleLoader.retrieveFileNameFromScheduleURL();
        assert gotFileName.equals(fileName);
    }
}
