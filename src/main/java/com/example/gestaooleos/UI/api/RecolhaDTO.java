package com.example.gestaooleos.UI.api;

import java.time.LocalDate;

public class RecolhaDTO {
    private Long idrecolha;
    private LocalDate data;
    private double quantidade;
    private String observacoes;
    private int numbidoes;
    private int idcontrato;
    private int idutilizador;
    private int idestadorecolha;
    private String morada;

    // Campos para exibição (ViewModel)
    private String nomeContrato;
    private String estado;

    // Getters e Setters
    public Long getIdrecolha() { return idrecolha; }
    public void setIdrecolha(Long idrecolha) { this.idrecolha = idrecolha; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public int getNumbidoes() { return numbidoes; }
    public void setNumbidoes(int numbidoes) { this.numbidoes = numbidoes; }

    public int getIdcontrato() { return idcontrato; }
    public void setIdcontrato(int idcontrato) { this.idcontrato = idcontrato; }

    public int getIdutilizador() { return idutilizador; }
    public void setIdutilizador(int idutilizador) { this.idutilizador = idutilizador; }

    public int getIdestadorecolha() { return idestadorecolha; }
    public void setIdestadorecolha(int idestadorecolha) { this.idestadorecolha = idestadorecolha; }

    public String getMorada() { return morada; }
    public void setMorada(String morada) { this.morada = morada; }

    public String getNomeContrato() { return nomeContrato; }
    public void setNomeContrato(String nomeContrato) { this.nomeContrato = nomeContrato; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
