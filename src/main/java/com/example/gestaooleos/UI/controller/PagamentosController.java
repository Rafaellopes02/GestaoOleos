package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.PagamentosClient;
import com.example.gestaooleos.UI.api.PagamentoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.stage.Stage;

import java.util.Map;

import java.util.List;

public class PagamentosController {

    @FXML
    private Label lblRecebido;
    @FXML
    private Label lblPendentes;

    @FXML private Button btnBack;

    private final PagamentosClient pagamentosClient = new PagamentosClient();

    @FXML
    public void initialize() {
        carregarTotais();
    }

    private void carregarTotais() {
        pagamentosClient.buscarTotais((totais) -> {
            Platform.runLater(() -> {
                double recebido = ((Number) totais.get("totalRecebido")).doubleValue();
                double pendente = ((Number) totais.get("totalPendente")).doubleValue();

                lblRecebido.setText(String.format("%.2f€", recebido));
                lblPendentes.setText(String.format("%.2f€", pendente));
            });
        }, erro -> {
            Platform.runLater(() -> {
                lblRecebido.setText("Erro");
                lblPendentes.setText("Erro");
            });
            System.err.println("Erro ao buscar totais: " + erro);
        });
    }

    @FXML
    private void voltarHome() {
        // Aqui colocas a lógica para voltar para a home
        // Exemplo básico:
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/home-funcionario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Página Inicial");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
