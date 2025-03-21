package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.EstadosRecolhas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosRecolhasRepository extends CrudRepository<EstadosRecolhas, Long> {
}
