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
public class LinhaProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String turno;
    private Boolean ativa;

    @JsonIgnore
    @OneToMany(mappedBy = "linhaProducao")
    private List<Cabina> cabinas;

    @JsonIgnore
    @OneToMany(mappedBy = "linhaProducao")
    private List<Estacao> estacoes;

    @JsonIgnore
    @OneToMany(mappedBy = "linhaProducao")
    private List<Relatorio> relatorios;

    public void cadastrar() {
        // Implementar lógica de cadastro
    }

    public double calcularDPU() {
        // Implementar lógica de cálculo de DPU
        return 0.0;
    }
}
