package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.ContratoDTO;
import com.example.gestaooleos.UI.api.ContratosClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ContratosController {

    @FXML
    private TableView<ContratoDTO> tabelaContratos;

    @FXML
    private TableColumn<ContratoDTO, String> nomeCol;

    @FXML
    private TableColumn<ContratoDTO, String> dataInicioCol;

    @FXML
    private TableColumn<ContratoDTO, String> dataFimCol;

    @FXML
    private TableColumn<ContratoDTO, String> estadoCol;

    private final ContratosClient contratosClient = new ContratosClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        tabelaContratos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaContratos.setFocusTraversable(false);
        tabelaContratos.setSelectionModel(null);

        // Liga as colunas aos campos do DTO
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        dataInicioCol.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        dataFimCol.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Carrega os dados da API
        carregarContratos();
    }

    private void carregarContratos() {
        contratosClient.buscarContratos(json -> {
            try {
                List<ContratoDTO> contratos = mapper.readValue(json, new TypeReference<>() {});
                Platform.runLater(() ->
                        tabelaContratos.setItems(FXCollections.observableArrayList(contratos))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> Platform.runLater(() -> {
            System.out.println("Erro ao buscar contratos: " + erro);
        }));
    }
}
