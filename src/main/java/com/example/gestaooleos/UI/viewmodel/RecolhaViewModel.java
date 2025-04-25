package com.example.gestaooleos.UI.viewmodel;

public class RecolhaViewModel {
    private String nome;
    private String morada;
    private String data;
    private String estado;

    public RecolhaViewModel(String nome, String morada, String data, String estado) {
        this.nome = nome;
        this.morada = morada;
        this.data = data;
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public String getMorada() {
        return morada;
    }

    public String getData() {
        return data;
    }

    public String getEstado() {
        return estado;
    }
}
