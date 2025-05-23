package com.example.gestaooleos.UI;

import com.example.gestaooleos.UI.utils.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carregar fontes personalizadas
        String[] fontes = {
                "JetBrainsMono-Bold.ttf",
                "JetBrainsMono-Regular.ttf",
                "JetBrainsMono-ExtraBold.ttf"
        };
        primaryStage.getIcons().add(new Image(getClass().getResource("/image/icon.png").toExternalForm()));


        for (String fonte : fontes) {
            Font.loadFont(getClass().getResource("/fonts/" + fonte).toExternalForm(), 14);
        }

        // Carregar a página inicial (login)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);

        primaryStage.setTitle("Gestão de Óleos");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ativar fullscreen logo no arranque
        FullscreenHelper.ativarFullscreen(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
