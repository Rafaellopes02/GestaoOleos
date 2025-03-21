package com.example.gestaooleos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Table("pedidoscontrato")
public class PedidosContrato {
    @Id
    private Long idpedidocontrato;
    private Date data;
    private int idestadopedido;
    private int idcontrato;
}
