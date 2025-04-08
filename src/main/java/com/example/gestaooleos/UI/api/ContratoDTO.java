package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratoDTO {

    private Long idcontrato;
    private String nome;

    @JsonProperty("datainicio")
    private String dataInicio;

    @JsonProperty("datafim")
    private String dataFim;

    private String estado;

    // Getters
    public Long getIdcontrato() {
        return idcontrato;
    }

    public String getNome() {
        return nome;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public String getEstado() {
        return estado;
    }

    // Setters
    public void setIdcontrato(Long idcontrato) {
        this.idcontrato = idcontrato;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
