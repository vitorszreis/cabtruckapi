package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.LinhaProducao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LinhaProducaoService {

    private final ConcurrentHashMap<Integer, LinhaProducao> bancoMemoria = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public List<LinhaProducao> getLinhas() {
        return new ArrayList<>(bancoMemoria.values());
    }

    public Optional<LinhaProducao> getLinhaById(Integer id) {
        return Optional.ofNullable(bancoMemoria.get(id));
    }

    public LinhaProducao salvar(LinhaProducao linhaProducao) {
        validar(linhaProducao);
        if (linhaProducao.getId() == null) {
            linhaProducao.setId(sequence.getAndIncrement());
        }
        bancoMemoria.put(linhaProducao.getId(), linhaProducao);
        return linhaProducao;
    }

    public void excluir(LinhaProducao linhaProducao) {
        if (linhaProducao.getId() == null) {
            throw new RegraNegocioException("Linha de producao sem id");
        }
        bancoMemoria.remove(linhaProducao.getId());
    }

    public void validar(LinhaProducao linhaProducao) {
        if (linhaProducao.getNome() == null || linhaProducao.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome da linha invalido");
        }
        if (linhaProducao.getTurno() == null || linhaProducao.getTurno().trim().isEmpty()) {
            throw new RegraNegocioException("Turno da linha invalido");
        }
        if (linhaProducao.getAtiva() == null) {
            throw new RegraNegocioException("Status da linha invalido");
        }
    }
}
