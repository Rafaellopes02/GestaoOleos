package com.example.gestaooleos.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Registar todas as variações da JetBrains Mono
        String[] fontes = {
                "JetBrainsMono-Bold.ttf",
                "JetBrainsMono-Regular.ttf",
                "JetBrainsMono-ExtraBold.ttf"
        };

        for (String fonte : fontes) {
            Font.loadFont(getClass().getResource("/fonts/" + fonte).toExternalForm(), 14);
        }


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        primaryStage.setTitle("Gestão de Óleos");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
