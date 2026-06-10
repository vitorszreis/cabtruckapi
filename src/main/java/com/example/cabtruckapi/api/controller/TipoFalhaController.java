package com.example.cabtruckapi.api.controller;

import com.example.cabtruckapi.api.dto.TipoFalhaDTO;
import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.TipoFalha;
import com.example.cabtruckapi.service.TipoFalhaService;
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
@RequestMapping("/api/v1/tipos-falha")
@RequiredArgsConstructor
@CrossOrigin
public class TipoFalhaController {

    private final TipoFalhaService service;

    @GetMapping
    public ResponseEntity<List<TipoFalhaDTO>> get() {
        return ResponseEntity.ok(service.getTiposFalha().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        Optional<TipoFalha> tipoFalha = service.getTipoFalhaById(id);
        if (tipoFalha.isEmpty()) {
            return new ResponseEntity<>("Tipo de falha nao encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDTO(tipoFalha.get()));
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody TipoFalhaDTO dto) {
        try {
            return new ResponseEntity<>(toDTO(service.salvar(converter(dto))), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @RequestBody TipoFalhaDTO dto) {
        if (service.getTipoFalhaById(id).isEmpty()) {
            return new ResponseEntity<>("Tipo de falha nao encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            TipoFalha tipoFalha = converter(dto);
            tipoFalha.setId(id);
            return ResponseEntity.ok(toDTO(service.salvar(tipoFalha)));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
        Optional<TipoFalha> tipoFalha = service.getTipoFalhaById(id);
        if (tipoFalha.isEmpty()) {
            return new ResponseEntity<>("Tipo de falha nao encontrado", HttpStatus.NOT_FOUND);
        }
        service.excluir(tipoFalha.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private TipoFalha converter(TipoFalhaDTO dto) {
        return new ModelMapper().map(dto, TipoFalha.class);
    }

    private TipoFalhaDTO toDTO(TipoFalha tipoFalha) {
        TipoFalhaDTO dto = TipoFalhaDTO.create(tipoFalha);
        if (tipoFalha.getFalhas() != null) {
            dto.setFalhaIds(tipoFalha.getFalhas().stream().map(f -> f.getId()).collect(Collectors.toList()));
        }
        return dto;
    }
}
