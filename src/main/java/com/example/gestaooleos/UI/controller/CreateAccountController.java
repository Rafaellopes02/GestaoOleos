package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.api.UtilizadoresClient;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CreateAccountController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField moradaField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordField1;
    @FXML private Button CriarButton;
    @FXML private Hyperlink loginPage;
    @FXML private Label erroLabel;

    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();

    @FXML
    public void initialize() {
        CriarButton.setOnAction(e -> criarConta());
        loginPage.setOnAction(e -> loginPagehyperlink());
    }

    private void criarConta() {
        String nome = nameField.getText();
        String telefone = phoneField.getText();
        String morada = moradaField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirm = passwordField1.getText();

        if (nome.isBlank() || telefone.isBlank() || morada.isBlank() || username.isBlank() || password.isBlank()) {
            mostrarErro("Por favor, preencha todos os campos!");
            return;
        }

        if (!password.equals(passwordConfirm)) {
            mostrarErro("As passwords não coincidem.");
            return;
        }

        utilizadoresClient.buscarUtilizadores(json -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<UtilizadorDTO> utilizadores = mapper.readValue(json, new TypeReference<>() {});

                boolean usernameExiste = utilizadores.stream()
                        .anyMatch(u -> username.equalsIgnoreCase(u.getUsername()));

                if (usernameExiste) {
                    Platform.runLater(() -> mostrarErro("Esse username já está em uso."));
                    return;
                }

                UtilizadorDTO novo = new UtilizadorDTO();
                novo.setNome(nome);
                novo.setTelefone(telefone);
                novo.setMorada(morada);
                novo.setUsername(username);
                novo.setPassword(password);
                novo.setIdtipoutilizador(1);

                utilizadoresClient.criarUtilizador(novo,
                        resposta -> Platform.runLater(this::redirecionarParaLogin),
                        erro -> Platform.runLater(() -> mostrarErro("Falha ao criar conta: " + erro))
                );
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> mostrarErro("Erro ao processar utilizadores."));
            }
        }, erro -> Platform.runLater(() -> mostrarErro("Erro ao comunicar com servidor.")));
    }

    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setStyle("-fx-text-fill: #b20000; -fx-font-weight: bold; -fx-background-color: #ffd6d6; -fx-padding: 8 8; -fx-background-radius: 8;-fx-font-family: Consolas;");
        erroLabel.setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
            Platform.runLater(() -> erroLabel.setVisible(false));
        }).start();
    }

    private void redirecionarParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/Login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) CriarButton.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Login");

            FullscreenHelper.ativarFullscreen(stage);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar a página de login.");
        }
    }

    private void loginPagehyperlink() {
        redirecionarParaLogin();
    }
}
