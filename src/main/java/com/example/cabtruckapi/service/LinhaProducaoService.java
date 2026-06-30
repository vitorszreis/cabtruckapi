package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.LinhaProducao;
import com.example.cabtruckapi.model.repository.LinhaProducaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LinhaProducaoService {

    private LinhaProducaoRepository repository;

    public LinhaProducaoService(LinhaProducaoRepository repository) {
        this.repository = repository;
    }

    public List<LinhaProducao> getLinhas() {
        return repository.findAll();
    }

    public Optional<LinhaProducao> getLinhaById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public LinhaProducao salvar(LinhaProducao linhaProducao) {
        validar(linhaProducao);
        return repository.save(linhaProducao);
    }

    @Transactional
    public void excluir(LinhaProducao linhaProducao) {
        Objects.requireNonNull(linhaProducao.getId());
        repository.delete(linhaProducao);
    }

    public double calcularDPU(LinhaProducao linhaProducao) {
        return linhaProducao.calcularDPU();
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
