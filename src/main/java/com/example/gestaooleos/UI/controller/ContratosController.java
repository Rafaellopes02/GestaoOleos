package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.*;
import com.example.gestaooleos.UI.api.ContratoDTO;
import com.example.gestaooleos.UI.api.ContratosClient;
import com.example.gestaooleos.UI.utils.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;       // ← necessário
import javafx.scene.Parent;          // ← necessário
import javafx.scene.Scene;           // ← necessário
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
public class ContratosController {

    @FXML private Label lblAtivos;
    @FXML private Label lblConcluidos;

    @FXML private TableView<ContratoDTO> tabelaContratos;
    @FXML private TableColumn<ContratoDTO, String> nomeCol;
    @FXML private TableColumn<ContratoDTO, String> dataInicioCol;
    @FXML private TableColumn<ContratoDTO, String> dataFimCol;
    @FXML private TableColumn<ContratoDTO, String> estadoCol;

    @FXML private Button btnAdicionar;
    @FXML private Button btnBack;

    private final ContratosClient contratosClient = new ContratosClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        dataInicioCol.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        dataFimCol.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));
        btnAdicionar.setOnAction(e -> abrirModalAdicionarContrato());
        carregarContratos();
        carregarContadores();
    }

    public void carregarContratos() {
        contratosClient.buscarContratos(json -> {
            try {
                List<ContratoDTO> lista = mapper.readValue(json, new TypeReference<>() {});
                Platform.runLater(() ->
                        tabelaContratos.setItems(FXCollections.observableArrayList(lista))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> System.err.println("Erro ao buscar contratos: " + erro));
    }

    public void carregarContadores() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Contratos/contar-estados"))
                .GET().build();

        HttpClient.newHttpClient()
                .sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        ContadorDTO dto = mapper.readValue(json, ContadorDTO.class);
                        Platform.runLater(() -> {
                            lblAtivos.setText(String.valueOf(dto.getAtivos()));
                            lblConcluidos.setText(String.valueOf(dto.getConcluidos()));
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @FXML
    private void abrirModalAdicionarContrato() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com.example.gestaooleos/view/ModalAdicionarContrato.fxml"
            ));
            Parent root = loader.load();               // ← load()
            ModalAdicionarContratoController modalCtrl = loader.getController(); // ← getController()

            modalCtrl.setContratosController(this);

            Stage modal = new Stage();
            modal.initStyle(StageStyle.UNDECORATED);
            modal.initModality(Modality.WINDOW_MODAL);
            modal.initOwner(btnAdicionar.getScene().getWindow());
            modal.setScene(new Scene(root, 420, 460));
            modal.setResizable(false);
            modal.centerOnScreen();
            modal.showAndWait();

            carregarContratos();
            carregarContadores();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Não foi possível abrir o modal.").showAndWait();
        }
    }

    @FXML
    private void voltarHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.gestaooleos/view/home-funcionario.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Página Inicial");
            FullscreenHelper.ativarFullscreen(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
            // podes mostrar um alerta aqui, se quiseres
        }
    }

}