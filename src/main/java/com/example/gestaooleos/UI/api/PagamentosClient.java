package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

public class PagamentosClient {

    private final String BASE_URL = "http://localhost:8080/api/pagamentos";

    public void buscarTotais(Consumer<Map<String, Object>> onSuccess, Consumer<String> onError) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/totais");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int status = conn.getResponseCode();
                if (status == 200) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> dados = mapper.readValue(
                            new InputStreamReader(conn.getInputStream()),
                            new TypeReference<>() {}
                    );
                    onSuccess.accept(dados);
                } else {
                    onError.accept("Erro HTTP: " + status);
                }

                conn.disconnect();
            } catch (Exception e) {
                onError.accept("Erro: " + e.getMessage());
            }
        }).start();
    }
}
