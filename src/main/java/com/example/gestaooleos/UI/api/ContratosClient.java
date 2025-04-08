package com.example.gestaooleos.UI.api;

import java.net.URI;
import java.net.http.*;
import java.util.function.Consumer;

public class ContratosClient {

    private static final String API_URL = "http://localhost:8080/Contratos";
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
}
