package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.UtilizadorDTO;
import com.example.gestaooleos.UI.api.UtilizadoresClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModalAdicionarContratoController {

    @FXML private TextField txtNome;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private ComboBox<UtilizadorDTO> cbCliente;
    @FXML private TextField txtValor;

    private final ObjectMapper mapper = new ObjectMapper();
    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();
    private ContratosController contratosController;

    @FXML
    public void initialize() {
        configurarComboBoxCliente();
        configurarDatePicker(dpInicio);
        configurarDatePicker(dpFim);
        carregarClientes();
    }

    private void configurarDatePicker(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(string, formatter);
            }
        });
    }

    private void configurarComboBoxCliente() {
        cbCliente.setConverter(new StringConverter<>() {
            @Override
            public String toString(UtilizadorDTO cliente) {
                return cliente != null ? cliente.getNome() : "";
            }

            @Override
            public UtilizadorDTO fromString(String string) {
                return null;
            }
        });
    }

    private void carregarClientes() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Utilizadores/clientes"))
                .GET()
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        List<UtilizadorDTO> clientes = mapper.readValue(response, new TypeReference<List<UtilizadorDTO>>() {});
                        Platform.runLater(() -> cbCliente.getItems().addAll(clientes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    @FXML
    private void guardarContrato() {
        String nome = txtNome.getText().trim();
        LocalDate dataInicio = dpInicio.getValue();
        LocalDate dataFim = dpFim.getValue();
        UtilizadorDTO clienteSelecionado = cbCliente.getValue();
        String valorTexto = txtValor.getText().trim();

        limparEstilos();

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
        if (clienteSelecionado == null) {
            cbCliente.getStyleClass().add("campo-obrigatorio");
            valido = false;
        }
        if (valorTexto.isEmpty()) {
            txtValor.getStyleClass().add("campo-obrigatorio");
            valido = false;
        }

        if (dataInicio == null) {
            mostrarToast("Por favor, selecione uma data de início.", false);
            dpInicio.getStyleClass().add("campo-obrigatorio");
            return;
        }

        if (dataFim == null) {
            mostrarToast("Por favor, selecione uma data de fim.", false);
            dpFim.getStyleClass().add("campo-obrigatorio");
            return;
        }

        if (!valido) {
            mostrarToast("Preencha todos os campos obrigatórios.", false);
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorTexto);
        } catch (NumberFormatException e) {
            txtValor.getStyleClass().add("campo-obrigatorio");
            mostrarToast("Valor inválido. Introduza um número!", false);
            return;
        }

        // ⚡️ Aqui, garantimos que enviamos a data no formato correto "yyyy-MM-dd"
        Map<String, Object> contrato = new HashMap<>();
        contrato.put("nome", nome);
        contrato.put("dataInicio", dataInicio.toString()); // LocalDate.toString() já dá "yyyy-MM-dd"
        contrato.put("dataFim", dataFim.toString());
        contrato.put("idutilizador", clienteSelecionado.getIdutilizador());
        contrato.put("valor", valor);
        contrato.put("idEstadoContrato", 2);

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

                                new Thread(() -> {
                                    try {
                                        Thread.sleep(2200);
                                    } catch (InterruptedException ignored) {}
                                    Platform.runLater(this::fecharModal);
                                }).start();
                            });
                        } else {
                            Platform.runLater(() -> {
                                String errorBody = response.body();
                                mostrarToast("Erro ao criar contrato: " + errorBody, false);
                            });
                        }
                    })
                    .exceptionally(ex -> {
                        Platform.runLater(() -> mostrarToast("Erro ao conectar: " + ex.getMessage(), false));
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            mostrarToast("Erro ao preparar envio do contrato.", false);
        }
    }

    private void limparEstilos() {
        txtNome.getStyleClass().remove("campo-obrigatorio");
        dpInicio.getStyleClass().remove("campo-obrigatorio");
        dpFim.getStyleClass().remove("campo-obrigatorio");
        cbCliente.getStyleClass().remove("campo-obrigatorio");
        txtValor.getStyleClass().remove("campo-obrigatorio");
    }

    @FXML
    private void fecharModal() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    public void setContratosController(ContratosController contratosController) {
        this.contratosController = contratosController;
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
        wrapper.setPadding(new Insets(30, 20, 0, 20));

        Scene scene = new Scene(wrapper);
        scene.setFill(null);

        Stage toastStage = new Stage();
        toastStage.initOwner(txtNome.getScene().getWindow());
        toastStage.initStyle(StageStyle.TRANSPARENT);
        toastStage.setAlwaysOnTop(true);
        toastStage.setResizable(false);
        toastStage.setScene(scene);

        Stage owner = (Stage) txtNome.getScene().getWindow();
        double centerX = owner.getX() + owner.getWidth() / 2 - 197;
        double topY = owner.getY() - 10;

        toastStage.setX(centerX);
        toastStage.setY(topY);

        toastStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
            Platform.runLater(toastStage::close);
        }).start();
    }
}
