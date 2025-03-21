package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.Utilizadores;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilizadoresRepository extends CrudRepository<Utilizadores, Long> {
}
