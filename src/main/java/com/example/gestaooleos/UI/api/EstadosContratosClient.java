package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.function.Consumer;

public class EstadosContratosClient {

    private static final String ENDPOINT = "http://localhost:8080/EstadosContratos";
    private final ObjectMapper mapper = new ObjectMapper();

    public void buscarEstados(Consumer<String> onSuccess, Consumer<Throwable> onError) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .GET()
                .build();

        HttpClient.newHttpClient()
                .sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(onSuccess)
                .exceptionally(erro -> {
                    onError.accept(erro);
                    return null;
                });
    }

    public static List<EstadoContratoDTO> parseEstados(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<>() {});
    }
}
