package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Consumer;



public class RecolhasClient {

    private static final String API_URL = "http://localhost:8080/recolhas";
    private static final String API_EM_ANDAMENTO = API_URL + "/em-andamento";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // Buscar recolhas "em andamento"
    public void buscarRecolhas(Consumer<List<RecolhaDTO>> onSuccess, Consumer<String> onError) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        List<RecolhaDTO> lista = mapper.readValue(json, new TypeReference<List<RecolhaDTO>>() {});
                        onSuccess.accept(lista);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onError.accept("Erro ao converter JSON: " + e.getMessage());
                    }
                })
                .exceptionally(ex -> {
                    onError.accept("Erro na requisição: " + ex.getMessage());
                    return null;
                });
    }

    // Adicionar nova recolha
    public void adicionarRecolha(RecolhaDTO recolha, Consumer<String> onSuccess, Consumer<String> onError) {
        try {
            String json = mapper.writeValueAsString(recolha);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(onSuccess)
                    .exceptionally(ex -> {
                        onError.accept("Erro ao adicionar recolha: " + ex.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            onError.accept("Erro ao converter para JSON: " + e.getMessage());
        }
    }

    // Atualizar estado da recolha
    public void atualizarEstadoRecolha(Long idRecolha, int novoIdEstado, Consumer<String> onSuccess, Consumer<String> onError) {
        String url = API_URL + "/" + idRecolha + "/estado/" + novoIdEstado;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(onSuccess)
                .exceptionally(ex -> {
                    onError.accept("Erro ao atualizar estado: " + ex.getMessage());
                    return null;
                });
    }

    public void buscarRecolhasPorFuncionario(int idFuncionario, Consumer<List<RecolhaDTO>> onSuccess, Consumer<String> onError) {
        String url = API_URL + "/funcionario/" + idFuncionario; // <- Corrigido aqui

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        List<RecolhaDTO> lista = mapper.readValue(json, new TypeReference<List<RecolhaDTO>>() {});
                        onSuccess.accept(lista);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onError.accept("Erro ao converter JSON: " + e.getMessage());
                    }
                })
                .exceptionally(ex -> {
                    onError.accept("Erro na requisição: " + ex.getMessage());
                    return null;
                });
    }

    public void atualizarObservacoes(Long idRecolha, String observacoes, Consumer<Void> onSuccess, Consumer<String> onError) {
        String url = "http://localhost:8080/recolhas/" + idRecolha + "/observacoes";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("\"" + observacoes + "\""))
                .header("Content-Type", "application/json")
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) onSuccess.accept(null);
                    else onError.accept("Código: " + response.statusCode());
                })
                .exceptionally(ex -> {
                    onError.accept(ex.getMessage());
                    return null;
                });
    }






    public ObjectMapper getMapper() {
        return mapper;
    }
}
