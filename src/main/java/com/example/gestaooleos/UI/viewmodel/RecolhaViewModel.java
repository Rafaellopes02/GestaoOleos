package com.example.gestaooleos.UI.viewmodel;

public class RecolhaViewModel {
    private String nome;
    private String morada;
    private String data;
    private String estado;
    private String observacoes;

    public RecolhaViewModel(String nome, String morada, String data, String estado, String observacoes) {
        this.nome = nome;
        this.morada = morada;
        this.data = data;
        this.estado = estado;
        this.observacoes = observacoes;
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

    public String getObservacoes() { return observacoes; }
    }

