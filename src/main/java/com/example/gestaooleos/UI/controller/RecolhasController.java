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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecolhasController {

    @FXML private ComboBox<ContratoDTO> comboContrato;
    @FXML private TextField txtQuantidade;
    @FXML private DatePicker dateRecolha;
    @FXML private TextField txtMorada;
    @FXML private TextField txtBidoes;
    @FXML private TextArea txtObservacoes;
    @FXML private Button btnSolicitar;
    @FXML private Button btnBack;
    @FXML private TableView<RecolhaViewModel> tabelaRecolhas;
    @FXML private TableColumn<RecolhaViewModel, String> nomeContratoRecolha;
    @FXML private TableColumn<RecolhaViewModel, String> MoradaRecolha;
    @FXML private TableColumn<RecolhaViewModel, String> DataRecolha;
    @FXML private TableColumn<RecolhaViewModel, String> estadoRecolha;
    @FXML private TableColumn<RecolhaViewModel, Void> verRecolha;

    private final RecolhasClient recolhasClient = new RecolhasClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final ContratosClient contratosClient = new ContratosClient();
    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();
    private final EstadosRecolhaClient estadosRecolhaClient = new EstadosRecolhaClient();

    private List<Long> idsRecolhas;

    @FXML
    public void initialize() {
        nomeContratoRecolha.setCellValueFactory(new PropertyValueFactory<>("nome"));
        MoradaRecolha.setCellValueFactory(new PropertyValueFactory<>("morada"));
        DataRecolha.setCellValueFactory(new PropertyValueFactory<>("data"));
        estadoRecolha.setCellValueFactory(new PropertyValueFactory<>("estado"));
        btnSolicitar.setOnAction(event -> solicitarRecolha());
        carregarRecolhas();
        carregarContratos();
        adicionarColunaVer();
    }

    private void adicionarColunaVer() {
        TableColumn<RecolhaViewModel, Void> colBtn = new TableColumn<>("Ver");

        colBtn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button();

            {
                btn.setStyle("-fx-background-color: transparent;");
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/image/ver.png")));
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                btn.setGraphic(imageView);
                btn.setOnAction(event -> {
                    int index = getIndex();
                    if (index >= 0 && index < tabelaRecolhas.getItems().size()) {
                        RecolhaViewModel data = tabelaRecolhas.getItems().get(index);
                        Long idRecolha = idsRecolhas.get(index);
                        abrirDialogEditarRecolha(data, idRecolha);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        tabelaRecolhas.getColumns().add(colBtn);
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
                        comboEstado.getItems().add(estado.getNome());
                    }
                    comboEstado.setValue(recolha.getEstado());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> System.err.println("Erro ao carregar estados: " + erro));

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: #f4d35e; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");

        Button btnFechar = new Button("Fechar");
        btnFechar.setStyle("-fx-background-color: #f4d35e; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");

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
                                sucesso -> {
                                    System.out.println("✅ Estado atualizado com sucesso!");
                                    Platform.runLater(() -> {
                                        carregarRecolhas();
                                        dialogStage.close();
                                    });
                                },
                                erro -> System.err.println("❌ Erro ao atualizar estado: " + erro));
                    } else {
                        System.err.println("❌ Estado selecionado não encontrado!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, erro -> System.err.println("Erro ao buscar estados para update: " + erro));
        });

        HBox botoes = new HBox(10, btnGuardar, btnFechar);
        botoes.setStyle("-fx-alignment: center; -fx-padding: 10 0 0 0;");

        conteudo.getChildren().addAll(lblTitulo, lblNome, lblMorada, lblData, new Label("Estado:"), comboEstado, botoes);

        Scene scene = new Scene(conteudo);
        dialogStage.setScene(scene);
        dialogStage.initOwner(tabelaRecolhas.getScene().getWindow());
        dialogStage.setResizable(false);
        dialogStage.show();
    }


    public void carregarRecolhas() {
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
                            List<RecolhaViewModel> modelos = futuros.stream()
                                    .map(CompletableFuture::join)
                                    .toList();

                            Platform.runLater(() -> {
                                tabelaRecolhas.setItems(FXCollections.observableArrayList(modelos));
                                idsRecolhas = lista.stream()
                                        .map(RecolhaDTO::getIdrecolha)
                                        .toList();
                            });
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
}