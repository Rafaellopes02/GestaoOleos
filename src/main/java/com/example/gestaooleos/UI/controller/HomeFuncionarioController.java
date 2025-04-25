package com.example.gestaooleos.UI.controller;
import com.example.gestaooleos.UI.utils.SessaoUtilizador;
import com.example.gestaooleos.UI.api.ContadorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color; // ✅ CORRETO
import java.awt.*;
import java.net.URI;
import javafx.fxml.FXML;
import com.example.gestaooleos.UI.api.PagamentosClient;
import com.example.gestaooleos.UI.api.TotalRecebidoPorDiaDTO;
import javafx.scene.control.Tooltip;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;



public class HomeFuncionarioController {

    @FXML private Label lblAtivos;
    @FXML private Label lblBemVindo;
    @FXML private Label lblPendentes;
    @FXML private LineChart<String, Number> graficoTotaisPorDia;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;




    @FXML
    public void initialize() {
        carregarTotais();
        carregarGraficoTotaisPorDia();
        carregarContadores();
        String nome = SessaoUtilizador.getNomeUtilizador();
        if (nome != null && !nome.isEmpty()) {
            lblBemVindo.setText("Bem-vindo, " + nome + "!");
        } else {
            lblBemVindo.setText("Bem-vindo!");
        }
    }


    public void carregarContadores() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Contratos/contar-estados"))
                .GET()
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        ContadorDTO contadores = mapper.readValue(json, ContadorDTO.class);

                        Platform.runLater(() -> {
                            lblAtivos.setText(String.valueOf(contadores.getAtivos()));
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }
    public void carregarTotais() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/pagamentos/totais"))
                .GET()
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        var node = mapper.readTree(json);
                        double pendente = node.get("totalPendente").asDouble();

                        Platform.runLater(() -> {
                            lblPendentes.setText(String.format("%.2f€", pendente));
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> lblPendentes.setText("Erro"));
                    }
                });
    }

    private void carregarGraficoTotaisPorDia() {
        PagamentosClient client = new PagamentosClient();

        xAxis.setTickLabelFill(Color.GRAY);
        xAxis.setTickLabelGap(10);
        yAxis.setTickLabelFill(Color.GRAY);
        yAxis.setTickUnit(100);
        yAxis.setAutoRanging(true);
        yAxis.setMinorTickVisible(false);

        client.buscarTotaisPorDia(totais -> {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Recebido");

            for (TotalRecebidoPorDiaDTO item : totais) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(item.getData(), item.getTotal());
                serie.getData().add(data);
            }

            Platform.runLater(() -> {
                graficoTotaisPorDia.getData().clear();
                graficoTotaisPorDia.getData().add(serie);

                // Adiciona tooltip e estilo após os nodes estarem visíveis
                for (XYChart.Data<String, Number> data : serie.getData()) {
                    Node node = data.getNode();
                    if (node != null) {
                        // Estilo do ponto
                        node.getStyleClass().add("grafico-ponto");

                        // Tooltip personalizada
                        Tooltip tooltip = new Tooltip("Recebido: " + data.getYValue() + "€");
                        tooltip.setShowDelay(Duration.millis(100));
                        tooltip.getStyleClass().add("grafico-tooltip");
                        Tooltip.install(node, tooltip);
                    }
                }
            });


        }, erro -> erro.printStackTrace());
    }



}
