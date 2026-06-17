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
        if (!"DISPONIVEL".equalsIgnoreCase(this.status)) {
            throw new IllegalStateException("Cabina precisa estar DISPONIVEL para iniciar producao");
        }
        this.status = "EM_PRODUCAO";
    }

    public void finalizar() {
        if (!"EM_PRODUCAO".equalsIgnoreCase(this.status)) {
            throw new IllegalStateException("Cabina precisa estar EM_PRODUCAO para ser finalizada");
        }
        this.status = "FINALIZADA";
    }

    public List<Falha> consultarFalhas() {
        return this.falhas;
    }
}
