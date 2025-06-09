package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.ContratosClient;
import com.example.gestaooleos.UI.api.RecolhasClient;
import com.example.gestaooleos.UI.api.RecolhaDTO;
import com.example.gestaooleos.UI.utils.FullscreenHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConfirmarRecolhasController {

    @FXML private Button btnBack;
    @FXML private TableView<RecolhaDTO> tabelaConfirmar;
    @FXML private TableColumn<RecolhaDTO, String> nomeContratoRecolha;
    @FXML private TableColumn<RecolhaDTO, String> colMorada;
    @FXML private TableColumn<RecolhaDTO, String> colData;
    @FXML private TableColumn<RecolhaDTO, Integer> colQuantidade;
    @FXML private TableColumn<RecolhaDTO, Void> colAcoes;

    private final RecolhasClient recolhasClient = new RecolhasClient();
    private final ContratosClient contratosClient = new ContratosClient(); // <- Adicionado
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @FXML
    public void initialize() {
        System.out.println("🔄 Inicializando controller...");
        configurarTabela();
        carregarRecolhasEmAndamento();
    }

    private void configurarTabela() {
        nomeContratoRecolha.setCellValueFactory(new PropertyValueFactory<>("nomeContrato"));
        colMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnConfirmar = new Button("✅");
            private final Button btnCancelar = new Button("❌");
            private final HBox box = new HBox(10, btnConfirmar, btnCancelar);

            {
                btnConfirmar.setOnAction(e -> {
                    RecolhaDTO r = getTableView().getItems().get(getIndex());
                    System.out.println("🟢 Confirmar recolha ID: " + r.getIdrecolha());
                    atualizarEstadoRecolha(r.getIdrecolha(), 3); // 3 = Concluído
                });
                btnCancelar.setOnAction(e -> {
                    RecolhaDTO r = getTableView().getItems().get(getIndex());
                    System.out.println("🔴 Cancelar recolha ID: " + r.getIdrecolha());
                    atualizarEstadoRecolha(r.getIdrecolha(), 4); // 4 = Cancelado
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void carregarRecolhasEmAndamento() {
        System.out.println("🔁 Início do carregamento de recolhas...");

        recolhasClient.buscarRecolhas(lista -> {
            System.out.println("📦 Recolhas totais: " + lista.size());

            List<RecolhaDTO> filtradas = lista.stream()
                    .filter(r -> r.getIdestadorecolha() == 2) // Estado 2 = Em andamento
                    .toList();

            List<CompletableFuture<Void>> tarefas = filtradas.stream().map(dto -> {
                CompletableFuture<Void> f = new CompletableFuture<>();
                contratosClient.buscarContratoPorId((long) dto.getIdcontrato(),
                        resposta -> {
                            try {
                                ObjectMapper m = new ObjectMapper();
                                JsonNode json = m.readTree(resposta);
                                String nome = json.get("nome").asText();
                                dto.setNomeContrato(nome);
                                f.complete(null);
                            } catch (Exception e) {
                                dto.setNomeContrato("Erro ao ler nome");
                                f.complete(null);
                            }
                        },
                        erro -> {
                            dto.setNomeContrato("Erro ao carregar");
                            f.complete(null);
                        });
                return f;
            }).toList();

            CompletableFuture.allOf(tarefas.toArray(new CompletableFuture[0]))
                    .thenRun(() -> Platform.runLater(() -> {
                        tabelaConfirmar.getItems().setAll(filtradas);
                        System.out.println("📊 Tabela atualizada com " + filtradas.size() + " itens.");
                    }));

        }, erro -> Platform.runLater(() -> mostrarPopup("Erro ao buscar recolhas: " + erro, false)));
    }

    private void atualizarEstadoRecolha(Long idRecolha, int novoEstadoId) {
        recolhasClient.atualizarEstadoRecolha(idRecolha, novoEstadoId,
                sucesso -> Platform.runLater(() -> {
                    System.out.println("✅ Estado atualizado com sucesso para ID " + idRecolha);
                    mostrarPopup("Estado atualizado com sucesso!", true);
                    carregarRecolhasEmAndamento();
                }),
                erro -> Platform.runLater(() -> {
                    System.err.println("❌ Erro ao atualizar estado: " + erro);
                    mostrarPopup("Erro ao atualizar estado: " + erro, false);
                }));
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
        popupStage.initOwner(tabelaConfirmar.getScene().getWindow());
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.setAlwaysOnTop(true);

        VBox root = new VBox(label);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(null);
        popupStage.setScene(scene);
        popupStage.show();

        Stage owner = (Stage) tabelaConfirmar.getScene().getWindow();
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
}
