package com.example.gestaooleos.UI.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.stage.Modality;
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

        // Validação visual
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
            mostrarAlerta("Campos obrigatórios", "Por favor preencha todos os campos.", Alert.AlertType.WARNING);
            return;
        }

        // Mapeia estado → ID
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
                                mostrarAlerta("✔ Sucesso", "Contrato criado com sucesso!", Alert.AlertType.INFORMATION);
                                if (contratosController != null) {
                                    contratosController.carregarContratos();
                                    contratosController.carregarContadores();
                                }
                                fecharModal();
                            });
                        } else {
                            Platform.runLater(() ->
                                    mostrarAlerta("Erro", "Falha ao criar contrato. Código: " + response.statusCode(), Alert.AlertType.ERROR));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao enviar os dados do contrato.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void fecharModal() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);

        // 👉 Garante que o alerta aparece sobre o modal
        Stage stage = (Stage) txtNome.getScene().getWindow();
        alerta.initOwner(stage); // Associa o alerta ao modal
        alerta.initModality(Modality.WINDOW_MODAL); // Modal acima da janela principal

        alerta.showAndWait();
    }


    public void setContratosController(ContratosController controller) {
        this.contratosController = controller;
    }
}
