package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.api.UtilizadoresClient;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class CreateAccountController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField moradaField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordField1;
    @FXML private Button CriarButton;
    @FXML private Hyperlink loginPage;

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

        if (!password.equals(passwordConfirm)) {
            mostrarAlerta("Erro", "As passwords não coincidem.", Alert.AlertType.ERROR);
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
                erro -> Platform.runLater(() -> mostrarAlerta("Erro", "Falha ao criar conta: " + erro, Alert.AlertType.ERROR))
        );
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void redirecionarParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/Login-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) CriarButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");

            FullscreenHelper.ativarFullscreen(stage);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar a página de contratos.", Alert.AlertType.ERROR);
        }
    }

    private void loginPagehyperlink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginPage.getScene().getWindow();

            stage.getScene().setRoot(root);
            stage.setTitle("Login");

            FullscreenHelper.ativarFullscreen(stage);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar a página de login.", Alert.AlertType.ERROR);
        }
    }

}
