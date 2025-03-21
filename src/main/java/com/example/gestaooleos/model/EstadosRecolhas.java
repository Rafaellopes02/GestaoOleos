package com.example.gestaooleos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("estadosrecolhas")
public class EstadosRecolhas {
    @Id
    private Long idestadorecolha;
    private String nome;
}
