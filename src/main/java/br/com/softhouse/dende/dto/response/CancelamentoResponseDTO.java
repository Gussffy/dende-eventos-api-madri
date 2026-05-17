package br.com.softhouse.dende.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO de resposta para o cancelamento de um ingresso
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoResponseDTO {
    private String mensagem;
    private Double valorPago;
    private Double valorReembolsado;
    private String codigoIngresso;
}
