package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tipo;

    @ManyToOne
    private LinhaProducao linhaProducao;

    public void gerar() {
        // Implementar lógica de geração
    }

    public void exportar() {
        // Implementar lógica de exportação
    }
}
