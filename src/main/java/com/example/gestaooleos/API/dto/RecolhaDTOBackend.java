package com.example.gestaooleos.API.dto;

public class RecolhaDTOBackend {
    private Long idrecolha;
    private String data;
    private double quantidade;
    private String observacoes;
    private int numbidoes;
    private String morada;
    private int idestadorecolha;



    public int getIdestadorecolha() { return idestadorecolha; }
    public void setIdestadorecolha(int idestadorecolha) { this.idestadorecolha = idestadorecolha; }


    // Getters e Setters
    public Long getIdrecolha() {
        return idrecolha;
    }

    public void setIdrecolha(Long idrecolha) {
        this.idrecolha = idrecolha;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getNumbidoes() {
        return numbidoes;
    }

    public void setNumbidoes(int numbidoes) {
        this.numbidoes = numbidoes;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }
}
