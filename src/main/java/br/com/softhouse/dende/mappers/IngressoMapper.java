package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.IngressoDTO;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.Ingresso;

/**
 * Mapper para converter entre as classes de modelo (entidade) e as classes de DTO (Data Transfer Object).
 * Esta classe é responsável por transformar os dados entre as camadas de apresentação (DTOs) e a camada de negócio (entidades).
 */

// Mapper para converter entre Ingresso e IngressoDTO
public class IngressoMapper {

    private IngressoMapper() {}

    // Converte um objeto Ingresso (entidade) para um IngressoDTO, incluindo informações do evento
    public static IngressoDTO toDTO(Ingresso ingresso, Evento evento) {
        if (ingresso == null) return null;

        // Criar um novo objeto IngressoDTO e preencher seus campos com os dados do ingresso e do evento
        IngressoDTO dto = new IngressoDTO();
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
        dto.setEventoVinculadoId(ingresso.getEventoVinculadoId());

        return dto;
    }

    // Converte um IngressoDTO para um objeto Ingresso (entidade), preenchendo apenas os campos básicos
    public static Ingresso createIngresso(Long usuarioId, Long eventoId, Double valorPago) {
        return new Ingresso(usuarioId, eventoId, valorPago);
    }

    // Cria um ingresso vinculado a outro evento, marcando-o como ingresso secundário
    public static Ingresso createIngressoVinculado(Long usuarioId, Long eventoId, Long eventoVinculadoId, Double valorPago) {
        Ingresso ingresso = new Ingresso(usuarioId, eventoId, valorPago);
        ingresso.setEventoVinculadoId(eventoVinculadoId);
        ingresso.setIngressoPrincipal(false);
        return ingresso;
    }
}