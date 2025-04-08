package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.Notificacoes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacoesRepository extends CrudRepository<Notificacoes, Long> {
}
