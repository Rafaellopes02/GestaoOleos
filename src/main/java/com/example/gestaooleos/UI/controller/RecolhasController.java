package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.*;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import com.example.gestaooleos.UI.viewmodel.RecolhaViewModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecolhasController {

    @FXML private Button btnBack;
    @FXML private TableView<RecolhaViewModel> tabelaRecolhas;
    @FXML private TableColumn<RecolhaViewModel, String> nomeContratoRecolha;
    @FXML private TableColumn<RecolhaViewModel, String> MoradaRecolha;
    @FXML private TableColumn<RecolhaViewModel, String> DataRecolha;
    @FXML private TableColumn<RecolhaViewModel, String> estadoRecolha;
    @FXML private TableColumn<RecolhaViewModel, Void> verRecolha;
    @FXML private ComboBox<UtilizadorDTO> comboEmpregados;
    @FXML private Button btnNotificar;

    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();
    private final RecolhasClient recolhasClient = new RecolhasClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final ContratosClient contratosClient = new ContratosClient();
    private final EstadosRecolhaClient estadosRecolhaClient = new EstadosRecolhaClient();

    private List<Long> idsRecolhas;

    @FXML
    public void initialize() {
        nomeContratoRecolha.setCellValueFactory(new PropertyValueFactory<>("nome"));
        MoradaRecolha.setCellValueFactory(new PropertyValueFactory<>("morada"));
        DataRecolha.setCellValueFactory(new PropertyValueFactory<>("data"));
        estadoRecolha.setCellValueFactory(new PropertyValueFactory<>("estado"));
        btnNotificar.setOnAction(e -> notificarEmpregado());
        carregarEmpregados();


        verRecolha.setCellFactory(coluna -> new TableCell<>() {
            private final Button btn = new Button();

            {
                ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/image/ver.png")));
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                btn.setGraphic(icon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setOnAction(event -> {
                    RecolhaViewModel recolha = getTableView().getItems().get(getIndex());
                    Long idRecolha = idsRecolhas.get(getIndex());
                    abrirDialogEditarRecolha(recolha, idRecolha);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        carregarRecolhas();
    }

    private void abrirDialogEditarRecolha(RecolhaViewModel recolha, Long idRecolha) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Detalhes da Recolha");

        VBox conteudo = new VBox(10);
        conteudo.setStyle("-fx-background-color: #FFF8DC; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label lblTitulo = new Label("Detalhes da Recolha");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblNome = new Label("Nome do Contrato: " + recolha.getNome());
        Label lblMorada = new Label("Morada: " + recolha.getMorada());
        Label lblData = new Label("Data: " + recolha.getData());

        ComboBox<String> comboEstado = new ComboBox<>();
        comboEstado.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        estadosRecolhaClient.buscarEstados(json -> {
            try {
                List<EstadoRecolhaDTO> estados = mapper.readValue(json, new TypeReference<>() {});
                Platform.runLater(() -> {
                    comboEstado.getItems().clear();
                    for (EstadoRecolhaDTO estado : estados) {
                        String nome = estado.getNome().toLowerCase();
                        if (!nome.equals("concluído") && !nome.equals("cancelado")) {
                            comboEstado.getItems().add(estado.getNome());
                        }
                    }
                    comboEstado.setValue(recolha.getEstado());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> System.err.println("Erro ao carregar estados: " + erro));

        Button btnGuardar = new Button("Guardar");
        Button btnFechar = new Button("Fechar");
        btnGuardar.setStyle("-fx-background-color: #f4d35e; -fx-font-weight: bold;");
        btnFechar.setStyle("-fx-background-color: #f4d35e; -fx-font-weight: bold;");
        btnFechar.setOnAction(e -> dialogStage.close());

        btnGuardar.setOnAction(e -> {
            String novoEstadoNome = comboEstado.getValue();
            estadosRecolhaClient.buscarEstados(json -> {
                try {
                    List<EstadoRecolhaDTO> estados = mapper.readValue(json, new TypeReference<>() {});
                    EstadoRecolhaDTO estadoSelecionado = estados.stream()
                            .filter(est -> est.getNome().equals(novoEstadoNome))
                            .findFirst()
                            .orElse(null);

                    if (estadoSelecionado != null) {
                        recolhasClient.atualizarEstadoRecolha(idRecolha, estadoSelecionado.getId(),
                                sucesso -> Platform.runLater(() -> {
                                    carregarRecolhas();
                                    dialogStage.close();
                                }),
                                erro -> System.err.println("Erro ao atualizar estado: " + erro));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, erro -> System.err.println("Erro ao buscar estados: " + erro));
        });

        HBox botoes = new HBox(10, btnGuardar, btnFechar);
        botoes.setStyle("-fx-alignment: center;");

        conteudo.getChildren().addAll(lblTitulo, lblNome, lblMorada, lblData, new Label("Estado:"), comboEstado, botoes);
        Scene scene = new Scene(conteudo);
        dialogStage.setScene(scene);
        dialogStage.initOwner(tabelaRecolhas.getScene().getWindow());
        dialogStage.setResizable(false);
        dialogStage.show();
    }

    private void carregarRecolhas() {
        recolhasClient.buscarRecolhas(json -> {
            try {
                List<RecolhaDTO> lista = mapper.readValue(json, new TypeReference<>() {});
                List<CompletableFuture<RecolhaViewModel>> futuros = lista.stream().map(dto -> {
                    CompletableFuture<String> nomeContratoFuture = new CompletableFuture<>();
                    contratosClient.buscarContratoPorId(Long.valueOf(dto.getIdcontrato()),
                            resposta -> {
                                try {
                                    ContratoDTO contrato = mapper.readValue(resposta, ContratoDTO.class);
                                    nomeContratoFuture.complete(contrato.getNome());
                                } catch (Exception e) {
                                    nomeContratoFuture.complete("Erro Contrato");
                                }
                            },
                            erro -> nomeContratoFuture.complete("Erro Contrato"));

                    CompletableFuture<String> estadoFuture = new CompletableFuture<>();
                    estadosRecolhaClient.buscarEstadoPorId(dto.getIdestadorecolha(),
                            resposta -> {
                                try {
                                    EstadoRecolhaDTO e = mapper.readValue(resposta, EstadoRecolhaDTO.class);
                                    estadoFuture.complete(e.getNome());
                                } catch (Exception e1) {
                                    estadoFuture.complete("Erro Estado");
                                }
                            },
                            erro -> estadoFuture.complete("Erro Estado"));

                    return CompletableFuture.allOf(nomeContratoFuture, estadoFuture)
                            .thenApply(__ -> new RecolhaViewModel(
                                    nomeContratoFuture.join(),
                                    dto.getMorada(),
                                    dto.getData().toString(),
                                    estadoFuture.join()
                            ));
                }).toList();

                CompletableFuture.allOf(futuros.toArray(new CompletableFuture[0]))
                        .thenRun(() -> {
                            List<RecolhaViewModel> modelos = futuros.stream().map(CompletableFuture::join).toList();
                            Platform.runLater(() -> {
                                tabelaRecolhas.setItems(FXCollections.observableArrayList(modelos));
                                idsRecolhas = lista.stream().map(RecolhaDTO::getIdrecolha).toList();
                            });
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> System.err.println("Erro ao buscar recolhas: " + erro));
    }

    private void carregarEmpregados() {
        utilizadoresClient.buscarEmpregados(json -> {
            try {
                List<UtilizadorDTO> empregados = mapper.readValue(json, new TypeReference<>() {});
                Platform.runLater(() -> comboEmpregados.setItems(FXCollections.observableArrayList(empregados)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> System.err.println("Erro ao carregar empregados: " + erro));
    }

    private void notificarEmpregado() {
        RecolhaViewModel selecionada = tabelaRecolhas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            System.out.println("⚠️ Selecione uma recolha na tabela!");
            return;
        }

        UtilizadorDTO empregado = comboEmpregados.getSelectionModel().getSelectedItem();
        if (empregado == null) {
            System.out.println("⚠️ Selecione um empregado!");
            return;
        }

        // Aqui identificas a recolha via índice
        int index = tabelaRecolhas.getItems().indexOf(selecionada);
        Long idRecolha = idsRecolhas.get(index);

        // Aqui poderias usar um estado "Notificado", por exemplo ID = 4
        int idEstadoNotificado = 4;
        recolhasClient.atualizarEstadoRecolha(idRecolha, idEstadoNotificado,
                sucesso -> Platform.runLater(() -> {
                    carregarRecolhas();
                    comboEmpregados.getSelectionModel().clearSelection();
                    tabelaRecolhas.getSelectionModel().clearSelection();
                    System.out.println("✅ Empregado notificado!");
                }),
                erro -> System.err.println("Erro ao notificar: " + erro));
    }



    @FXML
    private void voltarHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/home-funcionario.fxml"));
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
