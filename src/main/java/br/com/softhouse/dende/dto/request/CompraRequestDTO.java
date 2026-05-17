package br.com.softhouse.dende.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para receber dados de uma requisição de compra de ingresso
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraRequestDTO {
    private String usuarioEmail;
    private Double totalPago;
}
