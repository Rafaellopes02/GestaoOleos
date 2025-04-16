package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.api.UtilizadoresClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;


public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink PageCriar;

    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> fazerLogin());
        PageCriar.setOnAction(e -> CriarPagehyperlink());
    }
    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();

    private void fazerLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Erro", "Por favor preenche todos os campos.", Alert.AlertType.WARNING);
            return;
        }

        utilizadoresClient.buscarUtilizadores(json -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<UtilizadorDTO> utilizadores = mapper.readValue(json, new TypeReference<>() {});
                UtilizadorDTO utilizadorEncontrado = utilizadores.stream()
                        .filter(u -> username.equals(u.getUsername()) && password.equals(u.getPassword()))
                        .findFirst()
                        .orElse(null);

                if (utilizadorEncontrado != null) {
                    Platform.runLater(this::redirecionarParaContratos);
                } else {
                    Platform.runLater(() -> mostrarAlerta("Login falhou", "Credenciais inválidas.", Alert.AlertType.ERROR));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> mostrarAlerta("Erro", "Erro ao processar resposta da API.", Alert.AlertType.ERROR));
            }
        }, erro -> Platform.runLater(() -> mostrarAlerta("Erro", "Erro ao comunicar com o servidor: " + erro, Alert.AlertType.ERROR)));
    }

    private void CriarPagehyperlink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/CreateAccount-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) PageCriar.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Criar Conta");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar a página de login.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void redirecionarParaContratos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/contratos-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Contratos");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar a página de contratos.", Alert.AlertType.ERROR);
        }
    }
}
