package ru.jeki.schedulenow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.jeki.schedulenow.cache.ApplicationCacheService;
import ru.jeki.schedulenow.controllers.StartController;

import java.io.IOException;
import java.util.Properties;

public class ScheduleNow extends Application {

    private Properties configuration = new Properties();

    @Override
    public void init() throws Exception {
        try {
            configuration.load(getClass().getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(type -> new StartController(configuration));
            Parent root = loader.load(getClass().getResourceAsStream("/fxml/start.fxml"));

            primaryStage.setTitle(configuration.getProperty("scheduleNow.form.title"));
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(false);
            primaryStage.setOnHidden(e -> ApplicationCacheService.getInstance().save());
            Image image = new Image("icon32.png");
            primaryStage.getIcons().add(image);

            primaryStage.show();

        } catch (IOException e) {
            System.out.println("Start fxml file not loaded.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(ScheduleNow.class, args);
    }
}
