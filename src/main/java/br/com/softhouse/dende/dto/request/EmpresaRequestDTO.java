package br.com.softhouse.dende.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO de requisição de empresa.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequestDTO {

    private Long id;
    private Long organizadorId;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;

    // Mantém um construtor compatível com a versão anterior usada pelos mappers
    public EmpresaRequestDTO(Long organizadorId, String cnpj, String razaoSocial, String nomeFantasia) {
        this.organizadorId = organizadorId;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
    }
}

