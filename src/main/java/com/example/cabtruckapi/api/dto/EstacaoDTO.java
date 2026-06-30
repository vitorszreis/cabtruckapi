package com.example.cabtruckapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import java.util.List;

import com.example.cabtruckapi.model.entity.Estacao;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstacaoDTO {
    private Integer id;
    private String nome;
    private Integer ordem;
    private String descricao;

    private Integer linhaProducaoId;
    private List<Integer> falhaIds;

    public static EstacaoDTO create(Estacao e) {
        ModelMapper modelMapper = new ModelMapper();
        EstacaoDTO dto = modelMapper.map(e, EstacaoDTO.class);
        return dto;
    }
}
