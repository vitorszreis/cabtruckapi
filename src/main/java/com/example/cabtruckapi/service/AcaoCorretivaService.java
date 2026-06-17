package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.AcaoCorretiva;
import com.example.cabtruckapi.model.repository.AcaoCorretivaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AcaoCorretivaService {

    private AcaoCorretivaRepository repository;

    public AcaoCorretivaService(AcaoCorretivaRepository repository) {
        this.repository = repository;
    }

    public List<AcaoCorretiva> getAcoesCorretivas() {
        return repository.findAll();
    }

    public Optional<AcaoCorretiva> getAcaoCorretivaById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public AcaoCorretiva salvar(AcaoCorretiva acaoCorretiva) {
        validar(acaoCorretiva);
        return repository.save(acaoCorretiva);
    }

    @Transactional
    public void excluir(AcaoCorretiva acaoCorretiva) {
        Objects.requireNonNull(acaoCorretiva.getId());
        repository.delete(acaoCorretiva);
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
