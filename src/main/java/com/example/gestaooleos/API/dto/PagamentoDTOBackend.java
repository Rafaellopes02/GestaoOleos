package com.example.gestaooleos.API.dto;

import java.math.BigDecimal;

public class PagamentoDTOBackend {
    private Long idpagamento;
    private String datapagamento;
    private BigDecimal valor;
    private String nomeContrato;
    private String nomeCliente;
    private String estado;

    // Getters e setters
    public Long getIdpagamento() {
        return idpagamento;
    }

    public void setIdpagamento(Long idpagamento) {
        this.idpagamento = idpagamento;
    }

    public String getDatapagamento() {
        return datapagamento;
    }

    public void setDatapagamento(String datapagamento) {
        this.datapagamento = datapagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNomeContrato() {
        return nomeContrato;
    }

    public void setNomeContrato(String nomeContrato) {
        this.nomeContrato = nomeContrato;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
