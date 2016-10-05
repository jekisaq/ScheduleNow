package ru.jeki.schedulenow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.jeki.schedulenow.cache.ApplicationCacheService;

import java.io.IOException;

public class ScheduleNow extends Application {

    private Parent root;

    public void start(Stage primaryStage) {
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));

            primaryStage.setTitle("Schedule Now - узнай расписание");
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(false);

            primaryStage.setOnCloseRequest(e -> ApplicationCacheService.getInstance().save());

            primaryStage.show();

        } catch (IOException e) {
            System.out.println("Fxml file not loaded.");
            e.printStackTrace();
        }
    }
}
