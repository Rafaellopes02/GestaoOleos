package com.example.gestaooleos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table("estadospedidos")
public class EstadosPedidos {
    @Id
    private Long idestadopedido;
    private String nome;
}
