package com.example.gestaooleos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Table("tipoutilizador")
public class TipoUtilizador {
    @Id
    private Long idtipoutilizador;
    private String nome;
}
