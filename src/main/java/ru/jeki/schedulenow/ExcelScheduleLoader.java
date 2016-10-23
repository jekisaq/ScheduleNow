package ru.jeki.schedulenow;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class ExcelScheduleLoader {

    private final URL linkToDepartmentSchedule;
    private final Path localFilePath;

    public ExcelScheduleLoader(URL linkToDepartmentSchedule) {
        this.linkToDepartmentSchedule = linkToDepartmentSchedule;
        this.localFilePath = Paths.get("cache", "schedule", retrieveFileNameFromScheduleURL()).toAbsolutePath();
    }

    String retrieveFileNameFromScheduleURL() {
        String[] strings = linkToDepartmentSchedule.getFile().split("/");
        return strings[strings.length - 1];
    }

    public Optional<InputStream> loadInputStream() {
        Optional<InputStream> optionalLoadedExcelScheduleInputStream = Optional.empty();

        try {
            if (!Files.exists(localFilePath)) {
                Files.createDirectories(localFilePath.getParent());
                Files.createFile(localFilePath);
                loadFromInternet();
            }

            optionalLoadedExcelScheduleInputStream = Optional.of(Files.newInputStream(localFilePath));
        } catch (IOException e) {
            System.out.println("Excel schedule file cannot be load. May be load from internet isn't working...");
            e.printStackTrace();
        }

        return optionalLoadedExcelScheduleInputStream;
    }

    private void loadFromInternet() throws IOException {
        Files.copy(linkToDepartmentSchedule.openStream(), localFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
