package ru.jeki.schedulenow.cache;

import javafx.beans.property.StringProperty;
import ru.jeki.schedulenow.AlertBox;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationCacheService {

    private static ApplicationCacheService instance;

    public static ApplicationCacheService getInstance() {
        if (instance == null) {
            instance = new ApplicationCacheService();
        }
        return instance;
    }

    private final File CACHE_FILE = Paths.get("cache", "fields.properties").toAbsolutePath().toFile();

    private Properties fieldValues = new Properties();
    private Map<String, StringProperty> keyToProperty = new LinkedHashMap<>();

    private ApplicationCacheService() {
        initializeProperties();

        createFileIfDoesntExist();

        try (InputStream cacheFileInputStream = new FileInputStream(CACHE_FILE)) {
            fieldValues.load(cacheFileInputStream);
        } catch (IOException e) {
            System.out.println("Cache cannot be load. May be the path is broken?");
            System.out.println();
            e.printStackTrace();
        }
    }

    private void initializeProperties() {
        fieldValues.setProperty("group_name", "");
        fieldValues.setProperty("subgroup", "");
        fieldValues.setProperty("department_name", "");
    }

    private void createFileIfDoesntExist() {
        if (!CACHE_FILE.exists()) {
            createNonExistingFile();
        }
    }

    private void createNonExistingFile() {
        try {
            CACHE_FILE.getParentFile().mkdirs();
            if (!CACHE_FILE.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPropertiesValues() {
        for (Map.Entry<String, StringProperty> pair : keyToProperty.entrySet()) {
            pair.getValue().setValue(fieldValues.getProperty(pair.getKey(), ""));
        }
    }

    public void save() {
        try (FileOutputStream cacheFileOutputStream = new FileOutputStream(CACHE_FILE)) {
            fieldValues.store(cacheFileOutputStream, "");
        } catch (Exception e) {
            AlertBox.display("Schedule Now", "Ошибка: Данные в форме не сохранены.");
        }
    }

    public void wire(String key, StringProperty valueProperty) {
        valueProperty.addListener((observable, oldValue, newValue) -> fieldValues.setProperty(key, newValue));

        keyToProperty.put(key, valueProperty);
    }
}
