package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.*;
import com.example.gestaooleos.UI.viewmodel.RecolhaViewModel;
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
import com.example.gestaooleos.UI.utils.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javafx.stage.StageStyle;
import javafx.util.StringConverter;


public class RecolhasController {

    @FXML private ComboBox<ContratoDTO> comboContrato;
    @FXML private TextField txtQuantidade;
    @FXML private DatePicker dateRecolha;
    @FXML private TextField txtMorada;
    @FXML private TextField txtBidoes;
    @FXML private TextArea txtObservacoes;
    @FXML private Button btnSolicitar;
    @FXML private Button btnBack;

    @FXML private TableView<RecolhaDTO> tabelaRecolhas;
    @FXML private TableColumn<RecolhaDTO, String> nomeContratoRecolha;
    @FXML private TableColumn<RecolhaDTO, String> MoradaRecolha;
    @FXML private TableColumn<RecolhaDTO, String> DataRecolha;
    @FXML private TableColumn<RecolhaDTO, String> estadoRecolha;
    @FXML private TableColumn<RecolhaDTO, Void> verRecolha;


    private final RecolhasClient recolhasClient = new RecolhasClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final ContratosClient contratosClient = new ContratosClient();
    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();
    private final EstadosRecolhaClient estadosRecolhaClient = new EstadosRecolhaClient();

    @FXML
    public void initialize() {
        nomeContratoRecolha.setCellValueFactory(new PropertyValueFactory<>("nomeContrato"));
        MoradaRecolha.setCellValueFactory(new PropertyValueFactory<>("morada"));
        DataRecolha.setCellValueFactory(new PropertyValueFactory<>("data"));
        estadoRecolha.setCellValueFactory(new PropertyValueFactory<>("estado"));
        verRecolha.setCellFactory(coluna -> new TableCell<RecolhaDTO, Void>() {
            private final Button btn = new Button();

            {
                ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/image/ver.png")));
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                btn.setGraphic(icon);
                btn.setStyle("-fx-background-color: transparent;");
                btn.setOnAction(event -> {
                    RecolhaDTO recolha = getTableView().getItems().get(getIndex());
                    abrirDialogRecolha(recolha);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        btnSolicitar.setOnAction(event -> solicitarRecolha());
        carregarRecolhas();
        carregarContratos();
    }

    public void carregarRecolhas() {
        recolhasClient.buscarRecolhas(json -> {
            try {
                List<RecolhaDTO> lista = mapper.readValue(json, new TypeReference<>() {});

                // Enriquecer cada recolha com dados extra
                List<CompletableFuture<RecolhaViewModel>> futuros = lista.stream().map(dto -> {
                    // Cria futures para buscar os dados associados
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

                    // Quando os 3 estiverem prontos, cria o ViewModel
                    return CompletableFuture.allOf(nomeContratoFuture, estadoFuture)
                            .thenApply(__ -> new RecolhaViewModel(
                                    nomeContratoFuture.join(),
                                    dto.getMorada(),
                                    dto.getData().toString(),
                                    estadoFuture.join()
                            ));
                }).toList();

                // Esperar que todos terminem
                CompletableFuture.allOf(futuros.toArray(new CompletableFuture[0]))
                        .thenRun(() -> {
                            List<RecolhaViewModel> modelos = futuros.stream()
                                    .map(CompletableFuture::join)
                                    .toList();
                            Platform.runLater(() -> tabelaRecolhas.setItems(FXCollections.observableArrayList()));
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> System.err.println("Erro ao buscar recolhas: " + erro));
    }

    private void carregarContratos() {
        contratosClient.buscarContratossemestado(json -> {
            try {
                System.out.println("📥 JSON contratos recebido: " + json);
                List<ContratoDTO> contratos = mapper.readValue(json, new TypeReference<>() {});
                Platform.runLater(() -> {
                    comboContrato.setItems(FXCollections.observableArrayList(contratos));
                    comboContrato.setConverter(new StringConverter<>() {
                        @Override
                        public String toString(ContratoDTO contrato) {
                            return contrato != null ? contrato.getNome() : "";
                        }

                        @Override
                        public ContratoDTO fromString(String string) {
                            return comboContrato.getItems().stream()
                                    .filter(c -> c.getNome().equals(string))
                                    .findFirst()
                                    .orElse(null);
                        }
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> System.err.println("Erro ao carregar contratos: " + erro));
    }
    @FXML
    private void solicitarRecolha() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de Recolha");
        confirmacao.setHeaderText("Deseja mesmo registar esta recolha?");
        confirmacao.setContentText("Clique em 'Sim' para continuar ou 'Não' para cancelar.");

        ButtonType botaoSim = new ButtonType("Sim");
        ButtonType botaoNao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacao.getButtonTypes().setAll(botaoSim, botaoNao);

        confirmacao.showAndWait().ifPresent(botao -> {
            if (botao == botaoSim) {
                ContratoDTO contratoSelecionado = comboContrato.getSelectionModel().getSelectedItem();

                if (contratoSelecionado == null) {
                    System.out.println("❌ Nenhum contrato selecionado!");
                    return;
                }

                int idutilizador = contratoSelecionado.getIdutilizador();
                System.out.println("🧪 ID Utilizador do contrato selecionado: " + idutilizador);

                if (idutilizador == 0) {
                    System.out.println("❌ Este contrato não tem utilizador associado!");
                    return;
                }

                try {
                    LocalDate data = dateRecolha.getValue();
                    double quantidade = Double.parseDouble(txtQuantidade.getText());
                    int numbidoes = Integer.parseInt(txtBidoes.getText());
                    String observacoes = txtObservacoes.getText();
                    String morada = txtMorada.getText();

                    RecolhaDTO novaRecolha = new RecolhaDTO();
                    novaRecolha.setData(data);
                    novaRecolha.setQuantidade((int) quantidade);
                    novaRecolha.setNumbidoes(numbidoes);
                    novaRecolha.setObservacoes(observacoes);
                    novaRecolha.setIdcontrato(contratoSelecionado.getIdcontrato().intValue());
                    novaRecolha.setIdutilizador(idutilizador);
                    novaRecolha.setIdestadorecolha(1);
                    novaRecolha.setMorada(morada);

                    System.out.println("📤 JSON enviado: " + mapper.writeValueAsString(novaRecolha));

                    recolhasClient.adicionarRecolha(novaRecolha,
                            resposta -> {
                                System.out.println("✅ Recolha registada com sucesso!");
                                Platform.runLater(() -> {
                                    carregarRecolhas();
                                    limparCampos();
                                });
                            },
                            erro -> System.err.println("❌ Erro ao registar recolha: " + erro)
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("❌ Erro ao processar dados do formulário.");
                }
            }
        });
    }

    private void limparCampos() {
        comboContrato.getSelectionModel().clearSelection();
        txtQuantidade.clear();
        txtBidoes.clear();
        txtObservacoes.clear();
        dateRecolha.setValue(null);
        txtMorada.clear();
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

    private void abrirDialogRecolha(RecolhaDTO recolha) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/ver-recolha-dialog.fxml"));

            Parent root = loader.load();

            VerRecolhaController controller = loader.getController();
            controller.setRecolha(recolha);
            controller.setOnSaveCallback(this::carregarRecolhas);

            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(btnBack.getScene().getWindow());
            dialog.setTitle("Detalhes do Recolha");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}