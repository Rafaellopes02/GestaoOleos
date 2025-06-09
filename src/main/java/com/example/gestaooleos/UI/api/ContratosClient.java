package com.example.gestaooleos.UI.api;

import java.net.URI;
import java.net.http.*;
import java.util.function.Consumer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ContratosClient {

    private static final String API_URL = "http://localhost:8080/Contratos/com-estado";
    private static final String API_URL1 = "http://localhost:8080/Contratos";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();



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
    public void buscarContratossemestado(Consumer<String> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL1))
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

    public void buscarContratoPorId(Long id, Consumer<String> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Contratos/" + id))
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

    public void buscarContratosDoCliente(Long idCliente, Consumer<String> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Contratos/com-estado/cliente/" + idCliente))
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

    public void atualizarContrato(Long id, ContratoDTO contrato, Runnable onSuccess, Consumer<Throwable> onError) {
        try {
            String json = mapper.writeValueAsString(contrato);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/Contratos/" + id))
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .header("Content-Type", "application/json")
                    .build();

            HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200 || response.statusCode() == 204) {
                            onSuccess.run();
                        } else {
                            onError.accept(new RuntimeException("Erro: " + response.body()));
                        }
                    })
                    .exceptionally(e -> {
                        onError.accept(e);
                        return null;
                    });

        } catch (Exception e) {
            onError.accept(e);
        }
    }


}
