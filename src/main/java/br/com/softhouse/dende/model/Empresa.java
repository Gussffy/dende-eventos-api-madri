package br.com.softhouse.dende.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * MODELO DE EMPRESA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Empresa {

    // Atributos da empresa
    private Long id;
    private Long organizadorId;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
}

