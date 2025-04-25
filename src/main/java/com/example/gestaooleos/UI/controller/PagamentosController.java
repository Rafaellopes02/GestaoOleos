package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.PagamentosClient;
import com.example.gestaooleos.UI.api.PagamentoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.math.BigDecimal;


public class PagamentosController {

    @FXML
    private TableColumn<PagamentoDTO, String> colData;

    @FXML
    private TableColumn<PagamentoDTO, BigDecimal> colValor;

    @FXML
    private TableColumn<PagamentoDTO, String> colCliente;

    @FXML
    private TableColumn<PagamentoDTO, String> colContrato;

    @FXML
    private TableColumn<PagamentoDTO, String> colEstado;

    @FXML
    private TableView<PagamentoDTO> tabelaPagamentos;

    @FXML
    private Label lblRecebido;
    @FXML
    private Label lblPendentes;

    @FXML private Button btnBack;

    private final PagamentosClient pagamentosClient = new PagamentosClient();

    @FXML
    public void initialize() {
        colData.setCellValueFactory(new PropertyValueFactory<>("datapagamento"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colContrato.setCellValueFactory(new PropertyValueFactory<>("nomeContrato"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Carregar dados da tabela
        carregarTabelaPagamentos();
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

    private void carregarTabelaPagamentos() {
        pagamentosClient.buscarPagamentosCompletos(pagamentos -> {
            Platform.runLater(() -> {
                tabelaPagamentos.getItems().setAll(pagamentos);
            });
        }, erro -> {
            System.err.println("Erro ao carregar pagamentos: " + erro.getMessage());
        });
    }


}
