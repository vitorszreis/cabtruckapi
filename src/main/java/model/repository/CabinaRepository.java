package model.repository;

import model.entity.Cabina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CabinaRepository extends JpaRepository<Cabina, Integer> {
    List<Cabina> findByNumeroSerieContainingIgnoreCase(String numeroSerie);
    List<Cabina> findByModeloContainingIgnoreCase(String modelo);
    List<Cabina> findByStatus(String status);
    List<Cabina> findByLinhaProducaoId(Integer linhaProducaoId);
}
