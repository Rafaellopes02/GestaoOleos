package com.example.gestaooleos.API.dto;

public class ContratoClienteDTOBackend {
    private Long idcontrato;
    private String nomeContrato;
    private String dataInicio;
    private String dataFim;
    private String estado;
    private String moradaCliente;

    // Getters e Setters
    public Long getIdcontrato() {
        return idcontrato;
    }

    public void setIdcontrato(Long idcontrato) {
        this.idcontrato = idcontrato;
    }

    public String getNomeContrato() {
        return nomeContrato;
    }

    public void setNomeContrato(String nomeContrato) {
        this.nomeContrato = nomeContrato;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMoradaCliente() {
        return moradaCliente;
    }

    public void setMoradaCliente(String moradaCliente) {
        this.moradaCliente = moradaCliente;
    }
}
