package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuLateralController {

    @FXML private Button btnPaginaInicial;
    @FXML private Button btnConfirmaRecolhas;
    @FXML private Button btnContratos;
    @FXML private Button btnRecolhas;
    @FXML private Button btnPagamentos;
    @FXML private Button btnUtilizadores;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
            int tipo = SessaoUtilizador.getTipoUtilizador();

            if (tipo == 1) { // Cliente
                btnPaginaInicial.setOnAction(e -> navegar("home-cliente.fxml"));

                btnContratos.setText(" Meus Contratos");
                btnContratos.setOnAction(e -> navegar("contratos-cliente.fxml"));

                btnPagamentos.setText(" Meus Pagamentos");
                btnPagamentos.setOnAction(e -> navegar("pagamentos-cliente.fxml"));

                btnRecolhas.setVisible(false);
                btnRecolhas.setManaged(false);

                btnUtilizadores.setVisible(false);
                btnUtilizadores.setManaged(false);

            } else if (tipo == 2) { // Empregado (normal)
                btnPaginaInicial.setOnAction(e -> navegar("home-funcionario.fxml"));
                btnContratos.setVisible(false);
                btnContratos.setManaged(false);
                btnRecolhas.setVisible(false);
                btnRecolhas.setManaged(false);
                btnPagamentos.setVisible(false);
                btnPagamentos.setManaged(false);
                btnUtilizadores.setVisible(false);
                btnUtilizadores.setManaged(false);

            } else if (tipo == 3) { // Escritório
                btnPaginaInicial.setOnAction(e -> navegar("home-escritorio.fxml"));

                btnRecolhas.setOnAction(e -> navegar("recolhas-view.fxml"));
                btnUtilizadores.setText(" Confirma Recolha");
                btnUtilizadores.setOnAction(e -> navegar("confirmar-recolha.fxml"));
                btnPagamentos.setOnAction(e -> navegar("pagamentos-view.fxml"));

                btnContratos.setVisible(false);
                btnContratos.setManaged(false);
                btnPagamentos.setVisible(true);
                btnPagamentos.setManaged(true);

            } else if (tipo == 4) { // Comercial
                btnPaginaInicial.setOnAction(e -> navegar("home-comercial.fxml"));
                btnContratos.setOnAction(e -> navegar("contratos-view.fxml"));

                btnUtilizadores.setVisible(false);
                btnUtilizadores.setManaged(false);
                btnContratos.setVisible(true);
                btnContratos.setManaged(true);
                btnRecolhas.setVisible(false);
                btnRecolhas.setManaged(false);
                btnPagamentos.setVisible(false);
                btnPagamentos.setManaged(false);
            }else { // Admin ou genérico
                btnPaginaInicial.setOnAction(e -> navegar("home-funcionario.fxml"));
                btnContratos.setOnAction(e -> navegar("contratos-view.fxml"));
                btnRecolhas.setOnAction(e -> navegar("recolhas-view.fxml"));
                btnPagamentos.setOnAction(e -> navegar("pagamentos-view.fxml"));
                btnUtilizadores.setOnAction(e -> navegar("utilizadores-view.fxml"));
            }

            btnLogout.setOnAction(e -> logout());
        }


        private void navegar(String nomeFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.gestaooleos/view/" + nomeFxml));
            Parent root = loader.load();
            Stage stage = (Stage) btnContratos.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com.example.gestaooleos/view/login-view.fxml")
            );
            Parent login = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.getScene().setRoot(login);
            stage.setTitle("Login");
            FullscreenHelper.ativarFullscreen(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}