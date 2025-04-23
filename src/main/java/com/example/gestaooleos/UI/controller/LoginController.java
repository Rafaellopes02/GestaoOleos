package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.api.UtilizadoresClient;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import java.util.List;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink PageCriar;
    @FXML private Label erroLabel;


    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();

    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> fazerLogin());
        PageCriar.setOnAction(e -> abrirCriarConta());
    }

    private void fazerLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            mostrarErro(" Por Favor, preencha todos os campos!");
            return;
        }

        utilizadoresClient.buscarUtilizadores(
                json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        List<UtilizadorDTO> utilizadores = mapper.readValue(json, new TypeReference<>() {});
                        UtilizadorDTO found = utilizadores.stream()
                                .filter(u -> username.equals(u.getUsername()) && password.equals(u.getPassword()))
                                .findFirst()
                                .orElse(null);

                        if (found != null) {
                            Platform.runLater(() -> redirecionarParaHome(found));

                        } else {
                            Platform.runLater(() ->
                                    mostrarErro("Credenciais inválidas.")

                            );
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Platform.runLater(() ->
                                mostrarErro("Erro ao processar resposta da API.")
                        );
                    }
                },
                erro -> Platform.runLater(() ->
                        mostrarErro( "Erro ao comunicar com o servidor:" + erro)
                )
        );
    }

    private void abrirCriarConta() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.gestaooleos/view/CreateAccount-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) PageCriar.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Criar Conta");
            FullscreenHelper.ativarFullscreen(stage);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar a página de criação de conta.");
        }
    }

    private void redirecionarParaHome(UtilizadorDTO utilizador) {
        try {
            SessaoUtilizador.setNomeUtilizador(utilizador.getNome());

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.gestaooleos/view/home-funcionario.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Página Inicial - " + utilizador.getNome());
            FullscreenHelper.ativarFullscreen(stage);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro( "Erro ao carregar a página inicial.");
        }
    }



    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setStyle("-fx-text-fill: #b20000; -fx-font-weight: bold; -fx-background-color: #ffd6d6; -fx-padding: 8 6; -fx-background-radius: 8; -fx-font-family: Consolas;");
        erroLabel.setVisible(true);

        // Apagar após 3 segundos
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
            Platform.runLater(() -> erroLabel.setVisible(false));
        }).start();
    }


}
