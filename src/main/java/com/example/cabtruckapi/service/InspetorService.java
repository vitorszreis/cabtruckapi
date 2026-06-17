package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Inspetor;
import com.example.cabtruckapi.model.repository.InspetorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InspetorService {

    private InspetorRepository repository;

    public InspetorService(InspetorRepository repository) {
        this.repository = repository;
    }

    public List<Inspetor> getInspetores() {
        return repository.findAll();
    }

    public Optional<Inspetor> getInspetorById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Inspetor salvar(Inspetor inspetor) {
        validar(inspetor);
        return repository.save(inspetor);
    }

    @Transactional
    public void excluir(Inspetor inspetor) {
        Objects.requireNonNull(inspetor.getId());
        repository.delete(inspetor);
    }

    public void validar(Inspetor inspetor) {
        if (inspetor.getNome() == null || inspetor.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do inspetor invalido");
        }
        if (inspetor.getMatricula() == null || inspetor.getMatricula().trim().isEmpty()) {
            throw new RegraNegocioException("Matricula do inspetor invalida");
        }
        if (inspetor.getTurno() == null || inspetor.getTurno().trim().isEmpty()) {
            throw new RegraNegocioException("Turno do inspetor invalido");
        }
        if (inspetor.getAtivo() == null) {
            throw new RegraNegocioException("Status do inspetor invalido");
        }
    }
}
