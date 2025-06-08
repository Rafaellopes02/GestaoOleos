package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.PagamentosClient;
import com.example.gestaooleos.UI.api.PagamentoDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

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

    @FXML
    private Button btnBack;

    private final PagamentosClient pagamentosClient = new PagamentosClient();

    @FXML
    public void initialize() {
        colData.setCellValueFactory(new PropertyValueFactory<>("datapagamento"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colContrato.setCellValueFactory(new PropertyValueFactory<>("nomeContrato"));

        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        configurarColunaEstado();

        carregarTabelaPagamentos();
        carregarTotais();
    }

    private void configurarColunaEstado() {
        colEstado.setCellFactory(new Callback<TableColumn<PagamentoDTO, String>, TableCell<PagamentoDTO, String>>() {
            @Override
            public TableCell<PagamentoDTO, String> call(TableColumn<PagamentoDTO, String> param) {
                return new TableCell<>() {
                    private final Button btnPagar = new Button("Pagar");

                    {
                        btnPagar.getStyleClass().add("botao-pagar");
                        btnPagar.setOnAction(event -> {
                            PagamentoDTO pagamento = getTableView().getItems().get(getIndex());
                            abrirModalPagamento(pagamento);
                        });
                    }

                    @Override
                    protected void updateItem(String estado, boolean empty) {
                        super.updateItem(estado, empty);

                        if (empty || estado == null) {
                            setGraphic(null);
                            setText(null);
                        } else if (estado.trim().equalsIgnoreCase("Pendente")) {
                            setGraphic(btnPagar);
                            setText(null);
                        } else {
                            setGraphic(null);
                            setText(estado);
                        }
                    }
                };
            }
        });
    }

    @FXML private Label lblQtdPendentes;
    @FXML private Label lblValorPendentes;


    private void carregarTotais() {
        pagamentosClient.buscarPagamentosCompletos(pagamentos -> {
            Platform.runLater(() -> {
                long qtdPendentes = Arrays.stream(pagamentos)
                        .filter(p -> "Pendente".equalsIgnoreCase(p.getEstado()))
                        .count();

                double valorPendentes = Arrays.stream(pagamentos)
                        .filter(p -> "Pendente".equalsIgnoreCase(p.getEstado()))
                        .mapToDouble(p -> p.getValor().doubleValue())
                        .sum();

                double valorRecebido = Arrays.stream(pagamentos)
                        .filter(p -> "Concluido".equalsIgnoreCase(p.getEstado()))
                        .mapToDouble(p -> p.getValor().doubleValue())
                        .sum();

                lblQtdPendentes.setText(qtdPendentes + "");
                lblValorPendentes.setText(String.format("%.2f€", valorPendentes));
                lblRecebido.setText(String.format("%.2f€", valorRecebido));
            });
        }, erro -> {
            Platform.runLater(() -> {
                lblQtdPendentes.setText("Erro");
                lblValorPendentes.setText("Erro");
                lblRecebido.setText("Erro");
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
            List<PagamentoDTO> listaOrdenada = new ArrayList<>(Arrays.asList(pagamentos));
            listaOrdenada.sort(Comparator.comparing(PagamentoDTO::getIdpagamento).reversed());

            Platform.runLater(() -> {
                tabelaPagamentos.getItems().setAll(listaOrdenada);
            });
        }, erro -> {
            System.err.println("Erro ao carregar pagamentos: " + erro.getMessage());
        });
    }

    private void abrirModalPagamento(PagamentoDTO pagamento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/SelecionarMetodoPagamento.fxml"));
            Parent root = loader.load();

            SelecionarMetodoPagamentoController controller = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Efetuar Pagamento");
            stage.showAndWait();

            Long idMetodoSelecionado = controller.getMetodoSelecionado();
            if (idMetodoSelecionado == null) return;

            pagamentosClient.atualizarEstadoParaConcluido(pagamento.getIdpagamento(), idMetodoSelecionado,
                    () -> Platform.runLater(() -> {
                        carregarTabelaPagamentos();
                        carregarTotais();
                    }),
                    erro -> Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar pagamento: " + erro.getMessage(), ButtonType.OK);
                        alert.showAndWait();
                    })
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processarPagamento(PagamentoDTO pagamento) {
        System.out.println("A pagar pagamento ID: " + pagamento.getIdpagamento());
    }
}
