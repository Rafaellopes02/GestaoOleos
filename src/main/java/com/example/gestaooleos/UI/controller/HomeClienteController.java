package com.example.gestaooleos.UI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class HomeClienteController {

    @FXML private Label lblTotalContratos;
    @FXML private Label lblPagamentosPendentes;
    @FXML private Label lblRecolhas;
    @FXML private Label lblMensagem;

    @FXML
    public void initialize() {
        // Aqui tu podes colocar chamadas reais à API no futuro
        carregarResumoCliente();
    }

    private void carregarResumoCliente() {
        // Simulação de dados
        Platform.runLater(() -> {
            lblTotalContratos.setText("3");
            lblPagamentosPendentes.setText("2");
            lblRecolhas.setText("1");
            lblMensagem.setText("Obrigado por confiar nos nossos serviços!");
        });
    }
}
