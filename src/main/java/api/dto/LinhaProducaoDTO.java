package com.example.cabtruckapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import model.entity.Relatorio;
import org.modelmapper.ModelMapper;

import java.util.List;

import model.entity.LinhaProducao;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinhaProducaoDTO {
    private Integer id;
    private String nome;
    private String turno;
    private Boolean ativa;

    // referências por id
    private List<Integer> cabinaIds;
    private List<Integer> estacaoIds;
    private List<Integer> relatorioIds;

    public static LinhaProducaoDTO create(LinhaProducao lp) {
        ModelMapper modelMapper = new ModelMapper();
        LinhaProducaoDTO dto = modelMapper.map(lp, LinhaProducaoDTO.class);
        return dto;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatorioDTO {
        private Integer id;
        private String tipo;

        private Integer linhaProducaoId;

        public static RelatorioDTO create(Relatorio r) {
            ModelMapper modelMapper = new ModelMapper();
            RelatorioDTO dto = modelMapper.map(r, RelatorioDTO.class);
            return dto;
        }
    }
}
