package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("estadoscontratos")
public class EstadosContratos {
    @Id
    private int idestadocontrato;
    private String nome;
}
