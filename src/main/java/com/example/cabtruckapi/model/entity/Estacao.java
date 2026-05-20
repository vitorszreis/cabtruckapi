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
        // Implementar lógica de cadastro
    }
}
