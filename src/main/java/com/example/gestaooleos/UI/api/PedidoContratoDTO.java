package com.example.gestaooleos.UI.api;

import lombok.Data;

@Data
public class PedidoContratoDTO {
    private Long idpedidocontrato;
    private String nomeContrato;
    private String dataInicio;
    private String dataFim;
    private String nomeUtilizador;
    private int idestadopedido; // Necessário para filtrar pedidos pendentes
}
