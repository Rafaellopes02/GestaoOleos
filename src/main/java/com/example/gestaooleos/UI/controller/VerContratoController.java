package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class VerContratoController {

    @FXML private Label labelId;
    @FXML private Label labelNome;
    @FXML private Label labelDataInicio;
    @FXML private Label labelDataFim;
    @FXML private Label labelNomeUtilizador;
    @FXML private ComboBox<String> comboEstado;


    private final EstadosContratosClient estadosClient = new EstadosContratosClient();
    private final UtilizadoresClient utilizadoresClient = new UtilizadoresClient();

    private ContratoDTO contrato;
    private Runnable onSaveCallback;

    public void setContrato(ContratoDTO contrato) {
        this.contrato = contrato;

        labelId.setText(String.valueOf(contrato.getIdcontrato()));
        labelNome.setText(contrato.getNome());
        labelDataInicio.setText(contrato.getDataInicio());
        labelDataFim.setText(contrato.getDataFim());

        carregarEstados();

        // Buscar nome do utilizador
        utilizadoresClient.buscarUtilizadorPorId(
                contrato.getIdutilizador(),
                utilizador -> Platform.runLater(() -> labelNomeUtilizador.setText(contrato.getNome())),
                erro -> Platform.runLater(() -> labelNomeUtilizador.setText("Erro ao carregar"))
        );
    }

    private void carregarEstados() {
        estadosClient.buscarEstados(json -> {
            try {
                List<EstadoContratoDTO> estados = EstadosContratosClient.parseEstados(json);
                Platform.runLater(() -> {
                    comboEstado.getItems().clear();

                    for (EstadoContratoDTO e : estados) {
                        comboEstado.getItems().add(e.getNome());
                        if (e.getIdestado() == contrato.getIdEstadoContrato()) {
                            comboEstado.setValue(e.getNome()); // ← isto faz a seleção automática
                        }
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> Platform.runLater(() -> {
            new Alert(Alert.AlertType.ERROR, "Erro ao carregar estados").showAndWait();
        }));
    }


    @FXML
    private void guardar() {
        String estadoSelecionado = comboEstado.getValue();

        estadosClient.buscarEstados(json -> {
            try {
                List<EstadoContratoDTO> estados = EstadosContratosClient.parseEstados(json);

                for (EstadoContratoDTO e : estados) {
                    if (e.getNome().equals(estadoSelecionado)) {
                        contrato.setIdEstadoContrato(e.getIdestado());
                        contrato.setDataInicio(labelDataInicio.getText());
                        contrato.setDataFim(labelDataFim.getText());
                        break;
                    }
                }

                // Atualizar na base de dados
                new ContratosClient().atualizarContrato(
                        contrato.getIdcontrato(),
                        contrato,
                        () -> Platform.runLater(() -> {
                            if (onSaveCallback != null) onSaveCallback.run();
                            fechar();
                        }),
                        erro -> Platform.runLater(() ->
                                new Alert(Alert.AlertType.ERROR, "Erro ao guardar: " + erro.getMessage()).showAndWait()
                        )
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, erro -> erro.printStackTrace());
    }


    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) comboEstado.getScene().getWindow();
        stage.close();
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}
