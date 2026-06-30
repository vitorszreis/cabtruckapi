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
public class Estacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private Integer ordem;
    private String descricao;

    @ManyToOne
    private LinhaProducao linhaProducao;

    @JsonIgnore
    @OneToMany(mappedBy = "estacao")
    private List<Falha> falhas;

    public void cadastrar() {
        if (this.nome == null || this.nome.trim().isEmpty()) {
            throw new IllegalStateException("Nome da estacao e obrigatorio");
        }
        if (this.ordem == null || this.ordem < 1) {
            throw new IllegalStateException("Ordem da estacao deve ser maior que zero");
        }
        if (this.linhaProducao == null) {
            throw new IllegalStateException("Estacao deve estar vinculada a uma linha de producao");
        }
    }
}
