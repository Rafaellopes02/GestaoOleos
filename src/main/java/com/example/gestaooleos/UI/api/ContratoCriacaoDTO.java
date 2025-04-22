package com.example.gestaooleos.UI.api;

public class ContratoCriacaoDTO {
    private String nome;
    private String datainicio;
    private String datafim;
    private int idestadocontrato;

    public ContratoCriacaoDTO(String nome, String datainicio, String datafim, int idestadocontrato) {
        this.nome = nome;
        this.datainicio = datainicio;
        this.datafim = datafim;
        this.idestadocontrato = idestadocontrato;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public String getDatainicio() {
        return datainicio;
    }

    public String getDatafim() {
        return datafim;
    }

    public int getIdestadocontrato() {
        return idestadocontrato;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDatainicio(String datainicio) {
        this.datainicio = datainicio;
    }

    public void setDatafim(String datafim) {
        this.datafim = datafim;
    }

    public void setIdestadocontrato(int idestadocontrato) {
        this.idestadocontrato = idestadocontrato;
    }
}
