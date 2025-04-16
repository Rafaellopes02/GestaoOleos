package com.example.gestaooleos.UI.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UtilizadorDTO {

    private Long idutilizador;
    private String nome;
    private String telefone;
    private String morada;
    private Integer idtipoutilizador;
    private String username;
    private String password;

    public Long getIdutilizador() {
        return idutilizador;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {return telefone;}

    public String getMorada() {return morada;}

    public Integer getIdtipoutilizador() {return idtipoutilizador;}

    public String getUsername() {return username;}

    public String getPassword() {return password;}


    public void setIdutilizador(Long idutilizador) { this.idutilizador = idutilizador; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {this.telefone = telefone;}

    public void setMorada(String morada) {this.morada = morada;}

    public void setIdtipoutilizador(Integer idtipoutilizador) { this.idtipoutilizador = idtipoutilizador; }

    public void setUsername(String username) { this.username = username;}

    public void setPassword(String password) { this.password = password;}
}
