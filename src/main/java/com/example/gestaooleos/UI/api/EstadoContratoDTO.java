package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EstadoContratoDTO {

    @JsonProperty("idestadocontrato")
    private int idestado; // ← usa este nome internamente se quiseres

    private String nome;

    public int getIdestado() {
        return idestado;
    }

    public void setIdestado(int idestado) {
        this.idestado = idestado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
