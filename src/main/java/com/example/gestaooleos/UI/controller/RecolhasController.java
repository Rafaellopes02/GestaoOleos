package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.*;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import com.example.gestaooleos.UI.viewmodel.RecolhaViewModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
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
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    @FXML private Label lblRecolhasPedidas;

    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();
    private final RecolhasClient recolhasClient = new RecolhasClient();
    private final ContratosClient contratosClient = new ContratosClient();
    private final EstadosRecolhaClient estadosRecolhaClient = new EstadosRecolhaClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private List<Long> idsRecolhas;

    @FXML
    public void initialize() {
        nomeContratoRecolha.setCellValueFactory(new PropertyValueFactory<>("nome"));
        MoradaRecolha.setCellValueFactory(new PropertyValueFactory<>("morada"));
        DataRecolha.setCellValueFactory(new PropertyValueFactory<>("data"));
        estadoRecolha.setCellValueFactory(new PropertyValueFactory<>("estado"));

        carregarEmpregados();
        carregarRecolhasPendentes();
        carregarRecolhasEmAndamento();

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

    private void carregarRecolhas() {
        recolhasClient.buscarRecolhas(lista -> {
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
                                estadoFuture.join(),
                                dto.getObservacoes()
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
        }, erro -> System.err.println("Erro ao buscar recolhas: " + erro));
    }

    private void carregarEmpregados() {
        utilizadoresClient.buscarEmpregados(json -> {
            try {
                List<UtilizadorDTO> empregados = mapper.readValue(json, new TypeReference<>() {});
                Platform.runLater(() -> {
                    comboEmpregados.setItems(FXCollections.observableArrayList(empregados));
                    comboEmpregados.setConverter(new StringConverter<>() {
                        @Override
                        public String toString(UtilizadorDTO utilizador) {
                            return utilizador != null ? utilizador.getNome() : "";
                        }
                        @Override
                        public UtilizadorDTO fromString(String string) {
                            return comboEmpregados.getItems().stream()
                                    .filter(emp -> emp.getNome().equals(string))
                                    .findFirst()
                                    .orElse(null);
                        }
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> System.err.println("Erro ao carregar empregados: " + erro));
    }

    private void carregarRecolhasPendentes() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/recolhas/contar?estado=1"))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        var node = mapper.readTree(json);
                        int quantidade = node.get("quantidade").asInt();
                        Platform.runLater(() -> lblRecolhasPedidas.setText(String.valueOf(quantidade)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> lblRecolhasPedidas.setText("Erro"));
                    }
                });
    }

    private void carregarRecolhasEmAndamento() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/recolhas/em-andamento"))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        List<RecolhaDTO> recolhas = mapper.readValue(json, new TypeReference<>() {});
                        Platform.runLater(() -> {
                            // Este método deve ser ajustado se quiseres renderizar os ViewModels com nome e estado formatado
                            carregarRecolhas();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @FXML
    private void notificarSelecionado() {
        int selectedIndex = tabelaRecolhas.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            mostrarPopup("Seleciona uma recolha!", false);
            return;
        }

        UtilizadorDTO empregado = comboEmpregados.getSelectionModel().getSelectedItem();
        if (empregado == null) {
            mostrarPopup("Seleciona um empregado!", false);
            return;
        }

        Long idRecolha = idsRecolhas.get(selectedIndex);
        Long idEmpregado = empregado.getIdutilizador();

        notificarEmpregado(idRecolha, idEmpregado);
    }

    private void notificarEmpregado(Long idRecolha, Long idEmpregado) {
        String url = "http://localhost:8080/recolhas/" + idRecolha + "/notificar/5/" + idEmpregado;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        Platform.runLater(() -> {
                            mostrarPopup("Empregado notificado com sucesso!", true);
                            carregarRecolhasEmAndamento();
                        });
                    } else {
                        Platform.runLater(() -> mostrarPopup("Erro ao notificar! Código: " + response.statusCode(), false));
                    }
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> mostrarPopup("Erro: " + ex.getMessage(), false));
                    return null;
                });
    }

    private void mostrarPopup(String mensagem, boolean sucesso) {
        Label label = new Label(mensagem);
        label.setStyle("-fx-background-color: " + (sucesso ? "#90ee90" : "#f08080") + ";"
                + "-fx-text-fill: black;"
                + "-fx-padding: 10px 20px;"
                + "-fx-background-radius: 10px;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-border-color: transparent;");

        Stage popupStage = new Stage();
        popupStage.initOwner(tabelaRecolhas.getScene().getWindow());
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.setAlwaysOnTop(true);

        VBox root = new VBox(label);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(null);
        popupStage.setScene(scene);
        popupStage.show();

        Stage owner = (Stage) tabelaRecolhas.getScene().getWindow();
        popupStage.setX(owner.getX() + owner.getWidth() - 500);
        popupStage.setY(owner.getY() + owner.getHeight() - 100);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> popupStage.close());

        new SequentialTransition(fadeIn, pause, fadeOut).play();
    }

    @FXML
    private void voltarHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/home-escritorio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Página Inicial");
            FullscreenHelper.ativarFullscreen(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirDialogEditarRecolha(RecolhaViewModel recolha, Long idRecolha) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Editar Estado da Recolha");

        VBox conteudo = new VBox(10);
        conteudo.setStyle("-fx-background-color: #FFF8DC; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label lblTitulo = new Label("Editar Estado da Recolha");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblNome = new Label("Contrato: " + recolha.getNome());
        Label lblMorada = new Label("Morada: " + recolha.getMorada());
        Label lblData = new Label("Data: " + recolha.getData());
        Label lblObservacoes = new Label("Observações: " + recolha.getObservacoes());


        ComboBox<String> comboEstado = new ComboBox<>();
        comboEstado.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        estadosRecolhaClient.buscarEstados(json -> {
            try {
                List<EstadoRecolhaDTO> estados = mapper.readValue(json, new TypeReference<>() {});
                Platform.runLater(() -> {
                    comboEstado.getItems().clear();
                    for (EstadoRecolhaDTO estado : estados) {
                        String nome = estado.getNome();
                        if (nome.equalsIgnoreCase("pendente") || nome.equalsIgnoreCase("em andamento") || nome.equalsIgnoreCase("notificado")) {
                            comboEstado.getItems().add(nome);
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
                            .filter(est -> est.getNome().equalsIgnoreCase(novoEstadoNome))
                            .findFirst()
                            .orElse(null);

                    if (estadoSelecionado != null) {
                        recolhasClient.atualizarEstadoRecolha(idRecolha, estadoSelecionado.getId(),
                                sucesso -> Platform.runLater(() -> {
                                    carregarRecolhas(); // recarrega tabela
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

        conteudo.getChildren().addAll(lblTitulo, lblNome, lblMorada,lblObservacoes, lblData, new Label("Estado:"), comboEstado, botoes);
        Scene scene = new Scene(conteudo);
        dialogStage.setScene(scene);
        dialogStage.initOwner(tabelaRecolhas.getScene().getWindow());
        dialogStage.setResizable(false);
        dialogStage.show();
    }

}
