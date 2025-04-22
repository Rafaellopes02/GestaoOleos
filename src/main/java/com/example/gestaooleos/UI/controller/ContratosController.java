package com.example.gestaooleos.UI.controller;
import javafx.stage.Modality;
import javafx.scene.control.Label;
import com.example.gestaooleos.UI.api.ContratoDTO;
import com.example.gestaooleos.UI.api.ContratosClient;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import com.example.gestaooleos.UI.api.ContadorDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.List;
import javafx.stage.StageStyle;

public class ContratosController {

    @FXML private Button btnUtilizadores;

    @FXML private Label lblAtivos;
    @FXML private Label lblConcluidos;

    @FXML
    private TableView<ContratoDTO> tabelaContratos;

    @FXML
    private Button btnAdicionar;

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
        btnUtilizadores.setOnAction(e -> redirecionarUtilizadores());

        tabelaContratos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaContratos.setFocusTraversable(false);
        tabelaContratos.setSelectionModel(null);

        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        dataInicioCol.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        dataFimCol.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));
        btnAdicionar.setOnAction(e -> abrirModalAdicionarContrato());

        carregarContratos();
        carregarContadores(); // <--- ESSENCIAL
    }


    public void carregarContratos() {
        contratosClient.buscarContratos(json -> {
            System.out.println("JSON recebido: " + json); // 👈 ADICIONA AQUI
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

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void redirecionarUtilizadores() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/utilizadores-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnUtilizadores.getScene().getWindow();

            // ⚠️ Trocar o root primeiro
            stage.getScene().setRoot(root);
            stage.setTitle("Utilizadores");

            FullscreenHelper.ativarFullscreen(stage); // aplica fullscreen depois do layout

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar a página de utilizadores.", Alert.AlertType.ERROR);
        }
    }
    public void carregarContadores() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Contratos/contar-estados"))
                .GET()
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        ContadorDTO contadores = mapper.readValue(json, ContadorDTO.class);

                        Platform.runLater(() -> {
                            lblAtivos.setText(String.valueOf(contadores.getAtivos()));
                            lblConcluidos.setText(String.valueOf(contadores.getConcluidos()));
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }

    @FXML
    private void abrirModalAdicionarContrato() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/ModalAdicionarContrato.fxml"));
            Parent root = loader.load();

            ModalAdicionarContratoController controller = loader.getController();
            controller.setContratosController(this); // permite refrescar a tabela depois

            Stage modalStage = new Stage();
            modalStage.initStyle(StageStyle.UNDECORATED); // <- aqui está o correto!
            modalStage.setTitle("Adicionar Contrato");
            modalStage.setScene(new Scene(root, 400, 400)); // ⬅️ Definindo tamanho direto

            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(btnUtilizadores.getScene().getWindow());

            modalStage.setResizable(false);
            modalStage.centerOnScreen();
            modalStage.showAndWait(); // Espera o modal fechar para continuar

            carregarContratos(); // Refresca após fechar
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir o modal.", Alert.AlertType.ERROR);
        }
    }





}
