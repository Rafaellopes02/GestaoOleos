package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("recolhas")
public class Recolhas {

    @Id
    private Long idrecolha;
    private LocalDate data;
    private double quantidade;
    private String observacoes;
    private int numbidoes;
    private Long idcontrato;
    private Long idutilizador;
    private int idestadorecolha;
    private String morada;
}
