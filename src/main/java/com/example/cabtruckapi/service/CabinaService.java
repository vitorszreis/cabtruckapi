package com.example.cabtruckapi.service;

import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Cabina;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CabinaService {

    private final ConcurrentHashMap<Integer, Cabina> bancoMemoria = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public List<Cabina> getCabinas() {
        return new ArrayList<>(bancoMemoria.values());
    }

    public Optional<Cabina> getCabinaById(Integer id) {
        return Optional.ofNullable(bancoMemoria.get(id));
    }

    public Cabina salvar(Cabina cabina) {
        validar(cabina);
        if (cabina.getId() == null) {
            cabina.setId(sequence.getAndIncrement());
        }
        bancoMemoria.put(cabina.getId(), cabina);
        return cabina;
    }

    public void excluir(Cabina cabina) {
        if (cabina.getId() == null) {
            throw new RegraNegocioException("Cabina sem id");
        }
        bancoMemoria.remove(cabina.getId());
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
