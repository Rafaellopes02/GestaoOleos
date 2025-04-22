package com.example.gestaooleos.API.dto;

public class ContratoDTOBackend {
    private Long idcontrato;
    private String nome;
    private String dataInicio;
    private String dataFim;
    private int idEstadoContrato;
    private String estado; // nome do estado (ex: "Ativo")

    public Long getIdcontrato() {
        return idcontrato;
    }

    public void setIdcontrato(Long idcontrato) {
        this.idcontrato = idcontrato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public int getIdEstadoContrato() {
        return idEstadoContrato;
    }

    public void setIdEstadoContrato(int idEstadoContrato) {
        this.idEstadoContrato = idEstadoContrato;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
