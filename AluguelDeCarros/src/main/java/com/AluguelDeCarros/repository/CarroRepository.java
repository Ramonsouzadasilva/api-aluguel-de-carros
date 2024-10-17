package com.AluguelDeCarros.repository;

import com.AluguelDeCarros.entity.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarroRepository extends JpaRepository<Carro, Long> {
    Optional<Carro> findByModeloAndDisponivelTrue(String modelo);
    Optional<Carro> findByIdAndDisponivelTrue(Long carroId);

    //Método para buscar carros com filtros usando LIKE
    @Query("SELECT c FROM Carro c " +
            "WHERE (:ano IS NULL OR c.ano = :ano) " +
            "AND (:modelo IS NULL OR LOWER(c.modelo) LIKE LOWER(CONCAT('%', :modelo, '%'))) " +
            "AND (:descricao IS NULL OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))) ")
    List<Carro> filtrarCarroslike(@Param("ano") Integer ano,
                               @Param("modelo") String modelo,
                               @Param("descricao") String descricao);

}