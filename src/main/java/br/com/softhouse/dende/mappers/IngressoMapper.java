package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.IngressoResponseDTO;
import br.com.softhouse.dende.model.Evento;
import br.com.softhouse.dende.model.Ingresso;

public class IngressoMapper {

    private IngressoMapper() {}

    public static IngressoResponseDTO toResponseDTO(Ingresso ingresso, Evento evento) {
        if (ingresso == null) return null;
        return new IngressoResponseDTO(ingresso, evento.getNome(), evento.getDataInicio(), evento.getLocal());
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