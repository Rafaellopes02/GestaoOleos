package com.example.gestaooleos.UI.utils;

import javafx.application.Platform;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class FullscreenHelper {

    public static void ativarFullscreen(Stage stage) {
        Platform.runLater(() -> {
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setFullScreen(true);
        });
    }
}
