package br.com.softhouse.dende.mappers;

import br.com.softhouse.dende.dto.EventoDTO;
import br.com.softhouse.dende.dto.EventoResumoDTO;
import br.com.softhouse.dende.model.Evento;

// Mapper para converter entre Evento, EventoDTO e EventoResumoDTO
public class EventoMapper {

    private EventoMapper() {}

    // Converte um EventoDTO para um objeto Evento (entidade)
    public static Evento toEntity(EventoDTO dto, Long organizadorId) {
        if (dto == null) return null;

        // Criar um novo objeto Evento e preencher seus campos com os dados do DTO
        Evento evento = new Evento();
        evento.setOrganizadorId(organizadorId);
        evento.setNome(dto.getNome());
        evento.setPagina(dto.getPagina());
        evento.setDescricao(dto.getDescricao());
        evento.setDataInicio(dto.getDataInicio());
        evento.setDataFinal(dto.getDataFinal());
        evento.setTipoEvento(dto.getTipoEvento());
        evento.setEventoPrincipalId(dto.getEventoPrincipalId());
        evento.setModalidade(dto.getModalidade());
        evento.setCapacidadeMaxima(dto.getCapacidadeMaxima());
        evento.setLocal(dto.getLocal());
        evento.setPrecoIngresso(dto.getPrecoIngresso());
        evento.setEstornaCancelamento(dto.getEstornaCancelamento());
        evento.setTaxaEstorno(dto.getTaxaEstorno());
        evento.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : false);
        evento.setIngressosVendidos(dto.getIngressosVendidos() != null ? dto.getIngressosVendidos() : 0);

        return evento;
    }

    // Converte um objeto Evento (entidade) para um EventoDTO
    public static EventoDTO toDTO(Evento evento) {
        if (evento == null) return null;

        EventoDTO dto = new EventoDTO();
        dto.setId(evento.getId());
        dto.setOrganizadorId(evento.getOrganizadorId());
        dto.setNome(evento.getNome());
        dto.setPagina(evento.getPagina());
        dto.setDescricao(evento.getDescricao());
        dto.setDataInicio(evento.getDataInicio());
        dto.setDataFinal(evento.getDataFinal());
        dto.setPeriodo(evento.getPeriodoFormatado());
        dto.setTipoEvento(evento.getTipoEvento());
        dto.setEventoPrincipalId(evento.getEventoPrincipalId());
        dto.setModalidade(evento.getModalidade());
        dto.setCapacidadeMaxima(evento.getCapacidadeMaxima());
        dto.setLocal(evento.getLocal());
        dto.setAtivo(evento.getAtivo());
        dto.setPrecoIngresso(evento.getPrecoIngresso());
        dto.setEstornaCancelamento(evento.getEstornaCancelamento());
        dto.setTaxaEstorno(evento.getTaxaEstorno());
        dto.setIngressosVendidos(evento.getIngressosVendidos());
        dto.setIngressosDisponiveis(evento.ingressosDisponiveis());

        return dto;
    }

    // Converte um objeto Evento (entidade) para um EventoResumoDTO
    public static EventoResumoDTO toResumoDTO(Evento evento) {
        if (evento == null) return null;

        EventoResumoDTO dto = new EventoResumoDTO();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setPeriodo(evento.getPeriodoFormatado());
        dto.setLocal(evento.getLocal());
        dto.setPrecoIngresso(evento.getPrecoIngresso());
        dto.setIngressosVendidos(evento.getIngressosVendidos());
        dto.setCapacidadeMaxima(evento.getCapacidadeMaxima());
        dto.setAtivo(evento.getAtivo());

        return dto;
    }

    // Atualiza os campos de um objeto Evento com os dados de um EventoDTO (usado para update)
    public static Evento updateEntity(Evento evento, EventoDTO dto) {
        if (dto == null) return evento;

        if (dto.getNome() != null) {
            evento.setNome(dto.getNome());
        }
        if (dto.getPagina() != null) {
            evento.setPagina(dto.getPagina());
        }
        if (dto.getDescricao() != null) {
            evento.setDescricao(dto.getDescricao());
        }
        if (dto.getDataInicio() != null) {
            evento.setDataInicio(dto.getDataInicio());
        }
        if (dto.getDataFinal() != null) {
            evento.setDataFinal(dto.getDataFinal());
        }
        if (dto.getTipoEvento() != null) {
            evento.setTipoEvento(dto.getTipoEvento());
        }
        if (dto.getEventoPrincipalId() != null) {
            evento.setEventoPrincipalId(dto.getEventoPrincipalId());
        }
        if (dto.getModalidade() != null) {
            evento.setModalidade(dto.getModalidade());
        }
        if (dto.getCapacidadeMaxima() != null) {
            evento.setCapacidadeMaxima(dto.getCapacidadeMaxima());
        }
        if (dto.getLocal() != null) {
            evento.setLocal(dto.getLocal());
        }
        if (dto.getPrecoIngresso() != null) {
            evento.setPrecoIngresso(dto.getPrecoIngresso());
        }
        if (dto.getEstornaCancelamento() != null) {
            evento.setEstornaCancelamento(dto.getEstornaCancelamento());
        }
        if (dto.getTaxaEstorno() != null) {
            evento.setTaxaEstorno(dto.getTaxaEstorno());
        }
        if (dto.getAtivo() != null) {
            evento.setAtivo(dto.getAtivo());
        }
        if (dto.getIngressosVendidos() != null) {
            evento.setIngressosVendidos(dto.getIngressosVendidos());
        }

        return evento;
    }
}