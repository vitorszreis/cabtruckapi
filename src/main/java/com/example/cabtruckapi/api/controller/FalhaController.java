package com.example.cabtruckapi.api.controller;

import com.example.cabtruckapi.api.dto.FalhaDTO;
import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.model.entity.Cabina;
import com.example.cabtruckapi.model.entity.Estacao;
import com.example.cabtruckapi.model.entity.Falha;
import com.example.cabtruckapi.model.entity.Inspetor;
import com.example.cabtruckapi.model.entity.TipoFalha;
import com.example.cabtruckapi.service.CabinaService;
import com.example.cabtruckapi.service.EstacaoService;
import com.example.cabtruckapi.service.FalhaService;
import com.example.cabtruckapi.service.InspetorService;
import com.example.cabtruckapi.service.TipoFalhaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/falhas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Falhas", description = "Registro e acompanhamento de falhas")
public class FalhaController {

    private final FalhaService service;
    private final CabinaService cabinaService;
    private final EstacaoService estacaoService;
    private final TipoFalhaService tipoFalhaService;
    private final InspetorService inspetorService;

    @Operation(summary = "Listar todas as falhas")
    @GetMapping
    public ResponseEntity<List<FalhaDTO>> get() {
        return ResponseEntity.ok(service.getFalhas().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar falha por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {
        Optional<Falha> falha = service.getFalhaById(id);
        if (falha.isEmpty()) {
            return new ResponseEntity<>("Falha nao encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDTO(falha.get()));
    }

    @Operation(summary = "Consultar falhas por cabina (RF09)")
    @GetMapping("/cabina/{cabinaId}")
    public ResponseEntity<List<FalhaDTO>> getPorCabina(@PathVariable("cabinaId") Integer cabinaId) {
        return ResponseEntity.ok(
                service.getFalhasByCabina(cabinaId).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Consultar falhas por estacao em um periodo (RF10)")
    @GetMapping("/estacao/{estacaoId}")
    public ResponseEntity<List<FalhaDTO>> getPorEstacao(
            @PathVariable("estacaoId") Integer estacaoId,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(
                service.getFalhasByEstacao(estacaoId, inicio, fim).stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Registrar falha em cabina (RF06)")
    @PostMapping
    public ResponseEntity<?> post(@RequestBody FalhaDTO dto) {
        try {
            return new ResponseEntity<>(toDTO(service.registrar(converter(dto))), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Resolver falha (RF08)")
    @PutMapping("/{id}/resolver")
    public ResponseEntity<?> resolver(@PathVariable("id") Integer id) {
        Optional<Falha> falha = service.getFalhaById(id);
        if (falha.isEmpty()) {
            return new ResponseEntity<>("Falha nao encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            return ResponseEntity.ok(toDTO(service.resolver(falha.get())));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Atualizar falha")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Integer id, @RequestBody FalhaDTO dto) {
        if (service.getFalhaById(id).isEmpty()) {
            return new ResponseEntity<>("Falha nao encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Falha falha = converter(dto);
            falha.setId(id);
            return ResponseEntity.ok(toDTO(service.salvar(falha)));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Excluir falha")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Integer id) {
        Optional<Falha> falha = service.getFalhaById(id);
        if (falha.isEmpty()) {
            return new ResponseEntity<>("Falha nao encontrada", HttpStatus.NOT_FOUND);
        }
        service.excluir(falha.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Falha converter(FalhaDTO dto) {
        Falha falha = new ModelMapper().map(dto, Falha.class);

        if (dto.getCabinaId() != null) {
            Cabina cabina = cabinaService.getCabinaById(dto.getCabinaId())
                    .orElseThrow(() -> new RegraNegocioException("Cabina informada nao existe"));
            falha.setCabina(cabina);
        }
        if (dto.getEstacaoId() != null) {
            Estacao estacao = estacaoService.getEstacaoById(dto.getEstacaoId())
                    .orElseThrow(() -> new RegraNegocioException("Estacao informada nao existe"));
            falha.setEstacao(estacao);
        }
        if (dto.getTipoFalhaId() != null) {
            TipoFalha tipoFalha = tipoFalhaService.getTipoFalhaById(dto.getTipoFalhaId())
                    .orElseThrow(() -> new RegraNegocioException("Tipo de falha informado nao existe"));
            falha.setTipoFalha(tipoFalha);
        }
        if (dto.getInspetorId() != null) {
            Inspetor inspetor = inspetorService.getInspetorById(dto.getInspetorId())
                    .orElseThrow(() -> new RegraNegocioException("Inspetor informado nao existe"));
            falha.setInspetor(inspetor);
        }

        return falha;
    }

    private FalhaDTO toDTO(Falha falha) {
        FalhaDTO dto = FalhaDTO.create(falha);
        dto.setCabinaId(falha.getCabina() != null ? falha.getCabina().getId() : null);
        dto.setEstacaoId(falha.getEstacao() != null ? falha.getEstacao().getId() : null);
        dto.setTipoFalhaId(falha.getTipoFalha() != null ? falha.getTipoFalha().getId() : null);
        dto.setInspetorId(falha.getInspetor() != null ? falha.getInspetor().getId() : null);
        if (falha.getAcoesCorrativas() != null) {
            dto.setAcaoCorretivaIds(falha.getAcoesCorrativas().stream().map(a -> a.getId()).collect(Collectors.toList()));
        }
        return dto;
    }
}
