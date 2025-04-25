package com.example.gestaooleos.UI.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class EstadosRecolhaClient {

    private final HttpClient client = HttpClient.newHttpClient(); // <- Estava em falta

    public void buscarEstadoPorId(int id, Consumer<String> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/EstadosRecolhas/" + id))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(onSuccess)
                .exceptionally(ex -> {
                    onError.accept(ex.toString()); // <- substituí getMessage() por toString()
                    return null;
                });
    }
}
