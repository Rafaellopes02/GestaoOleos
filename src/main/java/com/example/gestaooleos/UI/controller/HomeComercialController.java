package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.api.ContadorDTO;
import com.example.gestaooleos.UI.api.PedidoContratoDTO;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HomeComercialController {

    @FXML private Label lblAtivos;
    @FXML private Label lblBemVindo;
    @FXML private Label lblPendentes;
    @FXML private Label lblRecolhasHoje;

    @FXML private TableView<PedidoContratoDTO> tabelaPedidos;
    @FXML private TableColumn<PedidoContratoDTO, String> colNomeContrato;
    @FXML private TableColumn<PedidoContratoDTO, String> colDataInicio;
    @FXML private TableColumn<PedidoContratoDTO, String> colDataFim;
    @FXML private TableColumn<PedidoContratoDTO, String> colUtilizador;
    @FXML private TableColumn<PedidoContratoDTO, Void> colAcoes;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        carregarTotais();
        carregarContadores();
        carregarPedidosPendentes();
        carregarPedidosTabela();

        String nome = SessaoUtilizador.getNomeUtilizador();
        lblBemVindo.setText((nome != null && !nome.isEmpty()) ? "Bem-vindo, " + nome + "!" : "Bem-vindo!");
    }

    public void carregarContadores() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Contratos/contar-estados"))
                .GET().build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        ContadorDTO contadores = mapper.readValue(json, ContadorDTO.class);
                        Platform.runLater(() -> lblAtivos.setText(String.valueOf(contadores.getAtivos())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void carregarTotais() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/pagamentos/totais"))
                .GET().build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        var node = mapper.readTree(json);
                        double pendente = node.get("totalPendente").asDouble();
                        Platform.runLater(() -> lblPendentes.setText(String.format("%.2f€", pendente)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> lblPendentes.setText("Erro"));
                    }
                });
    }

    public void carregarPedidosPendentes() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/PedidosContrato/pendentes/count"))
                .GET().build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(resposta -> {
                    try {
                        int pendentes = Integer.parseInt(resposta.trim());
                        Platform.runLater(() -> lblRecolhasHoje.setText(String.valueOf(pendentes)));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Platform.runLater(() -> lblRecolhasHoje.setText("Erro"));
                    }
                });
    }

    public void carregarPedidosTabela() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/PedidosContrato/detalhes"))
                .GET()
                .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        List<PedidoContratoDTO> lista = mapper.readValue(json,
                                mapper.getTypeFactory().constructCollectionType(List.class, PedidoContratoDTO.class));

                        // Corrigido: filtra pelos pedidos com estado 1 (pendente)
                        List<PedidoContratoDTO> pendentes = lista.stream()
                                .filter(p -> p.getIdestadopedido() == 1)
                                .toList();

                        Platform.runLater(() -> {
                            tabelaPedidos.getItems().setAll(pendentes);

                            colNomeContrato.setCellValueFactory(new PropertyValueFactory<>("nomeContrato"));
                            colDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
                            colDataFim.setCellValueFactory(new PropertyValueFactory<>("dataFim"));
                            colUtilizador.setCellValueFactory(new PropertyValueFactory<>("nomeUtilizador"));

                            colAcoes.setCellFactory(col -> new TableCell<>() {
                                private final Button btnAceitar = new Button("Aceitar");
                                private final Button btnRejeitar = new Button("Rejeitar");
                                private final HBox box;

                                {
                                    btnAceitar.setOnAction(e -> {
                                        PedidoContratoDTO item = getTableView().getItems().get(getIndex());
                                        atualizarEstadoPedido(item.getIdpedidocontrato(), 3, 1); // Pedido -> Aceite (3), Contrato -> Ativo (1)
                                    });

                                    btnRejeitar.setOnAction(e -> {
                                        PedidoContratoDTO item = getTableView().getItems().get(getIndex());
                                        atualizarEstadoPedido(item.getIdpedidocontrato(), 4, 5); // Pedido -> Rejeitado (4), Contrato -> Rejeitado (5)
                                    });

                                    box = new HBox(5, btnAceitar, btnRejeitar);
                                }

                                @Override
                                protected void updateItem(Void item, boolean empty) {
                                    super.updateItem(item, empty);
                                    setGraphic(empty ? null : box);
                                }
                            });
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    public void atualizarEstadoPedido(Long id, int novoEstado) {
        String body = "{\"idestadopedido\": " + novoEstado + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/PedidosContrato/" + id))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body)) // certifica-te que o PATCH é suportado
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenRun(this::carregarPedidosTabela); // refrescar após atualizar
    }

    public void atualizarEstadoPedido(Long id, int novoEstadoPedido, int novoEstadoContrato) {
        String body = String.format("{\"idestadopedido\": %d, \"idestadocontrato\": %d}", novoEstadoPedido, novoEstadoContrato);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/PedidosContrato/" + id))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenRun(this::carregarPedidosTabela);
    }

}
