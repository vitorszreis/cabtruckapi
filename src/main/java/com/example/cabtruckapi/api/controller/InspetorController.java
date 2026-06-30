package com.example.cabtruckapi.api.controller;

import com.example.cabtruckapi.api.dto.InspetorDTO;
import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Inspetor;
import com.example.cabtruckapi.service.InspetorService;
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
@RequestMapping("/api/v1/inspetores")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Inspetores", description = "Gerenciamento de inspetores")
public class InspetorController {

    private final InspetorService service;

    @Operation(summary = "Listar todos os inspetores")
    @GetMapping
    public ResponseEntity<List<InspetorDTO>> get() {
        return ResponseEntity.ok(service.getInspetores().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar inspetor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        Optional<Inspetor> inspetor = service.getInspetorById(id);
        if (inspetor.isEmpty()) {
            return new ResponseEntity<>("Inspetor nao encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDTO(inspetor.get()));
    }

    @Operation(summary = "Cadastrar novo inspetor")
    @PostMapping
    public ResponseEntity<?> post(@RequestBody InspetorDTO dto) {
        try {
            return new ResponseEntity<>(toDTO(service.salvar(converter(dto))), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Atualizar inspetor")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @RequestBody InspetorDTO dto) {
        if (service.getInspetorById(id).isEmpty()) {
            return new ResponseEntity<>("Inspetor nao encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Inspetor inspetor = converter(dto);
            inspetor.setId(id);
            return ResponseEntity.ok(toDTO(service.salvar(inspetor)));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Excluir inspetor")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
        Optional<Inspetor> inspetor = service.getInspetorById(id);
        if (inspetor.isEmpty()) {
            return new ResponseEntity<>("Inspetor nao encontrado", HttpStatus.NOT_FOUND);
        }
        service.excluir(inspetor.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Inspetor converter(InspetorDTO dto) {
        return new ModelMapper().map(dto, Inspetor.class);
    }

    private InspetorDTO toDTO(Inspetor inspetor) {
        InspetorDTO dto = InspetorDTO.create(inspetor);
        if (inspetor.getFalhas() != null) {
            dto.setFalhaIds(inspetor.getFalhas().stream().map(f -> f.getId()).collect(Collectors.toList()));
        }
        return dto;
    }
}
