package ru.jeki.schedulenow.services;

import com.google.common.collect.Maps;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.jeki.schedulenow.controllers.Controller;

import java.util.Map;
import java.util.Properties;

public class SceneNavigationService {
    private final Stage navigableStage;

    private Map<String, Scene> nameToScene = Maps.newHashMap();
    private Map<String, Controller> nameToSceneController = Maps.newHashMap();

    public SceneNavigationService(Stage stage, Properties configuration) {
        this.navigableStage = stage;

        navigableStage.setResizable(false);
        navigableStage.setFullScreen(false);
        navigableStage.setTitle(configuration.getProperty("form.title"));
        navigableStage.setOnHidden(value -> ApplicationCacheService.getInstance().save());

        navigableStage.getIcons().add(new Image("icon32.png"));
    }

    public void apply(String sceneName) {
        if (!nameToScene.containsKey(sceneName) || !nameToSceneController.containsKey(sceneName)) {
            throw new NoSuchSceneException("There's no specified scene to load it");
        }

        Scene scene = nameToScene.get(sceneName);
        Controller controller = nameToSceneController.get(sceneName);
        controller.onSceneApply();

        navigableStage.setScene(scene);
        navigableStage.show();
    }

    public void addScene(String name, Scene scene, Controller startController) {
        nameToScene.put(name, scene);
        nameToSceneController.put(name, startController);
    }
}
