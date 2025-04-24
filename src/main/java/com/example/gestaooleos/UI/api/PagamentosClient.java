package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class PagamentosClient {

    private final String BASE_URL = "http://localhost:8080/api/pagamentos";
    private final ObjectMapper mapper = new ObjectMapper();

    public void buscarTotais(Consumer<java.util.Map<String, Object>> onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/totais");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Scanner scanner = new Scanner(conn.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> totais = mapper.readValue(response, java.util.Map.class);
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
}
