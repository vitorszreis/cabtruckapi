package com.example.cabtruckapi.api.controller;

import com.example.cabtruckapi.api.dto.CabinaDTO;
import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Cabina;
import com.example.cabtruckapi.model.entity.LinhaProducao;
import com.example.cabtruckapi.service.CabinaService;
import com.example.cabtruckapi.service.LinhaProducaoService;
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
@RequestMapping("/api/v1/cabinas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Cabinas", description = "Gerenciamento de cabinas de caminhão")
public class CabinaController {

    private final CabinaService service;
    private final LinhaProducaoService linhaProducaoService;

    @Operation(summary = "Listar todas as cabinas")
    @GetMapping
    public ResponseEntity<List<CabinaDTO>> get() {
        return ResponseEntity.ok(service.getCabinas().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar cabina por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        Optional<Cabina> cabina = service.getCabinaById(id);
        if (cabina.isEmpty()) {
            return new ResponseEntity<>("Cabina nao encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDTO(cabina.get()));
    }

    @Operation(summary = "Criar nova cabina")
    @PostMapping
    public ResponseEntity<?> post(@RequestBody CabinaDTO dto) {
        try {
            return new ResponseEntity<>(toDTO(service.salvar(converter(dto))), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Atualizar cabina")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @RequestBody CabinaDTO dto) {
        if (service.getCabinaById(id).isEmpty()) {
            return new ResponseEntity<>("Cabina nao encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Cabina cabina = converter(dto);
            cabina.setId(id);
            return ResponseEntity.ok(toDTO(service.salvar(cabina)));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Excluir cabina")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
        Optional<Cabina> cabina = service.getCabinaById(id);
        if (cabina.isEmpty()) {
            return new ResponseEntity<>("Cabina nao encontrada", HttpStatus.NOT_FOUND);
        }
        service.excluir(cabina.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Cabina converter(CabinaDTO dto) {
        Cabina cabina = new ModelMapper().map(dto, Cabina.class);
        if (dto.getLinhaProducaoId() != null) {
            LinhaProducao linhaProducao = linhaProducaoService.getLinhaById(dto.getLinhaProducaoId())
                    .orElseThrow(() -> new RegraNegocioException("Linha de producao informada nao existe"));
            cabina.setLinhaProducao(linhaProducao);
        } else {
            cabina.setLinhaProducao(null);
        }
        return cabina;
    }

    private CabinaDTO toDTO(Cabina cabina) {
        CabinaDTO dto = CabinaDTO.create(cabina);
        dto.setLinhaProducaoId(cabina.getLinhaProducao() != null ? cabina.getLinhaProducao().getId() : null);
        if (cabina.getFalhas() != null) {
            dto.setFalhaIds(cabina.getFalhas().stream().map(f -> f.getId()).collect(Collectors.toList()));
        }
        return dto;
    }
}
