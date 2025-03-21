package com.example.gestaooleos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Table("contratos")
public class Contratos {
    @Id
    private Long idcontrato;
    private String nome;
    private int idestadocontrato;
    private Date datainicio;
    private Date datafim;
    private int idutilizador;
}
