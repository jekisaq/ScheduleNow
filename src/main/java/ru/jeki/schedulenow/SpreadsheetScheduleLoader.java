package ru.jeki.schedulenow;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SpreadsheetScheduleLoader {

    private final URL linkToDepartmentSchedule;
    private final Path localFilePath;

    public SpreadsheetScheduleLoader(URL linkToDepartmentSchedule) {
        this.linkToDepartmentSchedule = linkToDepartmentSchedule;
        this.localFilePath = Paths.get("cache", "schedule", retrieveFileNameFromScheduleURL()).toAbsolutePath();
    }

    String retrieveFileNameFromScheduleURL() {
        String[] strings = linkToDepartmentSchedule.getFile().split("/");
        return strings[strings.length - 1];
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
        Files.copy(linkToDepartmentSchedule.openStream(), localFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
