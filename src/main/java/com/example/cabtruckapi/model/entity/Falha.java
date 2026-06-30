package com.example.cabtruckapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
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
    private LocalDate dataRegistro;

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
        if (this.descricao == null || this.descricao.trim().isEmpty()) {
            throw new IllegalStateException("Descricao da falha e obrigatoria");
        }
        if (this.severidade == null || this.severidade.trim().isEmpty()) {
            throw new IllegalStateException("Severidade da falha e obrigatoria");
        }
        this.status = "ABERTA";
    }

    public void resolver() {
        if (!"ABERTA".equalsIgnoreCase(this.status)) {
            throw new IllegalStateException("Apenas falhas com status ABERTA podem ser resolvidas");
        }
        this.status = "RESOLVIDA";
    }
}
