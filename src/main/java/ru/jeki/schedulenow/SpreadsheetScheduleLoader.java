package ru.jeki.schedulenow;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SpreadsheetScheduleLoader {

    private final String spreadsheetScheduleFileName;
    private final Path localFilePath;

    public SpreadsheetScheduleLoader(String spreadsheetScheduleFileName) {
        this.spreadsheetScheduleFileName = spreadsheetScheduleFileName;

        String[] splits = spreadsheetScheduleFileName.split("/");
        String realFileName = splits[splits.length - 1];

        this.localFilePath = Paths.get("cache", "schedule", realFileName).toAbsolutePath();
    }

    public InputStream getInputStream() {
        InputStream scheduleInputStream;

        try {
            if (!Files.exists(localFilePath)) {
                Files.createDirectories(localFilePath.getParent());
                Files.createFile(localFilePath);
                loadFromInternet();
            }

            scheduleInputStream = Files.newInputStream(localFilePath);
        } catch (IOException e) {
            throw new IllegalStateException("Spreadsheet schedule file cannot be loaded. May be load from Internet isn't working...", e);
        }

        return scheduleInputStream;
    }

    private void loadFromInternet() throws IOException {
        URL spreadsheetScheduleURL = new URL(String.join("http://ntgmk.ru/", spreadsheetScheduleFileName));
        Files.copy(spreadsheetScheduleURL.openStream(), localFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
