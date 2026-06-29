package com.example.cabtruckapi.api.controller;

import com.example.cabtruckapi.api.dto.LinhaProducaoDTO;
import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.LinhaProducao;
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
@RequestMapping("/api/v1/linhas-producao")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Linhas de Producao", description = "Gerenciamento de linhas de producao e indicadores")
public class LinhaProducaoController {

    private final LinhaProducaoService service;

    @GetMapping
    public ResponseEntity<List<LinhaProducaoDTO>> get() {
        List<LinhaProducaoDTO> linhas = service.getLinhas().stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(linhas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        Optional<LinhaProducao> linha = service.getLinhaById(id);
        if (linha.isEmpty()) {
            return new ResponseEntity<>("Linha de producao nao encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDTO(linha.get()));
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody LinhaProducaoDTO dto) {
        try {
            LinhaProducao linha = service.salvar(converter(dto));
            return new ResponseEntity<>(toDTO(linha), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @RequestBody LinhaProducaoDTO dto) {
        if (service.getLinhaById(id).isEmpty()) {
            return new ResponseEntity<>("Linha de producao nao encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            LinhaProducao linha = converter(dto);
            linha.setId(id);
            return ResponseEntity.ok(toDTO(service.salvar(linha)));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
        Optional<LinhaProducao> linha = service.getLinhaById(id);
        if (linha.isEmpty()) {
            return new ResponseEntity<>("Linha de producao nao encontrada", HttpStatus.NOT_FOUND);
        }
        service.excluir(linha.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Calcular indicador DPU da linha (RF12)")
    @GetMapping("/{id}/dpu")
    public ResponseEntity<?> calcularDPU(@PathVariable("id") Integer id) {
        Optional<LinhaProducao> linha = service.getLinhaById(id);
        if (linha.isEmpty()) {
            return new ResponseEntity<>("Linha de producao nao encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(service.calcularDPU(linha.get()));
    }

    private LinhaProducao converter(LinhaProducaoDTO dto) {
        return new ModelMapper().map(dto, LinhaProducao.class);
    }

    private LinhaProducaoDTO toDTO(LinhaProducao linhaProducao) {
        LinhaProducaoDTO dto = LinhaProducaoDTO.create(linhaProducao);
        if (linhaProducao.getCabinas() != null) {
            dto.setCabinaIds(linhaProducao.getCabinas().stream().map(c -> c.getId()).collect(Collectors.toList()));
        }
        if (linhaProducao.getEstacoes() != null) {
            dto.setEstacaoIds(linhaProducao.getEstacoes().stream().map(e -> e.getId()).collect(Collectors.toList()));
        }
        if (linhaProducao.getRelatorios() != null) {
            dto.setRelatorioIds(linhaProducao.getRelatorios().stream().map(r -> r.getId()).collect(Collectors.toList()));
        }
        return dto;
    }
}
