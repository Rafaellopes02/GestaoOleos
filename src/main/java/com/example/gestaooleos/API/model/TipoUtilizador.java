package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tipoutilizador")
public class TipoUtilizador {
    @Id
    private Long idtipoutilizador;
    private String nome;
}
