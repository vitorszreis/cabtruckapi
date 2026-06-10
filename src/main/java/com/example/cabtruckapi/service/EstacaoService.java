package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Estacao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EstacaoService {

    private final ConcurrentHashMap<Integer, Estacao> bancoMemoria = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public List<Estacao> getEstacoes() {
        return new ArrayList<>(bancoMemoria.values());
    }

    public Optional<Estacao> getEstacaoById(Integer id) {
        return Optional.ofNullable(bancoMemoria.get(id));
    }

    public Estacao salvar(Estacao estacao) {
        validar(estacao);
        if (estacao.getId() == null) {
            estacao.setId(sequence.getAndIncrement());
        }
        bancoMemoria.put(estacao.getId(), estacao);
        return estacao;
    }

    public void excluir(Estacao estacao) {
        if (estacao.getId() == null) {
            throw new RegraNegocioException("Estacao sem id");
        }
        bancoMemoria.remove(estacao.getId());
    }

    public void validar(Estacao estacao) {
        if (estacao.getNome() == null || estacao.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome da estacao invalido");
        }
        if (estacao.getOrdem() == null || estacao.getOrdem() < 1) {
            throw new RegraNegocioException("Ordem da estacao invalida");
        }
    }
}
