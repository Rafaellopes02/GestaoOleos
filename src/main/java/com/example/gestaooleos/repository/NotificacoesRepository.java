package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.Notificacoes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacoesRepository extends CrudRepository<Notificacoes, Long> {
}
