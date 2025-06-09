package com.example.gestaooleos.API.repository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.example.gestaooleos.API.model.Utilizadores;

@Repository
public interface UtilizadoresRepository extends CrudRepository<Utilizadores, Long> {

    @Query("SELECT * FROM utilizadores WHERE idtipoutilizador = 1")
    List<Utilizadores> findClientes();

    Optional<Utilizadores> findByUsername(String username);

    List<Utilizadores> findByIdtipoutilizador(Integer idTipo);
}
