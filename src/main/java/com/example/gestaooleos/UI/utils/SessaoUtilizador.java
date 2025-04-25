package com.example.gestaooleos.UI.utils;

public class SessaoUtilizador {

    private static String nomeUtilizador;
    private static Integer tipoUtilizador;

    public static void setNomeUtilizador(String nome) {
        nomeUtilizador = nome;
    }

    public static String getNomeUtilizador() {
        return nomeUtilizador;
    }

    public static void setTipoUtilizador(Integer tipo) {
        tipoUtilizador = tipo;
    }

    public static Integer getTipoUtilizador() {
        return tipoUtilizador;
    }

    public static void limparSessao() {
        nomeUtilizador = null;
        tipoUtilizador = null;
    }
}
