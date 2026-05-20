package com.example.cabtruckapi.model.repository;

import com.example.cabtruckapi.model.entity.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Integer> {
    List<Relatorio> findByTipoContainingIgnoreCase(String tipo);
    List<Relatorio> findByLinhaProducaoId(Integer linhaProducaoId);
}
