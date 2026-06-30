package com.example.cabtruckapi.model.entity;

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
        if (this.nome == null || this.nome.trim().isEmpty()) {
            throw new IllegalStateException("Nome da linha de producao e obrigatorio");
        }
        if (this.turno == null || this.turno.trim().isEmpty()) {
            throw new IllegalStateException("Turno da linha de producao e obrigatorio");
        }
        this.ativa = true;
    }

    public double calcularDPU() {
        if (this.cabinas == null || this.cabinas.isEmpty()) {
            return 0.0;
        }
        long totalFalhas = this.cabinas.stream()
                .filter(c -> c.getFalhas() != null)
                .mapToLong(c -> c.getFalhas().size())
                .sum();
        return (double) totalFalhas / this.cabinas.size();
    }
}
