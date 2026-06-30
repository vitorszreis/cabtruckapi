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
public class TipoFalha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codigo;
    private String descricao;
    private String categoria;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoFalha")
    private List<Falha> falhas;

    public void cadastrar() {
        if (this.codigo == null || this.codigo.trim().isEmpty()) {
            throw new IllegalStateException("Codigo do tipo de falha e obrigatorio");
        }
        if (this.descricao == null || this.descricao.trim().isEmpty()) {
            throw new IllegalStateException("Descricao do tipo de falha e obrigatoria");
        }
        if (this.categoria == null || this.categoria.trim().isEmpty()) {
            throw new IllegalStateException("Categoria do tipo de falha e obrigatoria");
        }
    }
}
