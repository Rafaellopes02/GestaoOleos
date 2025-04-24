package com.example.gestaooleos.UI.controller;

import com.example.gestaooleos.UI.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuLateralController {

    @FXML private Button btnPaginaInicial;
    @FXML private Button btnContratos;
    @FXML private Button btnRecolhas;
    @FXML private Button btnPagamentos;
    @FXML private Button btnUtilizadores;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        btnPaginaInicial.setOnAction(e -> navegar("home-funcionario.fxml"));
        btnContratos.setOnAction(e -> navegar("contratos-view.fxml"));
        btnRecolhas.setOnAction(e -> navegar("recolhas-view.fxml"));
        btnPagamentos.setOnAction(e -> navegar("pagamentos-view.fxml"));
        btnUtilizadores.setOnAction(e -> navegar("utilizadores-view.fxml"));
        btnPagamentos.setOnAction(e -> navegar("pagamentos-view.fxml"));
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
