package com.example.cabtruckapi.api.controller;

import com.example.cabtruckapi.api.dto.EstacaoDTO;
import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Estacao;
import com.example.cabtruckapi.model.entity.LinhaProducao;
import com.example.cabtruckapi.service.EstacaoService;
import com.example.cabtruckapi.service.LinhaProducaoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/estacoes")
@RequiredArgsConstructor
@CrossOrigin
public class EstacaoController {

    private final EstacaoService service;
    private final LinhaProducaoService linhaProducaoService;

    @GetMapping
    public ResponseEntity<List<EstacaoDTO>> get() {
        return ResponseEntity.ok(service.getEstacoes().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        Optional<Estacao> estacao = service.getEstacaoById(id);
        if (estacao.isEmpty()) {
            return new ResponseEntity<>("Estacao nao encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDTO(estacao.get()));
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody EstacaoDTO dto) {
        try {
            return new ResponseEntity<>(toDTO(service.salvar(converter(dto))), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @RequestBody EstacaoDTO dto) {
        if (service.getEstacaoById(id).isEmpty()) {
            return new ResponseEntity<>("Estacao nao encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Estacao estacao = converter(dto);
            estacao.setId(id);
            return ResponseEntity.ok(toDTO(service.salvar(estacao)));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
        Optional<Estacao> estacao = service.getEstacaoById(id);
        if (estacao.isEmpty()) {
            return new ResponseEntity<>("Estacao nao encontrada", HttpStatus.NOT_FOUND);
        }
        service.excluir(estacao.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Estacao converter(EstacaoDTO dto) {
        Estacao estacao = new ModelMapper().map(dto, Estacao.class);
        if (dto.getLinhaProducaoId() != null) {
            LinhaProducao linhaProducao = linhaProducaoService.getLinhaById(dto.getLinhaProducaoId())
                    .orElseThrow(() -> new RegraNegocioException("Linha de producao informada nao existe"));
            estacao.setLinhaProducao(linhaProducao);
        } else {
            estacao.setLinhaProducao(null);
        }
        return estacao;
    }

    private EstacaoDTO toDTO(Estacao estacao) {
        EstacaoDTO dto = EstacaoDTO.create(estacao);
        dto.setLinhaProducaoId(estacao.getLinhaProducao() != null ? estacao.getLinhaProducao().getId() : null);
        if (estacao.getFalhas() != null) {
            dto.setFalhaIds(estacao.getFalhas().stream().map(f -> f.getId()).collect(Collectors.toList()));
        }
        return dto;
    }
}
