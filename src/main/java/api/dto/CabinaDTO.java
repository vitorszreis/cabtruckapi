package com.example.cabtruckapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import java.util.List;

import model.entity.Cabina;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CabinaDTO {
    private Integer id;
    private String numeroSerie;
    private String modelo;
    private String status;

    private Integer linhaProducaoId;
    private List<Integer> falhaIds;

    public static CabinaDTO create(Cabina c) {
        ModelMapper modelMapper = new ModelMapper();
        CabinaDTO dto = modelMapper.map(c, CabinaDTO.class);
        return dto;
    }
}
