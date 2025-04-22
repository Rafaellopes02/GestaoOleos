package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadoresClient;
import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
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
import javafx.stage.Stage;

import java.util.List;

public class UtilizadoresController {

    @FXML private Button btnContratos;
    @FXML private Label ClienteCountLabel;
    @FXML private Label FuncionarioCountLabel;
    @FXML private Label EscritorioCountLabel;
    @FXML private Label ComercialCountLabel;


    @FXML
    private TableView<UtilizadorDTO> tabelaUtilizador;

    @FXML
    private TableColumn<UtilizadorDTO, String> nomeUtilizador;

    @FXML
    private TableColumn<UtilizadorDTO, String> telefoneUtilizador;

    @FXML
    private TableColumn<UtilizadorDTO, String> moradaUtilizador;

    @FXML
    private TableColumn<UtilizadorDTO, Integer> idtipoutilizadorUtilizador;

    @FXML
    private TableColumn<UtilizadorDTO, String> usernameUtilizador;

    private final UtilizadoresClient utilizadorClient = new UtilizadoresClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        btnContratos.setOnAction(e -> redirecionarContratos());
        nomeUtilizador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        telefoneUtilizador.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        moradaUtilizador.setCellValueFactory(new PropertyValueFactory<>("morada"));
        idtipoutilizadorUtilizador.setCellValueFactory(new PropertyValueFactory<>("idtipoutilizador"));
        usernameUtilizador.setCellValueFactory(new PropertyValueFactory<>("username"));

        idtipoutilizadorUtilizador.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String tipoTexto = switch (item) {
                        case 1 -> "Cliente";
                        case 2 -> "Funcionário";
                        case 3 -> "Escritório";
                        case 6 -> "Comercial";
                        default -> "Outro";
                    };
                    setText(tipoTexto);
                }
            }
        });

        carregarUtilizadores();
    }

    private void carregarUtilizadores() {
        utilizadorClient.buscarUtilizadores(json -> {
            try {
                List<UtilizadorDTO> utilizadores = mapper.readValue(json, new TypeReference<>() {});

                // Atualizar tabela
                Platform.runLater(() -> tabelaUtilizador.setItems(FXCollections.observableArrayList(utilizadores)));

                // Contagem por tipo de utilizador
                long clientes = utilizadores.stream().filter(u -> u.getIdtipoutilizador() != null && u.getIdtipoutilizador() == 1).count();
                long funcionarios = utilizadores.stream().filter(u -> u.getIdtipoutilizador() == 2).count();
                long escritorios = utilizadores.stream().filter(u -> u.getIdtipoutilizador() == 3).count();
                long comerciais = utilizadores.stream().filter(u -> u.getIdtipoutilizador() == 6).count();

                // Atualizar labels dos cartões
                Platform.runLater(() -> {
                    ClienteCountLabel.setText(String.valueOf(clientes));
                    FuncionarioCountLabel.setText(String.valueOf(funcionarios));
                    EscritorioCountLabel.setText(String.valueOf(escritorios));
                    ComercialCountLabel.setText(String.valueOf(comerciais));
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> Platform.runLater(() ->
                System.out.println("Erro ao buscar utilizadores: " + erro)
        ));
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void redirecionarContratos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/contratos-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnContratos.getScene().getWindow();


            stage.getScene().setRoot(root);
            stage.setTitle("Contratos");

            FullscreenHelper.ativarFullscreen(stage); // aplica fullscreen depois do layout

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar a página de contratos.", Alert.AlertType.ERROR);
        }
    }
}
