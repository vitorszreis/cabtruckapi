package com.example.cabtruckapi.model.repository;

import com.example.cabtruckapi.model.entity.LinhaProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinhaProducaoRepository extends JpaRepository<LinhaProducao, Integer> {
    List<LinhaProducao> findByNomeContainingIgnoreCase(String nome);
    List<LinhaProducao> findByAtivaTrue();
    List<LinhaProducao> findByTurno(String turno);
}
