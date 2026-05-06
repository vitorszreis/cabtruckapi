package com.example.cabtruckapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import model.entity.Relatorio;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioDTO {
    private Integer id;
    private String tipo;

    private Integer linhaProducaoId;

    public static RelatorioDTO create(Relatorio r) {
        ModelMapper modelMapper = new ModelMapper();
        RelatorioDTO dto = modelMapper.map(r, RelatorioDTO.class);
        return dto;
    }
}
