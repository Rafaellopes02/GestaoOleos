package com.example.gestaooleos.service;

import com.example.gestaooleos.model.Notificacoes;
import com.example.gestaooleos.repository.NotificacoesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificacoesService {
    private final NotificacoesRepository notificacoesRepository;

    public NotificacoesService(NotificacoesRepository notificacoesRepository) {
        this.notificacoesRepository = notificacoesRepository;
    }

    public Iterable<Notificacoes> listarNotificacoes() {
        return notificacoesRepository.findAll();
    }

    public Optional<Notificacoes> obterNotificacoes(Long id) {
        return notificacoesRepository.findById(id);
    }

    public Notificacoes criarNotificacoes(Notificacoes contrato) {
        return notificacoesRepository.save(contrato);
    }

    public void removeNotificacoes(Long id) {
        notificacoesRepository.deleteById(id);
    }
}
