package com.example.cabtruckapi.model.repository;

import com.example.cabtruckapi.model.entity.AcaoCorretiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcaoCorretivaRepository extends JpaRepository<AcaoCorretiva, Integer> {
    List<AcaoCorretiva> findByFalhaId(Integer falhaId);
    List<AcaoCorretiva> findByEficaciaContainingIgnoreCase(String eficacia);
}
