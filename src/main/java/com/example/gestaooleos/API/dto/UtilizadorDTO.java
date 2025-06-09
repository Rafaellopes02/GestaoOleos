package com.example.gestaooleos.API.dto;

public class UtilizadorDTO {
    private Long idutilizador;
    private String nome;

    public UtilizadorDTO(Long idutilizador, String nome) {
        this.idutilizador = idutilizador;
        this.nome = nome;
    }

    public Long getidutilizador() { return idutilizador; }
    public void setidutilizador(Long idutilizador) { this.idutilizador = idutilizador; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}

