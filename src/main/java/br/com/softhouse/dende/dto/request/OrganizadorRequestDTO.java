package br.com.softhouse.dende.dto.request;

import br.com.softhouse.dende.model.enums.Sexo;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO de requisição de organizador.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizadorRequestDTO {
    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private String email;
    private String senha;
    private Boolean ativo;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
}

