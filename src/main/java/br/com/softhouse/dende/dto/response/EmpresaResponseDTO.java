package br.com.softhouse.dende.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * DTO de resposta de empresa.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmpresaResponseDTO {

    private Long id;
    private Long organizadorId;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
}
