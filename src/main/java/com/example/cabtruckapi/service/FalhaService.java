package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Falha;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FalhaService {

    private final ConcurrentHashMap<Integer, Falha> bancoMemoria = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public List<Falha> getFalhas() {
        return new ArrayList<>(bancoMemoria.values());
    }

    public Optional<Falha> getFalhaById(Integer id) {
        return Optional.ofNullable(bancoMemoria.get(id));
    }

    public Falha salvar(Falha falha) {
        validar(falha);
        if (falha.getId() == null) {
            falha.setId(sequence.getAndIncrement());
        }
        bancoMemoria.put(falha.getId(), falha);
        return falha;
    }

    public void excluir(Falha falha) {
        if (falha.getId() == null) {
            throw new RegraNegocioException("Falha sem id");
        }
        bancoMemoria.remove(falha.getId());
    }

    public void validar(Falha falha) {
        if (falha.getDescricao() == null || falha.getDescricao().trim().isEmpty()) {
            throw new RegraNegocioException("Descricao da falha invalida");
        }
        if (falha.getSeveridade() == null || falha.getSeveridade().trim().isEmpty()) {
            throw new RegraNegocioException("Severidade da falha invalida");
        }
        if (falha.getStatus() == null || falha.getStatus().trim().isEmpty()) {
            throw new RegraNegocioException("Status da falha invalido");
        }
    }
}
