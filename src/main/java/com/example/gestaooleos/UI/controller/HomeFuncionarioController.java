package com.example.gestaooleos.UI.controller;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import com.example.gestaooleos.UI.api.ContadorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class HomeFuncionarioController {

    @FXML private Label lblAtivos;
    @FXML private Label lblBemVindo;



    @FXML
    public void initialize() {
        carregarContadores();
        String nome = SessaoUtilizador.getNomeUtilizador();
        if (nome != null && !nome.isEmpty()) {
            lblBemVindo.setText("Bem-vindo, " + nome + "!");
        } else {
            lblBemVindo.setText("Bem-vindo!");
        }
    }


    public void carregarContadores() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Contratos/contar-estados"))
                .GET()
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        ContadorDTO contadores = mapper.readValue(json, ContadorDTO.class);

                        Platform.runLater(() -> {
                            lblAtivos.setText(String.valueOf(contadores.getAtivos()));
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }

}
