package com.example.gestaooleos.API.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.hmacShaKeyFor("chave-super-secreta-e-grande-o-suficiente-123456".getBytes());

    public String generateToken(Long idUtilizador, int tipoUtilizador) {
        return Jwts.builder()
                .setSubject(String.valueOf(idUtilizador))
                .claim("tipo", tipoUtilizador) // 👈 ADICIONAR ESTA LINHA
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }

}
