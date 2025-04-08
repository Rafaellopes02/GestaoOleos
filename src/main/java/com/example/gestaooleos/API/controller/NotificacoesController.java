package com.example.gestaooleos.API.controller;


import com.example.gestaooleos.API.model.Notificacoes;
import com.example.gestaooleos.API.service.NotificacoesService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Notificacoes")
public class NotificacoesController {
    private final NotificacoesService notificacoesService;

    public NotificacoesController(NotificacoesService notificacoesService) {
        this.notificacoesService = notificacoesService;
    }

    @GetMapping
    public Iterable<Notificacoes> listarNotificacoes() {
        return notificacoesService.listarNotificacoes();
    }

    @GetMapping("/{id}")
    public Optional<Notificacoes> obterNotificacoes(@PathVariable Long id) {
        return notificacoesService.obterNotificacoes(id);
    }

    @PostMapping
    public Notificacoes criarNotificacoes(@RequestBody Notificacoes notificacao) {
        return notificacoesService.criarNotificacoes(notificacao);
    }

    @DeleteMapping("/{id}")
    public void removeNotificacoes(@PathVariable Long id) {
        notificacoesService.removeNotificacoes(id);
    }
}
