package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;

import model.entity.AcaoCorretiva;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcaoCorretivaDTO {
    private Integer id;
    private String descricao;
    private String eficacia;

    private Integer falhaId;

    public static AcaoCorretivaDTO create(AcaoCorretiva a) {
        ModelMapper modelMapper = new ModelMapper();
        AcaoCorretivaDTO dto = modelMapper.map(a, AcaoCorretivaDTO.class);
        return dto;
    }
}
