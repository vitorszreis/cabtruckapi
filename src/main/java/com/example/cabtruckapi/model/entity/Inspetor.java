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
public class Inspetor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String matricula;
    private String turno;
    private Boolean ativo;

    @JsonIgnore
    @OneToMany(mappedBy = "inspetor")
    private List<Falha> falhas;

    public void cadastrar() {
        if (this.nome == null || this.nome.trim().isEmpty()) {
            throw new IllegalStateException("Nome do inspetor e obrigatorio");
        }
        if (this.matricula == null || this.matricula.trim().isEmpty()) {
            throw new IllegalStateException("Matricula do inspetor e obrigatoria");
        }
        if (this.turno == null || this.turno.trim().isEmpty()) {
            throw new IllegalStateException("Turno do inspetor e obrigatorio");
        }
        this.ativo = true;
    }
}
