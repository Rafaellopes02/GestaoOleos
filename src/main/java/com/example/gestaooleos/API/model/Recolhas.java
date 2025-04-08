package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Table("recolhas")
public class Recolhas {
    @Id
    private Long idrecolha;
    private Date data;
    private int quantidade;
    private String observacoes;
    private int numbidoes;
    private int idcontrato;
    private int idutilizador;
    private int idestadorecolha;
}
