package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PagamentosClienteClient {

    private static final String BASE_URL = "http://localhost:8080/api/pagamentos";

    public static Task<List<PagamentoClienteDTO>> listarPagamentosPendentes(Long idcliente) {
        return new Task<>() {
            @Override
            protected List<PagamentoClienteDTO> call() throws Exception {
                URL url = new URL(BASE_URL + "/pendentes/" + idcliente);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Erro ao carregar pagamentos pendentes");
                }

                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder json = new StringBuilder();
                while (scanner.hasNext()) {
                    json.append(scanner.nextLine());
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json.toString());
                List<PagamentoClienteDTO> pagamentos = new ArrayList<>();

                for (JsonNode node : root) {
                    PagamentoClienteDTO dto = new PagamentoClienteDTO();
                    dto.setIdpagamento(node.get("idpagamento").asLong());
                    dto.setValor(node.get("valor").asDouble());
                    dto.setDatapagamento(node.get("datapagamento").asText());

                    // Estes dois não vêm do backend, então vamos definir "placeholder" no frontend:
                    dto.setNomeContrato("Contrato " + node.get("idcontrato").asLong()); // Só para mostrar algo
                    int idEstado = node.get("idestadospagamento").asInt();
                    String estadoTexto = (idEstado == 2) ? "Pendente" : "Outro";
                    dto.setEstado(estadoTexto);

                    pagamentos.add(dto);
                }

                return pagamentos;
            }
        };
    }

    public static Task<Void> pagarPagamento(Long idPagamento, Long idMetodoPagamento) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Adicionar o parâmetro idmetodopagamento na URL
                URL url = new URL("http://localhost:8080/api/pagamentos/pagar/" + idPagamento + "?idmetodopagamento=" + idMetodoPagamento);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.connect(); // Não é preciso mandar body

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Erro ao pagar pagamento: " + conn.getResponseCode());
                }

                return null;
            }
        };
    }


    public static Task<Void> cancelarPagamento(Long idPagamento) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                URL url = new URL("http://localhost:8080/api/pagamentos/cancelar/" + idPagamento);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.connect();

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Erro ao cancelar pagamento: " + conn.getResponseCode());
                }

                return null;
            }
        };
    }

}
