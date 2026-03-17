package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.IngressoDTO;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.Ingresso;

public class IngressoMapper {

    private IngressoMapper() {}

    public static IngressoDTO toDTO(Ingresso ingresso, Evento evento) {
        if (ingresso == null) return null;

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

    public static Ingresso createIngresso(Long usuarioId, Long eventoId, Double valorPago) {
        return new Ingresso(usuarioId, eventoId, valorPago);
    }

    public static Ingresso createIngressoVinculado(Long usuarioId, Long eventoId, Long eventoVinculadoId, Double valorPago) {
        Ingresso ingresso = new Ingresso(usuarioId, eventoId, valorPago);
        ingresso.setEventoVinculadoId(eventoVinculadoId);
        ingresso.setIngressoPrincipal(false);
        return ingresso;
    }
}