package com.example.gestaooleos.UI.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.stage.StageStyle;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ModalAdicionarContratoController {

    @FXML private TextField txtNome;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private ComboBox<String> cbEstado;

    private final ObjectMapper mapper = new ObjectMapper();
    private ContratosController contratosController;

    @FXML
    public void initialize() {
        cbEstado.getItems().addAll("Ativo", "Suspenso", "Concluído");
    }

    @FXML
    private void guardarContrato() {
        String nome = txtNome.getText().trim();
        LocalDate dataInicio = dpInicio.getValue();
        LocalDate dataFim = dpFim.getValue();
        String estadoSelecionado = cbEstado.getValue();

        // Limpa estilos antigos
        txtNome.getStyleClass().remove("campo-obrigatorio");
        dpInicio.getStyleClass().remove("campo-obrigatorio");
        dpFim.getStyleClass().remove("campo-obrigatorio");
        cbEstado.getStyleClass().remove("campo-obrigatorio");

        boolean valido = true;

        if (nome.isEmpty()) {
            txtNome.getStyleClass().add("campo-obrigatorio");
            valido = false;
        }
        if (dataInicio == null) {
            dpInicio.getStyleClass().add("campo-obrigatorio");
            valido = false;
        }
        if (dataFim == null) {
            dpFim.getStyleClass().add("campo-obrigatorio");
            valido = false;
        }
        if (estadoSelecionado == null) {
            cbEstado.getStyleClass().add("campo-obrigatorio");
            valido = false;
        }

        if (!valido) {
            mostrarToast("Preencha todos os campos obrigatórios.", false);
            return;
        }

        int idEstadoContrato = switch (estadoSelecionado) {
            case "Ativo" -> 1;
            case "Suspenso" -> 2;
            case "Concluído" -> 3;
            default -> 1;
        };

        Map<String, Object> contrato = new HashMap<>();
        contrato.put("nome", nome);
        contrato.put("datainicio", dataInicio.toString());
        contrato.put("datafim", dataFim.toString());
        contrato.put("idestadocontrato", idEstadoContrato);
        contrato.put("idutilizador", 1); // ⚠️ TEMPORÁRIO

        try {
            String json = mapper.writeValueAsString(contrato);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/Contratos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200 || response.statusCode() == 201) {
                            Platform.runLater(() -> {
                                mostrarToast("Contrato criado com sucesso!", true);

                                if (contratosController != null) {
                                    contratosController.carregarContratos();
                                    contratosController.carregarContadores();
                                }

                                // Espera o toast desaparecer antes de fechar o modal
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(2200); // ⏳ 2.2 segundos
                                    } catch (InterruptedException ignored) {}
                                    Platform.runLater(this::fecharModal);
                                }).start();
                            });
                        } else {
                            Platform.runLater(() ->
                                    mostrarToast("Erro ao criar contrato. Código: " + response.statusCode(), false));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            mostrarToast("Erro ao enviar os dados do contrato.", false);
        }
    }


    @FXML
    private void fecharModal() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    public void setContratosController(ContratosController controller) {
        this.contratosController = controller;
    }

    private void mostrarToast(String mensagem, boolean sucesso) {
        Label lbl = new Label(mensagem);
        lbl.setStyle(
                "-fx-background-color: " + (sucesso ? "#d4edda" : "#f8d7da") + ";" +
                        "-fx-text-fill: " + (sucesso ? "#155724" : "#721c24") + ";" +
                        "-fx-padding: 10px 18px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'JetBrains Mono';"
        );

        StackPane wrapper = new StackPane(lbl);
        wrapper.setStyle("-fx-background-color: transparent;");
        wrapper.setPadding(new Insets(30, 20, 0, 20)); // move para cima

        Scene scene = new Scene(wrapper);
        scene.setFill(null);

        Stage toastStage = new Stage();
        toastStage.initOwner(txtNome.getScene().getWindow());
        toastStage.initStyle(StageStyle.TRANSPARENT);
        toastStage.setAlwaysOnTop(true);
        toastStage.setResizable(false);
        toastStage.setScene(scene);

        // posicionar no topo
        Stage owner = (Stage) txtNome.getScene().getWindow();
        double centerX = owner.getX() + owner.getWidth() / 2 - 197; // Ajusta o -150 consoante largura do toast
        double topY = owner.getY() - 10 ; // Sobe mais o toast

        toastStage.setX(centerX);
        toastStage.setY(topY);


        toastStage.show();

        // fechar automaticamente após 2.5s
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
            Platform.runLater(toastStage::close);
        }).start();
    }


}
