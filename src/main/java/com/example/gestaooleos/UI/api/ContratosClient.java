package com.example.gestaooleos.UI.api;

import java.net.URI;
import java.net.http.*;
import java.util.function.Consumer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ContratosClient {

    private static final String API_URL = "http://localhost:8080/Contratos/com-estado";
    private final HttpClient client = HttpClient.newHttpClient();

    public void buscarContratos(Consumer<String> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(onSuccess)
                .exceptionally(ex -> {
                    onError.accept(ex.getMessage());
                    return null;
                });
    }

    public void adicionarContrato(ContratoCriacaoDTO contrato, Consumer<String> onSuccess, Consumer<String> onError) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(contrato);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/Contratos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(onSuccess)
                    .exceptionally(ex -> {
                        onError.accept(ex.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            onError.accept(e.getMessage());
        }
    }
}
