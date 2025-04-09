package com.example.gestaooleos.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Objects;

public class PreviewApp extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestaooleos/view/contratos-view.fxml"));
        Parent root = loader.load();

        scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Preview Layout (CTRL+S no CSS e F5 aqui)");

        applyStylesheet(); // aplicar CSS pela primeira vez

        // Recarregar CSS com clique do rato
        scene.setOnMouseClicked(e -> applyStylesheet());

        // Ou recarregar com tecla F5
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F5) {
                applyStylesheet();
            }
        });

        stage.show();
    }

    private void applyStylesheet() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        System.out.println("✔ CSS recarregado!");
    }

    public static void main(String[] args) {
        launch();
    }
}