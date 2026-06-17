package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Estacao;
import com.example.cabtruckapi.model.repository.EstacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EstacaoService {

    private EstacaoRepository repository;

    public EstacaoService(EstacaoRepository repository) {
        this.repository = repository;
    }

    public List<Estacao> getEstacoes() {
        return repository.findAll();
    }

    public Optional<Estacao> getEstacaoById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Estacao salvar(Estacao estacao) {
        validar(estacao);
        return repository.save(estacao);
    }

    @Transactional
    public void excluir(Estacao estacao) {
        Objects.requireNonNull(estacao.getId());
        repository.delete(estacao);
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
