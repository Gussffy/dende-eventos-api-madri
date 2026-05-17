package br.com.softhouse.dende.model;

import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;
import java.time.Period;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Classe de modelo para representar um usuário do sistema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    // Atributos do usuário
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String senha;
    private Boolean ativo = true; // Por padrão, o usuário é criado como ativo

    //Metodo para calcular a idade do usuário
    public String getIdade() {
        if (dataNascimento == null) return "";
        Period periodo = Period.between(dataNascimento, LocalDate.now());
        return periodo.getYears() + " anos, " + periodo.getMonths() + " meses, " + periodo.getDays() + " dias";
    }
}