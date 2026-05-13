package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import java.util.List;

import model.entity.TipoFalha;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoFalhaDTO {
    private Integer id;
    private String codigo;
    private String descricao;
    private String categoria;

    private List<Integer> falhaIds;

    public static TipoFalhaDTO create(TipoFalha t) {
        ModelMapper modelMapper = new ModelMapper();
        TipoFalhaDTO dto = modelMapper.map(t, TipoFalhaDTO.class);
        return dto;
    }
}
