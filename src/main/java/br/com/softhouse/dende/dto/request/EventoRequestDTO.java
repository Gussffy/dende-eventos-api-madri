package br.com.softhouse.dende.dto.request;

import br.com.softhouse.dende.model.enums.ModalidadeEvento;
import br.com.softhouse.dende.model.enums.TipoEvento;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO de requisição de evento.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequestDTO {
    private String nome;
    private String pagina;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
    private TipoEvento tipoEvento;
    private Long eventoPrincipalId;
    private ModalidadeEvento modalidade;
    private Integer capacidadeMaxima;
    private String local;
    private Boolean ativo;
    private Double precoIngresso;
    private Boolean estornaCancelamento;
    private Double taxaEstorno;
}

