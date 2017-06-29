package ru.jeki.schedulenow.service;

import ru.jeki.schedulenow.AlertBox;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ApplicationPropertyCache {

    private Path cacheFilePath = Paths.get("cache", "fields.properties").toAbsolutePath();

    private Properties fieldValues = new Properties();

    public ApplicationPropertyCache() {
        loadCache();
    }

    private void loadCache() {
        try {
            if (!Files.exists(cacheFilePath)) {
                Files.createDirectories(cacheFilePath.getParent());
                Files.createFile(cacheFilePath);
            }

            fieldValues.load(Files.newInputStream(cacheFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (OutputStream cacheFileOutStream = Files.newOutputStream(cacheFilePath)) {
            fieldValues.store(cacheFileOutStream, "");
        } catch (Exception e) {
            AlertBox.display("ScheduleSource Now", "Ошибка: Данные в форме не сохранены.");
        }
    }

    public void setProperty(String name, String value) {
        fieldValues.setProperty(name, value);
    }

    public String getProperty(String group, String s) {
        return fieldValues.getProperty(group, s);
    }
}
