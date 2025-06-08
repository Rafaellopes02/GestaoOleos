package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.dto.PedidoContratoDetalhadoDTO;
import com.example.gestaooleos.API.model.PedidosContrato;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidosContratoRepository extends CrudRepository<PedidosContrato, Long> {

    long countByIdestadopedido(int idestadopedido);

    @Query("""
        SELECT pc.idpedidocontrato, c.nome AS nomeContrato,
               TO_CHAR(c.datainicio, 'YYYY-MM-DD') AS dataInicio,
               TO_CHAR(c.datafim, 'YYYY-MM-DD') AS dataFim,
               u.nome AS nomeUtilizador, pc.idestadopedido
        FROM pedidoscontrato pc
        JOIN contratos c ON pc.idcontrato = c.idcontrato
        JOIN utilizadores u ON c.idutilizador = u.idutilizador
        WHERE pc.idestadopedido = 1
        ORDER BY pc.idpedidocontrato ASC
    """)
    List<PedidoContratoDetalhadoDTO> listarPedidosComDetalhes();
}
