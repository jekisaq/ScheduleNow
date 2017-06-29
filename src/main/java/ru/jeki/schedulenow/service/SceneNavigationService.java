package ru.jeki.schedulenow.service;

import com.google.common.collect.Maps;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.jeki.schedulenow.controller.Controller;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class SceneNavigationService {
    private final Stage navigableStage;

    private Map<String, Parent> nameToSceneRoot = Maps.newHashMap();
    private Map<String, Controller> nameToSceneController = Maps.newHashMap();

    public SceneNavigationService(Stage stage, Properties configuration) {
        this.navigableStage = stage;

        navigableStage.setResizable(false);
        navigableStage.setFullScreen(false);
        navigableStage.setTitle(configuration.getProperty("form.title"));

        navigableStage.getIcons().add(new Image("ico/app.png"));
    }

    public void apply(String sceneName) {
        if (!nameToSceneRoot.containsKey(sceneName) || !nameToSceneController.containsKey(sceneName)) {
            throw new NoSuchSceneException("There's no specified scene root to provide it");
        }

        Controller controller = nameToSceneController.get(sceneName);
        controller.onSceneApply();

        Parent root = nameToSceneRoot.get(sceneName);

        if (navigableStage.getScene() == null) {
            navigableStage.setScene(new Scene(root));
        } else {
            navigableStage.getScene().setRoot(root);
            navigableStage.getScene().getWindow().sizeToScene();
        }

        navigableStage.show();
    }

    public void registerSceneRoot(String name, Controller controller) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(type -> controller);

        try {
            String fxmlPath = "/fxml/" + name + ".fxml";
            Parent root = loader.load(getClass().getResourceAsStream(fxmlPath));

            nameToSceneRoot.put(name, root);
            nameToSceneController.put(name, controller);
        } catch (IOException e) {
            throw new NoSuchSceneException("");
        }
    }

    class NoSuchSceneException extends RuntimeException {
        NoSuchSceneException(String message) {
            super(message);
        }
    }

}
