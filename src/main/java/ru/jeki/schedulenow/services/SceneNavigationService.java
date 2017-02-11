package ru.jeki.schedulenow.services;

import com.google.common.collect.Maps;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Properties;

public class SceneNavigationService {
    private final Stage navigableStage;

    private Map<String, Scene> scenes = Maps.newHashMap();

    public SceneNavigationService(Stage stage, Properties configuration) {
        this.navigableStage = stage;

        navigableStage.setResizable(false);
        navigableStage.setFullScreen(false);
        navigableStage.setTitle(configuration.getProperty("scheduleNow.form.title"));
    }

    public void apply(String sceneName) {
        if (!scenes.containsKey(sceneName)) {
            throw new NoSuchSceneException("There's no specified scene to load it");
        }

        Scene scene = scenes.get(sceneName);
        navigableStage.setScene(scene);
        navigableStage.show();
    }
}
