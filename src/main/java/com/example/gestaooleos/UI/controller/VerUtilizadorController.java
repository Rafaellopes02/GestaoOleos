package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.api.UtilizadoresClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class VerUtilizadorController {

    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private TextField moradaField;
    @FXML private TextField tipoField;
    @FXML private TextField usernameField;
    @FXML private Runnable onSaveCallback;

    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();
    private UtilizadorDTO utilizador;

    public void setUtilizador(UtilizadorDTO utilizador) {
        this.utilizador = utilizador;
        nomeField.setText(utilizador.getNome());
        telefoneField.setText(utilizador.getTelefone());
        moradaField.setText(utilizador.getMorada());
        tipoField.setText(tipoParaTexto(utilizador.getIdtipoutilizador()));
        usernameField.setText(utilizador.getUsername());
    }

    @FXML
    private void guardarDados() {
        utilizador.setNome(nomeField.getText());
        utilizador.setTelefone(telefoneField.getText());
        utilizador.setMorada(moradaField.getText());
        utilizador.setUsername(usernameField.getText());

        utilizadoresClient.atualizarUtilizador(
                utilizador.getIdutilizador(),
                utilizador,
                sucesso -> Platform.runLater(() -> {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Dados guardados com sucesso.");

                    if (onSaveCallback != null) {
                        onSaveCallback.run();
                    }

                    fecharJanela();
                }),
                erro -> Platform.runLater(() ->
                        mostrarAlerta(Alert.AlertType.ERROR, "Erro ao guardar: " + erro)
                )
        );
    }


    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        Stage stage = (Stage) nomeField.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }

    private String tipoParaTexto(int tipo) {
        return switch (tipo) {
            case 1 -> "Cliente";
            case 2 -> "Funcionário";
            case 3 -> "Escritório";
            case 6 -> "Comercial";
            default -> "Outro";
        };
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}
