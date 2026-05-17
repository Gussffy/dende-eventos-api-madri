package br.com.softhouse.dende.dto.response;

import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO de resposta de organizador.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizadorResponseDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String idade;
    private Sexo sexo;
    private String email;
    private Boolean ativo;

    // Dados opcionais de empresa
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
}
