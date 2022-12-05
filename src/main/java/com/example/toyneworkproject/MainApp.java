package com.example.toyneworkproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage appStage;

    public static MainApp getInstance() {
        return instance;
    }

    private static MainApp instance = new MainApp();

    @Override
    public void start(Stage stage) throws IOException {
        appStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("scenes/startScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680,380);
        stage.setTitle("Toy Social Network");
        stage.setScene(scene);
        stage.show();

    }

    public static void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(MainApp.class.getResource(fxml));
        appStage.getScene().setRoot(pane);
    }

    public static void sendParameterToScene(Object argument){
        appStage.setUserData(argument);

    }

    public static Object getParameterFromScene(){
        Object parameter = appStage.getUserData();
        appStage.setUserData(null);
        return parameter;
    }
    public static void startApp(){
        launch();
    }
}
