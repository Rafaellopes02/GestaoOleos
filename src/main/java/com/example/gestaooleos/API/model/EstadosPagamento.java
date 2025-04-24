package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("estadospagamento")
public class EstadosPagamento {
    @Id
    private Long idestadospagamento;
    private String nome;
}
