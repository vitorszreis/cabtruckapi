package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Falha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descricao;
    private String severidade;
    private String status;

    @ManyToOne
    private Cabina cabina;

    @ManyToOne
    private Estacao estacao;

    @ManyToOne
    private TipoFalha tipoFalha;

    @ManyToOne
    private Inspetor inspetor;

    @JsonIgnore
    @OneToMany(mappedBy = "falha")
    private List<AcaoCorretiva> acoesCorrativas;

    public void registrar() {
        // Implementar lógica de registro
    }

    public void resolver() {
        // Implementar lógica de resolução
    }
}
