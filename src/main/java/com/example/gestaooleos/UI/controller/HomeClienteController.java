package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class HomeClienteController {

    @FXML private Label lblTotalContratos;
    @FXML private Label lblPagamentosPendentes;
    @FXML private Label lblRecolhas;
    @FXML private Label lblMensagem;
    @FXML private Label lblBemVindo;

    @FXML
    public void initialize() {
        // Aqui tu podes colocar chamadas reais à API no futuro
        carregarResumoCliente();
        String nome = SessaoUtilizador.getNomeUtilizador();
        if (nome != null && !nome.isEmpty()) {
            lblBemVindo.setText("Bem-vindo, " + nome + "!");
        } else {
            lblBemVindo.setText("Bem-vindo!");
        }
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
