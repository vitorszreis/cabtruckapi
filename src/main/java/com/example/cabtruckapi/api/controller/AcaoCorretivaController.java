package com.example.cabtruckapi.api.controller;

import com.example.cabtruckapi.api.dto.AcaoCorretivaDTO;
import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.AcaoCorretiva;
import com.example.cabtruckapi.model.entity.Falha;
import com.example.cabtruckapi.service.AcaoCorretivaService;
import com.example.cabtruckapi.service.FalhaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/acoes-corretivas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Ações Corretivas", description = "Gerenciamento de ações corretivas associadas a falhas")
public class AcaoCorretivaController {

    private final AcaoCorretivaService service;
    private final FalhaService falhaService;

    @Operation(summary = "Listar todas as ações corretivas")
    @GetMapping
    public ResponseEntity<List<AcaoCorretivaDTO>> get() {
        return ResponseEntity.ok(service.getAcoesCorretivas().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar ação corretiva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        Optional<AcaoCorretiva> acao = service.getAcaoCorretivaById(id);
        if (acao.isEmpty()) {
            return new ResponseEntity<>("Acao corretiva nao encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDTO(acao.get()));
    }

    @Operation(summary = "Criar nova ação corretiva")
    @PostMapping
    public ResponseEntity<?> post(@RequestBody AcaoCorretivaDTO dto) {
        try {
            return new ResponseEntity<>(toDTO(service.salvar(converter(dto))), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Atualizar ação corretiva")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @RequestBody AcaoCorretivaDTO dto) {
        if (service.getAcaoCorretivaById(id).isEmpty()) {
            return new ResponseEntity<>("Acao corretiva nao encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            AcaoCorretiva acao = converter(dto);
            acao.setId(id);
            return ResponseEntity.ok(toDTO(service.salvar(acao)));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Excluir ação corretiva")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
        Optional<AcaoCorretiva> acao = service.getAcaoCorretivaById(id);
        if (acao.isEmpty()) {
            return new ResponseEntity<>("Acao corretiva nao encontrada", HttpStatus.NOT_FOUND);
        }
        service.excluir(acao.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private AcaoCorretiva converter(AcaoCorretivaDTO dto) {
        AcaoCorretiva acao = new ModelMapper().map(dto, AcaoCorretiva.class);
        if (dto.getFalhaId() != null) {
            Falha falha = falhaService.getFalhaById(dto.getFalhaId())
                    .orElseThrow(() -> new RegraNegocioException("Falha informada nao existe"));
            acao.setFalha(falha);
        }
        return acao;
    }

    private AcaoCorretivaDTO toDTO(AcaoCorretiva acaoCorretiva) {
        AcaoCorretivaDTO dto = AcaoCorretivaDTO.create(acaoCorretiva);
        dto.setFalhaId(acaoCorretiva.getFalha() != null ? acaoCorretiva.getFalha().getId() : null);
        return dto;
    }
}
