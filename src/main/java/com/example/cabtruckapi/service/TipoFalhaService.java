package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.TipoFalha;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TipoFalhaService {

    private final ConcurrentHashMap<Integer, TipoFalha> bancoMemoria = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public List<TipoFalha> getTiposFalha() {
        return new ArrayList<>(bancoMemoria.values());
    }

    public Optional<TipoFalha> getTipoFalhaById(Integer id) {
        return Optional.ofNullable(bancoMemoria.get(id));
    }

    public TipoFalha salvar(TipoFalha tipoFalha) {
        validar(tipoFalha);
        if (tipoFalha.getId() == null) {
            tipoFalha.setId(sequence.getAndIncrement());
        }
        bancoMemoria.put(tipoFalha.getId(), tipoFalha);
        return tipoFalha;
    }

    public void excluir(TipoFalha tipoFalha) {
        if (tipoFalha.getId() == null) {
            throw new RegraNegocioException("Tipo de falha sem id");
        }
        bancoMemoria.remove(tipoFalha.getId());
    }

    public void validar(TipoFalha tipoFalha) {
        if (tipoFalha.getCodigo() == null || tipoFalha.getCodigo().trim().isEmpty()) {
            throw new RegraNegocioException("Codigo do tipo de falha invalido");
        }
        if (tipoFalha.getDescricao() == null || tipoFalha.getDescricao().trim().isEmpty()) {
            throw new RegraNegocioException("Descricao do tipo de falha invalida");
        }
        if (tipoFalha.getCategoria() == null || tipoFalha.getCategoria().trim().isEmpty()) {
            throw new RegraNegocioException("Categoria do tipo de falha invalida");
        }
    }
}
