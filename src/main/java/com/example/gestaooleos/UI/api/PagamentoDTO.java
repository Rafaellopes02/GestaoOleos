package com.example.gestaooleos.UI.api;

public class PagamentoDTO {
    private Long idpagamento;
    private Long idcontrato;
    private String datapagamento;
    private double valor;
    private Long idmetodopagamento;
    private Long idestadospagamento;

    // Getters e Setters
    public Long getIdpagamento() {
        return idpagamento;
    }

    public void setIdpagamento(Long idpagamento) {
        this.idpagamento = idpagamento;
    }

    public Long getIdcontrato() {
        return idcontrato;
    }

    public void setIdcontrato(Long idcontrato) {
        this.idcontrato = idcontrato;
    }

    public String getDatapagamento() {
        return datapagamento;
    }

    public void setDatapagamento(String datapagamento) {
        this.datapagamento = datapagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Long getIdmetodopagamento() {
        return idmetodopagamento;
    }

    public void setIdmetodopagamento(Long idmetodopagamento) {
        this.idmetodopagamento = idmetodopagamento;
    }

    public Long getIdestadospagamento() {
        return idestadospagamento;
    }

    public void setIdestadospagamento(Long idestadospagamento) {
        this.idestadospagamento = idestadospagamento;
    }
}
