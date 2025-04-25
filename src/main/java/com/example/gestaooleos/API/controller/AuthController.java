package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.repository.UtilizadoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UtilizadoresRepository utilizadoresRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Utilizadores utilizador = StreamSupport
                .stream(utilizadoresRepository.findAll().spliterator(), false)
                .filter(u ->
                        u.getUsername() != null &&
                                u.getPassword() != null &&
                                u.getUsername().equals(request.getUsername()) &&
                                u.getPassword().equals(request.getPassword())
                )
                .findFirst()
                .orElse(null);

        if (utilizador == null) {
            return ResponseEntity.status(401).body("Credenciais inválidas.");
        }

        return ResponseEntity.ok(utilizador);
    }


    // Classe interna para receber o JSON com username e password
    public static class LoginRequest {
        private String username;
        private String password;

        // Getters e Setters
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
