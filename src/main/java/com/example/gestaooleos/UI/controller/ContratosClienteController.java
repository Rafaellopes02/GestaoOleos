package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.ContratosClient;
import com.example.gestaooleos.UI.api.ContratosClienteDTO;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class ContratosClienteController {

    @FXML
    private TableView<ContratosClienteDTO> tabelaContratos;
    @FXML
    private TableColumn<ContratosClienteDTO, String> nomeContratoColuna;
    @FXML
    private TableColumn<ContratosClienteDTO, String> dataInicioColuna;
    @FXML
    private TableColumn<ContratosClienteDTO, String> dataFimColuna;
    @FXML
    private TableColumn<ContratosClienteDTO, String> estadoColuna;
    @FXML
    private TableColumn<ContratosClienteDTO, String> moradaColuna;
    @FXML
    private Label lblSemContratos;
    @FXML
    private Button btnBack;

    private final ContratosClient contratosClient = new ContratosClient();

    @FXML
    public void initialize() {
        configurarTabela();
        carregarContratosCliente();
    }

    private void configurarTabela() {
        nomeContratoColuna.setCellValueFactory(new PropertyValueFactory<>("nomeContrato"));
        dataInicioColuna.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        dataFimColuna.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        estadoColuna.setCellValueFactory(new PropertyValueFactory<>("estado"));
        moradaColuna.setCellValueFactory(new PropertyValueFactory<>("moradaCliente"));
    }

    private void carregarContratosCliente() {
        Long idUtilizador = SessaoUtilizador.getIdUtilizador() != null
                ? SessaoUtilizador.getIdUtilizador().longValue()
                : null;

        if (idUtilizador == null) {
            System.err.println("ID do utilizador não encontrado na sessão!");
            return;
        }

        contratosClient.buscarContratosDoCliente(idUtilizador, resposta -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ContratosClienteDTO[] contratos = mapper.readValue(resposta, ContratosClienteDTO[].class);

                Platform.runLater(() -> {
                    tabelaContratos.getItems().clear();
                    if (contratos.length > 0) {
                        tabelaContratos.getItems().addAll(contratos);
                        lblSemContratos.setVisible(false);
                    } else {
                        lblSemContratos.setVisible(true);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> {
            Platform.runLater(() -> System.err.println(erro));
        });
    }


    @FXML
    private void voltarHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/home-cliente.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Página Inicial");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
