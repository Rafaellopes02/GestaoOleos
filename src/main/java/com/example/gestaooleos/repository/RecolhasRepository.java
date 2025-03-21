package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.Recolhas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecolhasRepository extends CrudRepository<Recolhas, Long> {
}
