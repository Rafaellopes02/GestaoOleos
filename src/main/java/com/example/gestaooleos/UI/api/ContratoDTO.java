package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratoDTO {

    private Long idcontrato;
    private String nome;

    @JsonProperty("dataInicio")
    private String dataInicio;

    @JsonProperty("dataFim")
    private String dataFim;

    @JsonProperty("idestadocontrato")
    private int idEstadoContrato;

    @JsonProperty("estado")
    private String estado;

    // Getters obrigatórios para o TableView funcionar
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

    public int getIdEstadoContrato() {
        return idEstadoContrato;
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

    public void setIdEstadoContrato(int idEstadoContrato) {
        this.idEstadoContrato = idEstadoContrato;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
