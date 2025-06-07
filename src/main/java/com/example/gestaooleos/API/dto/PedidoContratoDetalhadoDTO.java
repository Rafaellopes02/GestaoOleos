package com.example.gestaooleos.API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PedidoContratoDetalhadoDTO {
    private Long idpedidocontrato;
    private String nomeContrato;
    private String dataInicio;
    private String dataFim;
    private String nomeUtilizador;
    private int idestadopedido;
}
