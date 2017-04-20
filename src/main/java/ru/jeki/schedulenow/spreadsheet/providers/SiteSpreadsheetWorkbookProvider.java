package ru.jeki.schedulenow.spreadsheet.providers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class SiteSpreadsheetWorkbookProvider implements SpreadsheetWorkbookProvider {

    private final Properties configuration;

    public SiteSpreadsheetWorkbookProvider(Properties configuration) {

        this.configuration = configuration;
    }

    public Workbook provide(String path) {
        String[] splits = path.split("/");
        String filename = splits[splits.length - 1];

        Path localFilePath = Paths.get("cache", "spreadsheets", filename).toAbsolutePath();

        return loadWorkbook(path, localFilePath);
    }

    private Workbook loadWorkbook(String sourceFilePath, Path localFilePath) {
        try {
            return tryToLoadWorkbook(sourceFilePath, localFilePath);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Workbook tryToLoadWorkbook(String siteFilePath, Path localFilePath) throws IOException {
        if (!Files.exists(localFilePath)) {
            Files.createDirectories(localFilePath.getParent());
            Files.createFile(localFilePath);

            String sourcePath = String.join("/", configuration.getProperty("site.path"), siteFilePath);
            loadFromInternet(sourcePath, localFilePath);
        }

        InputStream scheduleInputStream = Files.newInputStream(localFilePath);

        return new HSSFWorkbook(scheduleInputStream);
    }

    private void loadFromInternet(String sourcePath, Path destination) throws IOException {
        URL spreadsheetScheduleURL = new URL(sourcePath);
        Files.copy(spreadsheetScheduleURL.openStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    }
}
