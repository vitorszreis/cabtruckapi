package com.example.cabtruckapi.model.repository;

import com.example.cabtruckapi.model.entity.Inspetor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspetorRepository extends JpaRepository<Inspetor, Integer> {
    List<Inspetor> findByNomeContainingIgnoreCase(String nome);
    Inspetor findByMatricula(String matricula);
    List<Inspetor> findByAtivoTrue();
    List<Inspetor> findByTurno(String turno);
}
