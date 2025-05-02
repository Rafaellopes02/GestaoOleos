package com.example.gestaooleos.UI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SelecionarMetodoPagamentoController {

    @FXML
    private ComboBox<String> comboMetodo;

    private String metodoSelecionado;

    @FXML
    private HBox sucessoBox;

    @FXML
    private Label sucessoLabel;




    @FXML
    public void initialize() {
        comboMetodo.getItems().addAll("Cartão", "MBWay","Multibanco", "Transferência");
    }

    @FXML
    private void confirmar() {
        metodoSelecionado = comboMetodo.getValue();
        fechar();
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) comboMetodo.getScene().getWindow();
        stage.close();
    }

    public Long getMetodoSelecionado() {
        if (metodoSelecionado == null) return null;

        return switch (metodoSelecionado) {
            case "MBWay" -> 1L;
            case "Transferência" -> 2L;
            case "Multibanco" -> 3L;
            case "Cartão" -> 4L;
            default -> throw new IllegalArgumentException("Método de pagamento inválido: " + metodoSelecionado);
        };
    }

    public void mostrarSucesso(String mensagem) {
        sucessoLabel.setText(mensagem);
        sucessoBox.setVisible(true);
    }

}
