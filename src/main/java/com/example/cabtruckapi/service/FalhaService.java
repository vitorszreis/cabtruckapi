package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Falha;
import com.example.cabtruckapi.model.repository.FalhaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FalhaService {

    private FalhaRepository repository;

    public FalhaService(FalhaRepository repository) {
        this.repository = repository;
    }

    public List<Falha> getFalhas() {
        return repository.findAll();
    }

    public Optional<Falha> getFalhaById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Falha salvar(Falha falha) {
        validar(falha);
        return repository.save(falha);
    }

    @Transactional
    public Falha registrar(Falha falha) {
        falha.setStatus("ABERTA");
        falha.setDataRegistro(LocalDate.now());
        validar(falha);
        return repository.save(falha);
    }

    @Transactional
    public Falha resolver(Falha falha) {
        if (!"ABERTA".equalsIgnoreCase(falha.getStatus())) {
            throw new RegraNegocioException("Apenas falhas com status ABERTA podem ser resolvidas");
        }
        if (falha.getAcoesCorrativas() == null || falha.getAcoesCorrativas().isEmpty()) {
            throw new RegraNegocioException("Falha precisa de ao menos uma acao corretiva para ser resolvida");
        }
        falha.setStatus("RESOLVIDA");
        return repository.save(falha);
    }

    public List<Falha> getFalhasByCabina(Integer cabinaId) {
        return repository.findByCabinaId(cabinaId);
    }

    public List<Falha> getFalhasByEstacao(Integer estacaoId, LocalDate inicio, LocalDate fim) {
        return repository.findByEstacaoIdAndDataRegistroBetweenOrderByDataRegistroAsc(estacaoId, inicio, fim);
    }

    @Transactional
    public void excluir(Falha falha) {
        Objects.requireNonNull(falha.getId());
        repository.delete(falha);
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
