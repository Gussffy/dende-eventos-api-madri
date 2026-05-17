package br.com.softhouse.dende.model;

import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;
import java.time.Period;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * MODELO DE ORGANIZADOR
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organizador {

    // Atributos do organizador
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String senha;
    private Boolean ativo = true;
    private Empresa empresa; // Relacionamento com Empresa (0..1)

    /**
     * Verifica se o organizador possui uma empresa vinculada
     */
    public boolean temEmpresa() {
        return empresa != null;
    }

    // Metodo para calcular a idade do organizador
    public String getIdade() {
        if (dataNascimento == null) return "";
        Period periodo = Period.between(dataNascimento, LocalDate.now());
        return periodo.getYears() + " anos, " + periodo.getMonths() + " meses, " + periodo.getDays() + " dias";
    }
}