package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@Table("notificacoes")
public class Notificacoes {
    @Id
    private Long idnotificacao;
    private String mensagem;
    private Date data;
    private int idutilizador;
}
