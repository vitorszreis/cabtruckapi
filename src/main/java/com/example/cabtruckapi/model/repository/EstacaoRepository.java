package com.example.cabtruckapi.model.repository;

import com.example.cabtruckapi.model.entity.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstacaoRepository extends JpaRepository<Estacao, Integer> {
    List<Estacao> findByNomeContainingIgnoreCase(String nome);
    List<Estacao> findByOrdem(Integer ordem);
    List<Estacao> findByLinhaProducaoId(Integer linhaProducaoId);
}
