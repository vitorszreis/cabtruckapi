package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Inspetor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InspetorService {

    private final ConcurrentHashMap<Integer, Inspetor> bancoMemoria = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public List<Inspetor> getInspetores() {
        return new ArrayList<>(bancoMemoria.values());
    }

    public Optional<Inspetor> getInspetorById(Integer id) {
        return Optional.ofNullable(bancoMemoria.get(id));
    }

    public Inspetor salvar(Inspetor inspetor) {
        validar(inspetor);
        if (inspetor.getId() == null) {
            inspetor.setId(sequence.getAndIncrement());
        }
        bancoMemoria.put(inspetor.getId(), inspetor);
        return inspetor;
    }

    public void excluir(Inspetor inspetor) {
        if (inspetor.getId() == null) {
            throw new RegraNegocioException("Inspetor sem id");
        }
        bancoMemoria.remove(inspetor.getId());
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
