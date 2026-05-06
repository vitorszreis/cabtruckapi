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
        // Implementar lógica de cadastro
    }
}
