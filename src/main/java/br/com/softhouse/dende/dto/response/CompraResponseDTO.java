package br.com.softhouse.dende.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO de resposta para a compra de ingressos, contendo informações sobre o resultado da compra
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDTO {
    private String mensagem;
    private List<String> codigosIngressos;
    private Double valorTotal;
    private String status;
}
