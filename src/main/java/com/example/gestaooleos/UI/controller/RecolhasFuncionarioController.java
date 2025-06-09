package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.RecolhasClient;
import com.example.gestaooleos.UI.api.RecolhaDTO;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.List;

public class RecolhasFuncionarioController {

    @FXML private TableView<RecolhaDTO> tabelaRecolhas;
    @FXML private TableColumn<RecolhaDTO, String> colMorada;
    @FXML private TableColumn<RecolhaDTO, String> colData;
    @FXML private TableColumn<RecolhaDTO, Double> colQuantidade;
    @FXML private TableColumn<RecolhaDTO, Void> colAcoes;

    private final RecolhasClient recolhasClient = new RecolhasClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @FXML
    public void initialize() {
        configurarTabela();
        carregarRecolhasDoFuncionario();
    }

    private void configurarTabela() {
        colMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnConcluir = new Button("Aceitar");
            private final Button btnProblema = new Button("Cancelar");

            {
                btnConcluir.getStyleClass().add("botao-aceitar");
                btnProblema.getStyleClass().add("botao-cancelar");

                btnConcluir.setOnAction(e -> {
                    RecolhaDTO r = getTableView().getItems().get(getIndex());
                    System.out.println("Concluir recolha: " + r.getIdrecolha());
                    atualizarEstado(r.getIdrecolha(), 3); // 3 = Concluído
                });

                btnProblema.setOnAction(e -> {
                    RecolhaDTO r = getTableView().getItems().get(getIndex());

                    Stage dialogStage = new Stage();
                    dialogStage.initStyle(StageStyle.UNDECORATED); // sem barra superior
                    dialogStage.initOwner(getTableView().getScene().getWindow());
                    dialogStage.setAlwaysOnTop(true);

                    VBox root = new VBox(10);
                    root.setStyle("-fx-background-color: #fff8dc; -fx-padding: 20; -fx-border-color: black; -fx-border-width: 2;-fx-border-radius: 10; -fx-background-radius: 10;");
                    root.setPrefWidth(400);

                    Label titulo = new Label("Cancelar Recolha");
                    titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");

                    Label lblMotivo = new Label("Escreve o motivo:");
                    TextField txtMotivo = new TextField();
                    txtMotivo.setPromptText("Motivo...");
                    txtMotivo.setStyle("-fx-pref-width: 300px; -fx-padding: 8;");

                    Button btnOk = new Button("OK");
                    btnOk.setStyle("-fx-background-color: #f4d35e; -fx-font-weight: bold;");
                    btnOk.setOnAction(ev -> {
                        String motivo = txtMotivo.getText().trim();
                        if (!motivo.isEmpty()) {
                            System.out.println("⚠ Problema na recolha: " + r.getIdrecolha() + " | Motivo: " + motivo);
                            recolhasClient.atualizarObservacoes(r.getIdrecolha(), motivo,
                                    sucesso -> atualizarEstado(r.getIdrecolha(), 4),
                                    erro -> Platform.runLater(() -> mostrarPopup("Erro ao guardar observações: " + erro, false)));
                            dialogStage.close();
                        }
                    });

                    Button btnCancel = new Button("Cancelar");
                    btnCancel.setStyle("-fx-background-color: #f08080; -fx-text-fill: white; -fx-font-weight: bold;");
                    btnCancel.setOnAction(ev -> dialogStage.close());

                    HBox botoes = new HBox(10, btnOk, btnCancel);
                    botoes.setStyle("-fx-alignment: center;");

                    root.getChildren().addAll(titulo, lblMotivo, txtMotivo, botoes);

                    Scene scene = new Scene(root);
                    dialogStage.setScene(scene);
                    dialogStage.show();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnConcluir, btnProblema);
                    setGraphic(box);
                }
            }
        });
    }


    private void carregarRecolhasDoFuncionario() {
        int idFuncionario = SessaoUtilizador.getIdUtilizador();

        recolhasClient.buscarRecolhasPorFuncionario(idFuncionario, lista -> {
            List<RecolhaDTO> notificadas = lista.stream()
                    .filter(r -> r.getIdestadorecolha() == 5)
                    .toList();

            Platform.runLater(() -> {
                tabelaRecolhas.getItems().clear();
                tabelaRecolhas.getItems().addAll(notificadas);
            });
        }, erro -> Platform.runLater(() -> mostrarPopup("Erro ao carregar recolhas atribuídas: " + erro, false)));
    }

    private void atualizarEstado(Long idRecolha, int novoEstado) {
        recolhasClient.atualizarEstadoRecolha(idRecolha, novoEstado,
                sucesso -> Platform.runLater(() -> {
                    mostrarPopup("Estado atualizado com sucesso.", true);
                    carregarRecolhasDoFuncionario();
                }),
                erro -> Platform.runLater(() -> mostrarPopup("Erro ao atualizar: " + erro, false)));
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
}
