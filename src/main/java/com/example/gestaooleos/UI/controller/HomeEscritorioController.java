package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.ContadorDTO;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HomeEscritorioController {

    @FXML private Label lblBemVindo;
    @FXML private Label lblAtivos;
    @FXML private Label lblRecolhasPedidas;

    @FXML
    public void initialize() {
        String nome = SessaoUtilizador.getNomeUtilizador();
        lblBemVindo.setText("Bem-vindo, " + (nome != null ? nome : "Escritório") + "!");

        carregarContadores();
        carregarRecolhasPendentes();
    }

    private void carregarContadores() {
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

                        Platform.runLater(() -> lblAtivos.setText(String.valueOf(contadores.getAtivos())));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void carregarRecolhasPendentes() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Recolhas/contar?estado=1"))
                .GET()
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        var node = mapper.readTree(json);
                        int quantidade = node.get("quantidade").asInt();

                        Platform.runLater(() -> lblRecolhasPedidas.setText(String.valueOf(quantidade)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> lblRecolhasPedidas.setText("Erro"));
                    }
                });
    }

}
