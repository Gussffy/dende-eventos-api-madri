package br.com.softhouse.dende.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Classe DTO para receber os dados de solicitação de mudança de status do ingresso
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangeRequestDTO {
    private String senha;
    private Long eventoId;
}
