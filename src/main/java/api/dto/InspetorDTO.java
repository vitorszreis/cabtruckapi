package com.example.cabtruckapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import java.util.List;

import model.entity.Inspetor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspetorDTO {
    private Integer id;
    private String nome;
    private String matricula;
    private String turno;
    private Boolean ativo;

    private List<Integer> falhaIds;

    public static InspetorDTO create(Inspetor i) {
        ModelMapper modelMapper = new ModelMapper();
        InspetorDTO dto = modelMapper.map(i, InspetorDTO.class);
        return dto;
    }
}
