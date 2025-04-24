package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.api.UtilizadoresClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class VerUtilizadorController {

    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private TextField moradaField;
    @FXML private ComboBox<String> tipoComboBox;
    @FXML private TextField usernameField;
    @FXML private Runnable onSaveCallback;

    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();
    private UtilizadorDTO utilizador;

    public void setUtilizador(UtilizadorDTO utilizador) {
        this.utilizador = utilizador;

        // Preencher ComboBox com as opções (sem "Outro")
        tipoComboBox.getItems().setAll("Cliente", "Funcionário", "Escritório", "Comercial");

        nomeField.setText(utilizador.getNome());
        telefoneField.setText(utilizador.getTelefone());
        moradaField.setText(utilizador.getMorada());
        tipoComboBox.setValue(tipoParaTexto(utilizador.getIdtipoutilizador()));
        usernameField.setText(utilizador.getUsername());
    }

    @FXML
    private void guardarDados() {
        utilizador.setNome(nomeField.getText());
        utilizador.setTelefone(telefoneField.getText());
        utilizador.setMorada(moradaField.getText());
        utilizador.setUsername(usernameField.getText());
        utilizador.setIdtipoutilizador(textoParaTipo(tipoComboBox.getValue()));

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
            default -> "Cliente"; // Valor padrão caso venha inválido
        };
    }

    private int textoParaTipo(String tipo) {
        return switch (tipo) {
            case "Cliente" -> 1;
            case "Funcionário" -> 2;
            case "Escritório" -> 3;
            case "Comercial" -> 6;
            default -> 1; // Valor padrão seguro
        };
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}