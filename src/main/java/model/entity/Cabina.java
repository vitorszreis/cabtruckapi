package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cabina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String numeroSerie;
    private String modelo;
    private String status;

    @ManyToOne
    private LinhaProducao linhaProducao;

    @JsonIgnore
    @OneToMany(mappedBy = "cabina")
    private List<Falha> falhas;

    public void iniciarProducao() {
        // Implementar lógica de início de produção
    }

    public void finalizar() {
        // Implementar lógica de finalização
    }

    public List<Falha> consultarFalhas() {
        // Implementar lógica de consulta de falhas
        return this.falhas;
    }
}
