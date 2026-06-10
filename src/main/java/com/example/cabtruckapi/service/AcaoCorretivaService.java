package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.AcaoCorretiva;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AcaoCorretivaService {

    private final ConcurrentHashMap<Integer, AcaoCorretiva> bancoMemoria = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public List<AcaoCorretiva> getAcoesCorretivas() {
        return new ArrayList<>(bancoMemoria.values());
    }

    public Optional<AcaoCorretiva> getAcaoCorretivaById(Integer id) {
        return Optional.ofNullable(bancoMemoria.get(id));
    }

    public AcaoCorretiva salvar(AcaoCorretiva acaoCorretiva) {
        validar(acaoCorretiva);
        if (acaoCorretiva.getId() == null) {
            acaoCorretiva.setId(sequence.getAndIncrement());
        }
        bancoMemoria.put(acaoCorretiva.getId(), acaoCorretiva);
        return acaoCorretiva;
    }

    public void excluir(AcaoCorretiva acaoCorretiva) {
        if (acaoCorretiva.getId() == null) {
            throw new RegraNegocioException("Acao corretiva sem id");
        }
        bancoMemoria.remove(acaoCorretiva.getId());
    }

    public void validar(AcaoCorretiva acaoCorretiva) {
        if (acaoCorretiva.getDescricao() == null || acaoCorretiva.getDescricao().trim().isEmpty()) {
            throw new RegraNegocioException("Descricao da acao corretiva invalida");
        }
        if (acaoCorretiva.getEficacia() == null || acaoCorretiva.getEficacia().trim().isEmpty()) {
            throw new RegraNegocioException("Eficacia da acao corretiva invalida");
        }
    }
}
