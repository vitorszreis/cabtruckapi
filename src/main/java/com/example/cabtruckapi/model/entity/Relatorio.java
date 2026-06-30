package com.example.cabtruckapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tipo;
    private LocalDateTime dataGeracao;

    @ManyToOne
    private LinhaProducao linhaProducao;

    public void gerar() {
        if (this.tipo == null || this.tipo.trim().isEmpty()) {
            throw new IllegalStateException("Tipo do relatorio e obrigatorio");
        }
        if (this.linhaProducao == null) {
            throw new IllegalStateException("Relatorio deve estar vinculado a uma linha de producao");
        }
        this.dataGeracao = LocalDateTime.now();
    }

    public void exportar() {
        if (this.dataGeracao == null) {
            throw new IllegalStateException("Relatorio precisa ser gerado antes de ser exportado");
        }
    }
}
