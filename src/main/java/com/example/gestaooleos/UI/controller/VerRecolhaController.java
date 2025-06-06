package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.RecolhaDTO;
import com.example.gestaooleos.UI.api.RecolhasClient;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class VerRecolhaController {

    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private TextField moradaField;
    @FXML private ComboBox<String> tipoComboBox;
    @FXML private TextField usernameField;
    @FXML private Runnable onSaveCallback;

    private final RecolhasClient recolhasClient = new RecolhasClient();
    private RecolhaDTO recolha;

    public void setRecolha(RecolhaDTO recolha) {
        this.recolha = recolha;

        nomeField.setText("Rafael Lopes");
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}