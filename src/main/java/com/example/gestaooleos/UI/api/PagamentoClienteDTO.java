package com.example.gestaooleos.UI.api;

import javafx.beans.property.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PagamentoClienteDTO {

    private LongProperty idpagamento = new SimpleLongProperty();
    private StringProperty nomeContrato = new SimpleStringProperty();
    private DoubleProperty valor = new SimpleDoubleProperty();
    private StringProperty datapagamento = new SimpleStringProperty();
    private StringProperty estado = new SimpleStringProperty();

    public Long getIdpagamento() { return idpagamento.get(); }
    public void setIdpagamento(Long id) { this.idpagamento.set(id); }
    public LongProperty idpagamentoProperty() { return idpagamento; }

    public String getNomeContrato() { return nomeContrato.get(); }
    public void setNomeContrato(String nome) { this.nomeContrato.set(nome); }
    public StringProperty nomeContratoProperty() { return nomeContrato; }

    public Double getValor() { return valor.get(); }
    public void setValor(Double valor) { this.valor.set(valor); }
    public DoubleProperty valorProperty() { return valor; }

    public String getDatapagamento() { return datapagamento.get(); }
    public void setDatapagamento(String data) { this.datapagamento.set(data); }
    public StringProperty datapagamentoProperty() { return datapagamento; }

    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }
    public StringProperty estadoProperty() { return estado; }
}
