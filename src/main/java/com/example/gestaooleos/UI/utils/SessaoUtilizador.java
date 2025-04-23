package com.example.gestaooleos.UI.utils;

public class SessaoUtilizador {
    private static String nomeUtilizador;

    public static void setNomeUtilizador(String nome) {
        nomeUtilizador = nome;
    }

    public static String getNomeUtilizador() {
        return nomeUtilizador;
    }

    public static void limparSessao() {
        nomeUtilizador = null;
    }
}
