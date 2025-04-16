package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("utilizadores")
public class Utilizadores {
    @Id
    private Long idutilizador;
    private String nome;
    private String telefone;
    private String morada;
    private int idtipoutilizador;
    private String username;
    private String password;
}
