package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.PagamentoClienteDTO;
import com.example.gestaooleos.UI.api.PagamentosClienteClient;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class PagamentosClienteController {

    @FXML private TableView<PagamentoClienteDTO> tabelaPagamentos;
    @FXML private TableColumn<PagamentoClienteDTO, String> nomeContratoCol;
    @FXML private TableColumn<PagamentoClienteDTO, Number> valorCol;
    @FXML private TableColumn<PagamentoClienteDTO, String> dataPagamentoCol;
    @FXML private TableColumn<PagamentoClienteDTO, String> estadoCol;
    @FXML private TableColumn<PagamentoClienteDTO, Void> acoesCol;
    @FXML private Button btnBack;

    private final ObservableList<PagamentoClienteDTO> pagamentos = FXCollections.observableArrayList();
    private Long idClienteAtual;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarPagamentosPendentes(SessaoUtilizador.getIdUtilizador().longValue());
    }

    private void configurarTabela() {
        nomeContratoCol.setCellValueFactory(new PropertyValueFactory<>("nomeContrato"));
        valorCol.setCellValueFactory(new PropertyValueFactory<>("valor"));
        dataPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("datapagamento"));
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));

        acoesCol.setCellFactory(param -> new TableCell<>() {
            private final Button pagarBtn = new Button("Pagar");
            private final Button cancelarBtn = new Button("Cancelar");
            private final HBox container = new HBox(5, pagarBtn, cancelarBtn);

            {
                pagarBtn.setOnAction(event -> {
                    PagamentoClienteDTO pagamento = getTableView().getItems().get(getIndex());
                    pagarPagamento(pagamento);
                });

                cancelarBtn.setOnAction(event -> {
                    PagamentoClienteDTO pagamento = getTableView().getItems().get(getIndex());
                    cancelarPagamento(pagamento);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        tabelaPagamentos.setItems(pagamentos);
    }

    public void carregarPagamentosPendentes(Long idCliente) {
        this.idClienteAtual = idCliente;

        Task<List<PagamentoClienteDTO>> task = PagamentosClienteClient.listarPagamentosPendentes(idCliente);
        task.setOnSucceeded(event -> pagamentos.setAll(task.getValue()));
        task.setOnFailed(event -> mostrarErro("Erro ao carregar pagamentos: " + task.getException().getMessage()));

        new Thread(task).start();
    }

    private void pagarPagamento(PagamentoClienteDTO pagamento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/SelecionarMetodoPagamento.fxml"));
            Parent root = loader.load();
            SelecionarMetodoPagamentoController controller = loader.getController();

            Stage stage = new Stage();
            stage.initOwner(btnBack.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle("Selecionar Método de Pagamento");
            stage.showAndWait();

            Long idMetodoSelecionado = controller.getMetodoSelecionado();
            if (idMetodoSelecionado == null) return;

            Task<Void> task = PagamentosClienteClient.pagarPagamento(pagamento.getIdpagamento(), idMetodoSelecionado);

            carregarPagamentosPendentes(idClienteAtual);
            task.setOnFailed(event -> mostrarErro("Erro ao pagar: " + task.getException().getMessage()));

            new Thread(task).start();

        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarErro("Erro ao abrir o modal de pagamento.");
        }
    }

    private void cancelarPagamento(PagamentoClienteDTO pagamento) {
        Task<Void> task = PagamentosClienteClient.cancelarPagamento(pagamento.getIdpagamento());

        carregarPagamentosPendentes(idClienteAtual);
        task.setOnFailed(event -> mostrarErro("Erro ao cancelar pagamento: " + task.getException().getMessage()));

        new Thread(task).start();
    }

    @FXML
    private void voltarHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/home-cliente.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Página Inicial");
            FullscreenHelper.ativarFullscreen(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarErro("Erro ao voltar à página inicial.");
        }
    }

    private void mostrarInfo(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Sucesso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
