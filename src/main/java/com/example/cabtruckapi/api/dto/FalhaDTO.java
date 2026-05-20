package com.example.cabtruckapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import java.util.List;

import com.example.cabtruckapi.model.entity.Falha;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FalhaDTO {
    private Integer id;
    private String descricao;
    private String severidade;
    private String status;

    private Integer cabinaId;
    private Integer estacaoId;
    private Integer tipoFalhaId;
    private Integer inspetorId;

    private List<Integer> acaoCorretivaIds;

    public static FalhaDTO create(Falha f) {
        ModelMapper modelMapper = new ModelMapper();
        FalhaDTO dto = modelMapper.map(f, FalhaDTO.class);
        return dto;
    }
}
