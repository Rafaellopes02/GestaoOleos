package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContadorDTO {
    private int ativos;
    private int concluidos;

    public int getAtivos() {
        return ativos;
    }

    public void setAtivos(int ativos) {
        this.ativos = ativos;
    }

    public int getConcluidos() {
        return concluidos;
    }

    public void setConcluidos(int concluidos) {
        this.concluidos = concluidos;
    }
}
