package com.example.gestaooleos.UI.api;

class ContratoDTO {
    private String nome;
    private String dataInicio;
    private String dataFim;
    private String estado;

    // Getters e setters obrigatórios para funcionar com TableView e Jackson
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }

    public String getDataFim() { return dataFim; }
    public void setDataFim(String dataFim) { this.dataFim = dataFim; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
