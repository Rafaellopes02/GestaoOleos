package com.example.gestaooleos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("estadoscontratos")
public class EstadosContratos {
    @Id
    private Long idestadocontrato;
    private String nome;
}
