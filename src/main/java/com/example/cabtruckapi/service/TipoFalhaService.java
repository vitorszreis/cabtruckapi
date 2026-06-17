package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.TipoFalha;
import com.example.cabtruckapi.model.repository.TipoFalhaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TipoFalhaService {

    private TipoFalhaRepository repository;

    public TipoFalhaService(TipoFalhaRepository repository) {
        this.repository = repository;
    }

    public List<TipoFalha> getTiposFalha() {
        return repository.findAll();
    }

    public Optional<TipoFalha> getTipoFalhaById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public TipoFalha salvar(TipoFalha tipoFalha) {
        validar(tipoFalha);
        return repository.save(tipoFalha);
    }

    @Transactional
    public void excluir(TipoFalha tipoFalha) {
        Objects.requireNonNull(tipoFalha.getId());
        repository.delete(tipoFalha);
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
