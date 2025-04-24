package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadoresClient;
import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.utils.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class UtilizadoresController {

    @FXML private Label ClienteCountLabel;
    @FXML private Label FuncionarioCountLabel;
    @FXML private Label EscritorioCountLabel;
    @FXML private Label ComercialCountLabel;
    @FXML private Button btnBack;

    @FXML private TableView<UtilizadorDTO> tabelaUtilizador;
    @FXML private TableColumn<UtilizadorDTO, String> nomeUtilizador;
    @FXML private TableColumn<UtilizadorDTO, String> telefoneUtilizador;
    @FXML private TableColumn<UtilizadorDTO, String> moradaUtilizador;
    @FXML private TableColumn<UtilizadorDTO, Integer> idtipoutilizadorUtilizador;
    @FXML private TableColumn<UtilizadorDTO, String> usernameUtilizador;
    @FXML private TableColumn<UtilizadorDTO, Void> verUtilizador;

    private final UtilizadoresClient utilizadorClient = new UtilizadoresClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        // Configurar colunas
        nomeUtilizador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        telefoneUtilizador.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        moradaUtilizador.setCellValueFactory(new PropertyValueFactory<>("morada"));
        idtipoutilizadorUtilizador.setCellValueFactory(new PropertyValueFactory<>("idtipoutilizador"));
        usernameUtilizador.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Traduzir ID para texto
        idtipoutilizadorUtilizador.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(switch (item) {
                        case 1 -> "Cliente";
                        case 2 -> "Funcionário";
                        case 3 -> "Escritório";
                        case 6 -> "Comercial";
                        default -> "Outro";
                    });
                }
            }
        });

        verUtilizador.setCellFactory(coluna -> new TableCell<>() {
            private final Button btn = new Button();

            {
                ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/image/ver.png")));
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                btn.setGraphic(icon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setOnAction(event -> {
                    UtilizadorDTO utilizador = getTableView().getItems().get(getIndex());
                    abrirDialogUtilizador(utilizador);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        carregarUtilizadores();
    }

    private void carregarUtilizadores() {
        utilizadorClient.buscarUtilizadores(json -> {
            try {
                List<UtilizadorDTO> lista = mapper.readValue(json, new TypeReference<>() {});
                long clientes = lista.stream().filter(u -> u.getIdtipoutilizador() == 1).count();
                long funcionarios = lista.stream().filter(u -> u.getIdtipoutilizador() == 2).count();
                long escritorios = lista.stream().filter(u -> u.getIdtipoutilizador() == 3).count();
                long comerciais = lista.stream().filter(u -> u.getIdtipoutilizador() == 6).count();

                Platform.runLater(() -> {
                    tabelaUtilizador.setItems(FXCollections.observableArrayList(lista));
                    ClienteCountLabel.setText(String.valueOf(clientes));
                    FuncionarioCountLabel.setText(String.valueOf(funcionarios));
                    EscritorioCountLabel.setText(String.valueOf(escritorios));
                    ComercialCountLabel.setText(String.valueOf(comerciais));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> Platform.runLater(() ->
                System.err.println("Erro ao buscar utilizadores: " + erro)
        ));
    }
    private void abrirDialogUtilizador(UtilizadorDTO utilizador) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/ver-utilizador-dialog.fxml"));

            Parent root = loader.load();

            VerUtilizadorController controller = loader.getController();
            controller.setUtilizador(utilizador);
            controller.setOnSaveCallback(this::carregarUtilizadores);

            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(btnBack.getScene().getWindow());
            dialog.setTitle("Detalhes do Utilizador");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltarHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestaooleos/view/home-funcionario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Página Inicial");
            FullscreenHelper.ativarFullscreen(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
