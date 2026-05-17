package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.Ingresso;
import br.com.softhouse.dende.dto.response.IngressoResponseDTO;

/**
 * Mapper para converter entre as classes de modelo (entidade) e as classes de DTO (Data Transfer Object).
 * Esta classe é responsável por transformar os dados entre as camadas de apresentação (DTOs) e a camada de negócio (entidades).
 */

// Mapper para converter entre Ingresso e IngressoDTO
public class IngressoMapper {

    private IngressoMapper() {}

    // Converte um objeto Ingresso (entidade) para um IngressoResponseDTO, incluindo informações do evento
    public static IngressoResponseDTO toDTO(Ingresso ingresso, Evento evento) {
        if (ingresso == null) return null;

        // Criar um novo objeto IngressoResponseDTO e preencher seus campos com os dados do ingresso e do evento
        IngressoResponseDTO dto = new IngressoResponseDTO();
        dto.setId(ingresso.getId());
        dto.setUsuarioId(ingresso.getUsuarioId());
        dto.setEventoId(ingresso.getEventoId());
        dto.setEventoNome(evento != null ? evento.getNome() : null);
        dto.setDataEvento(evento != null ? evento.getDataInicio() : null);
        dto.setLocal(evento != null ? evento.getLocal() : null);
        dto.setCodigo(ingresso.getCodigo());
        dto.setDataCompra(ingresso.getDataCompra());
        dto.setDataCompraFormatada(ingresso.getDataCompraFormatada());
        dto.setValorPago(ingresso.getValorPago());
        dto.setStatus(ingresso.getStatus());
        dto.setIngressoPrincipal(ingresso.getIngressoPrincipal());

        return dto;
    }

    // Converte os dados básicos para um objeto Ingresso (entidade)
    public static Ingresso createIngresso(Long usuarioId, Long eventoId, Double valorPago) {
        return new Ingresso(usuarioId, eventoId, valorPago);
    }

    // Cria um ingresso vinculado a outro evento, marcando-o como ingresso secundário
    public static Ingresso createIngressoVinculado(Long usuarioId, Long eventoId, Double valorPago) {
        Ingresso ingresso = new Ingresso(usuarioId, eventoId, valorPago);
        ingresso.setIngressoPrincipal(false);
        return ingresso;
    }
}