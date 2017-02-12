package ru.jeki.schedulenow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.jeki.schedulenow.controllers.ScheduleController;
import ru.jeki.schedulenow.controllers.StartController;
import ru.jeki.schedulenow.services.SceneNavigationService;
import ru.jeki.schedulenow.services.Services;

import java.io.IOException;
import java.util.Properties;

public class ScheduleNow extends Application {

    private Properties configuration = new Properties();

    @Override
    public void init() throws Exception {
        loadConfiguration();
    }

    private void loadConfiguration() {
        try {
            configuration.load(getClass().getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) {
        loadSceneNavigator(primaryStage);

        Services.getService(SceneNavigationService.class).apply("start");
    }

    private void loadSceneNavigator(Stage primaryStage) {
        SceneNavigationService sceneNavigationService = new SceneNavigationService(primaryStage, configuration);

        Parent root;
        FXMLLoader loader;
        try {
            StartController startController = new StartController(configuration);
            loader = new FXMLLoader();
            loader.setControllerFactory(type -> startController);
            root = loader.load(getClass().getResourceAsStream("/fxml/start.fxml"));
            sceneNavigationService.addScene("start", new Scene(root), startController);

            loader = new FXMLLoader();
            ScheduleController scheduleController = new ScheduleController(startController::getConstructedUser, configuration);
            loader.setControllerFactory(type -> scheduleController);
            root = loader.load(getClass().getResourceAsStream("/fxml/schedule.fxml"));
            sceneNavigationService.addScene("schedule", new Scene(root), scheduleController);
        } catch (IOException e) {
            System.out.println("Start fxml file not loaded.");
            e.printStackTrace();
        }

        Services.addService(sceneNavigationService);
    }

    public static void main(String[] args) {
        launch(ScheduleNow.class, args);
    }
}
