package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("metodopagamento")
public class MetodoPagamento {

    @Id
    private Long idmetodopagamento;
    private String metodo;
}
