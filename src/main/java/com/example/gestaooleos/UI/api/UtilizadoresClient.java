package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UtilizadoresClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String API_URL = "http://localhost:8080/Utilizadores";
    private static final String LOGIN_URL = "http://localhost:8080/api/auth/login";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String BASE_URL = "http://localhost:8080/Utilizadores";

    public void buscarUtilizadorPorId(Long id, Consumer<UtilizadorDTO> onSuccess, Consumer<Throwable> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        UtilizadorDTO utilizador = mapper.readValue(json, UtilizadorDTO.class);
                        onSuccess.accept(utilizador);
                    } catch (Exception e) {
                        onError.accept(e);
                    }
                })
                .exceptionally(erro -> {
                    onError.accept(erro);
                    return null;
                });
    }

    // LOGIN (POST)
    public void login(String username, String password, Consumer<String> onSuccess, Consumer<String> onError) {
        try {
            Map<String, String> credenciais = new HashMap<>();
            credenciais.put("username", username);
            credenciais.put("password", password);

            String json = mapper.writeValueAsString(credenciais);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(LOGIN_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
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

    // READ (GET)
    public void buscarUtilizadores(Consumer<String> onSuccess, Consumer<String> onError) {
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

    // CREATE (POST)
    public void criarUtilizador(UtilizadorDTO utilizador, Consumer<String> onSuccess, Consumer<String> onError) {
        try {
            String json = mapper.writeValueAsString(utilizador);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
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

    // UPDATE (PUT)
    public void atualizarUtilizador(Long id, UtilizadorDTO utilizador, Consumer<String> onSuccess, Consumer<String> onError) {
        try {
            String json = mapper.writeValueAsString(utilizador);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
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

    // DELETE
    public void apagarUtilizador(Long id, Consumer<String> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(onSuccess)
                .exceptionally(ex -> {
                    onError.accept(ex.getMessage());
                    return null;
                });
    }

    public void buscarUtilizadorPorId(int id, Consumer<String> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/Utilizadores/" + id))
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

    public void listarClientes(Consumer<String> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/utilizadores/clientes"))
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

    public void buscarEmpregados(Consumer<String> onSuccess, Consumer<String> onError) {
        String endpoint = "/api/utilizadores/empregados";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET()
                .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(onSuccess)
                .exceptionally(ex -> {
                    onError.accept(ex.getMessage());
                    return null;
                });
    }
}

