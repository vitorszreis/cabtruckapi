package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Cabina;
import com.example.cabtruckapi.model.repository.CabinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CabinaService {

    private CabinaRepository repository;

    public CabinaService(CabinaRepository repository) {
        this.repository = repository;
    }

    public List<Cabina> getCabinas() {
        return repository.findAll();
    }

    public Optional<Cabina> getCabinaById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Cabina salvar(Cabina cabina) {
        validar(cabina);
        return repository.save(cabina);
    }

    // RF05 - Iniciar producao de cabina
    @Transactional
    public Cabina iniciarProducao(Cabina cabina) {
        cabina.setStatus("EM_PRODUCAO");
        cabina.setDataInicio(LocalDate.now());
        validar(cabina);
        return repository.save(cabina);
    }

    // RF11 - Finalizar cabina (somente sem falhas em aberto)
    @Transactional
    public Cabina finalizar(Cabina cabina) {
        if (!"EM_PRODUCAO".equalsIgnoreCase(cabina.getStatus())) {
            throw new RegraNegocioException("Cabina precisa estar EM_PRODUCAO para ser finalizada");
        }
        boolean temFalhaAberta = cabina.getFalhas() != null && cabina.getFalhas().stream()
                .anyMatch(f -> "ABERTA".equalsIgnoreCase(f.getStatus()));
        if (temFalhaAberta) {
            throw new RegraNegocioException("Cabina possui falhas em aberto e nao pode ser finalizada");
        }
        cabina.setStatus("FINALIZADA");
        return repository.save(cabina);
    }

    @Transactional
    public void excluir(Cabina cabina) {
        Objects.requireNonNull(cabina.getId());
        repository.delete(cabina);
    }

    public void validar(Cabina cabina) {
        if (cabina.getNumeroSerie() == null || cabina.getNumeroSerie().trim().isEmpty()) {
            throw new RegraNegocioException("Numero de serie invalido");
        }
        if (cabina.getModelo() == null || cabina.getModelo().trim().isEmpty()) {
            throw new RegraNegocioException("Modelo da cabina invalido");
        }
        if (cabina.getStatus() == null || cabina.getStatus().trim().isEmpty()) {
            throw new RegraNegocioException("Status da cabina invalido");
        }
    }
}
