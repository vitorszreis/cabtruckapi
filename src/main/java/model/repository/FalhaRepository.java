package model.repository;

import model.entity.Falha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FalhaRepository extends JpaRepository<Falha, Integer> {
    List<Falha> findBySeveridade(String severidade);
    List<Falha> findByStatus(String status);
    List<Falha> findByTipoFalhaId(Integer tipoFalhaId);
    List<Falha> findByCabinaId(Integer cabinaId);
    List<Falha> findByEstacaoId(Integer estacaoId);
}
