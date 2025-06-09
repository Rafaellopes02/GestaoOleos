package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListaEmpregadosDTO {

    @JsonProperty("empregados")
    private List<UtilizadorDTO> empregados;

    public List<UtilizadorDTO> getEmpregados() {
        return empregados;
    }

    public void setEmpregados(List<UtilizadorDTO> empregados) {
        this.empregados = empregados;
    }
}
