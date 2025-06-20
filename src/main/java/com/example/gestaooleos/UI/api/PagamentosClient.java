package com.example.gestaooleos.UI.api;

import com.example.gestaooleos.UI.api.PagamentoDTO;
import com.example.gestaooleos.UI.api.TotalRecebidoPorDiaDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class PagamentosClient {

    private final String BASE_URL = "http://localhost:8080/api/pagamentos";
    private final ObjectMapper mapper = new ObjectMapper();

    public void buscarTotais(Consumer<Map<String, Object>> onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/totais");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Scanner scanner = new Scanner(conn.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                @SuppressWarnings("unchecked")
                Map<String, Object> totais = mapper.readValue(response, Map.class);
                onSuccess.accept(totais);
            } catch (Exception e) {
                onError.accept(e);
            }
        }).start();
    }

    public void buscarPagamentosCompletos(Consumer<PagamentoDTO[]> onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/completos");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Scanner scanner = new Scanner(conn.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                PagamentoDTO[] pagamentos = mapper.readValue(response, PagamentoDTO[].class);
                onSuccess.accept(pagamentos);
            } catch (IOException e) {
                onError.accept(e);
            }
        }).start();
    }

    public void buscarTotaisPorDia(Consumer<TotalRecebidoPorDiaDTO[]> onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/totais-dia");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Scanner scanner = new Scanner(conn.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                TotalRecebidoPorDiaDTO[] totais = mapper.readValue(response, TotalRecebidoPorDiaDTO[].class);
                onSuccess.accept(totais);
            } catch (Exception e) {
                onError.accept(e);
            }
        }).start();
    }
    public void atualizarEstadoParaConcluido(Long idPagamento, Long idMetodo, Runnable onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                URL url = new URL("http://localhost:8080/api/pagamentos/" + idPagamento + "/concluir");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                String json = "{\"idmetodopagamento\":" + idMetodo + "}";

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(json.getBytes(StandardCharsets.UTF_8));
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == 200 || responseCode == 204) {
                    onSuccess.run();
                } else {
                    onError.accept(new RuntimeException("Erro HTTP: " + responseCode));
                }
            } catch (Exception e) {
                onError.accept(e);
            }
        }).start();
    }
}
