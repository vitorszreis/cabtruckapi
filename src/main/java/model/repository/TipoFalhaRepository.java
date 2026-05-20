package model.repository;

import model.entity.TipoFalha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoFalhaRepository extends JpaRepository<TipoFalha, Integer> {
    TipoFalha findByCodigo(String codigo);
    List<TipoFalha> findByCategoria(String categoria);
    List<TipoFalha> findByDescricaoContainingIgnoreCase(String descricao);
}
