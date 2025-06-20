package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.api.UtilizadoresClient;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
            mostrarErro("Por favor, preencha todos os campos!");
            return;
        }

        utilizadoresClient.login(username, password,
                json -> {
                    try {
                        System.out.println("Resposta da API: " + json);
                        if (json.startsWith("{")) {
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode node = mapper.readTree(json);

                            if (!node.has("idutilizador") || !node.has("nome") || !node.has("idtipoutilizador")) {
                                Platform.runLater(() -> mostrarErro("Resposta inválida da API."));
                                return;
                            }

                            UtilizadorDTO utilizador = new UtilizadorDTO();
                            utilizador.setIdutilizador(node.get("idutilizador").asLong());
                            utilizador.setNome(node.get("nome").asText());
                            utilizador.setIdtipoutilizador(node.get("idtipoutilizador").asInt());

                            Platform.runLater(() -> redirecionarParaHome(utilizador));
                        } else {
                            Platform.runLater(() -> mostrarErro(json));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Platform.runLater(() -> mostrarErro("Erro ao processar resposta da API."));
                    }
                },
                erro -> Platform.runLater(() -> mostrarErro("Erro ao comunicar com o servidor: " + erro))
        );
    }

    private void abrirCriarConta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/CreateAccount-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) PageCriar.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Criar Conta");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar a página de criação de conta.");
        }
    }

    private void redirecionarParaHome(UtilizadorDTO utilizador) {
        try {
            if (utilizador.getIdutilizador() == null) {
                mostrarErro("ID do utilizador não foi fornecido pela API.");
                return;
            }

            SessaoUtilizador.setNomeUtilizador(utilizador.getNome());
            SessaoUtilizador.setTipoUtilizador(utilizador.getIdtipoutilizador());
            SessaoUtilizador.setIdUtilizador(utilizador.getIdutilizador().intValue());

            Integer idTipo = utilizador.getIdtipoutilizador();
            String paginaInicial;

            switch (idTipo) {
                case 1: // Cliente
                    paginaInicial = "/com.example.gestaooleos/view/home-cliente.fxml";
                    break;
                case 2: // Empregado
                    paginaInicial = "/com.example.gestaooleos/view/home-funcionario.fxml";
                    break;
                case 3: // Escritório
                    paginaInicial = "/com.example.gestaooleos/view/home-escritorio.fxml";
                    break;
                case 4: // Comercial
                    paginaInicial = "/com.example.gestaooleos/view/home-comercial.fxml";
                    break;
                default:
                    mostrarErro("Tipo de utilizador inválido.");
                    return;
            }


            FXMLLoader loader = new FXMLLoader(getClass().getResource(paginaInicial));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Página Inicial - " + utilizador.getNome());

        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar a página inicial.");
        }
    }

    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setStyle("-fx-text-fill: #b20000; -fx-font-weight: bold; -fx-background-color: #ffd6d6; -fx-padding: 8 6; -fx-background-radius: 8;");
        erroLabel.setVisible(true);

        new Thread(() -> {
            try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> erroLabel.setVisible(false));
        }).start();
    }
}
