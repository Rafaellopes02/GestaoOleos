package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Date;

@Data
@Table("recolhas")
public class Recolhas {
    @Id
    private Long idrecolha;
    private LocalDate data;
    private Double quantidade;
    private String observacoes;
    private Integer numbidoes;
    private Integer idcontrato;
    private Integer idutilizador;
    private Integer idestadorecolha;
    private String morada;
}
