package com.example.cabtruckapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import com.example.cabtruckapi.model.entity.AcaoCorretiva;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcaoCorretivaDTO {
    private Integer id;
    private String descricao;
    private String eficacia;
    private LocalDate dataExecucao;

    private Integer falhaId;

    public static AcaoCorretivaDTO create(AcaoCorretiva a) {
        ModelMapper modelMapper = new ModelMapper();
        AcaoCorretivaDTO dto = modelMapper.map(a, AcaoCorretivaDTO.class);
        return dto;
    }
}
